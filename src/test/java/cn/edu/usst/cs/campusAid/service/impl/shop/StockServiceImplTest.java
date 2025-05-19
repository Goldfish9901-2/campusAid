package cn.edu.usst.cs.campusAid.service.impl.shop;

import cn.edu.usst.cs.campusAid.dto.shop.ProductTransaction;
import cn.edu.usst.cs.campusAid.mapper.db.shop.GoodMapper;
import cn.edu.usst.cs.campusAid.mapper.db.shop.OrderMapper;
import cn.edu.usst.cs.campusAid.mapper.db.shop.ShopMapper;
import cn.edu.usst.cs.campusAid.mapper.db.shop.TransactionMapper;
import cn.edu.usst.cs.campusAid.model.shop.Good;
import cn.edu.usst.cs.campusAid.service.CampusAidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceImplTest {

    @Mock
    private ShopMapper shopMapper;
    @Mock
    private GoodMapper goodMapper;
    @Mock
    private TransactionMapper transactionMapper;
    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private StockServiceImpl stockService;

    private static final String TEST_SHOP_NAME = "TestShop";
    private static final String TEST_SHOP_PASSWORD = "password123";
    private static final String TEST_GOOD_NAME = "TestGood";
    private static final String TEST_GOOD_DESCRIPTION = "Test Description";
    private static final Float TEST_GOOD_PRICE = 10.0f;
    private static final Long TEST_GOOD_AMOUNT = 5L;

    @BeforeEach
    void setUp() {
        // 初始化测试环境
    }

    @Test
    void testVerify_WithValidCredentials() {
        // 准备测试数据
        when(shopMapper.verify(TEST_SHOP_NAME, TEST_SHOP_PASSWORD)).thenReturn(TEST_SHOP_NAME);

        // 执行测试
        String result = stockService.verify(TEST_SHOP_NAME, TEST_SHOP_PASSWORD);

        // 验证结果
        assertEquals(TEST_SHOP_NAME, result);
        verify(shopMapper).verify(TEST_SHOP_NAME, TEST_SHOP_PASSWORD);
    }

    @Test
    void testVerify_WithInvalidCredentials() {
        // 准备测试数据
        when(shopMapper.verify("InvalidShop", "InvalidPassword")).thenReturn(null);

        // 执行测试并验证异常
        assertThrows(CampusAidException.class, () -> stockService.verify("InvalidShop", "InvalidPassword"));
    }

    @Test
    void testAddProductToShop_NewProduct() {
        // 准备测试数据
        ProductTransaction productTransaction = ProductTransaction.builder()
                .name(TEST_GOOD_NAME)
                .description(TEST_GOOD_DESCRIPTION)
                .price(TEST_GOOD_PRICE)
                .amount(TEST_GOOD_AMOUNT)
                .build();

        when(goodMapper.selectGoodByShopAndProductName(TEST_SHOP_NAME, TEST_GOOD_NAME))
                .thenReturn(null);
        when(goodMapper.minFreeId()).thenReturn(1L);
        when(transactionMapper.minFreeID()).thenReturn(1L);

        // 执行测试
        Long result = stockService.addProductToShop(TEST_SHOP_NAME, productTransaction);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result);
        verify(goodMapper).insert(any(Good.class));
        verify(transactionMapper).insert(any());
    }

    @Test
    void testAddProductToShop_ExistingProduct() {
        // 准备测试数据
        ProductTransaction productTransaction = ProductTransaction.builder()
                .name(TEST_GOOD_NAME)
                .description(TEST_GOOD_DESCRIPTION)
                .price(TEST_GOOD_PRICE)
                .amount(TEST_GOOD_AMOUNT)
                .build();

        Good existingGood = Good.builder()
                .id(1L)
                .name(TEST_GOOD_NAME)
                .shop(TEST_SHOP_NAME)
                .build();

        when(goodMapper.selectGoodByShopAndProductName(TEST_SHOP_NAME, TEST_GOOD_NAME))
                .thenReturn(existingGood);
        when(transactionMapper.minFreeID()).thenReturn(1L);

        // 执行测试
        Long result = stockService.addProductToShop(TEST_SHOP_NAME, productTransaction);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result);
        verify(goodMapper, never()).insert(any(Good.class));
        verify(transactionMapper).insert(any());
    }

    @Test
    void testAddProductToShop_WrongShop() {
        // 准备测试数据
        ProductTransaction productTransaction = ProductTransaction.builder()
                .name(TEST_GOOD_NAME)
                .description(TEST_GOOD_DESCRIPTION)
                .price(TEST_GOOD_PRICE)
                .amount(TEST_GOOD_AMOUNT)
                .build();

        Good existingGood = Good.builder()
                .id(1L)
                .name(TEST_GOOD_NAME)
                .shop("OtherShop")
                .build();

        when(goodMapper.selectGoodByShopAndProductName(TEST_SHOP_NAME, TEST_GOOD_NAME))
                .thenReturn(existingGood);

        // 执行测试并验证异常
        assertThrows(CampusAidException.class, () -> stockService.addProductToShop(TEST_SHOP_NAME, productTransaction));
    }

    @Test
    void testAddProductToShop_NullProduct() {
        // 执行测试并验证异常
        assertThrows(CampusAidException.class, () -> stockService.addProductToShop(TEST_SHOP_NAME, null));
    }
} 