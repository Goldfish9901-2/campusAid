package cn.edu.usst.cs.campusAid.controller;

import cn.edu.usst.cs.campusAid.dto.auth.VerifyRequest;
import cn.edu.usst.cs.campusAid.model.ApiResponse;
import cn.edu.usst.cs.campusAid.service.CampusAidException;
import cn.edu.usst.cs.campusAid.model.User;
import cn.edu.usst.cs.campusAid.service.RegisterService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/register")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @PostMapping("/")
    public ApiResponse<String> sendRegisterCode(
            HttpSession session,
            @RequestBody User user
    ) throws CampusAidException {
        String code = registerService.generateVerificationCode(user.getId());
        session.setAttribute(SessionKeys.REG_CODE, code);
        session.setAttribute(SessionKeys.REG_NAME, user.getName());
        session.setAttribute(SessionKeys.REG_PHONE, user.getPhoneNumber());

        return ApiResponse.success("验证码已发送");
    }

    @PostMapping("/verify")
    public ApiResponse<String> verifyRegister(
            @SessionAttribute(SessionKeys.REG_CODE) String sessionCode,
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

