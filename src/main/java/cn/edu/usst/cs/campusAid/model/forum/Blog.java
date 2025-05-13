package cn.edu.usst.cs.campusAid.model.forum;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 博客实体类，对应数据库表 blog。
 *
 * @author zyp
 * @version 1.0
 */
@Data
public class Blog {
    /**
     * 博客ID，主键
     */
    private Long id;

    /**
     * 创建者ID，外键关联用户表 user.id
     */
    private Long creator;

    /**
     * 博客标题，最大长度50字符，不能为空
     */
    private String title;

    /**
     * 博客内容，可为空
     */
    private String content;

    /**
     * 发布时间，默认当前时间
     */
    private LocalDateTime sendTime;

    /**
     * 可见性控制，枚举值：'admin', 'sender', 'visible'
     * 默认为 'visible'
     */
    private String visibility;

}
