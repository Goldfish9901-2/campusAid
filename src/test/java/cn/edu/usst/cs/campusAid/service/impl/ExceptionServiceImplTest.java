package cn.edu.usst.cs.campusAid.service.impl;

import cn.edu.usst.cs.campusAid.mapper.db.ExceptionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExceptionServiceImplTest {

    @Mock
    private ExceptionMapper exceptionMapper;

    @InjectMocks
    private ExceptionServiceImpl exceptionService;

    private static final long TEST_EXCEPTION_ID = 1L;
    private static final String TEST_EXCEPTION_MESSAGE = "Test exception message";

    @BeforeEach
    void setUp() {
        // 初始化测试环境
    }

    @Test
    void testReportException() {
        // 准备测试数据
        Exception testException = new RuntimeException(TEST_EXCEPTION_MESSAGE);
        when(exceptionMapper.submitException(anyString())).thenReturn(TEST_EXCEPTION_ID);

        // 执行测试
        long result = exceptionService.reportException(testException);

        // 验证结果
        assertEquals(TEST_EXCEPTION_ID, result);
        verify(exceptionMapper).submitException(anyString());
    }

    @Test
    void testQueryException() {
        // 准备测试数据
        when(exceptionMapper.queryException(TEST_EXCEPTION_ID)).thenReturn(TEST_EXCEPTION_MESSAGE);

        // 执行测试
        String result = exceptionService.queryException(TEST_EXCEPTION_ID);

        // 验证结果
        assertEquals(TEST_EXCEPTION_MESSAGE, result);
        verify(exceptionMapper).queryException(TEST_EXCEPTION_ID);
    }

    @Test
    void testReportExceptionWithNullException() {
        // 执行测试并验证异常
        assertThrows(NullPointerException.class, () -> exceptionService.reportException(null));
    }

    @Test
    void testQueryExceptionWithInvalidId() {
        // 准备测试数据
        long invalidId = -1L;
        when(exceptionMapper.queryException(invalidId)).thenReturn(null);

        // 执行测试
        String result = exceptionService.queryException(invalidId);

        // 验证结果
        assertNull(result);
        verify(exceptionMapper).queryException(invalidId);
    }
} 