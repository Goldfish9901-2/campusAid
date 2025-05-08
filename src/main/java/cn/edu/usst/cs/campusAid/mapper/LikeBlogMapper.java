package cn.edu.usst.cs.campusAid.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import cn.edu.usst.cs.campusAid.model.forum.LikeBlog;

/**
 * 博客点赞数据访问接口
 */
@Mapper
public interface LikeBlogMapper {

    /**
     * 新增点赞记录
     * @param likeBlog 点赞实体
     * @return 影响行数
     */
    int insertLike(LikeBlog likeBlog);

    /**
     * 删除点赞记录
     * @param blogId 博客ID
     * @param liker 点赞用户
     * @return 影响行数
     */
    int deleteLike(@Param("blogId") Long blogId, @Param("liker") Long liker);
    /**
     * 判断某个用户是否已对某博客点赞
     * @param blogId 博客ID
     * @param liker 用户ID
     * @return 匹配数量（0 或 1）
     */
    int countLikesByUserAndPost(@Param("blogId") Long blogId, @Param("liker") Long liker);

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
