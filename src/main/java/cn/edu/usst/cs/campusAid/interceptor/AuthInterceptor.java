package cn.edu.usst.cs.campusAid.interceptor;

import cn.edu.usst.cs.campusAid.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String last_verified_time = LoginService.LAST_VERIFY_TIME;
        HttpSession session = request.getSession(false);
        if (session == null||session.getAttribute(last_verified_time)==null) {
            response.sendRedirect("/error?message=未登录");
            return false;
        }
        return true;
    }
}
