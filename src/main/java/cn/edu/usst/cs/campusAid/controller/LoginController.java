package cn.edu.usst.cs.campusAid.controller;

import cn.edu.usst.cs.campusAid.CampusAidException;
import cn.edu.usst.cs.campusAid.service.ExceptionService;
import cn.edu.usst.cs.campusAid.service.LoginService;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    LoginService loginService;

    ExceptionService exceptionService;

    public LoginController(
            @NonNull
            LoginService loginService,
            @NonNull
            ExceptionService exceptionService
    ) {
        this.loginService = loginService;
        this.exceptionService = exceptionService;
    }


    @GetMapping
    public ResponseEntity<String> submitInfoForVerification(
            HttpServletRequest request,
            @RequestParam(name = "id")
            String id_raw
    ) {
        try {
            Long.parseLong(id_raw);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(id_raw + " 不是一个有效学号");
        }
        try {
            loginService.ask(request, id_raw);
            return ResponseEntity.ok("请查收邮箱\n邮件发送时间：" + LocalTime.now());
        } catch (CampusAidException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    "未知错误，代码： " + exceptionService.reportException(e)
            );
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verify(
            @Nonnull
            HttpServletRequest request,
            @RequestParam(name = "code")
            String code
    ) {

        try {
            loginService.verify(request.getSession(false), code);
            return ResponseEntity.ok("验证成功");
        } catch (CampusAidException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            long id = exceptionService.reportException(e);
            return ResponseEntity.internalServerError().body("未知错误，代码：" + id);
        }
    }
}
