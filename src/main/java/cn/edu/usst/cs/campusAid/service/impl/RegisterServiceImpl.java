package cn.edu.usst.cs.campusAid.service.impl;

import cn.edu.usst.cs.campusAid.service.CampusAidException;
import cn.edu.usst.cs.campusAid.mapper.db.UserMapper;
import cn.edu.usst.cs.campusAid.model.User;
import cn.edu.usst.cs.campusAid.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterServiceImpl extends BaseAuthService
        implements RegisterService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 注册新用户
     */
    @Override
    public void completeRegister(User user) throws CampusAidException {
        // 检查用户是否已经注册
        if (userMapper.getUserById(user.getId()) != null) {
            throw new CampusAidException("该用户已经注册");
        }

        // 将用户信息保存到数据库
        userMapper.insertUser(user);
    }

    @Override
    public String generateVerificationCode(long id) throws CampusAidException {
        var code = generateVerificationCode();
        sendVerificationCode(id, code);
        return code;
    }
}
