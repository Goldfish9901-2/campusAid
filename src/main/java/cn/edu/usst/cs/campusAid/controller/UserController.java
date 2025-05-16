package cn.edu.usst.cs.campusAid.controller;

import cn.edu.usst.cs.campusAid.model.User;
import cn.edu.usst.cs.campusAid.service.UploadFileSystemService;
import cn.edu.usst.cs.campusAid.service.auth.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final UploadFileSystemService uploadFileSystemService;

    public UserController(UserService userService, UploadFileSystemService uploadFileSystemService) {
        this.userService = userService;
        this.uploadFileSystemService = uploadFileSystemService;
    }

    @GetMapping
    public User getUserInfo(
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId,
            @RequestParam(required = false) Long targetUserId
    ) {
        Long availableUserId = userService.getTargetUserId(userId, targetUserId);
        User targetUser = userService.getUserById(availableUserId);
        targetUser.setBalance(
                Objects.equals(targetUserId, availableUserId)
                ? userService.getBalance(availableUserId)
                : -1
        );
        return targetUser;
    }

    /**
     * 用户上传新头像
     *
     * @param userId 用户id
     * @param file   图文附件
     * @return 图文附件的映射uri
     */
    @PostMapping("/post/upload")
    public ResponseEntity<String> uploadPost(
            @RequestParam("file") MultipartFile file,
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId
    ) {
        var dir = uploadFileSystemService.getUserDir(userId);
        var uri = uploadFileSystemService.uploadFile(dir, file);
        return ResponseEntity.ok("上传成功 可在 " + uri + " 查看");
    }

    @GetMapping("/post/uploaded")
    public ResponseEntity<List<String>> getUploadedPosts(
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId
    ) {
        var dir = uploadFileSystemService.getUserDir(userId);
        var files = dir.listFiles();
        if (files == null)
            return ResponseEntity.ok(List.of());
        return ResponseEntity.ok(Arrays.stream(files)
                .map(File::getName)
                .toList());
    }


}