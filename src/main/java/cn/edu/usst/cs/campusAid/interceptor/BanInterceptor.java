package cn.edu.usst.cs.campusAid.interceptor;

import cn.edu.usst.cs.campusAid.controller.SessionKeys;
import cn.edu.usst.cs.campusAid.mapper.db.complaint.BanMapper;
import cn.edu.usst.cs.campusAid.model.complaint.Ban;
import cn.edu.usst.cs.campusAid.model.complaint.BanBlock;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class BanInterceptor extends BasicInterceptor {
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
            processResponse(response, 401, "未登录用户");
            return false;
        }
        List<Ban> bans = getBanMapper().countBan(userId, getBlock());
        if (!bans.isEmpty()) {
            processResponse(
                    response,
                    403,
                    "您已被禁言" + bans.get(0).getLengthByDay() + "天，原因：" + bans.get(0).getReason()
                            + "，到" + bans.get(0).getReleaseTime() + "前解除禁言"
            );
            return false;
        }
        return true;
    }
}
