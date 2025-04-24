package cn.edu.usst.cs.campusAid.interceptor;

import cn.edu.usst.cs.campusAid.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    String redirect_str = URLEncoder.encode("/error?message=未登录", StandardCharsets.UTF_8);

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            @NonNull
            HttpServletResponse response,
            @NonNull
            Object handler
    )
            throws Exception {
        String last_verified_time = LoginService.LAST_VERIFY_TIME;
        HttpSession session = request.getSession(false);
        if (session == null
                || session.getAttribute(last_verified_time) == null
                || !(session.getAttribute(last_verified_time) instanceof LocalTime time)
        ) {
            response.sendRedirect(redirect_str);
            return false;
        }
        if (time.plusMinutes(30).isBefore(LocalTime.now())) {
            response.sendRedirect(redirect_str);
            return false;
        }
        return true;
    }
}
