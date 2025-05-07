package cn.edu.usst.cs.campusAid.model;

import lombok.Data;

// 搜索参数封装类
@Data
public class BlogSearchParam {
    private String keyword;      // 搜索关键词
    private boolean searchTitle; // 是否搜索标题
    private boolean searchTag;   // 是否搜索标签
    private boolean searchAuthor; // 是否搜索发帖人
    private String sortBy;       // 排序方式：time/likes/replies
}