package cn.edu.usst.cs.campusAid.controller.complaint;

import cn.edu.usst.cs.campusAid.config.AdminConfig;
import cn.edu.usst.cs.campusAid.dto.complaint.ComplaintBlock;
import cn.edu.usst.cs.campusAid.dto.complaint.ComplaintRequest;
import cn.edu.usst.cs.campusAid.model.complaint.Complaint;
import cn.edu.usst.cs.campusAid.service.complaint.ComplaintService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComplaintControllerTest {

    @Mock
    private ComplaintService complaintService;

    @Mock
    private AdminConfig adminConfig;

    @InjectMocks
    private ComplaintController complaintController;

    private static final Long TEST_USER_ID = 2235062128L;
    private static final Long TEST_ADMIN_ID = 2235062129L;
    private static final Long TEST_COMPLAINT_ID = 1L;
    private static final String TEST_COMPLAINT_CONTENT = "Test complaint content";
    private static final String TEST_COMPLAINT_RESULT = "Test complaint result";

    private ComplaintRequest testRequest;
    private List<Complaint> testComplaints;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        testRequest = new ComplaintRequest();
        testRequest.setContent(TEST_COMPLAINT_CONTENT);
        testRequest.setBlock(ComplaintBlock.FORUM_BLOG);
        testRequest.setComplainedID(1L);
        testRequest.setTitle("Test Title");

        testComplaints = new ArrayList<>();
        testComplaints.add(Complaint.builder()
                .id(TEST_COMPLAINT_ID)
                .senderId(TEST_USER_ID)
                .title("Test Title")
                .description(TEST_COMPLAINT_CONTENT)
                .block(ComplaintBlock.FORUM_BLOG)
                .complainedID(1L)
                .build());
    }

    @Test
    void testPostComplaint() {
        // 准备测试数据
        when(complaintService.postComplaint(any(ComplaintRequest.class))).thenReturn(TEST_COMPLAINT_ID);

        // 执行测试
        ResponseEntity<String> response = complaintController.postComplaint(TEST_USER_ID, testRequest);

        // 验证结果
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("提交成功"));
        assertTrue(response.getBody().contains(TEST_COMPLAINT_ID.toString()));

        // 验证服务调用
        verify(complaintService).postComplaint(testRequest);
    }

    @Test
    void testListComplaints() {
        // 准备测试数据
        when(complaintService.listComplaints(TEST_ADMIN_ID)).thenReturn(testComplaints);
        doNothing().when(adminConfig).verifyIsAdmin(TEST_ADMIN_ID);

        // 执行测试
        List<Complaint> response = complaintController.listComplaints(TEST_ADMIN_ID);

        // 验证结果
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(TEST_COMPLAINT_ID, response.get(0).getId());
        assertEquals(TEST_COMPLAINT_CONTENT, response.get(0).getDescription());

        // 验证服务调用
        verify(adminConfig).verifyIsAdmin(TEST_ADMIN_ID);
        verify(complaintService).listComplaints(TEST_ADMIN_ID);
    }

    @Test
    void testProcessComplaint() {
        // 准备测试数据
        String expectedResult = "处理成功";
        when(complaintService.processComplaint(eq(TEST_ADMIN_ID), eq(TEST_COMPLAINT_ID), eq(TEST_COMPLAINT_RESULT), eq(7)))
                .thenReturn(expectedResult);
        doNothing().when(adminConfig).verifyIsAdmin(TEST_ADMIN_ID);

        // 执行测试
        ResponseEntity<String> response = complaintController.processComplaint(
                TEST_ADMIN_ID,
                TEST_COMPLAINT_ID,
                TEST_COMPLAINT_RESULT,
                7
        );

        // 验证结果
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResult, response.getBody());

        // 验证服务调用
        verify(adminConfig).verifyIsAdmin(TEST_ADMIN_ID);
        verify(complaintService).processComplaint(TEST_ADMIN_ID, TEST_COMPLAINT_ID, TEST_COMPLAINT_RESULT, 7);
    }

    @Test
    void testProcessComplaint_NoBan() {
        // 准备测试数据
        String expectedResult = "不封禁";
        when(complaintService.processComplaint(eq(TEST_ADMIN_ID), eq(TEST_COMPLAINT_ID), eq(TEST_COMPLAINT_RESULT), eq(0)))
                .thenReturn(expectedResult);
        doNothing().when(adminConfig).verifyIsAdmin(TEST_ADMIN_ID);

        // 执行测试
        ResponseEntity<String> response = complaintController.processComplaint(
                TEST_ADMIN_ID,
                TEST_COMPLAINT_ID,
                TEST_COMPLAINT_RESULT,
                0
        );

        // 验证结果
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResult, response.getBody());

        // 验证服务调用
        verify(adminConfig).verifyIsAdmin(TEST_ADMIN_ID);
        verify(complaintService).processComplaint(TEST_ADMIN_ID, TEST_COMPLAINT_ID, TEST_COMPLAINT_RESULT, 0);
    }
} 