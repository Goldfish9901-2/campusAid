package cn.edu.usst.cs.campusAid.dto.forum;

import lombok.Data;

@Data
public class ReplyView {
    private Long replyId;
    private Long replierId;
    private String replierName;
    private String replyTime;
    private String content;
}
