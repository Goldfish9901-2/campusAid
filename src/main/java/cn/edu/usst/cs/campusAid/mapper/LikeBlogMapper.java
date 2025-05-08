package cn.edu.usst.cs.campusAid.mapper;

import cn.edu.usst.cs.campusAid.model.LikeBlog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 点赞数据访问接口
 */
@Mapper
public interface LikeBlogMapper {

    /**
     * 新增点赞记录
     * @param like 点赞实体
     * @return 影响行数
     */
    int insertLike(LikeBlog like);

    /**
     * 删除点赞记录
     * @param blogId 博客ID
     * @param liker 点赞用户
     * @return 影响行数
     */
    int deleteLike(Long blogId, String liker);

    /**
     * 统计点赞量
     * @param blogId 博客ID
     * @return 点赞数量
     */
    int countLikes(Long blogId);

    /**
     * 根据博客ID删除所有点赞记录
     * @param blogId 博客ID
     * @return 影响行数
     */
    int deleteByBlogId(Long blogId);
}