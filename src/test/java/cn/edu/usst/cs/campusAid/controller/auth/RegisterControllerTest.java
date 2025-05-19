package cn.edu.usst.cs.campusAid.controller.auth;

import cn.edu.usst.cs.campusAid.controller.SessionKeys;
import cn.edu.usst.cs.campusAid.dto.ApiResponse;
import cn.edu.usst.cs.campusAid.dto.auth.VerifyRequest;
import cn.edu.usst.cs.campusAid.model.User;
import cn.edu.usst.cs.campusAid.service.CampusAidException;
import cn.edu.usst.cs.campusAid.service.auth.RegisterService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterControllerTest {

    @Mock
    private RegisterService registerService;

    @Mock
    private HttpSession session;

    @InjectMocks
    private RegisterController registerController;

    private static final Long TEST_USER_ID = 2235062129L;
    private static final String TEST_USER_NAME = "Test User";
    private static final Long TEST_PHONE = 13800138000L;
    private static final String TEST_CODE = "123456";

    private User testUser;
    private VerifyRequest verifyRequest;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(TEST_USER_ID)
                .name(TEST_USER_NAME)
                .phoneNumber(TEST_PHONE)
                .build();

        verifyRequest = new VerifyRequest();
        verifyRequest.setCode(TEST_CODE);
    }

    @Test
    void testSendRegisterCode() throws CampusAidException {
        // 准备测试数据
        when(registerService.generateVerificationCode(anyLong())).thenReturn(TEST_CODE);

        // 执行测试
        ApiResponse<String> response = registerController.sendRegisterCode(session, testUser);

        // 验证结果
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("验证码已发送", response.getData());

        // 验证会话属性设置
        verify(session).setAttribute(eq(SessionKeys.REG_ID), eq(TEST_USER_ID));
        verify(session).setAttribute(eq(SessionKeys.REG_NAME), eq(TEST_USER_NAME));
        verify(session).setAttribute(eq(SessionKeys.REG_PHONE), eq(TEST_PHONE));
        verify(session).setAttribute(eq(SessionKeys.REG_CODE), eq(TEST_CODE));

        // 验证服务调用
        verify(registerService).generateVerificationCode(TEST_USER_ID);
    }

    @Test
    void testVerifyRegister() throws CampusAidException {
        // 执行测试
        ApiResponse<String> response = registerController.verifyRegister(
                TEST_CODE, TEST_USER_NAME, TEST_USER_ID, TEST_PHONE, verifyRequest, session);

        // 验证结果
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("注册成功", response.getData());

        // 验证会话失效
        verify(session).invalidate();

        // 验证服务调用
        verify(registerService).completeRegister(any(User.class));
    }

    @Test
    void testSendRegisterCode_WhenServiceThrowsException() throws CampusAidException {
        // 准备测试数据
        when(registerService.generateVerificationCode(anyLong()))
                .thenThrow(new CampusAidException("发送验证码失败"));

        // 执行测试并验证异常
        CampusAidException exception = assertThrows(CampusAidException.class,
                () -> registerController.sendRegisterCode(session, testUser));

        // 验证异常信息
        assertEquals("发送验证码失败", exception.getMessage());

        // 验证服务调用
        verify(registerService).generateVerificationCode(TEST_USER_ID);
    }

    @Test
    void testVerifyRegister_WhenCodeMismatch() throws CampusAidException {
        // 执行测试并验证异常
        CampusAidException exception = assertThrows(CampusAidException.class,
                () -> registerController.verifyRegister(
                        "wrong_code", TEST_USER_NAME, TEST_USER_ID, TEST_PHONE, verifyRequest, session));

        // 验证异常信息
        assertEquals("验证码错误或已过期", exception.getMessage());
    }
} 