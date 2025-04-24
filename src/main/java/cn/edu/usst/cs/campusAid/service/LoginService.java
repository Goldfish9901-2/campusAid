package cn.edu.usst.cs.campusAid.service;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public interface LoginService {
    String LAST_VERIFY_TIME = "last_verify_time";

    /**
     * 前端发学号
     * 后端将学号存入会话
     * 并发送邮件验证码
     *
     * @param id 用户id，即学号
     */
    void ask(HttpServletRequest request, String id);

    /**
     * 提交信息进行验证
     *
     * @param session 用户会话
     */
    void verify(@Nullable HttpSession session, String code);
}
