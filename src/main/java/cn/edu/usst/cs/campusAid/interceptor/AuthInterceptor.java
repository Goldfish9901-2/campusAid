package cn.edu.usst.cs.campusAid.interceptor;

import cn.edu.usst.cs.campusAid.controller.SessionKeys;
import cn.edu.usst.cs.campusAid.service.auth.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Component
public class AuthInterceptor implements HandlerInterceptor {
    String redirect_str = URLEncoder.encode("/error?message=未登录", StandardCharsets.UTF_8);


    static String buildJsonResponse(int code, String message) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("message", message);
        json.put("data", JSONObject.NULL);
        return json.toString();
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ) throws IOException {
        String lastVerifiedKey = LoginService.LAST_VERIFY_TIME;
        HttpSession session = request.getSession(false);

        int active = 10;
        LocalDateTime now = LocalDateTime.now();
        response.setContentType("application/json;charset=UTF-8");


        if (session == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(buildJsonResponse(401, "未登录或会话已失效"));
            return false;
        }


        Object obj = session.getAttribute(SessionKeys.LOGIN_TIME);
        if (!(obj instanceof LocalDateTime time)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(buildJsonResponse(403, "未完成验证，禁止访问"));
            return false;
        }

        if (time.plusMinutes(active).isBefore(now)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(buildJsonResponse(401, "登录已过期，请重新登录"));
            return false;
        }
        session.setMaxInactiveInterval(60 * active);
        session.setAttribute(SessionKeys.LOGIN_TIME, now);

        return true;
    }


}
