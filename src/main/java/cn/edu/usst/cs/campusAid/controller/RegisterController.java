package cn.edu.usst.cs.campusAid.controller;

import cn.edu.usst.cs.campusAid.CampusAidRuntimeException;
import cn.edu.usst.cs.campusAid.model.User;
import cn.edu.usst.cs.campusAid.service.ExceptionService;
import cn.edu.usst.cs.campusAid.service.RegisterService;
import jakarta.mail.Session;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;

@RestController
@RequestMapping("/api/register")
public class RegisterController {

    RegisterService registerService;
    ExceptionService exceptionService;

    public RegisterController(
            @NonNull
            RegisterService registerService,
            @NonNull
            ExceptionService exceptionService
    ) {
        this.registerService = registerService;
        this.exceptionService = exceptionService;
    }

    @GetMapping
    public ResponseEntity<String> register(
            @NonNull
            HttpServletRequest request,
            @RequestParam(name = "id")
            long id,
            @RequestParam(name = "name")
            String name,
            @RequestParam(name = "phone")
            long phone
    ) {
        try {
            registerService.requestAdd(
                    request, id, name, phone
            );
            return ResponseEntity.ok("请查收邮箱\n邮件发送时间：" + LocalTime.now());
        } catch (CampusAidRuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    "未知错误，代码： " + exceptionService.reportException(e)
            );
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verify(
            @NonNull
            HttpServletRequest request,
            @RequestParam(name = "code")
            String code
    ) {
        try {
            registerService.verifyAndAdd(request, Long.parseLong(code));
            return ResponseEntity.ok("注册成功");
        } catch (CampusAidRuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    "未知错误，代码： " + exceptionService.reportException(e)
            );
        }
    }
}
