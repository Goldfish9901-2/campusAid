package cn.edu.usst.cs.campusAid.service.impl.shop;

import cn.edu.usst.cs.campusAid.dto.shop.Order;
import cn.edu.usst.cs.campusAid.dto.shop.OrderDTO;
import cn.edu.usst.cs.campusAid.dto.shop.ProductTransaction;
import cn.edu.usst.cs.campusAid.dto.shop.ShopInfo;
import cn.edu.usst.cs.campusAid.mapper.db.shop.GoodMapper;
import cn.edu.usst.cs.campusAid.mapper.db.shop.OrderMapper;
import cn.edu.usst.cs.campusAid.mapper.db.shop.ShopMapper;
import cn.edu.usst.cs.campusAid.mapper.db.shop.TransactionMapper;
import cn.edu.usst.cs.campusAid.model.shop.Good;
import cn.edu.usst.cs.campusAid.model.shop.Shop;
import cn.edu.usst.cs.campusAid.model.shop.ShopOrder;
import cn.edu.usst.cs.campusAid.service.CampusAidException;
import cn.edu.usst.cs.campusAid.service.shop.ShopService;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {
    ShopMapper shopMapper;
    GoodMapper goodMapper;
    OrderMapper orderMapper;
    TransactionMapper transactionMapper;

    public ShopServiceImpl(ShopMapper shopMapper, GoodMapper goodMapper, OrderMapper orderMapper, TransactionMapper transactionMapper) {
        this.shopMapper = shopMapper;
        this.goodMapper = goodMapper;
        this.orderMapper = orderMapper;
        this.transactionMapper = transactionMapper;
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
        }
        Long orderID = orderMapper.minFreeID();
        ShopOrder shopOrder = ShopOrder.builder()
                .id(orderID)
                .shop(shopName)
                .shopperId(orderDTO.getUserId())
                .build();
        orderMapper.insert(shopOrder);
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
