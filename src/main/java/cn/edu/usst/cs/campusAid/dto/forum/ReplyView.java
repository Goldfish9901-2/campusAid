package cn.edu.usst.cs.campusAid.dto.forum;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class ReplyView {
    private Long id;
    private Long blogId;
    private Long senderId;
    private String content;
    private String sendTime;
    private List<ReplyView> replies;
    private Long parentId;

    // 确保有 setter 方法，否则 MapStruct 会报错
    public void setReplies(List<ReplyView> replies) {
        this.replies = replies;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public List<ReplyView> getReplies() {
        return replies;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
