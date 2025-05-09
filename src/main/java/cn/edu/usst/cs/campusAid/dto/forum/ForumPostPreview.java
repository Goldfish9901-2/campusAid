package cn.edu.usst.cs.campusAid.dto.forum;

import lombok.Data;

import java.util.List;

@Data
public class ForumPostPreview {
    private Long postId;
    private String title;
    private String authorName;
    private String content;
    private Long authorId;
    private String publishTime;
    private int likeCount;
    private int replyCount;
    private boolean liked;
    private List<String> tags;
    private Visibility visibility; // enum: PUBLIC, HIDDEN_BY_ADMIN, HIDDEN_BY_AUTHOR
}
