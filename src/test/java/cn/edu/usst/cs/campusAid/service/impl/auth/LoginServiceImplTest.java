package cn.edu.usst.cs.campusAid.service.impl.auth;

import cn.edu.usst.cs.campusAid.mapper.db.UserMapper;
import cn.edu.usst.cs.campusAid.model.User;
import cn.edu.usst.cs.campusAid.service.CampusAidException;
import cn.edu.usst.cs.campusAid.service.auth.MailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private MailService mailService;

    @InjectMocks
    private LoginServiceImpl loginService;

    private static final Long TEST_USER_ID = 2235062129L;
    private static final String TEST_VERIFICATION_CODE = "123456";

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        // 初始化测试环境
        // 使用反射将mailService注入到BaseAuthService中
        Field mailServiceField = BaseAuthService.class.getDeclaredField("mailService");
        mailServiceField.setAccessible(true);
        mailServiceField.set(loginService, mailService);
    }

    @Test
    void testGenerateAndSendCode_WhenUserExists() throws CampusAidException {
        // 准备测试数据
        User user = User.builder()
                .id(TEST_USER_ID)
                .build();
        when(userMapper.getUserById(TEST_USER_ID)).thenReturn(user);
        doNothing().when(mailService).sendVerificationMail(anyString(), anyString());

        // 执行测试
        String code = loginService.generateAndSendCode(TEST_USER_ID);

        // 验证结果
        assertNotNull(code);
        assertEquals(6, code.length());
        verify(userMapper, times(1)).getUserById(TEST_USER_ID);
        verify(mailService, times(1)).sendVerificationMail(anyString(), anyString());
    }

    @Test
    void testGenerateAndSendCode_WhenUserDoesNotExist() {
        // 准备测试数据
        when(userMapper.getUserById(TEST_USER_ID)).thenReturn(null);

        // 执行测试并验证异常
        assertThrows(CampusAidException.class, () -> loginService.generateAndSendCode(TEST_USER_ID));
        verify(userMapper, times(1)).getUserById(TEST_USER_ID);
        verify(mailService, never()).sendVerificationMail(anyString(), anyString());
    }

    @Test
    void testVerifyCode_WhenCodeIsCorrect() throws CampusAidException {
        // 执行测试
        loginService.verifyCode(TEST_USER_ID, TEST_VERIFICATION_CODE, TEST_VERIFICATION_CODE);

        // 验证结果 - 不应该抛出异常
    }

    @Test
    void testVerifyCode_WhenCodeIsIncorrect() {
        // 执行测试并验证异常
        assertThrows(CampusAidException.class, 
            () -> loginService.verifyCode(TEST_USER_ID, TEST_VERIFICATION_CODE, "654321"));
    }

    @Test
    void testIsUserRegistered_WhenUserExists() throws CampusAidException {
        // 准备测试数据
        User user = User.builder()
                .id(TEST_USER_ID)
                .build();
        when(userMapper.getUserById(TEST_USER_ID)).thenReturn(user);

        // 执行测试
        boolean result = loginService.isUserRegistered(TEST_USER_ID);

        // 验证结果
        assertTrue(result);
        verify(userMapper, times(1)).getUserById(TEST_USER_ID);
    }

    @Test
    void testIsUserRegistered_WhenUserDoesNotExist() throws CampusAidException {
        // 准备测试数据
        when(userMapper.getUserById(TEST_USER_ID)).thenReturn(null);

        // 执行测试
        boolean result = loginService.isUserRegistered(TEST_USER_ID);

        // 验证结果
        assertFalse(result);
        verify(userMapper, times(1)).getUserById(TEST_USER_ID);
    }
} 