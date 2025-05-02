package cn.edu.usst.cs.campusAid.dto.forum;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 主页视图类，用于展示用户主页的相关信息
 */
@Data
@Builder
public class MainPageView {
    /**
     * 当前主页浏览者的 ID
     */
    private Long readerID;

    /**
     * 博客/动态信息列表
     */
    private List<ForumPostPreview> blogs;
}
