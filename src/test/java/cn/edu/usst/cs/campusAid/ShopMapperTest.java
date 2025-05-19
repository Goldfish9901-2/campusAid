package cn.edu.usst.cs.campusAid;
import cn.edu.usst.cs.campusAid.mapper.db.shop.GoodMapper;
import cn.edu.usst.cs.campusAid.mapper.db.shop.OrderMapper;
import cn.edu.usst.cs.campusAid.mapper.db.shop.ShopMapper;
import cn.edu.usst.cs.campusAid.mapper.db.shop.TransactionMapper;
import cn.edu.usst.cs.campusAid.model.shop.*;
import cn.edu.usst.cs.campusAid.dto.shop.ProductTransaction;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public class ShopMapperTest {
    private static final Logger logger = LoggerFactory.getLogger(ShopMapperTest.class);
    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private GoodMapper goodMapper;

    @Autowired
    private ShopMapper shopMapper;

    // 可选：用于测试数据的准备和清理
    private static final String TEST_SHOP_NAME = "TestShop";
    private static final String TEST_GOOD_NAME = "TestGood";
    private static final String TEST_USER_PASSWORD = "password123";
    private static final String TEST_SHOP_DESCRIPTION = "啦啦啦";

    @BeforeEach
    void setUp() {
        // 1. 插入测试店铺
        Shop shop = Shop.builder()
            .name(TEST_SHOP_NAME)
            .description(TEST_SHOP_DESCRIPTION)
            .password(TEST_USER_PASSWORD)
            .build();
        try { shopMapper.insert(shop); } catch (Exception ignored) {}
        // 2. 插入测试商品
        Good good = Good.builder()
            .id(1L)
            .name(TEST_GOOD_NAME)
            .shop(TEST_SHOP_NAME)
            .price(9.99)
            .build();
        try { goodMapper.insert(good); } catch (Exception ignored) {}
    }

    @AfterEach
    void tearDown() {
        // 删除测试商品和店铺
        try { goodMapper.deleteByShopAndName(TEST_SHOP_NAME, TEST_GOOD_NAME); } catch (Exception ignored) {}
        try { shopMapper.deleteByName(TEST_SHOP_NAME); } catch (Exception ignored) {}
        // 并发插入的商品清理
        for (int i = 0; i < 5; i++) {
            try { goodMapper.deleteByShopAndName(TEST_SHOP_NAME, TEST_GOOD_NAME + i); } catch (Exception ignored) {}
        }
    }

    @Test
    void testShopMapper() {
        assertNotNull(shopMapper, "ShopMapper 应该被注入成功");

        // 测试 getShopByName - 查询已存在的店铺
        Shop retrievedShop = shopMapper.getShopByName(TEST_SHOP_NAME);

        assertNotNull(retrievedShop, "数据库中应存在名为 TestShop 的店铺");
        assertEquals(TEST_SHOP_NAME, retrievedShop.getName(), "店铺名称应匹配");
        assertEquals(TEST_SHOP_DESCRIPTION, retrievedShop.getDescription(), "店铺描述应匹配");

        System.out.println("[INFO] 成功查到店铺信息：" + retrievedShop);

        // 测试 verify 方法 - 正确密码应返回用户名
        String resultName = shopMapper.verify(TEST_SHOP_NAME, TEST_USER_PASSWORD);
        assertNotNull(resultName, "验证正确密码时不应为 null");
        assertEquals(TEST_SHOP_NAME, resultName, "验证通过后应返回用户名");

        System.out.println("[INFO] 正确密码验证成功");

        // 测试 verify 方法 - 错误密码应抛出异常
//        System.out.println("[DEBUG] 即将测试错误密码登录...");
//        assertThrows(
//                Exception.class,
//                () -> shopMapper.verify(TEST_SHOP_NAME, "wrongPassword"),
//                "错误密码登录应该抛出异常"
//        );
//        System.out.println("[INFO] 错误密码验证成功抛出异常");
    }



    @Test
    @DisplayName("测试 GoodMapper 的插入、查询及商品列表功能")
    void testGoodMapper() {
        System.out.println("[INFO] 开始执行 testGoodMapper 测试");

        assertNotNull(goodMapper, "[ERROR] GoodMapper 注入失败");

//        // 1. 获取最小可用 ID
//        System.out.println("[DEBUG] 获取最小可用商品ID...");
//        Long freeId = goodMapper.minFreeId();
//        assertNotNull(freeId, "[ERROR] 最小可用ID为null，请检查数据库序列或表结构");
//
//        // 2. 构建并插入新商品
//        System.out.println("[DEBUG] 构建新商品对象...");
//        Good newGood = Good.builder()
//                .id(freeId)
//                .name(TEST_GOOD_NAME)
//                .description("A test good")
//                .shop(TEST_SHOP_NAME)
//                .price(9.99)
//                .build();
//
//        System.out.println("[DEBUG] 插入新商品: " + newGood);
//        goodMapper.insert(newGood);

        // 3. 查询插入的商品 - 根据 ID
        System.out.println("[DEBUG] 查询刚插入的商品 (by ID)...");
        Good insertedGood = goodMapper.selectGoodByID(1L);
        assertNotNull(insertedGood, "[ERROR] 按ID查询返回 null");
        assertEquals(TEST_GOOD_NAME, insertedGood.getName(), "[ERROR] 商品名称不匹配");

        // === 新增：插入一条进货记录，确保有库存 ===
        GoodTransaction restock = GoodTransaction.builder()
            .id(transactionMapper.minFreeID())
            .goodId(insertedGood.getId())
            .amount(10L)
            .orderId(null)
            .build();
        transactionMapper.insert(restock);

        // 4. 查询商店所有上架过的商品
        System.out.println("[DEBUG] 查询商店的所有上架商品...");
        List<ProductTransaction> allGoods = goodMapper.selectGoodsByShopName(TEST_SHOP_NAME);
        assertNotNull(allGoods, "[ERROR] 上架商品列表不应为空");
        assertTrue(allGoods.stream().anyMatch(p -> p.getName().equals(TEST_GOOD_NAME)),
                "[ERROR] 所有上架商品中未找到测试商品");

        System.out.println("[INFO] 查询所有上架商品成功，数量：" + allGoods.size());

        // 5. 查询商店当前可购买的商品
        System.out.println("[DEBUG] 查询商店当前可购买的商品...");
        List<ProductTransaction> availableGoods = goodMapper.selectAvalibleGoodsByShopName(TEST_SHOP_NAME);
        assertNotNull(availableGoods, "[ERROR] 可购买商品列表不应为空");
        assertTrue(availableGoods.stream().anyMatch(p -> p.getName().equals(TEST_GOOD_NAME)),
                "[ERROR] 当前可购买商品中未找到测试商品");

        System.out.println("[INFO] 查询当前可购买商品成功，数量：" + availableGoods.size());

        // 6. 输出测试数据以便观察
        System.out.println("[INFO] 所有上架商品: " + allGoods);
        System.out.println("[INFO] 当前可购买商品: " + availableGoods);
    }




    @Test
    @DisplayName("测试 TransactionMapper 的进货、进货量、销售量、库存查询功能")
    void testTransactionMapperNewMethods() {
        System.out.println("[INFO] 开始执行 testTransactionMapperNewMethods 测试");

        // 测试前清理同名商品，避免主键冲突
        goodMapper.deleteByShopAndName(TEST_SHOP_NAME, "TestGoodForStock");

        assertNotNull(transactionMapper, "[ERROR] TransactionMapper 注入失败");
        assertNotNull(goodMapper, "[ERROR] GoodMapper 注入失败");
        assertNotNull(orderMapper, "[ERROR] OrderMapper 注入失败");

        // 1. 获取最小可用事务ID和商品ID
        Long freeGoodId = goodMapper.minFreeId();
        assertNotNull(freeGoodId, "[ERROR] 商品ID为空，请检查数据库");

        Long freeOrderId = orderMapper.minFreeID();
        assertNotNull(freeOrderId, "[ERROR] 订单ID为空，请检查数据库");

        // 2. 构建并插入一个商品用于测试
        Good testGood = Good.builder()
                .id(freeGoodId)
                .name("TestGoodForStock")
                .shop(TEST_SHOP_NAME)
                .price(9.99D)
                .build();

        System.out.println("[DEBUG] 插入测试商品: " + testGood);
        goodMapper.insert(testGood);

        // 3. 第一次进货：插入 order_id = null 的交易记录
        GoodTransaction restock1 = GoodTransaction.builder()
                .id(transactionMapper.minFreeID())
                .goodId(testGood.getId())
                .amount(10L)
                .orderId(null) // 表示进货
                .build();
        transactionMapper.insert(restock1);
        System.out.println("[DEBUG] 插入第一次进货记录：" + restock1);

        // 4. 第二次进货
        GoodTransaction restock2 = GoodTransaction.builder()
                .id(transactionMapper.minFreeID())
                .goodId(testGood.getId())
                .amount(20L)
                .orderId(null)
                .build();
        transactionMapper.insert(restock2);
        System.out.println("[DEBUG] 插入第二次进货记录：" + restock2);

        // ✅ 新增：插入订单记录（模拟用户下单）
        ShopOrder testOrder = ShopOrder.builder()
                .id(freeOrderId)
                .shopperId(2235062128L) // 假设用户ID
                .shop(TEST_SHOP_NAME)
                .build();

        System.out.println("[DEBUG] 插入测试订单：" + testOrder);
        orderMapper.insert(testOrder);

        // ✅ 新增：使用刚插入的订单ID插入销售交易记录
        GoodTransaction sale = GoodTransaction.builder()
                .id(transactionMapper.minFreeID())
                .goodId(testGood.getId())
                .amount(5L)
                .orderId(testOrder.getId()) // 使用真实订单ID
                .build();

        System.out.println("[DEBUG] 插入销售记录：" + sale);
        transactionMapper.insert(sale);

        // 5. 查询进货量
        Long stock = transactionMapper.getAllSupplyHistory(testGood.getId());
        assertNotNull(stock, "[ERROR] 进货量不应为 null");
        assertEquals(30L, stock.longValue(), "[ERROR] 进货量应为 10 + 20 = 30");
        System.out.println("查询到进货量为"+stock);
        // 6. 查询销售量
        Long sold = transactionMapper.getTotalSold(testGood.getId());
        assertNotNull(sold, "[ERROR] 销售量不应为 null");
        assertEquals(5L, sold.longValue(), "[ERROR] 销售量应为 5");
        System.out.println("查询到销售量为"+sold);

        // 7. 查询库存量
        Long availableStock = transactionMapper.getAvailableStock(testGood.getId());
        assertNotNull(availableStock, "[ERROR] 库存量不应为 null");
        assertEquals(25L, availableStock.longValue(), "[ERROR] 库存量应为 30 - 5 = 25");
        System.out.println("查询到库存量为"+availableStock);
        System.out.println("[INFO] 所有 TransactionMapper 新增方法测试通过 ✅");
    }




    @Test
    @DisplayName("测试 OrderMapper 的订单插入功能")
    void testOrderMapper() {
        System.out.println("[INFO] 开始执行 testOrderMapper 测试");

        assertNotNull(orderMapper, "[ERROR] OrderMapper 注入失败");

        System.out.println("[DEBUG] 获取最小可用订单ID...");
        Long freeOrderId = orderMapper.minFreeID();
        assertNotNull(freeOrderId, "[ERROR] 订单ID为空，请检查数据库");

        System.out.println("[DEBUG] 构建新订单...");
        ShopOrder order = ShopOrder.builder()
                .id(freeOrderId)
                .shopperId(2235062128L)
                .shop(TEST_SHOP_NAME)
                .build();

        System.out.println("[DEBUG] 插入订单: " + order);
        orderMapper.insert(order);

        System.out.println("[INFO] 订单插入成功");
    }
    /**
     * 测试获取用户历史订单
     */
    @Test
    public void testGetHistory() {
        logger.info("【测试方法】获取用户历史订单");
        List<ProductTransaction> history = transactionMapper.getHistory(2235062128L);
        Assertions.assertNotNull(history, "历史记录不应为空");
        logger.info("✅ 历史订单条目数量: {}", history.size());
        for (ProductTransaction product : history) {
            logger.info(" - 店铺: {}, 商品名: {}, 数量: {}, 单价: {}, 描述: {}",
                    product.getShopName(),
                    product.getName(),
                    product.getAmount(),
                    product.getPrice(),
                    product.getDescription());
        }
    }

    /**
     * 1. 查询不存在的店铺应返回null
     */
    @Test
    @DisplayName("1. 查询不存在的店铺应返回null")
    void testGetNonExistentShop() {
        Shop shop = shopMapper.getShopByName("不存在的店铺");
        assertNull(shop, "不存在的店铺应返回null");
    }

    /**
     * 2. 插入重复主键应抛出异常
     */
    @Test
    @DisplayName("2. 插入重复主键应抛出异常")
    void testInsertDuplicateGood() {
        Good good = Good.builder()
            .id(1L)
            .name(TEST_GOOD_NAME)
            .shop(TEST_SHOP_NAME)
            .price(9.99)
            .build();
        assertThrows(Exception.class, () -> goodMapper.insert(good), "插入重复主键应抛出异常");
    }

    /**
     * 3. 插入空商品名应抛出异常
     */
    @Test
    @DisplayName("3. 插入空商品名应抛出异常")
    void testInsertEmptyGoodName() {
        Good good = Good.builder()
            .id(999L)
            .name("")
            .shop(TEST_SHOP_NAME)
            .price(9.99)
            .build();
        assertThrows(Exception.class, () -> goodMapper.insert(good), "商品名为空应抛出异常");
    }

    /**
     * 4. 并发插入商品测试
     */
    @Test
    @DisplayName("4. 并发插入商品测试")
    void testConcurrentInsertGood() throws InterruptedException {
        int threadCount = 5;
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            final int idx = i;
            threads[i] = new Thread(() -> {
                Good good = Good.builder()
                    .id(1000L + idx)
                    .name(TEST_GOOD_NAME + idx)
                    .shop(TEST_SHOP_NAME)
                    .price(9.99)
                    .build();
                try { goodMapper.insert(good); } catch (Exception ignored) {}
            });
            threads[i].start();
        }
        for (Thread t : threads) t.join();
        // 检查是否都插入成功
        for (int i = 0; i < threadCount; i++) {
            Good good = goodMapper.selectGoodByID(1000L + i);
            assertNotNull(good, "并发插入商品失败: " + i);
        }
    }
}
