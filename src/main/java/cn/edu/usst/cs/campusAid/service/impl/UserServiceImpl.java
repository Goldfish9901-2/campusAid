package cn.edu.usst.cs.campusAid.service.impl;

import cn.edu.usst.cs.campusAid.mapper.UserMapper;
import cn.edu.usst.cs.campusAid.model.User;
import cn.edu.usst.cs.campusAid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {


    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User getUserById(Long userId) {
        return userMapper.getUserById(userId);
    }
    /**
     * 判断用户是否为管理员
     *
     * @param userId 用户id
     * @return 是否为管理员
     */
    @Override
    public boolean isAdmin(Long userId) {
        User user = userMapper.getUserById(userId);
        return user != null && "2235062128".equals(userId); // 管理员id为2235062128
    }
}
