package cn.edu.usst.cs.campusAid.controller;

import cn.edu.usst.cs.campusAid.config.AdminConfig;
import cn.edu.usst.cs.campusAid.model.User;
import cn.edu.usst.cs.campusAid.service.CampusAidException;
import cn.edu.usst.cs.campusAid.service.auth.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final AdminConfig adminConfig;

    public UserController(UserService userService, AdminConfig adminConfig) {
        this.userService = userService;
        this.adminConfig = adminConfig;
    }

    @GetMapping
    public User getUserInfo(
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId,
            @RequestParam(required = false) Long targetUserId
    ) {
        Long userIdToQuery = targetUserId;
        if (targetUserId == null) {
            userIdToQuery = userId;
        }
        User targetUser = userService.getUserById(
                userIdToQuery
        );
        boolean hidePrivate = userService.getTargetUserId(
                userId,
                targetUserId
        )!=null;
        if (hidePrivate) {
            targetUser.setBalance(0);
        }

        return targetUser;
    }
}
