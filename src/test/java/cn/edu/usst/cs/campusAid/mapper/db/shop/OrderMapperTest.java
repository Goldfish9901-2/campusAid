package cn.edu.usst.cs.campusAid.mapper.db.shop;

import cn.edu.usst.cs.campusAid.dto.shop.ProductTransaction;
import cn.edu.usst.cs.campusAid.model.shop.ShopOrder;
import cn.edu.usst.cs.campusAid.model.User;
import cn.edu.usst.cs.campusAid.mapper.db.UserMapper;
import cn.edu.usst.cs.campusAid.model.shop.Shop;
import cn.edu.usst.cs.campusAid.model.shop.Good;
import cn.edu.usst.cs.campusAid.model.shop.GoodTransaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class OrderMapperTest {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private GoodMapper goodMapper;

    @Autowired
    private TransactionMapper transactionMapper;

    private ShopOrder createTestOrder(Long id, Long shopperId, String shop) {
        return ShopOrder.builder()
                .id(id)
                .shopperId(shopperId)
                .shop(shop)
                .build();
    }

    private User createTestUser(Long id) {
        return User.builder()
                .id(id)
                .name("测试用户")
                .phoneNumber(13800138000L)
                .build();
    }

    private Shop createTestShop(String name) {
        return Shop.builder()
                .name(name)
                .description("这是一个测试商店")
                .password("test123")
                .build();
    }

    @Test
    public void testInsertAndSelectAllOrdersFromUser() {
        // 准备测试数据
        Long userId = 1L;
        User user = createTestUser(userId);
        userMapper.insertUser(user);

        String shopName = "测试商店";
        Shop shop = createTestShop(shopName);
        shopMapper.insert(shop);

        // 创建商品
        Long goodId = goodMapper.minFreeId();
        Good good = Good.builder()
                .id(goodId)
                .name("测试商品")
                .description("这是一个测试商品")
                .shop(shopName)
                .price(99.9)
                .build();
        goodMapper.insert(good);

        // 创建订单
        Long orderId1 = orderMapper.minFreeID();
        ShopOrder order1 = createTestOrder(orderId1, userId, shopName);
        orderMapper.insert(order1);

        // 创建交易记录
        Long transactionId1 = transactionMapper.minFreeID();
        GoodTransaction transaction1 = GoodTransaction.builder()
                .id(transactionId1)
                .goodId(goodId)
                .orderId(orderId1)
                .amount(5L)
                .build();
        transactionMapper.insert(transaction1);

        Long orderId2 = orderMapper.minFreeID();
        ShopOrder order2 = createTestOrder(orderId2, userId, shopName);
        orderMapper.insert(order2);

        // 创建第二个交易记录
        Long transactionId2 = transactionMapper.minFreeID();
        GoodTransaction transaction2 = GoodTransaction.builder()
                .id(transactionId2)
                .goodId(goodId)
                .orderId(orderId2)
                .amount(3L)
                .build();
        transactionMapper.insert(transaction2);

        // 测试查询用户的所有订单
        List<ProductTransaction> orders = orderMapper.selectAllOrdersFromUser(userId);

        // 验证结果
        assertNotNull(orders);
        assertTrue(orders.size() >= 2); // 可能有其他测试数据
    }

    @Test
    public void testMinFreeID() {
        // 测试获取最小可用ID
        Long minFreeId = orderMapper.minFreeID();
        assertNotNull(minFreeId);
        assertTrue(minFreeId > 0);
    }
} 