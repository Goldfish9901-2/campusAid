package cn.edu.usst.cs.campusAid.dto.forum;

import lombok.Data;

@Data
public class ContentBlock {
    private ContentType type; // TEXT, IMAGE, VIDEO, FILE, REFERENCE
    private String value; // 内容，如文字、URL、引用格式 #shop-01
}
