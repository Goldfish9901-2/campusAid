package cn.edu.usst.cs.campusAid.service.impl.shop;

import cn.edu.usst.cs.campusAid.dto.shop.Order;
import cn.edu.usst.cs.campusAid.dto.shop.OrderDTO;
import cn.edu.usst.cs.campusAid.dto.shop.ProductTransaction;
import cn.edu.usst.cs.campusAid.dto.shop.ShopInfo;
import cn.edu.usst.cs.campusAid.mapper.db.shop.GoodMapper;
import cn.edu.usst.cs.campusAid.mapper.db.shop.OrderMapper;
import cn.edu.usst.cs.campusAid.mapper.db.shop.ShopMapper;
import cn.edu.usst.cs.campusAid.mapper.db.shop.TransactionMapper;
import cn.edu.usst.cs.campusAid.mapper.mapstruct.ProductToGoodTransaction;
import cn.edu.usst.cs.campusAid.model.shop.Good;
import cn.edu.usst.cs.campusAid.model.shop.GoodTransaction;
import cn.edu.usst.cs.campusAid.model.shop.Shop;
import cn.edu.usst.cs.campusAid.model.shop.ShopOrder;
import cn.edu.usst.cs.campusAid.service.CampusAidException;
import cn.edu.usst.cs.campusAid.service.auth.UserService;
import cn.edu.usst.cs.campusAid.service.shop.ShopService;
import io.micrometer.common.util.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {
    private final ProductToGoodTransaction productToGoodTransaction;
    private final SqlSessionFactory sqlSessionFactory;
    private final UserService userService;
    ShopMapper shopMapper;
    GoodMapper goodMapper;
    OrderMapper orderMapper;
    TransactionMapper transactionMapper;

    public ShopServiceImpl(
            ShopMapper shopMapper,
            GoodMapper goodMapper,
            OrderMapper orderMapper,
            TransactionMapper transactionMapper,
            ProductToGoodTransaction productToGoodTransaction,
            SqlSessionFactory sqlSessionFactory,
            UserService userService) {
        this.shopMapper = shopMapper;
        this.goodMapper = goodMapper;
        this.orderMapper = orderMapper;
        this.transactionMapper = transactionMapper;
        this.productToGoodTransaction = productToGoodTransaction;
        this.sqlSessionFactory = sqlSessionFactory;
        this.userService = userService;
    }

    @Override
    public ShopInfo getShopInfo(String shopName, String shopNameStored) {
        ShopInfo.ShopInfoBuilder builder = ShopInfo.builder();
        if (StringUtils.isBlank(shopName))
            return builder.build();
        Shop shop = shopMapper.getShopByName(shopName);
        if (shop == null)
            return builder.build();
        builder.name(shopName);
        builder.description(shop.getDescription());
        List<ProductTransaction> products =
                (shopName.equals(shopNameStored))
                        ? goodMapper.selectGoodsByShopName(shopName)
                        : goodMapper.selectAvalibleGoodsByShopName(shopName);
        builder.products(products);
        return builder.build();
    }

    @Override
    public Order checkout(OrderDTO orderDTO) {
        if (orderDTO.getItems().isEmpty())
            throw new CampusAidException("订单为空");
        String shopName = orderDTO.getShopName();
        Order.OrderBuilder builder = Order.builder();
        builder

                .shopName(shopName)
                .buyerID(orderDTO.getUserId())
                .products(orderDTO.getItems());
        double price = .0;
        Long orderID = orderMapper.minFreeID();
        ShopOrder shopOrder = ShopOrder.builder()
                .id(orderID)
                .shop(shopName)
                .shopperId(orderDTO.getUserId())
                .build();

        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.getConnection().setAutoCommit(false); // 开启事务
            orderMapper.insert(shopOrder);
            // 执行多个数据库操作
            for (ProductTransaction productTransaction : orderDTO.getItems()) {
                Good matchingCandidate = goodMapper.selectGoodByShopAndProductName(
                        shopName,
                        productTransaction.getName()
                );
                if (matchingCandidate == null)
                    throw new CampusAidException("商品不存在");
                Long currentStock = transactionMapper.getAvailableStock(matchingCandidate.getId());
                if (currentStock == null || currentStock < 0)
                    throw new CampusAidException("商品记录无效");
                if (currentStock < productTransaction.getAmount())
                    throw new CampusAidException("商品库存不足");
                price += matchingCandidate.getPrice() * productTransaction.getAmount();
                Long minFreeId = transactionMapper.minFreeID();
                GoodTransaction goodTransaction = productToGoodTransaction.fromProductTransaction(productTransaction);
                goodTransaction.setId(minFreeId);
                goodTransaction.setGoodId(matchingCandidate.getId());
                goodTransaction.setOrderId(orderID);
                transactionMapper.insert(goodTransaction);
            }
            if(userService.getBalance(orderDTO.getUserId())<price)
                throw new CampusAidException("余额不足");
            sqlSession.commit(); // 提交事务
        } catch (Exception e) {
            sqlSession.rollback(); // 回滚事务
            try {
                sqlSession.getConnection().setAutoCommit(true);
            }catch (SQLException sqlException){
                throw new CampusAidException("订单提交失败");
            }
            throw new CampusAidException(e.getMessage());
        } finally {
            sqlSession.close(); // 关闭会话
        }

        builder
                .id(orderID)
                .price(price);
        if (shopName == null)
            throw new CampusAidException("无法找到商家");
        return builder.build();
    }

    @Override
    public List<ProductTransaction> getHistory(Long userId) {
        return transactionMapper.getHistory(userId);
    }
}
