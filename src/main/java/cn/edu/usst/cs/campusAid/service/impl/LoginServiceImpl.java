package cn.edu.usst.cs.campusAid.service.impl;

import cn.edu.usst.cs.campusAid.CampusAidRuntimeException;
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
    public void ask(HttpServletRequest request, String id) {
        try {
            User user = userMapper.getUserById(Long.parseLong(id));
            long code = (long) (Math.random() * System.currentTimeMillis());
            // 设置session的过期时间

            mailService.sendVerificationMail(String.valueOf(user.getId()), String.valueOf(code));

            HttpSession session = request.getSession(true);
            session.setAttribute("code", code);
            session.setMaxInactiveInterval(60 * 3);
            logger.warn("code for id {}:{}", id, code);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("输入学号不符合要求");
        } catch (Exception e) {
            exceptionService.reportException(e);
        }
    }

    @Override
    public void verify(@Nullable HttpSession session, String code) {
        try {
            Objects.requireNonNull(session);
        } catch (NullPointerException e) {
            throw new CampusAidRuntimeException("请先获取验证码");
        }
        String sessionCode = session.getAttribute("code").toString();
        if (sessionCode.equals(code)) {
            session.setAttribute(LAST_VERIFY_TIME, LocalTime.now());
            return;
        }
        throw new CampusAidRuntimeException("验证码错误");
    }
}
