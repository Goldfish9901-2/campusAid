package cn.edu.usst.cs.campusAid.dto.forum;

import jakarta.annotation.Nullable;
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
    // 确保有 setter 方法，否则 MapStruct 会报错
//    private List<ReplyView> replies;
    @Nullable
    private Long parentId;

}
