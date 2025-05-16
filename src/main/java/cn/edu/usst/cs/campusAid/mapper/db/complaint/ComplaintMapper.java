package cn.edu.usst.cs.campusAid.mapper.db.complaint;

import cn.edu.usst.cs.campusAid.model.complaint.Complaint;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 投诉信息数据访问接口。
 * 提供对数据库中投诉记录的增删改查操作。
 */
@Mapper
public interface ComplaintMapper {
    /**
     * 查询最小可用的投诉ID（用于插入新投诉）。
     *
     * @return 最小可用的投诉ID
     */
    Long minFreeId();

    /**
     * 插入新的投诉记录到数据库。
     *
     * @param complaint 要插入的投诉对象
     */
    void insert(Complaint complaint);

    /**
     * 获取所有投诉记录列表。
     *
     * @return 所有投诉记录的列表
     */
    List<Complaint> listAllComplaints();

    /**
     * 根据投诉ID获取投诉信息。
     *
     * @param complaintId 投诉ID
     * @return 对应ID的投诉对象
     */
    Complaint getComplaintById(Long complaintId);

    /**
     * 更新投诉处理结果。
     *
     * @param complaintId 投诉ID
     * @param result      处理结果描述
     */
    void submitResult(Long complaintId, String result);
}