package cn.edu.usst.cs.campusAid.service.forum;

import cn.edu.usst.cs.campusAid.dto.forum.*;
import cn.edu.usst.cs.campusAid.model.forum.ReplyTreeNode;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ForumPostService {

    /**
     * 获取排序后的帖子列表，并附带每个帖子的点赞数、回复数及是否已点赞状态。
     *
     * <p>该方法支持以下功能：</p>
     * <ul>
     *     <li>按关键词类型（标题/发帖人/标签）进行过滤</li>
     *     <li>按时间/点赞量/回复量排序</li>
     *     <li>分页加载数据</li>
     *     <li>批量查询点赞数和回复数，避免N+1查询问题</li>
     *     <li>判断当前用户是否已经对每篇帖子点赞</li>
     * </ul>
     *
     * @param userId    当前登录用户ID，用于判断点赞状态
     * @param type      关键词匹配类型（TITLE: 标题, TAG: 内容中的标签, CREATOR: 发帖人）
     * @param keyword   搜索关键词
     * @param sortBy    排序方式（TIME: 时间, LIKE_COUNT: 点赞量, REPLY_COUNT: 回复量）
     * @param rowBounds 分页参数，控制偏移量和每页条目数
     * @return 返回经过筛选、排序和补充信息后的帖子预览列表
     */
    List<ForumPostPreview> getPostsSorted(Long userId, KeywordType type, String keyword, PostSortOrder sortBy, RowBounds rowBounds);

    ForumPostPreview getPostById(Long postId);


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
     *
     * @param postId 帖子ID
     * @return 回复列表
     */
    List<ReplyView> getRepliesByPostId(Long userId, Long postId);

    /**
     * 获取帖子的回复数量
     *
     * @param postId 常指 blogId
     * @return 回复数量
     */
    @Deprecated
    int getReplyCountByPostId(Long userId, Long postId);

    /**
     * 获取帖子的回复树
     * 适合前端递归渲染多级评论
     * 注意：已经不需要使用了，因为配合工具类已经可以将帖子处理成树形结构
     *
     * @param postId 帖子ID
     * @return 回复树
     */
    @Deprecated
    List<ReplyTreeNode> getRepliesTreeByPostId(Long userId, Long postId);

    /**
     * 批量查询多个博客的点赞数量
     *
     * @param blogIds 博客ID列表
     * @return key: blogId, value: likeCount
     */
    @Deprecated
    List<Map<String, Object>> getLikeCountsByPosts(List<Long> blogIds);

    /**
     * 批量查询多个博客的回复数量
     *
     * @param blogIds 博客ID列表
     * @return key: blogId, value: replyCount
     */
    @Deprecated
    List<Map<String, Object>> countRepliesByPosts(@Param("blogIds") List<Long> blogIds);

    /**
     * 删除回复
     *
     * @param replyId 回复ID
     * @param userId  用户ID
     */
    void deleteReply(Long replyId, Long userId);

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

    Long submitPost(Long userId, ForumPostPreview post);

    Long getAuthorID(Long postId);

    /**
     * 修改帖子可见性
     *
     * @param userId     当前用户ID，用于权限校验
     * @param postId     常量帖子ID
     * @param visibility 新的可见性状态（枚举类型）
     */
    void updatePostVisibility(Long userId, Long postId, Visibility visibility);

}


