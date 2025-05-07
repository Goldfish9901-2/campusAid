package cn.edu.usst.cs.campusAid.service;

import cn.edu.usst.cs.campusAid.dto.forum.ForumPostPreview;
import cn.edu.usst.cs.campusAid.dto.forum.ReplyView;
import cn.edu.usst.cs.campusAid.dto.forum.PostSortOrder;
import cn.edu.usst.cs.campusAid.dto.forum.ReportRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ForumPostService {

    /**
     * 获取论坛帖子列表，支持排序与关键词搜索
     *
     * @param keyword 关键词搜索
     * @param sortBy  排序方式
     * @return 排序后的帖子列表
     */
    List<ForumPostPreview> getPostsSorted(Long userId, String keyword, PostSortOrder sortBy);

    /**
     * 获取指定帖子的详细信息，包括回复
     *
     * @param postId 帖子ID
     * @return 帖子详情
     */
    ForumPostPreview getPostDetail(Long userId, Long postId);

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
     * 回复指定帖子
     *
     * @param postId 帖子ID
     * @param reply  回复内容
     */
    void replyPost(Long userId, Long postId, ReplyView reply);

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
