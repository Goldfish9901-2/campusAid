package cn.edu.usst.cs.campusAid.interceptor.api;

import cn.edu.usst.cs.campusAid.config.AdminConfig;
import cn.edu.usst.cs.campusAid.controller.SessionKeys;
import cn.edu.usst.cs.campusAid.mapper.db.complaint.BanMapper;
import cn.edu.usst.cs.campusAid.model.complaint.Ban;
import cn.edu.usst.cs.campusAid.model.complaint.BanBlock;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

/**
 * 拦截封禁用户后端请求
 * 可自动通过uri识别封禁板块
 */
@Component
public class BanApiInterceptor extends ApiInterceptor {
    private final BanMapper banMapper;
    private final AdminConfig adminConfig;

    @Override
    protected AdminConfig getAdminConfig() {
        return adminConfig;
    }

    public BanApiInterceptor(
            BanMapper banMapper,
            AdminConfig adminConfig) {
        super();
        this.banMapper = banMapper;
        this.adminConfig = adminConfig;
    }

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request,
                             @NotNull HttpServletResponse response,
                             @NotNull Object handler) throws Exception {
        Long userId = (Long) request.getSession(true).getAttribute(SessionKeys.LOGIN_ID);
        if (userId == null) {
            request.getSession().invalidate();
            return true;
        }
        String uri = request.getRequestURI();
        BanBlock banBlock = getBlock(uri);
        if (banBlock == null) {
            return true;
        }
        List<Ban> bans = banMapper.countBan(userId, banBlock);
        if (!bans.isEmpty()) {
            String message = "您已被禁言" + bans.get(0).getLengthByDay() + "天，原因：" + bans.get(0).getReason()
                    + "，到" + bans.get(0).getReleaseTime() + "前解除禁言";
            processResponse(response, SC_UNAUTHORIZED, message);
            return false;
        }

        return true;
    }

    private BanBlock getBlock(String uri) {
        for (BanBlock banBlock : BanBlock.values()) {
            if (uri.toLowerCase().contains(
                    banBlock.name().toLowerCase()
            )) {
                return banBlock;
            }
        }
        return null;
    }
}
