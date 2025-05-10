package cn.edu.usst.cs.campusAid.mapper.db;

import cn.edu.usst.cs.campusAid.model.forum.Reply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 回复数据访问接口
 */
@Mapper
public interface ReplyMapper {

    /**
     * 插入新回复
     * @param reply 回复实体
     * @return 影响行数
     */
    int insertReply(Reply reply);

    /**
     * 删除指定帖子的所有回复
     * @param blogId 帖子ID
     * @return 影响行数
     */
    int deleteByBlogId(Long blogId);
    /**
     * 获取某篇帖子的所有回复
     * @param blogId 帖子ID
     * @return 回复列表
     */
    List<Reply> selectByBlogId(
            @Param("userId")
            Long userId,
            @Param("blogId")
            Long blogId);
    /**
     * 根据博客ID查询回复数量
     * @param blogId 帖子ID
     * @return 回复数量
     */
    int countRepliesByBlogId(Long blogId);


    /**
     * 删除指定回复
     * @param id 回复ID
     * @return 影响行数
     */
    int deleteById(Long id);
    /***
     * 根据回复ID获取回复
     * @param id 回复ID
     * @return 回复实体
     */
    Reply selectById(Long id);
    /**
     * 批量查询多个博客的回复数量
     * @param blogIds 博客ID列表
     * @return key: blogId, value: replyCount
     */
    List<Map<String, Object>> countRepliesByBlogIds(@Param("blogIds") List<Long> blogIds);

}
