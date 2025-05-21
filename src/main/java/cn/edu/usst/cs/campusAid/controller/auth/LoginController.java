package cn.edu.usst.cs.campusAid.controller.auth;

import cn.edu.usst.cs.campusAid.config.AdminConfig;
import cn.edu.usst.cs.campusAid.controller.SessionKeys;
import cn.edu.usst.cs.campusAid.dto.ApiResponse;
import cn.edu.usst.cs.campusAid.dto.auth.LoginRequest;
import cn.edu.usst.cs.campusAid.dto.auth.VerifyRequest;
import cn.edu.usst.cs.campusAid.service.CampusAidException;
import cn.edu.usst.cs.campusAid.service.auth.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private AdminConfig adminConfig;

    @PostMapping
    public ApiResponse<String> sendLoginCode(
            HttpSession session,
            @RequestBody LoginRequest request
    ) throws CampusAidException {
        String code = loginService.generateAndSendCode(request.getId());
        session.setAttribute(SessionKeys.LOGIN_CODE, code);
        session.setAttribute(SessionKeys.LOGIN_ID, request.getId());
        session.setAttribute(SessionKeys.LOGIN_TIME, LocalTime.now());
        return ApiResponse.success("验证码已发送");
    }

    @PostMapping("/verify")
    public ApiResponse<String> verifyLogin(
            @SessionAttribute(SessionKeys.LOGIN_CODE) String sessionCode,
            @SessionAttribute(SessionKeys.LOGIN_ID) Long id,
            @RequestBody VerifyRequest request,
            HttpSession session
    ) throws CampusAidException {
        loginService.verifyCode(id, request.getCode(), sessionCode);
        session.setAttribute(SessionKeys.LOGIN_TIME, LocalTime.now());
        String message;
        try {
            adminConfig.verifyIsAdmin(id);
            message = "admin";
        } catch (CampusAidException ignored) {
            message = "user";
        }
        return ApiResponse.success(message);
    }

    /**
     * 登出
     */
    @GetMapping("/logout")
    public ApiResponse<String> logout(HttpSession session) {
        session.invalidate();
//        session.removeAttribute(SessionKeys.LOGIN_ID);
//        session.removeAttribute(SessionKeys.LOGIN_TIME);
        return ApiResponse.success("登出成功");
    }
}

