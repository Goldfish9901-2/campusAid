package cn.edu.usst.cs.campusAid.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/error")
public class ErrorController {
    @GetMapping
    public String error(
            Model model,
            @RequestParam(
                    name = "message",
                    defaultValue = "未知错误"
            )
            String message
    ) {
        model.addAttribute("message", message);
        return "error";
    }
}
