package cn.edu.usst.cs.campusAid.model.forum;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 博客点赞记录实体类，对应数据库表 like_blog。
 *
 * @version 1.0
 */
@Data
public class LikeBlog {
    /**
     * 被点赞的博客ID，联合主键之一
     */
    private Long blogId;

    /**
     * 点赞用户ID，联合主键之一
     */
    private Long liker;

    /**
     * 点赞时间，默认当前时间
     */
    private LocalDateTime likeTime;
}
