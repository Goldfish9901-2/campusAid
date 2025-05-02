package cn.edu.usst.cs.campusAid.dto.forum;

import lombok.Data;

import java.util.List;

@Data
public class ForumPostDetail {
    private Long postId;
    private String title;
    private String authorName;
    private Long authorId;
    private String publishTime;
    private List<ContentBlock> content;
    private List<String> tags;
    private int likeCount;
    private boolean liked;
    private List<ReplyView> replies;
    private Visibility visibility;
    private boolean isOwner; // 是否当前用户发的贴
}
