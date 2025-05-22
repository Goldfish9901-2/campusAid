package cn.edu.usst.cs.campusAid.interceptor.api;

import cn.edu.usst.cs.campusAid.controller.SessionKeys;
import cn.edu.usst.cs.campusAid.mapper.db.complaint.BanMapper;
import cn.edu.usst.cs.campusAid.model.complaint.Ban;
import cn.edu.usst.cs.campusAid.model.complaint.BanBlock;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BanInterceptor extends ApiInterceptor {
    private final BanMapper banMapper;

    public BanInterceptor(BanMapper banMapper) {
        super();
        this.banMapper = banMapper;
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
            processResponse(response, 403, message);
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
