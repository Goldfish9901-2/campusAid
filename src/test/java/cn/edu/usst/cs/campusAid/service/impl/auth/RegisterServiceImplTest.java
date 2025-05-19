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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private MailService mailService;

    @InjectMocks
    private RegisterServiceImpl registerService;

    private static final Long TEST_USER_ID = 2235062129L;
    private static final String TEST_VERIFICATION_CODE = "123456";

    private User testUser;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        testUser = User.builder()
                .id(TEST_USER_ID)
                .build();
    }

    @Test
    void testCompleteRegister_WhenUserNotExists() throws CampusAidException {
        // 准备测试数据
        when(userMapper.getUserById(TEST_USER_ID)).thenReturn(null);
        when(userMapper.insertUser(any(User.class))).thenReturn(1);

        // 执行测试
        registerService.completeRegister(testUser);

        // 验证结果
        verify(userMapper, times(1)).getUserById(TEST_USER_ID);
        verify(userMapper, times(1)).insertUser(testUser);
    }

    @Test
    void testCompleteRegister_WhenUserAlreadyExists() {
        // 准备测试数据
        when(userMapper.getUserById(TEST_USER_ID)).thenReturn(testUser);

        // 执行测试并验证异常
        assertThrows(CampusAidException.class, () -> registerService.completeRegister(testUser));
        verify(userMapper, times(1)).getUserById(TEST_USER_ID);
        verify(userMapper, never()).insertUser(any(User.class));
    }

    @Test
    void testGenerateVerificationCode() throws CampusAidException {
        // 准备测试数据
        doNothing().when(mailService).sendVerificationMail(anyString(), anyString());

        // 执行测试
        String code = registerService.generateVerificationCode(TEST_USER_ID);

        // 验证结果
        assertNotNull(code);
        assertEquals(6, code.length());
        verify(mailService, times(1)).sendVerificationMail(anyString(), anyString());
    }
} 