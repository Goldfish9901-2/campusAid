package cn.edu.usst.cs.campusAid.mapper.db.shop;

import cn.edu.usst.cs.campusAid.dto.shop.ProductTransaction;
import cn.edu.usst.cs.campusAid.model.shop.Good;
import cn.edu.usst.cs.campusAid.model.shop.GoodTransaction;
import cn.edu.usst.cs.campusAid.model.shop.Shop;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class GoodMapperTest {

    @Autowired
    private GoodMapper goodMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private TransactionMapper transactionMapper;

    private Good createTestGood(Long id, String name, String description, String shop, Double price) {
        return Good.builder()
                .id(id)
                .name(name)
                .description(description)
                .shop(shop)
                .price(price)
                .build();
    }

    private Shop createTestShop(String name) {
        return Shop.builder()
                .name(name)
                .description("这是一个测试商店")
                .password("test123")
                .build();
    }

    private void addStock(Long goodId, Long amount) {
        Long transactionId = transactionMapper.minFreeID();
        GoodTransaction transaction = GoodTransaction.builder()
                .id(transactionId)
                .goodId(goodId)
                .amount(amount)
                .orderId(null)
                .build();
        transactionMapper.insert(transaction);
    }

    @Test
    public void testInsertAndSelectById() {
        // 准备测试数据
        String shopName = "测试商店";
        Shop shop = createTestShop(shopName);
        shopMapper.insert(shop);

        Long goodId = goodMapper.minFreeId();
        String name = "测试商品";
        String description = "这是一个测试商品";
        Double price = 99.9;
        Good good = createTestGood(goodId, name, description, shopName, price);

        // 测试插入
        goodMapper.insert(good);

        // 测试查询
        Good retrievedGood = goodMapper.selectGoodByID(goodId);

        // 验证结果
        assertNotNull(retrievedGood);
        assertEquals(good.getId(), retrievedGood.getId());
        assertEquals(good.getName(), retrievedGood.getName());
        assertEquals(good.getDescription(), retrievedGood.getDescription());
        assertEquals(good.getShop(), retrievedGood.getShop());
        assertEquals(good.getPrice(), retrievedGood.getPrice());
    }

    @Test
    public void testSelectGoodByShopAndProductName() {
        // 准备测试数据
        String shopName = "测试商店";
        Shop shop = createTestShop(shopName);
        shopMapper.insert(shop);

        Long goodId = goodMapper.minFreeId();
        String name = "测试商品";
        String description = "这是一个测试商品";
        Double price = 99.9;
        Good good = createTestGood(goodId, name, description, shopName, price);
        goodMapper.insert(good);

        // 测试查询
        Good retrievedGood = goodMapper.selectGoodByShopAndProductName(shopName, name);

        // 验证结果
        assertNotNull(retrievedGood);
        assertEquals(good.getId(), retrievedGood.getId());
        assertEquals(good.getName(), retrievedGood.getName());
        assertEquals(good.getDescription(), retrievedGood.getDescription());
        assertEquals(good.getShop(), retrievedGood.getShop());
        assertEquals(good.getPrice(), retrievedGood.getPrice());

        // 测试不存在的商品
        Good nonExistentGood = goodMapper.selectGoodByShopAndProductName(shopName, "non_existent_good");
        assertNull(nonExistentGood);
    }

    @Test
    public void testSelectGoodsByShopName() {
        // 准备测试数据
        String shopName = "测试商店";
        Shop shop = createTestShop(shopName);
        shopMapper.insert(shop);

        Long goodId1 = goodMapper.minFreeId();
        Good good1 = createTestGood(goodId1, "商品1", "描述1", shopName, 99.9);
        goodMapper.insert(good1);

        Long goodId2 = goodMapper.minFreeId();
        Good good2 = createTestGood(goodId2, "商品2", "描述2", shopName, 199.9);
        goodMapper.insert(good2);

        // 测试查询
        List<ProductTransaction> transactions = goodMapper.selectGoodsByShopName(shopName);

        // 验证结果
        assertNotNull(transactions);
        assertEquals(2, transactions.size());
        assertTrue(transactions.stream().anyMatch(t -> t.getName().equals(good1.getName())));
        assertTrue(transactions.stream().anyMatch(t -> t.getName().equals(good2.getName())));
    }

    @Test
    public void testSelectAvalibleGoodsByShopName() {
        // 准备测试数据
        String shopName = "测试商店";
        Shop shop = createTestShop(shopName);
        shopMapper.insert(shop);

        Long goodId1 = goodMapper.minFreeId();
        Good good1 = createTestGood(goodId1, "商品1", "描述1", shopName, 99.9);
        goodMapper.insert(good1);
        addStock(goodId1, 10L); // 添加10个库存

        Long goodId2 = goodMapper.minFreeId();
        Good good2 = createTestGood(goodId2, "商品2", "描述2", shopName, 199.9);
        goodMapper.insert(good2);
        addStock(goodId2, 5L); // 添加5个库存

        // 测试查询
        List<ProductTransaction> transactions = goodMapper.selectAvalibleGoodsByShopName(shopName);

        // 验证结果
        assertNotNull(transactions);
        assertEquals(2, transactions.size());
        assertTrue(transactions.stream().anyMatch(t -> t.getName().equals(good1.getName())));
        assertTrue(transactions.stream().anyMatch(t -> t.getName().equals(good2.getName())));
    }

    @Test
    public void testDeleteByShopAndName() {
        // 准备测试数据
        String shopName = "测试商店";
        Shop shop = createTestShop(shopName);
        shopMapper.insert(shop);

        String name = "测试商品";
        Long goodId = goodMapper.minFreeId();
        Good good = createTestGood(goodId, name, "这是一个测试商品", shopName, 99.9);
        goodMapper.insert(good);

        // 验证商品存在
        Good retrievedGood = goodMapper.selectGoodByShopAndProductName(shopName, name);
        assertNotNull(retrievedGood);

        // 测试删除
        goodMapper.deleteByShopAndName(shopName, name);

        // 验证商品已被删除
        Good deletedGood = goodMapper.selectGoodByShopAndProductName(shopName, name);
        assertNull(deletedGood);
    }
} 