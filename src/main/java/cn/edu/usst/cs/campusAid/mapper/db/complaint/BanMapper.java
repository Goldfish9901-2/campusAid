package cn.edu.usst.cs.campusAid.mapper.db.complaint;

import cn.edu.usst.cs.campusAid.model.complaint.Ban;
import cn.edu.usst.cs.campusAid.model.complaint.BanBlock;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BanMapper {
    void insert(Ban build);
    /**
     * 查询现在用户有没有相应板块封禁
     * @param userId 用户ID
     * @return 返回论坛封禁数量，0表示无封禁
     */
    List<Ban> countBan(Long userId, BanBlock banBlock);


}
