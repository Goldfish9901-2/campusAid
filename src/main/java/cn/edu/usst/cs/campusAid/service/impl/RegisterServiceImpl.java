package cn.edu.usst.cs.campusAid.service.impl;

import cn.edu.usst.cs.campusAid.CampusAidRuntimeException;
import cn.edu.usst.cs.campusAid.mapper.UserMapper;
import cn.edu.usst.cs.campusAid.model.User;
import cn.edu.usst.cs.campusAid.service.MailService;
import cn.edu.usst.cs.campusAid.service.RegisterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    ExecutorService executorService;

    Logger logger = org.slf4j.LoggerFactory.getLogger(RegisterServiceImpl.class);
    @Autowired
    MailService mailService;

    @Autowired
    UserMapper userMapper;

    @Override
    public void requestAdd(HttpServletRequest request, long id, String name, long phone) {
        long code = (long) (Math.random() * System.currentTimeMillis());
        mailService.sendVerificationMail(String.valueOf(id), String.valueOf(code));

        HttpSession session = request.getSession(true);
        session.setAttribute("id", id);
        session.setAttribute("name", name);
        session.setAttribute("phone", phone);
        session.setAttribute("code", code);
        logger.warn("code for id {}:{}", id, code);
    }

    @Override
    public void verifyAndAdd(HttpServletRequest request, long code) {
        try {
            if (code != (long) (request.getSession().getAttribute("code")))
                throw new CampusAidRuntimeException("验证码错误，请重新输入！");
            request.getSession().removeAttribute("code");
            User user = new User();
            user.setId((long) request.getSession().getAttribute("id"));
            user.setName((String) request.getSession().getAttribute("name"));
            user.setPhone_number((long) request.getSession().getAttribute("phone"));
            userMapper.insertUser(user);

            request.getSession().removeAttribute("id");
            request.getSession().removeAttribute("name");
            request.getSession().removeAttribute("phone");
            request.getSession().removeAttribute("user");
            request.getSession().invalidate();

        } catch (NullPointerException e) {
            throw new CampusAidRuntimeException("请先提交个人信息");
        } catch (ClassCastException e) {
            throw new CampusAidRuntimeException("内部错误：个人信息验证失败");
        }


    }
}
