package cn.edu.usst.cs.campusAid.mapper.db.shop;

import cn.edu.usst.cs.campusAid.model.shop.Shop;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ShopMapperTest {

    @Autowired
    private ShopMapper shopMapper;

    private Shop createTestShop(String name, String description, String password) {
        return Shop.builder()
                .name(name)
                .description(description)
                .password(password)
                .build();
    }

    @Test
    public void testInsertAndGetByName() {
        // 准备测试数据
        String shopName = "测试商店";
        String description = "这是一个测试商店";
        String password = "test123";
        Shop shop = createTestShop(shopName, description, password);

        // 测试插入
        shopMapper.insert(shop);

        // 测试查询
        Shop retrievedShop = shopMapper.getShopByName(shopName);

        // 验证结果
        assertNotNull(retrievedShop);
        assertEquals(shop.getName(), retrievedShop.getName());
        assertEquals(shop.getDescription(), retrievedShop.getDescription());
        assertEquals(shop.getPassword(), retrievedShop.getPassword());
    }

    @Test
    public void testVerify() {
        // 准备测试数据
        String shopName = "测试商店";
        String description = "这是一个测试商店";
        String password = "test123";
        Shop shop = createTestShop(shopName, description, password);
        shopMapper.insert(shop);

        // 测试正确的用户名和密码
        String verifiedName = shopMapper.verify(shopName, password);
        assertEquals(shopName, verifiedName);

        // 测试错误的密码
        assertThrows(RuntimeException.class, () -> {
            shopMapper.verify(shopName, "wrong_password");
        });

        // 测试不存在的商店
        assertThrows(RuntimeException.class, () -> {
            shopMapper.verify("non_existent_shop", password);
        });
    }

    @Test
    public void testDeleteByName() {
        // 准备测试数据
        String shopName = "测试商店";
        String description = "这是一个测试商店";
        String password = "test123";
        Shop shop = createTestShop(shopName, description, password);
        shopMapper.insert(shop);

        // 验证商店存在
        Shop retrievedShop = shopMapper.getShopByName(shopName);
        assertNotNull(retrievedShop);

        // 测试删除
        shopMapper.deleteByName(shopName);

        // 验证商店已被删除
        Shop deletedShop = shopMapper.getShopByName(shopName);
        assertNull(deletedShop);
    }
} 