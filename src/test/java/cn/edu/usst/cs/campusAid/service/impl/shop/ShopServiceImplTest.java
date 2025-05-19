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
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShopServiceImplTest {

    @Mock
    private ShopMapper shopMapper;
    @Mock
    private GoodMapper goodMapper;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private TransactionMapper transactionMapper;
    @Mock
    private ProductToGoodTransaction productToGoodTransaction;
    @Mock
    private SqlSessionFactory sqlSessionFactory;
    @Mock
    private SqlSession sqlSession;
    @Mock
    private Connection connection;

    @InjectMocks
    private ShopServiceImpl shopService;

    private static final String TEST_SHOP_NAME = "TestShop";
    private static final String TEST_SHOP_DESCRIPTION = "Test Description";
    private static final String TEST_GOOD_NAME = "TestGood";
    private static final Long TEST_USER_ID = 2235062128L;

    @BeforeEach
    void setUp() throws SQLException {
        // 设置基本的mock行为
        Shop shop = Shop.builder()
                .name(TEST_SHOP_NAME)
                .description(TEST_SHOP_DESCRIPTION)
                .build();
        lenient().when(shopMapper.getShopByName(TEST_SHOP_NAME)).thenReturn(shop);

        // 设置SqlSession的mock行为
        lenient().when(sqlSessionFactory.openSession()).thenReturn(sqlSession);
        lenient().when(sqlSession.getConnection()).thenReturn(connection);
        lenient().doNothing().when(connection).setAutoCommit(false);
        lenient().doNothing().when(sqlSession).commit();
        lenient().doNothing().when(sqlSession).rollback();
        lenient().doNothing().when(sqlSession).close();
    }

    @Test
    void testGetShopInfo_WhenShopExists() {
        // 准备测试数据
        List<ProductTransaction> products = new ArrayList<>();
        ProductTransaction product = ProductTransaction.builder()
                .name(TEST_GOOD_NAME)
                .build();
        products.add(product);

        when(goodMapper.selectGoodsByShopName(TEST_SHOP_NAME)).thenReturn(products);

        // 执行测试
        ShopInfo result = shopService.getShopInfo(TEST_SHOP_NAME, TEST_SHOP_NAME);

        // 验证结果
        assertNotNull(result);
        assertEquals(TEST_SHOP_NAME, result.getName());
        assertEquals(TEST_SHOP_DESCRIPTION, result.getDescription());
        assertEquals(1, result.getProducts().size());
        assertEquals(TEST_GOOD_NAME, result.getProducts().get(0).getName());
    }

    @Test
    void testGetShopInfo_WhenShopDoesNotExist() {
        // 准备测试数据
        when(shopMapper.getShopByName("NonExistentShop")).thenReturn(null);

        // 执行测试
        ShopInfo result = shopService.getShopInfo("NonExistentShop", "NonExistentShop");

        // 验证结果
        assertNotNull(result);
        assertNull(result.getName());
        assertNull(result.getDescription());
        assertNull(result.getProducts());
    }

    @Test
    void testCheckout_WithValidOrder() throws SQLException {
        // 准备测试数据
        List<ProductTransaction> items = new ArrayList<>();
        ProductTransaction item = ProductTransaction.builder()
                .name(TEST_GOOD_NAME)
                .amount(1L)
                .build();
        items.add(item);

        OrderDTO orderDTO = OrderDTO.builder()
                .shopName(TEST_SHOP_NAME)
                .userId(TEST_USER_ID)
                .items(items)
                .build();

        Good good = Good.builder()
                .id(1L)
                .name(TEST_GOOD_NAME)
                .price(10.0)
                .build();

        GoodTransaction goodTransaction = GoodTransaction.builder()
                .id(1L)
                .goodId(1L)
                .orderId(1L)
                .build();

        when(orderMapper.minFreeID()).thenReturn(1L);
        when(goodMapper.selectGoodByShopAndProductName(TEST_SHOP_NAME, TEST_GOOD_NAME))
                .thenReturn(good);
        when(transactionMapper.getAvailableStock(1L)).thenReturn(10L);
        when(transactionMapper.minFreeID()).thenReturn(1L);
        when(productToGoodTransaction.fromProductTransaction(any(ProductTransaction.class)))
                .thenReturn(goodTransaction);

        // 执行测试
        Order result = shopService.checkout(orderDTO);

        // 验证结果
        assertNotNull(result);
        assertEquals(TEST_SHOP_NAME, result.getShopName());
        assertEquals(TEST_USER_ID, result.getBuyerID());
        assertEquals(1L, result.getId());
        assertEquals(10.0, result.getPrice());

        // 验证事务操作
        verify(sqlSession).getConnection();
        verify(connection).setAutoCommit(false);
        verify(sqlSession).commit();
        verify(sqlSession).close();
    }

    @Test
    void testCheckout_WithEmptyOrder() {
        // 准备测试数据
        OrderDTO orderDTO = OrderDTO.builder()
                .items(new ArrayList<>())
                .build();

        // 执行测试并验证异常
        assertThrows(CampusAidException.class, () -> shopService.checkout(orderDTO));
    }

    @Test
    void testCheckout_WithInsufficientStock() throws SQLException {
        // 准备测试数据
        List<ProductTransaction> items = new ArrayList<>();
        ProductTransaction item = ProductTransaction.builder()
                .name(TEST_GOOD_NAME)
                .amount(10L)
                .build();
        items.add(item);

        OrderDTO orderDTO = OrderDTO.builder()
                .shopName(TEST_SHOP_NAME)
                .userId(TEST_USER_ID)
                .items(items)
                .build();

        Good good = Good.builder()
                .id(1L)
                .name(TEST_GOOD_NAME)
                .price(10.0)
                .build();

        when(goodMapper.selectGoodByShopAndProductName(TEST_SHOP_NAME, TEST_GOOD_NAME))
                .thenReturn(good);
        when(transactionMapper.getAvailableStock(1L)).thenReturn(5L);

        // 执行测试并验证异常
        assertThrows(CampusAidException.class, () -> shopService.checkout(orderDTO));

        // 验证事务回滚
        verify(sqlSession).rollback();
        verify(sqlSession).close();
    }

    @Test
    void testGetHistory() {
        // 准备测试数据
        List<ProductTransaction> expectedHistory = new ArrayList<>();
        ProductTransaction transaction = ProductTransaction.builder()
                .name(TEST_GOOD_NAME)
                .build();
        expectedHistory.add(transaction);

        when(transactionMapper.getHistory(TEST_USER_ID)).thenReturn(expectedHistory);

        // 执行测试
        List<ProductTransaction> result = shopService.getHistory(TEST_USER_ID);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(TEST_GOOD_NAME, result.get(0).getName());
    }
} 