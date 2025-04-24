package cn.edu.usst.cs.campusAid.mapper;

import cn.edu.usst.cs.campusAid.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
//    @Insert("INSERT INTO user (id, name, phone_number) VALUES (#{id},#{name},#{phone_number})")
    int insertUser(User user);
    User getUserById(long id);
}
