package cn.edu.usst.cs.campusAid.model;

import lombok.Data;

import java.util.Date;

/**
 * 帖子点赞实体类，对应like_blog表
 *
 * <p>字段说明：
 * <ul>
 * <li>blogId - 关联帖子ID</li>
 * <li>liker - 点赞用户ID</li>
 * <li>likeTime - 点赞时间</li>
 * </ul>
 */
@Data
public class LikeBlog {
    private Long blogId;
    private Long liker;
    private Date likeTime;
}
