package cn.edu.usst.cs.campusAid.controller;

import cn.edu.usst.cs.campusAid.dto.forum.ForumPostPreview;
import cn.edu.usst.cs.campusAid.dto.forum.ReplyView;

import cn.edu.usst.cs.campusAid.service.ForumPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cn.edu.usst.cs.campusAid.dto.forum.PostSortOrder;
import java.util.List;

@RestController
@RequestMapping("/api/forum")
public class ForumController {
    @Autowired
    ForumPostService forumPostService;

    /**
     * 获取论坛帖子列表，支持排序与关键词搜索
     * @param keyword 关键词搜索
     */
    @GetMapping("/posts")
    public ResponseEntity<List<ForumPostPreview>> listPosts(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "TIME") PostSortOrder sortBy
    ) {
        List<ForumPostPreview> posts = forumPostService.getPostsSorted(keyword, sortBy);
        return ResponseEntity.ok().body(posts);
    }


    /**
     * 获取指定帖子详情（包括回复）
     */
    @GetMapping("/post/{postId}")
    public ResponseEntity<ForumPostPreview> getPostDetail(
            @PathVariable Long postId
    ) {
        return ResponseEntity.ok(forumPostService.getPostDetail(postId));
    }

    /**
     * 用户发帖
     */
    @PostMapping("/post")
    public ResponseEntity<String> createPost(@RequestBody ForumPostPreview blogView) {
        forumPostService.createPost(blogView);
        return ResponseEntity.ok("发帖成功"); // 待实现
    }

    /**
     * 删除指定帖子（仅限楼主）
     */
    @DeleteMapping("/post/{postId}")
    public ResponseEntity<String> deletePost(
            @PathVariable Long postId,
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId
    ) {
        forumPostService.deletePost(postId,userId);
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
        forumPostService.likePost(postId,userId);
        return ResponseEntity.ok("点赞成功"); // 待实现
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
        forumPostService.replyPost(userId,postId,reply);
        return ResponseEntity.ok("回复成功"); // 待实现
    }

    /**
     * 举报帖子
     */
    //TODO: 论坛举报要不要匿名
    @PostMapping("/post/{postId}/report")
    public ResponseEntity<String> reportPost(@PathVariable Long postId, @RequestParam String reason) {
        return ResponseEntity.ok("举报成功"); // 待实现
    }
}
