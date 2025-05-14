package cn.edu.usst.cs.campusAid.controller;

import cn.edu.usst.cs.campusAid.model.User;
import cn.edu.usst.cs.campusAid.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public User getUserInfo(
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId,
            @RequestParam(required = false) Long targetUserId
    ) {
        User targetUser = userService.getUserById(
                targetUserId == null ? userId : targetUserId
        );
        if (!Objects.equals(userId, targetUserId)) // 非本人，隐藏余额
            targetUser.setBalance(0);
        return targetUser;
    }
}