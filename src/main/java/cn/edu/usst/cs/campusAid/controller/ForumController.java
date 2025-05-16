package cn.edu.usst.cs.campusAid.controller;

import cn.edu.usst.cs.campusAid.dto.forum.*;

import cn.edu.usst.cs.campusAid.model.forum.Reply;
import cn.edu.usst.cs.campusAid.service.CampusAidException;
import cn.edu.usst.cs.campusAid.service.ForumPostService;
import cn.edu.usst.cs.campusAid.service.UploadFileSystemService;
import cn.edu.usst.cs.campusAid.util.ReplyTreeConverter;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/forum")
public class ForumController {
    @Autowired
    ForumPostService forumPostService;
    @Autowired
    private UploadFileSystemService uploadFileSystemService;

    /**
     * 获取论坛帖子列表，支持排序与关键词搜索
     *
     * @param keyword 关键词搜索
     */
    @GetMapping("/posts")
    public ResponseEntity<List<ForumPostPreview>> listPosts(
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId,
            @RequestParam(defaultValue = "TITLE") KeywordType type,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "TIME") PostSortOrder sortBy,
            @RequestParam(defaultValue = "0") int page
    ) {
        int pageSize = 10;
        int offset = page * pageSize;
        RowBounds rowBounds = new RowBounds(offset, pageSize);

        // 调用 service 并返回
        List<ForumPostPreview> posts = forumPostService.getPostsSorted(userId, type, keyword, sortBy, rowBounds);
        return ResponseEntity.ok(posts);
    }
    /**
     * 获取单个帖子详情
     */
    @GetMapping("/post/{postId}")
    public ResponseEntity<ForumPostPreview> getPost(
            @PathVariable Long postId,
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId
    ) {
        ForumPostPreview post = forumPostService.getPostById(postId);
        return ResponseEntity.ok(post);
    }

    /**
     * 获取回复列表
     */
    @GetMapping("/post/{postId}/replies")
    public ResponseEntity<List<ReplyView>> getReplies(
            @PathVariable Long postId,
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId
    ) {
//        if (!Objects.equals(
//                forumPostService.getAuthorID(postId),
//                userId
//        ))
//            throw new CampusAidException("无权限");
//        List<ReplyView> replyTree = ReplyTreeConverter.buildTree(
//                forumPostService.getRepliesByPostId(postId)
//        );
//        return ResponseEntity.ok(replyTree);
        List<ReplyView> replies = forumPostService.getRepliesByPostId(userId, postId);
        return ResponseEntity.ok(replies);
    }

    /**
     * 用户发帖
     */
    @PostMapping("/post/submit")
    public ResponseEntity<String> createPost(
            @RequestBody ForumPostPreview blogView
            ,
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId
    ) {
        forumPostService.createPost(userId, blogView);
        return ResponseEntity.ok("发帖成功"); // 待实现
    }

    @PostMapping("/post/upload")
    public ResponseEntity<String> uploadPost(
            @RequestParam("postId") Long postId,
            @RequestParam("file") MultipartFile file,
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId
    ) {
        String uri = forumPostService.uploadImage(userId, postId, file);
        return ResponseEntity.ok("上传成功 可在 " + uri + " 查看");
    }

    /**
     * 删除指定帖子（仅限楼主）
     */
    @DeleteMapping("/post/{postId}")
    public ResponseEntity<String> deletePost(
            @PathVariable Long postId,
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId
    ) {
        forumPostService.deletePost(postId, userId);
        return ResponseEntity.ok("删除成功"); // 待实现
    }

    /**
     * 点赞帖子
     */
    @PostMapping("/post/{postId}/like")
    public ResponseEntity<String> likePost(
            @PathVariable Long postId,
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId
    ) {
        forumPostService.likePost(postId, userId);
        return ResponseEntity.ok("点赞成功"); // 待实现
    }

    /**
     * 取消点赞帖子
     */
    @PostMapping("/post/{postId}/unlike")
    public ResponseEntity<String> unlikePost(
            @PathVariable Long postId,
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId
    ) {
        forumPostService.unlikePost(postId, userId);
        return ResponseEntity.ok("取消点赞"); // 待实现
    }

    /**
     * 回复帖子
     */
    @PostMapping("/post/{postId}/reply")
    public ResponseEntity<String> replyPost(
            @PathVariable Long postId,
            @RequestBody ReplyView reply,
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId
    ) {
        forumPostService.replyPost(userId, postId, reply);
        return ResponseEntity.ok("回复成功"); // 待实现
    }

    /**
     * 举报帖子
     */
    @PostMapping("/post/report")
    public ResponseEntity<String> reportPost(
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId,
            @RequestBody ReportRequest report
    ) {
        forumPostService.reportPost(userId, report);
        return ResponseEntity.ok("举报成功"); // 待实现
    }

    /**
     * 获取帖子附件
     *
     * @param userId 用户ID 校验用
     * @param postId 帖子ID
     * @return 文件列表
     */
    @GetMapping("/post/contents")
    public ResponseEntity<List<String>> getPostContents(
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId,
            @RequestParam Long postId
    ) {
        if (!Objects.equals(
                forumPostService.getAuthorID(postId),
                userId
        ))
            throw new CampusAidException("无权限");
        File contentDir = uploadFileSystemService.getBlogsUploadDir(postId);
        String[] files = contentDir.list();
        var contents = Arrays.asList(files == null ? new String[0] : files);
        return ResponseEntity.ok(contents);
    }
}
