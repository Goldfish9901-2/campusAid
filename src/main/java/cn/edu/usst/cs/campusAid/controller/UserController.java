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
        User targetUser = userService.getUserById(
                targetUserId == null ? userId : targetUserId
        );
        if (!Objects.equals(userId, targetUserId)) // 非本人，隐藏余额
            targetUser.setBalance(0);
        return targetUser;
    }

    /**
     * 用户向已有的帖子追加图文附件
     *
     * @param userId   用户id
     * @param file      图文附件
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