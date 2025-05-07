package cn.edu.usst.cs.campusAid.model;

import lombok.Data;

import java.util.Date;

/**
 * 论坛主帖实体类，对应blog表
 *
 * <p>字段说明：
 * <ul>
 * <li>id - 主键</li>
 * <li>creator - 发帖人ID</li>
 * <li>title - 帖子标题（最长50字符）</li>
 * <li>content - 帖子内容（文本类型）</li>
 * <li>sendTime - 发帖时间</li>
 * <li>visibility - 可见性（visible/sender/admin）</li>
 * </ul>
 */
@Data
public class Blog {
        private Long id;
        private Long creator;
        private String title;
        private String content;
        private Date sendTime;
        private String visibility;
}
