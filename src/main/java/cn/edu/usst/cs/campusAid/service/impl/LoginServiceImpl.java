package cn.edu.usst.cs.campusAid.service.impl;

import cn.edu.usst.cs.campusAid.CampusAidException;
import cn.edu.usst.cs.campusAid.mapper.UserMapper;
import cn.edu.usst.cs.campusAid.model.User;
import cn.edu.usst.cs.campusAid.service.ExceptionService;
import cn.edu.usst.cs.campusAid.service.LoginService;
import cn.edu.usst.cs.campusAid.service.MailService;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    MailService mailService;

    @Autowired
    ExceptionService exceptionService;
    private final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(LoginServiceImpl.class);

    /**
     * 根据学号发送消息
     *
     * @param id 用户id，即学号
     */
    @Override
    public void ask(HttpServletRequest request, String id) throws CampusAidException {
        try {
            Objects.requireNonNull(id);
            long id_long = Long.parseLong(id);
            User user = userMapper.getUserById(id_long);
            long code = (long) (Math.random() * System.currentTimeMillis());
            // 设置session的过期时间

            mailService.sendVerificationMail(String.valueOf(user.getId()), String.valueOf(code));

            HttpSession session = request.getSession(true);
            session.setAttribute("code", code);
            session.setMaxInactiveInterval(60 * 3);
            logger.warn("code for id {}:{}", id, code);
        } catch (NumberFormatException e) {
            throw new CampusAidException("输入学号不符合要求");
        } catch (NullPointerException e) {
            throw new CampusAidException("会话失效，请先登录");
        }
    }

    @Override
    public void verify(@Nullable HttpSession session, String code) throws CampusAidException {
        try {
            Objects.requireNonNull(session);
        } catch (NullPointerException e) {
            throw new CampusAidException("请先获取验证码");
        }
        String sessionCode = session.getAttribute("code").toString();
        if (sessionCode.equals(code)) {
            session.setAttribute(LAST_VERIFY_TIME, LocalTime.now());
            return;
        }
        throw new CampusAidException("验证码错误");
    }
}
