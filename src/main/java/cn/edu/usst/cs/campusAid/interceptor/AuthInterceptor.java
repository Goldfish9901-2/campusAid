package cn.edu.usst.cs.campusAid.interceptor;

import cn.edu.usst.cs.campusAid.controller.SessionKeys;
import cn.edu.usst.cs.campusAid.service.auth.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;


@Component
public class AuthInterceptor extends BasicInterceptor {
    private final SpringTemplateEngine templateEngine;
    String redirect_str = URLEncoder.encode("/error?message=未登录", StandardCharsets.UTF_8);

    public AuthInterceptor(SpringTemplateEngine templateEngine) {
        super();
        this.templateEngine = templateEngine;
    }

    @Override
    protected TemplateEngine getTemplateEngine() {
        return templateEngine;
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
