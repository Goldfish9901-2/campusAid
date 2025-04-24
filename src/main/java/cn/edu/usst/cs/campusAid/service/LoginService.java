package cn.edu.usst.cs.campusAid.service;

import cn.edu.usst.cs.campusAid.CampusAidException;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public interface LoginService {
    String LAST_VERIFY_TIME = "last_verify_time";

    /**
     * 前端发学号，请求验证码
     * 后端将学号存入会话
     * 并发送验证码邮件
     *
     * @param id 用户id，即学号
     */
    void ask(HttpServletRequest request, String id) throws CampusAidException;

    /**
     * 提交信息进行验证
     *
     * @param session 用户会话
     */
    void verify(@Nullable HttpSession session, String code)throws CampusAidException;
}
