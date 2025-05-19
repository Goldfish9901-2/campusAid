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

    private final ComplaintService complaintService;  // 构造函数注入
    private final AdminConfig adminConfig;

    // 构造函数注入
    public ComplaintController(ComplaintService complaintService, AdminConfig adminConfig) {
        this.complaintService = complaintService;
        this.adminConfig = adminConfig;
    }

    /**
     * 获取所有投诉列表，只有管理员可以查看
     */
    @GetMapping
    public List<Complaint> listComplaints(
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId
    ) {
        adminConfig.verifyIsAdmin(userId); // 验证管理员权限
        return complaintService.listComplaints(userId);  // 获取投诉列表
    }

    /**
     * 用户提交投诉
     * @param userId 用户ID
     * @param complaint 投诉内容
     * @return 提交结果
     */
    @PostMapping
    public ResponseEntity<String> postComplaint(
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId,
            @RequestBody ComplaintRequest complaint
    ) {
        complaint.setUserId(userId);
        var id = complaintService.postComplaint(complaint);
        return ResponseEntity.ok("提交成功。系统内编号：" + id);  // 返回投诉提交成功消息
    }

    /**
     * 处理投诉
     * @param userId 用户ID
     * @param complaintId 投诉ID
     * @param result 处理结果
     * @param banLength 封禁时长（默认0，不封禁）
     * @return 处理结果
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

        // 验证封禁时长是否大于0才执行封禁（防止无效封禁）
        if (banLength > 0) {
            // 执行封禁逻辑（例如记录封禁时长）
        }

        // 处理投诉
        var submitResult = complaintService.processComplaint(userId, complaintId, result, banLength);

        // 返回处理成功的响应
        return ResponseEntity.ok(submitResult);
    }
}

