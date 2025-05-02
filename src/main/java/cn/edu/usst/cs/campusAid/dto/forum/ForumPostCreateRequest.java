package cn.edu.usst.cs.campusAid.dto.forum;

import lombok.Data;

import java.util.List;

@Data
public class ForumPostCreateRequest {
    private String title;
    private List<ContentBlock> content;
    private List<String> tags;
}
