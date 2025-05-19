package cn.edu.usst.cs.campusAid.controller.shop;

import cn.edu.usst.cs.campusAid.dto.shop.Order;
import cn.edu.usst.cs.campusAid.dto.shop.OrderDTO;
import cn.edu.usst.cs.campusAid.dto.shop.ProductTransaction;
import cn.edu.usst.cs.campusAid.dto.shop.ShopInfo;
import cn.edu.usst.cs.campusAid.service.CampusAidException;
import cn.edu.usst.cs.campusAid.service.auth.UserService;
import cn.edu.usst.cs.campusAid.service.shop.ShopService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShopControllerTest {

    @Mock
    private ShopService shopService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ShopController shopController;

    private static final Long TEST_USER_ID = 2235062129L;
    private static final String TEST_SHOP_NAME = "Test Shop";
    private static final String TEST_SHOP_NAME_STORED = "2235062129"; // 学生账号
    private static final String TEST_PRODUCT_NAME = "Test Product";
    private static final Long TEST_PRODUCT_AMOUNT = 2L;

    private OrderDTO testOrder;
    private ProductTransaction testTransaction;
    private ShopInfo testShopInfo;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        testTransaction = ProductTransaction.builder()
                .name(TEST_PRODUCT_NAME)
                .amount(TEST_PRODUCT_AMOUNT)
                .build();

        testOrder = OrderDTO.builder()
                .userId(TEST_USER_ID)
                .items(Arrays.asList(testTransaction))
                .build();

        testShopInfo = ShopInfo.builder()
                .name(TEST_SHOP_NAME)
                .build();
    }

    @Test
    void testGetShopInfo_WhenViewingOwnShop() {
        // 准备测试数据
        when(shopService.getShopInfo(TEST_SHOP_NAME, TEST_SHOP_NAME_STORED)).thenReturn(testShopInfo);

        // 执行测试
        ResponseEntity<ShopInfo> response = shopController.getShopInfo(TEST_SHOP_NAME_STORED, TEST_SHOP_NAME);

        // 验证结果
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testShopInfo, response.getBody());

        // 验证服务调用
        verify(shopService).getShopInfo(TEST_SHOP_NAME, TEST_SHOP_NAME_STORED);
    }

    @Test
    void testGetShopInfo_WhenViewingOtherShop() {
        // 准备测试数据
        String otherShopName = "Other Shop";
        when(shopService.getShopInfo(otherShopName, TEST_SHOP_NAME_STORED)).thenReturn(testShopInfo);

        // 执行测试
        ResponseEntity<ShopInfo> response = shopController.getShopInfo(TEST_SHOP_NAME_STORED, otherShopName);

        // 验证结果
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testShopInfo, response.getBody());

        // 验证服务调用
        verify(shopService).getShopInfo(otherShopName, TEST_SHOP_NAME_STORED);
    }

    @Test
    void testCheckout() {
        // 准备测试数据
        String userId = TEST_USER_ID.toString();
        Order order = Order.builder()
                .id(1L)
                .buyerID(TEST_USER_ID)
                .price(100.0)
                .build();
        when(shopService.checkout(any(OrderDTO.class))).thenReturn(order);

        // 执行测试
        ResponseEntity<?> response = shopController.checkout(userId, testOrder);

        // 验证结果
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(order, response.getBody());

        // 验证服务调用
        verify(shopService).checkout(testOrder);
    }

    @Test
    void testCheckout_WhenUserIdMismatch() {
        // 准备测试数据
        String userId = "2235062130"; // 不同的用户ID
        testOrder.setUserId(2235062129L);

        // 执行测试并验证异常
        CampusAidException exception = assertThrows(CampusAidException.class,
                () -> shopController.checkout(userId, testOrder));

        // 验证异常信息
        assertEquals("用户ID不匹配，请本人登陆下单", exception.getMessage());

        // 验证服务调用
        verify(shopService, never()).checkout(any(OrderDTO.class));
    }

    @Test
    void testGetHistory_WhenViewingOwnHistory() {
        // 准备测试数据
        List<ProductTransaction> expectedHistory = Arrays.asList(testTransaction);
        when(userService.getTargetUserId(TEST_USER_ID, TEST_USER_ID)).thenReturn(TEST_USER_ID);
        when(shopService.getHistory(TEST_USER_ID)).thenReturn(expectedHistory);

        // 执行测试
        List<ProductTransaction> response = shopController.getHistory(TEST_USER_ID, TEST_USER_ID);

        // 验证结果
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(testTransaction, response.get(0));

        // 验证服务调用
        verify(userService).getTargetUserId(TEST_USER_ID, TEST_USER_ID);
        verify(shopService).getHistory(TEST_USER_ID);
    }

    @Test
    void testGetHistory_WhenViewingOtherHistory() {
        // 准备测试数据
        Long otherUserId = 2235062130L;
        List<ProductTransaction> expectedHistory = Arrays.asList(testTransaction);
        when(userService.getTargetUserId(TEST_USER_ID, otherUserId)).thenReturn(TEST_USER_ID);
        when(shopService.getHistory(TEST_USER_ID)).thenReturn(expectedHistory);

        // 执行测试
        List<ProductTransaction> response = shopController.getHistory(TEST_USER_ID, otherUserId);

        // 验证结果
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(testTransaction, response.get(0));

        // 验证服务调用
        verify(userService).getTargetUserId(TEST_USER_ID, otherUserId);
        verify(shopService).getHistory(TEST_USER_ID);
    }
} 