package cn.edu.usst.cs.campusAid.mapper.db.complaint;

import cn.edu.usst.cs.campusAid.model.complaint.Ban;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BanMapper {
    void insert(Ban build);
}
