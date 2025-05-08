package cn.edu.usst.cs.campusAid.model;

import lombok.Data;

import java.util.Date;

/**
 * 帖子回复实体类，对应reply表
 *
 * <p>字段说明：
 * <ul>
 * <li>id - 主键</li>
 * <li>blogId - 关联帖子ID</li>
 * <li>sender - 回复人ID</li>
 * <li>content - 回复内容</li>
 * <li>sendTime - 回复时间</li>
 * </ul>
 */
@Data
public class Reply {
    private Long id;
    private Long blogId;
    private Long sender;
    private String content;
    private Date sendTime;
}
