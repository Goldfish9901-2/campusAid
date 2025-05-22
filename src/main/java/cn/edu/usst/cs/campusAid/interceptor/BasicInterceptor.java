package cn.edu.usst.cs.campusAid.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public abstract class BasicInterceptor implements HandlerInterceptor {
    protected abstract void processResponse(
            HttpServletResponse response,
            int code,
            String message
    );

}
