package cn.edu.usst.cs.campusAid.mapper.db;

import cn.edu.usst.cs.campusAid.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    int insertUser(User user);
    User getUserById(long id);
}
