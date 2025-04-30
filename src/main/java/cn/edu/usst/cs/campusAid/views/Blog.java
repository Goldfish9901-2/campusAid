package cn.edu.usst.cs.campusAid.views;

import lombok.Data;

import java.util.List;

/**
 * Blog类用于封装博客内容及其相关的回复信息。
 * 使用Lombok的@Data注解可以自动生成类属性的getter和setter方法，
 * 以及equals、hashCode和toString方法，简化实体类的模板代码。
 */
@Data
public class Blog {
    /**
     * 博客的内容信息，使用List存储，支持多个内容条目，如文本、图片、视频等。
     */
    private List<String> contents;

    /**
     * 存储与该博客相关的回复ID列表。
     * 每个回复对应一个唯一的ID，使用List结构表示可能存在多个回复。
     */
    private List<Long> replyIDs;
    /**
     * 点赞数
     */
    private int likes;
    /**
     * 查阅者是否点过赞
     */
    private boolean liked;
}

