package cn.edu.usst.cs.campusAid.service.impl.auth;

import cn.edu.usst.cs.campusAid.service.CampusAidException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailServiceImplTest {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private MailServiceImpl mailService;

    private static final String TEST_USER_ID = "2235062129";
    private static final String TEST_VERIFICATION_CODE = "123456";
    private static final String TEST_SENDER_ADDR = "test@example.com";

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        // 使用反射设置sender_addr字段
        Field senderAddrField = MailServiceImpl.class.getDeclaredField("sender_addr");
        senderAddrField.setAccessible(true);
        senderAddrField.set(mailService, TEST_SENDER_ADDR);
    }

    @Test
    void testSendVerificationMail_Success() throws CampusAidException, MessagingException {
        // 准备测试数据
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("Test email content");

        // 执行测试
        mailService.sendVerificationMail(TEST_USER_ID, TEST_VERIFICATION_CODE);

        // 验证结果
        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void testSendVerificationMail_WhenSenderAddrIsNull() throws NoSuchFieldException, IllegalAccessException {
        // 准备测试数据
        Field senderAddrField = MailServiceImpl.class.getDeclaredField("sender_addr");
        senderAddrField.setAccessible(true);
        senderAddrField.set(mailService, null);

        // 执行测试并验证异常
        assertThrows(CampusAidException.class, 
            () -> mailService.sendVerificationMail(TEST_USER_ID, TEST_VERIFICATION_CODE));
    }

    @Test
    void testSendVerificationMail_WhenMessagingExceptionOccurs() {
        // 准备测试数据
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("Test email content");
        doThrow(new MailException("Test exception") {}).when(javaMailSender).send(any(MimeMessage.class));

        // 执行测试并验证异常
        assertThrows(CampusAidException.class, 
            () -> mailService.sendVerificationMail(TEST_USER_ID, TEST_VERIFICATION_CODE));
    }
} 