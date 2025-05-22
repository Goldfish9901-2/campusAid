package cn.edu.usst.cs.campusAid.interceptor;

import cn.edu.usst.cs.campusAid.service.CampusAidException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;

@Slf4j
public abstract class BasicInterceptor implements HandlerInterceptor {
    protected abstract TemplateEngine getTemplateEngine();
    protected void processResponse(
            HttpServletResponse response,
            int code,
            String message) {
        Context context = new Context();
        response.setStatus(code);
        context.setVariable("message", message);
        response.setContentType("text/html;charset=utf-8");
        try {
            String body = getTemplateEngine().process("error", context);
            log.info("error page body:{}", body);
            log.warn("************");
            response.getWriter().write(body);
        } catch (IOException e) {
            throw new CampusAidException(e);
        }

    }
}
