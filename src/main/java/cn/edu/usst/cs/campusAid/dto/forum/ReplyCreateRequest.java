package cn.edu.usst.cs.campusAid.dto.forum;

import lombok.Data;

@Data
public class ReplyCreateRequest {
    private Long postId;
    private String content;
}
