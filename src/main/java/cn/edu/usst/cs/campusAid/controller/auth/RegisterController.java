package cn.edu.usst.cs.campusAid.controller.auth;

import cn.edu.usst.cs.campusAid.config.AdminConfig;
import cn.edu.usst.cs.campusAid.controller.SessionKeys;
import cn.edu.usst.cs.campusAid.dto.auth.VerifyRequest;
import cn.edu.usst.cs.campusAid.dto.ApiResponse;
import cn.edu.usst.cs.campusAid.service.CampusAidException;
import cn.edu.usst.cs.campusAid.model.User;
import cn.edu.usst.cs.campusAid.service.auth.RegisterService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static cn.edu.usst.cs.campusAid.controller.SessionKeys.REG_CODE;

@RestController
@RequestMapping("/api/register")
public class RegisterController {

    private RegisterService registerService;
    private final AdminConfig adminConfig;

    public RegisterController(AdminConfig adminConfig) {
        this.adminConfig = adminConfig;
    }

    @Autowired
    public RegisterController(RegisterService registerService, AdminConfig adminConfig) {
        this.registerService = registerService;
        this.adminConfig = adminConfig;
    }

    @PostMapping("/")
    public ApiResponse<String> sendRegisterCode(
            HttpSession session,
            @RequestBody User user
    ) throws CampusAidException {
        String code = registerService.generateVerificationCode(user.getId());
        session.setAttribute(SessionKeys.REG_ID, user.getId());
        session.setAttribute(SessionKeys.REG_NAME, user.getName());
        session.setAttribute(SessionKeys.REG_PHONE, user.getPhoneNumber());

        session.setAttribute(REG_CODE, code);
        return ApiResponse.success("验证码已发送");
    }

    @PostMapping("/verify")
    public ApiResponse<String> verifyRegister(
            @SessionAttribute(REG_CODE) String sessionCode,
            @SessionAttribute(SessionKeys.REG_NAME) String name,
            @SessionAttribute(SessionKeys.REG_ID) Long id,
            @SessionAttribute(SessionKeys.REG_PHONE) Long phone,
            @RequestBody VerifyRequest request,
            HttpSession session
    ) throws CampusAidException {
        if (!sessionCode.equals(request.getCode())) {
            throw new CampusAidException("验证码错误或已过期");
        }
        User user = User.builder().id(id).name(name).phoneNumber(phone).build();
        registerService.completeRegister(user);
        session.invalidate();
        return ApiResponse.success("注册成功");
    }
}

