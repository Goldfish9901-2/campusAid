package cn.edu.usst.cs.campusAid.service.complaint;

import cn.edu.usst.cs.campusAid.model.complaint.Complaint;
import cn.edu.usst.cs.campusAid.dto.complaint.ComplaintRequest;

import java.util.*;

public interface ComplaintService {
    /**
     * 提交投诉
     * @param complaint 投诉内容
     * @return 投诉 ID
     */
    Long postComplaint(ComplaintRequest complaint);

    List<Complaint> listComplaints(Long userId);

    String processComplaint(Long adminId,Long complaintId,String result,int banLength);
}
