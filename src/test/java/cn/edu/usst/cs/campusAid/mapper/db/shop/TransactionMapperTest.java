package cn.edu.usst.cs.campusAid.mapper.db.shop;

import cn.edu.usst.cs.campusAid.dto.shop.ProductTransaction;
import cn.edu.usst.cs.campusAid.model.shop.Good;
import cn.edu.usst.cs.campusAid.model.shop.GoodTransaction;
import cn.edu.usst.cs.campusAid.model.shop.Shop;
import cn.edu.usst.cs.campusAid.model.shop.ShopOrder;
import cn.edu.usst.cs.campusAid.model.User;
import cn.edu.usst.cs.campusAid.mapper.db.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class TransactionMapperTest {

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private GoodMapper goodMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderMapper orderMapper;

    private GoodTransaction createTestTransaction(Long id, Long goodId, Long orderId, Long amount) {
        return GoodTransaction.builder()
                .id(id)
                .goodId(goodId)
                .orderId(orderId)
                .amount(amount)
                .build();
    }

    private Shop createTestShop(String name) {
        return Shop.builder()
                .name(name)
                .description("这是一个测试商店")
                .password("test123")
                .build();
    }

    private User createTestUser(Long id) {
        return User.builder()
                .id(id)
                .name("测试用户")
                .phoneNumber(13800138000L)
                .build();
    }

    private ShopOrder createTestOrder(Long id, Long shopperId, String shop) {
        return ShopOrder.builder()
                .id(id)
                .shopperId(shopperId)
                .shop(shop)
                .build();
    }

    @Test
    public void testInsertAndGetHistory() {
        // 准备测试数据
        String shopName = "测试商店";
        Shop shop = createTestShop(shopName);
        shopMapper.insert(shop);

        Long userId = 1L;
        User user = createTestUser(userId);
        userMapper.insertUser(user);

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
        Long orderId = orderMapper.minFreeID();
        ShopOrder order = createTestOrder(orderId, userId, shopName);
        orderMapper.insert(order);

        // 创建进货记录
        Long transactionId1 = transactionMapper.minFreeID();
        GoodTransaction transaction1 = createTestTransaction(transactionId1, goodId, null, 10L);
        transactionMapper.insert(transaction1);

        // 创建销售记录
        Long transactionId2 = transactionMapper.minFreeID();
        GoodTransaction transaction2 = createTestTransaction(transactionId2, goodId, orderId, 5L);
        transactionMapper.insert(transaction2);

        // 测试查询用户历史记录
        List<ProductTransaction> history = transactionMapper.getHistory(userId);

        // 验证结果
        assertNotNull(history);
        assertTrue(history.size() >= 1); // 可能有其他测试数据
    }

    @Test
    public void testGetAvailableStock() {
        // 准备测试数据
        String shopName = "测试商店";
        Shop shop = createTestShop(shopName);
        shopMapper.insert(shop);

        Long goodId = goodMapper.minFreeId();
        Good good = Good.builder()
                .id(goodId)
                .name("测试商品")
                .description("这是一个测试商品")
                .shop(shopName)
                .price(99.9)
                .build();
        goodMapper.insert(good);

        // 创建进货记录
        Long transactionId1 = transactionMapper.minFreeID();
        GoodTransaction transaction1 = createTestTransaction(transactionId1, goodId, null, 10L);
        transactionMapper.insert(transaction1);

        // 创建销售记录
        Long transactionId2 = transactionMapper.minFreeID();
        GoodTransaction transaction2 = createTestTransaction(transactionId2, goodId, 1L, 5L);
        transactionMapper.insert(transaction2);

        // 测试获取可用库存
        Long availableStock = transactionMapper.getAvailableStock(goodId);

        // 验证结果
        assertNotNull(availableStock);
        assertEquals(5L, availableStock); // 10 - 5 = 5
    }

    @Test
    public void testGetAllSupplyHistory() {
        // 准备测试数据
        String shopName = "测试商店";
        Shop shop = createTestShop(shopName);
        shopMapper.insert(shop);

        Long goodId = goodMapper.minFreeId();
        Good good = Good.builder()
                .id(goodId)
                .name("测试商品")
                .description("这是一个测试商品")
                .shop(shopName)
                .price(99.9)
                .build();
        goodMapper.insert(good);

        // 创建进货记录
        Long transactionId1 = transactionMapper.minFreeID();
        GoodTransaction transaction1 = createTestTransaction(transactionId1, goodId, null, 10L);
        transactionMapper.insert(transaction1);

        Long transactionId2 = transactionMapper.minFreeID();
        GoodTransaction transaction2 = createTestTransaction(transactionId2, goodId, null, 20L);
        transactionMapper.insert(transaction2);

        // 测试获取总进货量
        Long totalSupply = transactionMapper.getAllSupplyHistory(goodId);

        // 验证结果
        assertNotNull(totalSupply);
        assertEquals(30L, totalSupply); // 10 + 20 = 30
    }

    @Test
    public void testGetTotalSold() {
        // 准备测试数据
        String shopName = "测试商店";
        Shop shop = createTestShop(shopName);
        shopMapper.insert(shop);

        Long goodId = goodMapper.minFreeId();
        Good good = Good.builder()
                .id(goodId)
                .name("测试商品")
                .description("这是一个测试商品")
                .shop(shopName)
                .price(99.9)
                .build();
        goodMapper.insert(good);

        // 创建销售记录
        Long transactionId1 = transactionMapper.minFreeID();
        GoodTransaction transaction1 = createTestTransaction(transactionId1, goodId, 1L, 5L);
        transactionMapper.insert(transaction1);

        Long transactionId2 = transactionMapper.minFreeID();
        GoodTransaction transaction2 = createTestTransaction(transactionId2, goodId, 2L, 15L);
        transactionMapper.insert(transaction2);

        // 测试获取总销售量
        Long totalSold = transactionMapper.getTotalSold(goodId);

        // 验证结果
        assertNotNull(totalSold);
        assertEquals(20L, totalSold); // 5 + 15 = 20
    }

    @Test
    public void testRestock() {
        // 准备测试数据
        String shopName = "测试商店";
        Shop shop = createTestShop(shopName);
        shopMapper.insert(shop);

        Long goodId = goodMapper.minFreeId();
        Good good = Good.builder()
                .id(goodId)
                .name("测试商品")
                .description("这是一个测试商品")
                .shop(shopName)
                .price(99.9)
                .build();
        goodMapper.insert(good);

        // 测试进货
        Long transactionId = transactionMapper.minFreeID();
        transactionMapper.restock(transactionId, goodId, 10.0f);

        // 验证结果
        Long availableStock = transactionMapper.getAvailableStock(goodId);
        assertNotNull(availableStock);
        assertEquals(10L, availableStock);
    }
} 