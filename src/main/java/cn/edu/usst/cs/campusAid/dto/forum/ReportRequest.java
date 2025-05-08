package cn.edu.usst.cs.campusAid.dto.forum;

import lombok.Data;

import java.util.List;

@Data
public class ReportRequest {
    private Long postId;
    private String reasonText;
    private List<String> imageUrls;
}
