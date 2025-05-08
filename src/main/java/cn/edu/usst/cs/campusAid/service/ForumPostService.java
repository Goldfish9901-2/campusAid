package cn.edu.usst.cs.campusAid.service;

import cn.edu.usst.cs.campusAid.dto.forum.*;
import cn.edu.usst.cs.campusAid.model.forum.Reply;
import cn.edu.usst.cs.campusAid.model.forum.ReplyTreeNode;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

public interface ForumPostService {

    /**
     * 获取论坛帖子列表，支持排序与关键词搜索
     *
     * @param keyword 关键词搜索
     * @param sortBy  排序方式
     * @return 排序后的帖子列表
     */
    List<ForumPostPreview> getPostsSorted(Long userId, KeywordType type, String keyword, PostSortOrder sortBy);


    /**
     * 创建一个新的帖子
     *
     * @param post 帖子信息
     */
    void createPost(Long userId, ForumPostPreview post);

    /**
     * 删除指定帖子（仅限楼主删除）
     *
     * @param postId 帖子ID
     * @param userId 当前用户ID
     */
    void deletePost(Long postId, Long userId);

    /**
     * 点赞帖子
     *
     * @param postId 帖子ID
     * @param userId 当前用户ID
     */
    void likePost(Long postId, Long userId);
    /**
     * 取消点赞帖子
     *
     * @param postId 帖子ID
     * @param userId 当前用户ID
     */
    void unlikePost(Long postId, Long userId);

    /**
     * 检查用户是否已点赞该帖子
     *
     * @param postId 帖子ID
     * @param userId 用户ID
     * @return 是否已点赞
     */
    boolean isLikedByUser(Long postId, Long userId);

    /**
     * 获取帖子的点赞数量
     *
     * @param postId 帖子ID
     * @return 点赞数量
     */
    int getPostLikeCount(Long postId);

    /**
     * 回复指定帖子
     *
     * @param postId 帖子ID
     * @param reply  回复内容
     */
    void replyPost(Long userId, Long postId, ReplyView reply);
    /**
     * 获取帖子的所有回复
     * 适合简单展示或后台管理
     * @param postId 帖子ID
     * @return 回复列表
     */
    List<Reply> getRepliesByPostId(Long postId);
    /**
     * 获取帖子的回复树
     * 适合前端递归渲染多级评论
     * 注意：已经不需要使用了，因为配合工具类已经可以将帖子处理成树形结构
     * @param postId 帖子ID
     * @return 回复树
     */
    List<ReplyTreeNode> getRepliesTreeByPostId(Long postId);

    /**
     * 删除回复
     * @param replyId       回复ID
     * @param userId        用户ID
     */
    void deleteReply(Long replyId, Long userId) ;

    /**
     * 举报帖子
     *
     * @param userID        举报的用户ID
     * @param reportRequest 举报信息
     */
    void reportPost(Long userID, ReportRequest reportRequest);

    /**
     * 上传帖子图片
     *
     * @param userId 帖子的发布者，验证修改权限用
     * @param postId 帖子ID
     * @param file   图片文件
     */
    String uploadImage(Long userId, Long postId, MultipartFile file);
}
