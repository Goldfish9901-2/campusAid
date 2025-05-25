package cn.edu.usst.cs.campusAid.interceptor.api;

import cn.edu.usst.cs.campusAid.config.AdminConfig;
import cn.edu.usst.cs.campusAid.controller.SessionKeys;
import cn.edu.usst.cs.campusAid.service.auth.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 拦截未认证用户登录除认证以外其他所有后端请求
 */

@Component
public class AuthInterceptor extends ApiInterceptor {

    private final AdminConfig adminConfig;

    public AuthInterceptor(AdminConfig adminConfig) {
        super();
        this.adminConfig = adminConfig;
    }

    @Override
    protected AdminConfig getAdminConfig() {
        return adminConfig;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ) {
        String lastVerifiedKey = LoginService.LAST_VERIFY_TIME;
        HttpSession session = request.getSession(false);

        int active = 10;
        LocalDateTime now = LocalDateTime.now();
        response.setContentType("application/json;charset=UTF-8");

        if(adminConfig.isInTestMode())
            return true;

        if (session == null) {
            processResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "未登录，请登录");
            return false;
        }


        Object obj = session.getAttribute(SessionKeys.LOGIN_TIME);
        if (!(obj instanceof LocalDateTime time)) {
            processResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "会话状态异常，请重新登录");
            return false;
        }

        if (time.plusMinutes(active).isBefore(now)) {
            processResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "会话已过期，请重新登录");
            return false;
        }
        session.setMaxInactiveInterval(60 * active);
        session.setAttribute(SessionKeys.LOGIN_TIME, now);

        return true;
    }


}
