package cn.edu.usst.cs.campusAid.interceptor;

import cn.edu.usst.cs.campusAid.config.AdminConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public abstract class BasicInterceptor implements HandlerInterceptor {
    protected abstract void processResponse(
            HttpServletResponse response,
            int code,
            String message
    );
    protected abstract AdminConfig getAdminConfig();

}
