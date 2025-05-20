package cn.edu.usst.cs.campusAid.integration;

import cn.edu.usst.cs.campusAid.dto.complaint.ComplaintBlock;
import cn.edu.usst.cs.campusAid.dto.complaint.ComplaintRequest;
import cn.edu.usst.cs.campusAid.model.User;
import cn.edu.usst.cs.campusAid.model.complaint.Complaint;
import cn.edu.usst.cs.campusAid.mapper.db.UserMapper;
import cn.edu.usst.cs.campusAid.mapper.db.complaint.ComplaintMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(properties = {
    "jdk.instrument.traceUsage=false",
    "jdk.instrument.ignoreAgent=true"
})
public class ComplaintFlowIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ComplaintMapper complaintMapper;

    private User testUser;
    private User testAdmin;
    private Complaint testComplaint;
    private String userSessionCookie;
    private String adminSessionCookie;

    @BeforeEach
    void setUp() {
        // 创建测试用户
        testUser = createTestUser("测试用户", 13800138000L);

        // 创建测试管理员
        testAdmin = createTestUser("测试管理员", 13800138001L);

        // 创建测试投诉
        testComplaint = Complaint.builder()
                .id(generateUniqueId())
                .senderId(testUser.getId())
                .title("测试投诉")
                .description("这是一个测试投诉的内容")
                .block(ComplaintBlock.FORUM_BLOG)
                .complainedID(1L)
                .build();
        complaintMapper.insert(testComplaint);

        // 登录获取Session
        userSessionCookie = loginAndGetSessionCookie(String.valueOf(testUser.getPhoneNumber()), "123456");
        adminSessionCookie = loginAndGetSessionCookie(String.valueOf(testAdmin.getPhoneNumber()), "123456");
    }

    @Test
    void testSubmitComplaint() {
        // 1. 提交投诉
        ComplaintRequest request = new ComplaintRequest();
        request.setTitle("新投诉");
        request.setContent("这是一个新的投诉");
        request.setBlock(ComplaintBlock.FORUM_BLOG);
        request.setComplainedID(2L);

        HttpEntity<?> submitEntity = new HttpEntity<>(request, createSessionHeaders(userSessionCookie));
        ResponseEntity<String> submitResponse = restTemplate.exchange(
            getBaseUrl() + "/api/complaint",
            HttpMethod.POST,
            submitEntity,
            String.class
        );
        assertEquals(HttpStatus.OK, submitResponse.getStatusCode());
        assertNotNull(submitResponse.getBody());
        assertTrue(submitResponse.getBody().contains("提交成功"));
    }

    @Test
    void testListComplaints() {
        // 1. 获取投诉列表（管理员）
        HttpEntity<?> listEntity = new HttpEntity<>(createSessionHeaders(adminSessionCookie));
        ResponseEntity<List<Complaint>> listResponse = restTemplate.exchange(
            getBaseUrl() + "/api/complaint",
            HttpMethod.GET,
            listEntity,
            new ParameterizedTypeReference<List<Complaint>>() {}
        );
        assertEquals(HttpStatus.OK, listResponse.getStatusCode());
        assertNotNull(listResponse.getBody());
        assertFalse(listResponse.getBody().isEmpty());

        // 2. 验证投诉内容
        List<Complaint> complaints = listResponse.getBody();
        Complaint complaint = complaints.get(0);
        assertEquals(testComplaint.getTitle(), complaint.getTitle());
        assertEquals(testComplaint.getDescription(), complaint.getDescription());
        assertEquals(testComplaint.getBlock(), complaint.getBlock());
        assertEquals(testComplaint.getComplainedID(), complaint.getComplainedID());
    }

    @Test
    void testProcessComplaint() {
        // 1. 处理投诉（管理员）
        HttpEntity<?> processEntity = new HttpEntity<>(createSessionHeaders(adminSessionCookie));
        ResponseEntity<String> processResponse = restTemplate.exchange(
            getBaseUrl() + "/api/complaint/process?id=" + testComplaint.getId() + 
            "&result=已处理&ban_length=7",
            HttpMethod.POST,
            processEntity,
            String.class
        );
        assertEquals(HttpStatus.OK, processResponse.getStatusCode());
        assertNotNull(processResponse.getBody());
        assertTrue(processResponse.getBody().contains("处理成功"));

        // 2. 验证投诉处理结果
        Complaint updatedComplaint = complaintMapper.getComplaintById(testComplaint.getId());
        assertNotNull(updatedComplaint);
        assertEquals("已处理", updatedComplaint.getResult());
    }
} 