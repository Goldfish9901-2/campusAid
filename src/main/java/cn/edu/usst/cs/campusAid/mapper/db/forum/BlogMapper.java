package cn.edu.usst.cs.campusAid.mapper.db.forum;

import cn.edu.usst.cs.campusAid.dto.forum.Visibility;
import cn.edu.usst.cs.campusAid.model.forum.Blog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * 博客数据访问接口，提供对博客的增删改查操作。
 *
 * @since 2025-04-06
 */
@Mapper
public interface BlogMapper {
    /**
     * 查询所有博客
     *
     * @return 所有博客列表
     */
    List<Blog> selectAll();

    /**
     * 综合搜索并排序博客列表
     *
     * @param type      关键词类型（TITLE/TAG/CREATOR）
     * @param keyword   搜索关键词
     * @param sortBy    排序方式（TIME/LIKE_COUNT/REPLY_COUNT）
     * @param rowBounds 分页参数
     * @return 博客列表
     */
    List<Blog> selectBlogs(
            @Param("type") String type,
            @Param("keyword") String keyword,
            @Param("sortBy") String sortBy,
            RowBounds rowBounds
    );
//    @Select("SELECT * FROM blog WHERE id = #{postId}")
//    Blog selectById(@Param("postId") Long postId);

    /**
     * 根据标题模糊查询博客列表
     *
     * @param keyword   搜索关键词
     * @param rowBounds 分页参数
     * @return 博客列表
     */
    List<Blog> selectByTitle(@Param("keyword") String keyword, RowBounds rowBounds);

    /**
     * 根据发帖人ID查询博客列表
     *
     * @param keyword   发帖人ID（字符串形式）
     * @param rowBounds 分页参数
     * @return 博客列表
     */
    List<Blog> selectByCreator(@Param("keyword") String keyword, RowBounds rowBounds);

    /**
     * 根据内容中的标签（以 # 开头的词）查询博客列表
     *
     * @param keyword   标签名称
     * @param rowBounds 分页参数
     * @return 博客列表
     */
    List<Blog> selectByContentTag(@Param("keyword") String keyword, RowBounds rowBounds);

    /**
     * 统计标题匹配的博客数量
     *
     * @param keyword 搜索关键词
     * @return 匹配数量
     */
    int countByTitle(@Param("keyword") String keyword);

    /**
     * 统计指定发帖人的博客数量
     *
     * @param keyword 发帖人ID（字符串形式）
     * @return 匹配数量
     */
    int countByCreator(@Param("keyword") String keyword);

    /**
     * 统计包含特定标签的博客数量
     *
     * @param keyword 标签名称
     * @return 匹配数量
     */
    int countByContentTag(@Param("keyword") String keyword);

    /**
     * 查询所有博客并按发布时间排序
     *
     * @param rowBounds 分页参数
     * @return 博客列表
     */
    List<Blog> selectSortedByTime(RowBounds rowBounds);

    /**
     * 查询所有博客并按点赞量排序
     *
     * @param rowBounds 分页参数
     * @return 博客列表
     */
    List<Blog> selectSortedByLikeCount(RowBounds rowBounds);

    /**
     * 查询所有博客并按回复量排序
     *
     * @param rowBounds 分页参数
     * @return 博客列表
     */
    List<Blog> selectSortedByReplyCount(RowBounds rowBounds);

    /**
     * 插入一篇新博客
     *
     * @param blog 博客实体
     * @return 影响行数
     */
    int insertBlog(Blog blog);

    /**
     * 根据主键删除博客
     *
     * @param id 博客ID
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 根据主键获取博客详情
     *
     * @param id 博客ID
     * @return 博客实体
     */
    Blog selectById(Long id);

    /**
     * 更新帖子可见性
     *
     * @param postId     帖子ID
     * @param visibility 新的可见性状态（枚举类型）
     */
    void updateVisibility(@Param("postId") Long postId, @Param("visibility") Visibility visibility);

    Blog selectByReplyId(Long complaintSourceId);
}
