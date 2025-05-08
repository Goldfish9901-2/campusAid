package cn.edu.usst.cs.campusAid.mapper;

import cn.edu.usst.cs.campusAid.model.Blog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 主帖数据访问接口
 *
 * <p>主要功能：
 * <ul>
 * <li>帖子基础CRUD操作</li>
 * <li>复杂搜索查询</li>
 * <li>统计相关数据</li>
 * </ul>
 */
@Mapper
public interface BlogMapper {
    /**
     * 插入新帖子
     * @param blog 帖子实体
     * @return 影响行数
     */
    int insertBlog(Blog blog);

    /**
     * 根据主键删除帖子
     * @param id 帖子ID
     * @return 影响行数
     */
    int deleteBlog(Long id);

    /**
     * 分页搜索帖子
     * @param keyword 搜索关键词（标题/内容/发帖人）
     * @param orderBy 排序方式（time/likes/replies）
     * @return 分页结果
     */
    List<Blog> searchBlogs(@Param("keyword") String keyword,
                           @Param("orderBy") String orderBy);
}

