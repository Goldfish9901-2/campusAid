package cn.edu.usst.cs.campusAid.controller.forum;

import cn.edu.usst.cs.campusAid.config.AdminConfig;
import cn.edu.usst.cs.campusAid.controller.SessionKeys;
import cn.edu.usst.cs.campusAid.dto.forum.*;
import cn.edu.usst.cs.campusAid.service.CampusAidException;
import cn.edu.usst.cs.campusAid.service.UploadFileSystemService;
import cn.edu.usst.cs.campusAid.service.forum.ForumPostService;
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

    private final ForumPostService forumPostService;

    private final UploadFileSystemService uploadFileSystemService;

    private final AdminConfig adminConfig;


    public ForumController(
            ForumPostService forumPostService,
            UploadFileSystemService uploadFileSystemService,
            AdminConfig adminConfig) {
        this.forumPostService = forumPostService;
        this.uploadFileSystemService = uploadFileSystemService;
        this.adminConfig = adminConfig;
    }

    /**
     * 获取论坛帖子列表，支持排序与关键词搜索
     *
     * @param keyword 关键词搜索
     */
    @GetMapping("/posts")
    public ResponseEntity<List<ForumPostPreview>> listPosts(
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId,
            @RequestParam(defaultValue = "TITLE") KeywordType type,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "TIME") PostSortOrder sortBy,
            @RequestParam(defaultValue = "0") int page
    ) {
        int pageSize = 10;
        int offset = page * pageSize;
        RowBounds rowBounds = new RowBounds(offset, pageSize);

        // 调用 service 并返回
        List<ForumPostPreview> posts = forumPostService.getPostsSorted(userId, type, keyword, sortBy, rowBounds);
        posts = posts.stream().filter(
                post -> {
                    if (post.getVisibility() == Visibility.VISIBLE)
                        return true;
                    if (Objects.equals(post.getAuthorId(), userId))
                        return true;
                    try {
                        adminConfig.verifyIsAdmin(userId);
                        return true;
                    } catch (CampusAidException e) {
                        return false;
                    }
                }
        ).toList();
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
        if (post == null) {
            return ResponseEntity.notFound().build();
        }
        if (post.getVisibility() != Visibility.VISIBLE
                && !Objects.equals(userId, post.getAuthorId())
        )
            adminConfig.verifyIsAdmin(userId);
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

    /**
     * 用户向已有的帖子追加图文附件
     *
     * @param postId 帖子
     * @param file   图文附件
     * @param userId 用户ID 校验用
     * @return 图文附件的映射uri
     */
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
     * 管理员修改帖子可见性
     */
    @PostMapping("/post/{postId}/admin-visibility")
    public ResponseEntity<String> updatePostVisibilityByAdmin(
            @PathVariable Long postId,
            @RequestParam Visibility visibility,
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId
    ) {
        adminConfig.verifyIsAdmin(userId);
        // 验证参数合法性
        if (visibility == Visibility.SENDER) {
            throw new CampusAidException("用户已隐藏");
        }

        forumPostService.updatePostVisibility(userId, postId, visibility);
        return ResponseEntity.ok("管理员可见性修改成功");
    }

    /**
     * 发帖人修改帖子可见性
     */
    @PostMapping("/post/{postId}/sender-visibility")
    public ResponseEntity<String> updatePostVisibilityBySender(
            @PathVariable Long postId,
            @RequestParam Visibility visibility,
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId
    ) {
        // 验证参数合法性
        if (visibility ==Visibility.ADMIN) {
            throw new CampusAidException("管理员已隐藏");
        }
        if(!forumPostService.getPostById(postId).getAuthorId().equals(userId))
            throw new CampusAidException("无权修改此帖子的可见性");

        forumPostService.updatePostVisibility(userId, postId, visibility);
        return ResponseEntity.ok("发帖人可见性修改成功");
    }

    /**
     * 取消隐藏帖子
     */
    @PostMapping("/post/{postId}/unhide")
    public ResponseEntity<String> unhidePost(
            @PathVariable Long postId,
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId
    ) {
        ForumPostPreview post = forumPostService.getPostById(postId);
        if (!Objects.equals(post.getAuthorId(),  userId))
            adminConfig.verifyIsAdmin(userId);
        // 设置可见性为 VISIBLE，表示取消隐藏
        forumPostService.updatePostVisibility(userId, postId, Visibility.VISIBLE);
        return ResponseEntity.ok("帖子已成功取消隐藏");
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
        String[] files = uploadFileSystemService.listFiles(contentDir);
        return ResponseEntity.ok(Arrays.asList(files));
    }
}
