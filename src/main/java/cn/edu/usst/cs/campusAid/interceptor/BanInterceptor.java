package cn.edu.usst.cs.campusAid.interceptor;

import cn.edu.usst.cs.campusAid.controller.SessionKeys;
import cn.edu.usst.cs.campusAid.mapper.db.complaint.BanMapper;
import cn.edu.usst.cs.campusAid.model.complaint.BanBlock;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.HandlerInterceptor;

public abstract class BanInterceptor implements HandlerInterceptor {
    @NonNull
    protected abstract BanMapper getBanMapper();
    @NonNull
    protected abstract BanBlock getBlock();

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request,
                             @NotNull HttpServletResponse response,
                             @NotNull Object handler) throws Exception {
        Long userId = (Long) request.getSession().getAttribute(SessionKeys.LOGIN_ID);
        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"code\":401,\"message\":\"未登录或会话已失效\",\"data\":null}");
            return false;
        }
        int count = getBanMapper().countBan(userId,getBlock());
        return count == 0;
    }
}
