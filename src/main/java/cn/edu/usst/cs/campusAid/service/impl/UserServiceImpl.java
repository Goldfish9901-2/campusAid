package cn.edu.usst.cs.campusAid.service.impl;

import cn.edu.usst.cs.campusAid.config.AdminConfig;
import cn.edu.usst.cs.campusAid.mapper.db.UserMapper;
import cn.edu.usst.cs.campusAid.model.User;
import cn.edu.usst.cs.campusAid.service.CampusAidException;
import cn.edu.usst.cs.campusAid.service.auth.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {


    private final UserMapper userMapper;
    private final AdminConfig adminConfig;

    public UserServiceImpl(UserMapper userMapper, AdminConfig adminConfig) {
        this.userMapper = userMapper;
        this.adminConfig = adminConfig;
    }

    @Override
    public User getUserById(Long userId) {
        User user = userMapper.getUserById(userId);
        if (user == null)
            throw new CampusAidException("用户不存在");
        return user;
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
        return user != null && adminConfig.getAdmin().equals(userId.toString()); // 管理员id为2235062128
    }

    @Override
    public Long getTargetUserId(Long userId, Long targetUserId) {
        try {
            adminConfig.verifyIsAdmin(userId);
            return targetUserId;
        } catch (CampusAidException e) {
            if (userId == targetUserId)
                return userId;
            return null;
        }
    }
}
