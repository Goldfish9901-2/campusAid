package cn.edu.usst.cs.campusAid.service.impl.auth;

import cn.edu.usst.cs.campusAid.service.CampusAidException;
import cn.edu.usst.cs.campusAid.mapper.db.UserMapper;
import cn.edu.usst.cs.campusAid.model.User;
import cn.edu.usst.cs.campusAid.service.ExceptionService;
import cn.edu.usst.cs.campusAid.service.auth.LoginService;
import cn.edu.usst.cs.campusAid.service.auth.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoginServiceImpl
        extends BaseAuthService
        implements LoginService {

    private final UserMapper userMapper;

    public LoginServiceImpl(
            UserMapper userMapper,
            ExceptionService exceptionService,
            MailService mailService
    ) {
        super(mailService,exceptionService);
        this.userMapper = userMapper;
    }

    /**
     * 生成验证码并发送到用户邮箱
     */
    @Override
    public String generateAndSendCode(Long id) throws CampusAidException {
        // 检查用户是否注册
        if (!isUserRegistered(id)) {
            throw new CampusAidException("该用户尚未注册");
        }

        // 生成验证码
        String code = generateVerificationCode();

        // 发送验证码到邮箱
        sendVerificationCode(id, code);

        return code;
    }

    /**
     * 验证用户提交的验证码是否正确
     */
    @Override
    public void verifyCode(Long id, String inputCode, String expectedCode) throws CampusAidException {
        if (!inputCode.equals(expectedCode)) {
            throw new CampusAidException("验证码错误");
        }
        // 登录成功，但不保存用户信息到数据库
    }

    /**
     * 检查用户是否已经注册
     */
    @Override
    public boolean isUserRegistered(Long id) throws CampusAidException {
        User user = userMapper.getUserById(id);
        log.info("user:{}",user);
        return user != null;
    }
}
