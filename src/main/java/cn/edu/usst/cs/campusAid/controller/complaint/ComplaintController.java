package cn.edu.usst.cs.campusAid.controller.complaint;

import cn.edu.usst.cs.campusAid.config.AdminConfig;
import cn.edu.usst.cs.campusAid.controller.SessionKeys;
import cn.edu.usst.cs.campusAid.dto.complaint.ComplaintRequest;
import cn.edu.usst.cs.campusAid.model.complaint.Complaint;
import cn.edu.usst.cs.campusAid.service.complaint.ComplaintService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaint")
public class ComplaintController {

    private final ComplaintService complaintService;
    private final AdminConfig adminConfig;

    public ComplaintController(ComplaintService complaintService, AdminConfig adminConfig) {
        this.complaintService = complaintService;
        this.adminConfig = adminConfig;
    }

    @GetMapping
    public List<Complaint> listComplaints(
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId
    ) {
        adminConfig.verifyIsAdmin(userId);
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

    /**
     * 管理员处理投诉接口
     * 1. 需要管理员权限验证
     * 2. 支持设置处理结果和封禁时长
     * 3. 返回处理结果信息
     */
    @PostMapping("/process")
    public ResponseEntity<String> processComplaint(
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId,
            @RequestParam("id") Long complaintId,
            @RequestParam("result") String result,
            @RequestParam(value = "ban_length", defaultValue = "0") int banLength
    ) {
        // 管理员权限校验
        adminConfig.verifyIsAdmin(userId);
        
        // 调用服务层处理投诉
        var submitResult = complaintService.processComplaint(userId, complaintId, result, banLength);
        
        return ResponseEntity.ok(submitResult);
    }
}
