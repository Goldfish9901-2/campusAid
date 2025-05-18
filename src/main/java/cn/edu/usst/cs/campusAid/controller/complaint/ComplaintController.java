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
     * 处理投诉的控制器方法
     * 该方法用于处理用户提交的投诉，由管理员执行
     * 它依赖于用户的身份验证，确保只有管理员可以处理投诉
     *
     * @param userId      从会话属性中获取的用户ID，用于验证用户是否为管理员
     * @param complaintId 投诉的ID，标识特定的投诉事件
     * @param result      处理投诉的结果，描述投诉处理的结局
     * @param banLength   封禁时长（如果适用），默认为0，表示不进行封禁
     * @return 返回处理结果的HTTP响应，包括一个表示处理成功的消息
     */
    @PostMapping("/process")
    public ResponseEntity<String> processComplaint(
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId,
            @RequestParam("id") Long complaintId,
            @RequestParam("result") String result,
            @RequestParam(value = "ban_length", defaultValue = "0") int banLength
    ) {
        // 验证用户是否为管理员
        adminConfig.verifyIsAdmin(userId);
        // 处理投诉，包括更新投诉状态和执行相应的处理措施
        var submitResult = complaintService.processComplaint(userId, complaintId, result, banLength);
        // 返回处理成功的响应
        return ResponseEntity.ok(submitResult);
    }


}
