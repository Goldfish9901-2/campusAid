package cn.edu.usst.cs.campusAid.mapper.db.complaint;

import cn.edu.usst.cs.campusAid.model.complaint.Complaint;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ComplaintMapper {
    Long minFreeId();

    void insert(Complaint complaint);

    List<Complaint> listAllComplaints();

    Complaint getComplaintById(Long complaintId);

    void submitResult(Long complaintId, String result);
}
