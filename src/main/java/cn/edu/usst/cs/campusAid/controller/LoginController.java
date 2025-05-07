package cn.edu.usst.cs.campusAid.controller;

import cn.edu.usst.cs.campusAid.dto.LoginRequest;
import cn.edu.usst.cs.campusAid.dto.VerifyRequest;
import cn.edu.usst.cs.campusAid.model.ApiResponse;
import cn.edu.usst.cs.campusAid.service.CampusAidException;
import cn.edu.usst.cs.campusAid.service.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

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
        return ApiResponse.success("登录成功");
    }
}

