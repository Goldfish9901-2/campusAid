package cn.edu.usst.cs.campusAid.mapper;

import cn.edu.usst.cs.campusAid.model.Reply;
import org.apache.ibatis.annotations.Mapper;

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
}
