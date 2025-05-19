package cn.edu.usst.cs.campusAid.mapper.db.complaint;

import cn.edu.usst.cs.campusAid.dto.complaint.ComplaintBlock;
import cn.edu.usst.cs.campusAid.model.complaint.Complaint;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ComplaintMapperTest {

    @Autowired
    private ComplaintMapper complaintMapper;

    private static final AtomicLong counter = new AtomicLong(0);

    private Long generateUniqueId() {
        return LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(8)) * 1000000 + 
               counter.incrementAndGet();
    }

    @Test
    public void testInsertAndGetById() {
        // 准备测试数据
        Long complaintId = generateUniqueId();
        Complaint complaint = Complaint.builder()
                .id(complaintId)
                .senderId(1L)
                .title("测试投诉")
                .description("这是一个测试投诉")
                .block(ComplaintBlock.FORUM_BLOG)
                .complainedID(1L)
                .build();

        // 测试插入
        complaintMapper.insert(complaint);

        // 测试查询
        Complaint retrievedComplaint = complaintMapper.getComplaintById(complaintId);

        // 验证结果
        assertNotNull(retrievedComplaint);
        assertEquals(complaint.getId(), retrievedComplaint.getId());
        assertEquals(complaint.getSenderId(), retrievedComplaint.getSenderId());
        assertEquals(complaint.getTitle(), retrievedComplaint.getTitle());
        assertEquals(complaint.getDescription(), retrievedComplaint.getDescription());
        assertEquals(complaint.getBlock(), retrievedComplaint.getBlock());
        assertEquals(complaint.getComplainedID(), retrievedComplaint.getComplainedID());
    }

    @Test
    public void testListAllComplaints() {
        // 准备测试数据
        Long complaintId1 = generateUniqueId();
        Complaint complaint1 = Complaint.builder()
                .id(complaintId1)
                .senderId(1L)
                .title("测试投诉1")
                .description("这是第一个测试投诉")
                .block(ComplaintBlock.FORUM_BLOG)
                .complainedID(1L)
                .build();

        Long complaintId2 = generateUniqueId();
        Complaint complaint2 = Complaint.builder()
                .id(complaintId2)
                .senderId(2L)
                .title("测试投诉2")
                .description("这是第二个测试投诉")
                .block(ComplaintBlock.ERRAND)
                .complainedID(2L)
                .build();

        // 插入测试数据
        complaintMapper.insert(complaint1);
        complaintMapper.insert(complaint2);

        // 测试查询所有投诉
        List<Complaint> complaints = complaintMapper.listAllComplaints();

        // 验证结果
        assertNotNull(complaints);
        assertTrue(complaints.size() >= 2);
        assertTrue(complaints.stream().anyMatch(c -> c.getId().equals(complaintId1)));
        assertTrue(complaints.stream().anyMatch(c -> c.getId().equals(complaintId2)));
    }

    @Test
    public void testSubmitResult() {
        // 准备测试数据
        Long complaintId = generateUniqueId();
        Complaint complaint = Complaint.builder()
                .id(complaintId)
                .senderId(1L)
                .title("测试投诉")
                .description("这是一个测试投诉")
                .block(ComplaintBlock.FORUM_BLOG)
                .complainedID(1L)
                .build();

        // 插入测试数据
        complaintMapper.insert(complaint);

        // 测试提交处理结果
        String result = "投诉已处理，已对违规用户进行警告";
        complaintMapper.submitResult(complaintId, result);

        // 验证结果
        Complaint retrievedComplaint = complaintMapper.getComplaintById(complaintId);
        assertNotNull(retrievedComplaint);
        assertEquals(result, retrievedComplaint.getResult());
    }

    @Test
    public void testGetComplaintByIdWithNonExistentId() {
        // 测试查询不存在的投诉ID
        Complaint complaint = complaintMapper.getComplaintById(999L);
        assertNull(complaint);
    }
} 