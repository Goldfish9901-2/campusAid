package cn.edu.usst.cs.campusAid.controller.complaint;

import cn.edu.usst.cs.campusAid.config.AdminConfig;
import cn.edu.usst.cs.campusAid.controller.SessionKeys;
import cn.edu.usst.cs.campusAid.dto.complaint.ComplaintDetail;
import cn.edu.usst.cs.campusAid.dto.complaint.ComplaintRequest;
import cn.edu.usst.cs.campusAid.service.CampusAidException;
import cn.edu.usst.cs.campusAid.service.complaint.ComplaintService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaint")
public class ComplaintController {

    private ComplaintService complaintService;
    private final AdminConfig adminConfig;

    public ComplaintController(AdminConfig adminConfig) {
        this.adminConfig = adminConfig;
    }

    @GetMapping
    public List<ComplaintDetail> listComplaints(
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId
    ) {
        if (!adminConfig.getAdmin().equals(userId.toString()))
            throw new CampusAidException("无权限");
        return complaintService.listComplaints(userId);
    }

    @PostMapping
    public ResponseEntity<String> postComplaint(
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId,
            @RequestBody ComplaintRequest complaint
    ) {
        complaint.setUserId(userId);
        var id = complaintService.postComplaint(complaint);
        return ResponseEntity.ok("提交成功。系统内编号：" + id);
    }

    @PostMapping("/process")
    public ResponseEntity<String> processComplaint(
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId,
            @RequestParam("id") Long complaintId
    ) {
        if (!adminConfig.getAdmin().equals(userId.toString()))
            throw new CampusAidException("无权限");
        complaintService.processComplaint(complaintId);
        return ResponseEntity.ok("处理成功");
    }

}
