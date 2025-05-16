package cn.edu.usst.cs.campusAid;
import cn.edu.usst.cs.campusAid.dto.complaint.ComplaintBlock;
import cn.edu.usst.cs.campusAid.mapper.db.complaint.ComplaintMapper;
import cn.edu.usst.cs.campusAid.model.complaint.Complaint;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.FilterType;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
//@Transactional // 所有操作在事务中执行，测试完成后回滚
public class ComplaintMapperTest {


    private static final Logger logger = LoggerFactory.getLogger(ComplaintMapperTest.class);

    @Autowired
    private ComplaintMapper complaintMapper;

    private Long testComplaintId;

    @Test
    void queryById(){
        Long testId=1L;
        Complaint query = complaintMapper.getComplaintById(testId);
        logger.info("查询到插入的投诉: {}", query);
    }
    @Test
    void testInsertAndGetById() {
        // 步骤 1: 获取最小可用ID
        Long minFreeId = complaintMapper.minFreeId();
        logger.info("获取到最小可用ID: {}", minFreeId);
        assertThat(minFreeId).isNotNull().isGreaterThan(0);

        // 步骤 2: 构建投诉对象
        Complaint complaint = Complaint.builder()
                .id(minFreeId)
                .senderId(2235062128L)
                .block(ComplaintBlock.FORUM_BLOG)
                .complainedID(5001L)
                .title("集成测试标题")
                .description("这是集成测试的投诉内容")
                .result(null)
                .build();

        // 步骤 3: 插入投诉
        complaintMapper.insert(complaint);
        testComplaintId = complaint.getId();
        logger.info("成功插入投诉，ID: {}", testComplaintId);

        // 步骤 4: 查询插入的数据
        Complaint inserted = complaintMapper.getComplaintById(testComplaintId);
        logger.info("查询到插入的投诉: {}", inserted);

//        // 验证字段是否一致
//        assertThat(inserted).isNotNull();
//        assertThat(inserted.getId()).isEqualTo(testComplaintId);
//        assertThat(inserted.getSenderId()).isEqualTo(1001L);
//        assertThat(inserted.getTitle()).isEqualTo("集成测试标题");
//        assertThat(inserted.getDescription()).isEqualTo("这是集成测试的投诉内容");
//        assertThat(inserted.getBlock()).isEqualTo(ComplaintBlock.BLOG);
//        assertThat(inserted.getComplainedID()).isEqualTo(5001L);
//        assertThat(inserted.getResult()).isNull();
    }

    @Test
    void testListAllComplaintsAfterInsert() {
        // 先手动插入一条数据供测试
        Long minFreeId = complaintMapper.minFreeId();
        Complaint complaint = Complaint.builder()
                .id(minFreeId)
                .senderId(2235062128L)
                .block(ComplaintBlock.SHOP)
                .complainedID(6001L)
                .title("列表测试标题")
                .description("这是列表测试的投诉内容")
                .result(null)
                .build();

        complaintMapper.insert(complaint);
        logger.info("插入第二条测试数据，ID: {}", complaint.getId());

        // 查询所有投诉
        List<Complaint> complaints = complaintMapper.listAllComplaints();
        logger.info("当前投诉总数: {}", complaints.size());

        assertThat(complaints).isNotEmpty();
        boolean found = complaints.stream().anyMatch(c -> c.getId().equals(complaint.getId()));
        assertThat(found).isTrue();
    }

    @Test
    void testSubmitResultAndUpdate() {
        // 准备测试数据
        Long minFreeId = complaintMapper.minFreeId();
        Complaint complaint = Complaint.builder()
                .id(minFreeId)
                .senderId(2235062128L)
                .block(ComplaintBlock.ERRAND)
                .complainedID(7001L)
                .title("更新测试标题")
                .description("这是更新测试的投诉内容")
                .result(null)
                .build();

        complaintMapper.insert(complaint);
        logger.info("插入更新测试数据，ID: {}", complaint.getId());

        // 提交处理结果
        String resultText = "该投诉属实，已处理";
        complaintMapper.submitResult(complaint.getId(), resultText);
        logger.info("提交处理结果: {}", resultText);

        // 查询并验证结果是否更新
        Complaint updated = complaintMapper.getComplaintById(complaint.getId());
        logger.info("更新后的投诉结果: {}", updated.getResult());

        assertThat(updated).isNotNull();
        assertThat(updated.getResult()).isEqualTo(resultText);
    }
}
