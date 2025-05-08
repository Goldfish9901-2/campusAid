package cn.edu.usst.cs.campusAid.dto.forum;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class ReplyView {
    private Long id;
    private Long blogId;
    private Long senderId;
    private String senderName;
    private String content;
    private LocalDateTime sendTime;
    private List<ReplyView> replies; // 子回复列表
}
