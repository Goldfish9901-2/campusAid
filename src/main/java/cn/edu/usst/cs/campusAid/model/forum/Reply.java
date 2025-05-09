package cn.edu.usst.cs.campusAid.model.forum;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 回复实体类，对应数据库表 reply。
 *
 * @author [你的名字]
 * @version 1.0
 */
@Data
public class Reply {
    /**
     * 回复ID，自增主键
     */
    private Long id;

    /**
     * 所属博客ID，外键关联 blog.id
     */
    private Long blogId;

    /**
     * 回复人ID，外键关联 user.id
     */
    private Long sender;

    /**
     * 回复内容，可为空
     */
    private String content;

    /**
     * 发送时间，默认当前时间
     */
    private LocalDateTime sendTime;
    /**
     * 父回复ID，为空表示一级回复
     */
    private Long parentId;

    public Long getSender() {
        return sender;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }
}
