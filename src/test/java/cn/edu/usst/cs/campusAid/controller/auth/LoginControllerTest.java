package cn.edu.usst.cs.campusAid.controller.auth;

import cn.edu.usst.cs.campusAid.controller.SessionKeys;
import cn.edu.usst.cs.campusAid.dto.ApiResponse;
import cn.edu.usst.cs.campusAid.dto.auth.LoginRequest;
import cn.edu.usst.cs.campusAid.dto.auth.VerifyRequest;
import cn.edu.usst.cs.campusAid.service.CampusAidException;
import cn.edu.usst.cs.campusAid.service.auth.LoginService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @Mock
    private LoginService loginService;

    @Mock
    private HttpSession session;

    @InjectMocks
    private LoginController loginController;

    private static final Long TEST_USER_ID = 2235062129L;
    private static final String TEST_CODE = "123456";
    private LoginRequest loginRequest;
    private VerifyRequest verifyRequest;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
        loginRequest.setId(TEST_USER_ID);

        verifyRequest = new VerifyRequest();
        verifyRequest.setCode(TEST_CODE);
    }

    @Test
    void testSendLoginCode() throws CampusAidException {
        // 准备测试数据
        when(loginService.generateAndSendCode(anyLong())).thenReturn(TEST_CODE);

        // 执行测试
        ApiResponse<String> response = loginController.sendLoginCode(session, loginRequest);

        // 验证结果
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("验证码已发送", response.getData());

        // 验证会话属性设置
        verify(session).setAttribute(eq(SessionKeys.LOGIN_CODE), eq(TEST_CODE));
        verify(session).setAttribute(eq(SessionKeys.LOGIN_ID), eq(TEST_USER_ID));
        verify(session).setAttribute(eq(SessionKeys.LOGIN_TIME), any(LocalTime.class));

        // 验证服务调用
        verify(loginService).generateAndSendCode(TEST_USER_ID);
    }

    @Test
    void testVerifyLogin() throws CampusAidException {
        // 执行测试
        ApiResponse<String> response = loginController.verifyLogin(TEST_CODE, TEST_USER_ID, verifyRequest, session);

        // 验证结果
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("登录成功", response.getData());

        // 验证会话属性更新
        verify(session).setAttribute(eq(SessionKeys.LOGIN_TIME), any(LocalTime.class));

        // 验证服务调用
        verify(loginService).verifyCode(TEST_USER_ID, TEST_CODE, TEST_CODE);
    }

    @Test
    void testLogout() {
        // 执行测试
        ApiResponse<String> response = loginController.logout(session);

        // 验证结果
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("登出成功", response.getData());

        // 验证会话属性移除
        verify(session).removeAttribute(SessionKeys.LOGIN_ID);
        verify(session).removeAttribute(SessionKeys.LOGIN_TIME);
    }

    @Test
    void testSendLoginCode_WhenServiceThrowsException() throws CampusAidException {
        // 准备测试数据
        when(loginService.generateAndSendCode(anyLong()))
                .thenThrow(new CampusAidException("发送验证码失败"));

        // 执行测试并验证异常
        CampusAidException exception = assertThrows(CampusAidException.class,
                () -> loginController.sendLoginCode(session, loginRequest));

        // 验证异常信息
        assertEquals("发送验证码失败", exception.getMessage());

        // 验证服务调用
        verify(loginService).generateAndSendCode(TEST_USER_ID);
    }

    @Test
    void testVerifyLogin_WhenServiceThrowsException() throws CampusAidException {
        // 准备测试数据
        doThrow(new CampusAidException("验证码错误"))
                .when(loginService).verifyCode(anyLong(), anyString(), anyString());

        // 执行测试并验证异常
        CampusAidException exception = assertThrows(CampusAidException.class,
                () -> loginController.verifyLogin(TEST_CODE, TEST_USER_ID, verifyRequest, session));

        // 验证异常信息
        assertEquals("验证码错误", exception.getMessage());

        // 验证服务调用
        verify(loginService).verifyCode(TEST_USER_ID, TEST_CODE, TEST_CODE);
    }
} 