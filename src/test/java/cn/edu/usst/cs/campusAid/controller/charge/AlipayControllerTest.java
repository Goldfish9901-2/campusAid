package cn.edu.usst.cs.campusAid.controller.charge;

import cn.edu.usst.cs.campusAid.config.AlipayTemplate;
import cn.edu.usst.cs.campusAid.controller.SessionKeys;
import cn.edu.usst.cs.campusAid.model.charge.Charge;
import cn.edu.usst.cs.campusAid.service.CampusAidException;
import cn.edu.usst.cs.campusAid.service.auth.UserService;
import cn.edu.usst.cs.campusAid.service.charge.ChargeService;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlipayControllerTest {

    @Mock
    private SpringTemplateEngine templateEngine;

    @Mock
    private ChargeService chargeService;

    @Mock
    private UserService userService;

    @Mock
    private AlipayTemplate alipayTemplate;

    private AlipayController alipayController;

    private static final Long TEST_USER_ID = 2235062129L;
    private static final double TEST_AMOUNT = 100.0;
    private static final Long TEST_ORDER_ID = 1L;

    private HttpSession session;
    private Charge testCharge;
    private AlipayTradePagePayResponse testResponse;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        session = new MockHttpSession();
        session.setAttribute(SessionKeys.LOGIN_ID, TEST_USER_ID);

        testCharge = Charge.builder()
                .id(TEST_ORDER_ID)
                .userId(TEST_USER_ID)
                .amount(TEST_AMOUNT)
                .chargeTime(LocalDateTime.now())
                .build();

        testResponse = new AlipayTradePagePayResponse();
        testResponse.setBody("<form>支付宝支付表单</form>");

        // 初始化控制器
        alipayController = new AlipayController(templateEngine, chargeService, userService);
        alipayController.alipayTemplate = alipayTemplate;
    }

    @Test
    void testPay_WhenNoPendingCharge() throws Exception {
        // 准备测试数据
        when(alipayTemplate.pay(any(Charge.class))).thenReturn(testResponse);

        // 执行测试
        String response = alipayController.pay(TEST_AMOUNT, TEST_USER_ID, null, session);

        // 验证结果
        assertNotNull(response);
        assertTrue(response.contains("支付宝支付表单"));

        // 验证服务调用
        verify(alipayTemplate).pay(any(Charge.class));
        assertNotNull(session.getAttribute(SessionKeys.PENDING_CHARGE_REQUEST));
    }

    @Test
    void testPay_WhenHasPendingCharge() throws Exception {
        // 准备测试数据
        session.setAttribute(SessionKeys.PENDING_CHARGE_REQUEST, testCharge);

        // 执行测试并验证异常
        CampusAidException exception = assertThrows(CampusAidException.class,
                () -> alipayController.pay(TEST_AMOUNT, TEST_USER_ID, testCharge, session));

        // 验证异常信息
        assertEquals("请先完成之前的充值", exception.getMessage());

        // 验证服务调用
        verify(alipayTemplate, never()).pay(any(Charge.class));
    }

    @Test
    void testCancel() {
        // 准备测试数据
        session.setAttribute(SessionKeys.PENDING_CHARGE_REQUEST, testCharge);

        // 执行测试
        String response = alipayController.cancel(session);

        // 验证结果
        assertNotNull(response);
        assertTrue(response.contains("window.location.href"));

        // 验证session已被清除
        assertNull(session.getAttribute(SessionKeys.PENDING_CHARGE_REQUEST));
    }

    @Test
    void testPayNotify_WhenTradeSuccess() throws Exception {
        // 准备测试数据
        Map<String, String> params = new HashMap<>();
        params.put("trade_status", "TRADE_SUCCESS");
        params.put("out_trade_no", TEST_ORDER_ID.toString());
        params.put("total_amount", String.valueOf(TEST_AMOUNT));
        session.setAttribute(SessionKeys.PENDING_CHARGE_REQUEST, testCharge);

        try (MockedStatic<Factory> factoryMockedStatic = mockStatic(Factory.class)) {
            // 模拟Factory.Payment.Common().verifyNotify()返回true
            factoryMockedStatic.when(() -> {
                Factory.Payment.Common().verifyNotify(params);
            }).thenAnswer(invocation -> true);

            // 执行测试
            String response = alipayController.payNotify(
                    "TRADE_SUCCESS",
                    TEST_ORDER_ID.toString(),
                    "2024-01-01 12:00:00",
                    "2024010122001100000000000001",
                    "校园充值",
                    TEST_AMOUNT,
                    "2088102122458832",
                    "100.00",
                    params,
                    session
            );

            // 验证结果
            assertEquals("success", response);

            // 验证服务调用
            verify(chargeService).recordCharge(testCharge);
            assertNull(session.getAttribute(SessionKeys.PENDING_CHARGE_REQUEST));
        }
    }

    @Test
    void testPayNotify_WhenTradeFailed() throws Exception {
        // 准备测试数据
        Map<String, String> params = new HashMap<>();
        params.put("trade_status", "TRADE_FAILED");
        session.setAttribute(SessionKeys.PENDING_CHARGE_REQUEST, testCharge);

        try (MockedStatic<Factory> factoryMockedStatic = mockStatic(Factory.class)) {
            // 模拟Factory.Payment.Common().verifyNotify()返回true
            factoryMockedStatic.when(() -> {
                Factory.Payment.Common().verifyNotify(params);
            }).thenAnswer(invocation -> true);

            // 执行测试并验证异常
            CampusAidException exception = assertThrows(CampusAidException.class,
                    () -> alipayController.payNotify(
                            "TRADE_FAILED",
                            TEST_ORDER_ID.toString(),
                            "2024-01-01 12:00:00",
                            "2024010122001100000000000001",
                            "校园充值",
                            TEST_AMOUNT,
                            "2088102122458832",
                            "100.00",
                            params,
                            session
                    ));

            // 验证异常信息
            assertEquals("支付失败", exception.getMessage());

            // 验证服务调用
            verify(chargeService, never()).recordCharge(any(Charge.class));
            assertNull(session.getAttribute(SessionKeys.PENDING_CHARGE_REQUEST));
        }
    }

    @Test
    void testGetHistory_WhenViewingOwnHistory() {
        // 准备测试数据
        List<Charge> expectedCharges = List.of(testCharge);
        when(userService.getTargetUserId(TEST_USER_ID, TEST_USER_ID)).thenReturn(TEST_USER_ID);
        when(chargeService.getHistory(TEST_USER_ID)).thenReturn(expectedCharges);

        // 执行测试
        List<Charge> response = alipayController.getHistory(TEST_USER_ID, TEST_USER_ID);

        // 验证结果
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(testCharge, response.get(0));

        // 验证服务调用
        verify(userService).getTargetUserId(TEST_USER_ID, TEST_USER_ID);
        verify(chargeService).getHistory(TEST_USER_ID);
    }

    @Test
    void testGetHistory_WhenViewingOtherHistory() {
        // 准备测试数据
        Long otherUserId = 2235062130L;
        List<Charge> expectedCharges = List.of(testCharge);
        when(userService.getTargetUserId(TEST_USER_ID, otherUserId)).thenReturn(TEST_USER_ID);
        when(chargeService.getHistory(TEST_USER_ID)).thenReturn(expectedCharges);

        // 执行测试
        List<Charge> response = alipayController.getHistory(TEST_USER_ID, otherUserId);

        // 验证结果
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(testCharge, response.get(0));

        // 验证服务调用
        verify(userService).getTargetUserId(TEST_USER_ID, otherUserId);
        verify(chargeService).getHistory(TEST_USER_ID);
    }
} 