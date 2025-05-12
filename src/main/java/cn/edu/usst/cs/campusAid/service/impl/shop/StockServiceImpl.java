package cn.edu.usst.cs.campusAid.service.impl.shop;

import cn.edu.usst.cs.campusAid.dto.shop.ProductTransaction;
import cn.edu.usst.cs.campusAid.mapper.db.shop.GoodMapper;
import cn.edu.usst.cs.campusAid.mapper.db.shop.OrderMapper;
import cn.edu.usst.cs.campusAid.mapper.db.shop.ShopMapper;
import cn.edu.usst.cs.campusAid.mapper.db.shop.TransactionMapper;
import cn.edu.usst.cs.campusAid.model.shop.Good;
import cn.edu.usst.cs.campusAid.model.shop.GoodTransaction;
import cn.edu.usst.cs.campusAid.service.CampusAidException;
import cn.edu.usst.cs.campusAid.service.shop.StockService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class StockServiceImpl implements StockService {
    ShopMapper shopMapper;
    GoodMapper goodMapper;
    TransactionMapper transactionMapper;
    OrderMapper orderMapper;

    @Override
    @NonNull
    public String verify(String name, String password) {
        try {
            return Objects.requireNonNull(shopMapper.verify(name, password));
        } catch (NullPointerException e) {
            throw new CampusAidException("用户名或密码错误");
        }
    }


    @Override
    public Long addProductToShop(String shopName, ProductTransaction productTransaction) {
        try {
            Good good = goodMapper.selectGoodByShopAndProductName(
                    shopName,
                    productTransaction.getName()
            );
            Long existingGoodID;
            // 仅在没有该商品记录、记录需创建时，可以对商品描述和价格进行写操作
            // 否则无视这些字段
            if (good == null) {
                Long targetGoodID = goodMapper.minFreeId();
                good = Good.builder()
                        .id(targetGoodID)
                        .name(productTransaction.getName())
                        .description(productTransaction.getDescription())
                        .price((double) productTransaction.getPrice())
                        .shop(shopName)
                        .build();
                goodMapper.insert(good);
                // 货物已录入数据库，视为存在
                existingGoodID = targetGoodID;
            } else {
                if (!good.getShop().equals(shopName))
                    throw new CampusAidException("商品不属于此商家");
                existingGoodID = good.getId();
            }
            Long newTransactionDatabaseID = transactionMapper.minFreeID();
            GoodTransaction newTransaction =
                    GoodTransaction.builder()
                            .id(newTransactionDatabaseID)
                            .goodId(existingGoodID)
                            .amount(productTransaction.getAmount())
                            .orderId(null)
                            .build();
            transactionMapper.insert(
                    newTransaction
            );
            return newTransactionDatabaseID;
        } catch (NullPointerException e) {
            throw new CampusAidException("商品不存在");
        }
    }
}
