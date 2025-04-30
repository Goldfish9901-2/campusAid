package cn.edu.usst.cs.campusAid.interceptor;

import cn.edu.usst.cs.campusAid.controller.SessionKeys;
import cn.edu.usst.cs.campusAid.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.time.LocalTime;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    String redirect_str = URLEncoder.encode("/error?message=未登录", StandardCharsets.UTF_8);

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ) throws IOException {
        String lastVerifiedKey = LoginService.LAST_VERIFY_TIME;
        HttpSession session = request.getSession(false);

        response.setContentType("application/json;charset=UTF-8");

        if (session == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"code\":401,\"message\":\"未登录或会话已失效\",\"data\":null}");
            return false;
        }

        Object obj = session.getAttribute(SessionKeys.LOGIN_TIME);
        if (!(obj instanceof LocalTime time)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"code\":403,\"message\":\"未完成验证，禁止访问\",\"data\":null}");
            return false;
        }

        if (time.plusMinutes(30).isBefore(LocalTime.now())) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"code\":401,\"message\":\"登录已过期，请重新登录\",\"data\":null}");
            return false;
        }

        return true;
    }


}
