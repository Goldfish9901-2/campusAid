package cn.edu.usst.cs.campusAid.views;

import lombok.Data;

import java.util.List;

/**
 * 主页视图类，用于展示用户主页的相关信息
 */
@Data
public class MainPageView {
    /**
     * 用户ID，用于标识和获取用户信息
     */
    long readerID;

    /**
     * 博客列表，用于展示用户发布的所有博客
     */
    List<Blog> blogs;

}
