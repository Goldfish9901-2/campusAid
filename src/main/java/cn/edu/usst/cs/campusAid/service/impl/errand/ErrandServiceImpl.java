package cn.edu.usst.cs.campusAid.service.impl.errand;

import cn.edu.usst.cs.campusAid.config.AdminConfig;
import cn.edu.usst.cs.campusAid.dto.errand.ErrandOrderPreview;
import cn.edu.usst.cs.campusAid.dto.errand.ErrandOrderRequest;
import cn.edu.usst.cs.campusAid.dto.errand.ErrandOrderStatus;
import cn.edu.usst.cs.campusAid.mapper.db.UserMapper;
import cn.edu.usst.cs.campusAid.mapper.db.errand.ErrandMapper;
import cn.edu.usst.cs.campusAid.mapper.mapstruct.ErrandViewsMapper;
import cn.edu.usst.cs.campusAid.model.User;
import cn.edu.usst.cs.campusAid.model.errand.Errand;
import cn.edu.usst.cs.campusAid.service.CampusAidException;
import cn.edu.usst.cs.campusAid.service.auth.UserService;
import cn.edu.usst.cs.campusAid.service.errand.ErrandService;
import jakarta.annotation.Nullable;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class ErrandServiceImpl implements ErrandService {

    private final ErrandViewsMapper errandViewsMapper;
    private final ErrandMapper errandMapper;
    private final UserMapper userMapper;
    private final ScheduledExecutorService executorService;
    private final AdminConfig adminConfig;
    private final UserService userService;

    public ErrandServiceImpl(ErrandViewsMapper errandViewsMapper,
                             ErrandMapper errandMapper,
                             UserMapper userMapper,
                             ScheduledExecutorService executorService, AdminConfig adminConfig, UserService userService) {
        this.errandViewsMapper = errandViewsMapper;
        this.errandMapper = errandMapper;
        this.userMapper = userMapper;
        this.executorService = executorService;
        this.adminConfig = adminConfig;
        this.userService = userService;
    }

    /**
     * 检验订单状态
     *
     * @param userId 用户id
     * @param errand 跑腿订单
     */
    private static void verifyState(Long userId, @Nullable Errand errand) {
        if (errand == null) throw new CampusAidException("跑腿订单不存在");
        if (errand.getStatus() != ErrandOrderStatus.NORMAL) {
            throw new CampusAidException(userId + "：跑腿订单状态异常，您的确认无法接受");
        }
    }

    /**
     * 验证订单是不是该用户提交的
     *
     * @param userId 待验证的用户
     * @param errand 跑腿订单
     * @throws CampusAidException 订单不存在，或者订单不是该用户提交的
     */
    private static void verifyPublisher(Long userId, @Nullable Errand errand) {
        if (errand == null) throw new CampusAidException("跑腿订单不存在");
        if (!errand.getSenderId().equals(userId)) throw new CampusAidException("您无权修改此订单");
    }

    @Override
    public Long publishOrder(ErrandOrderRequest request, Long userId) {
        if (userService.getBalance(userId) < request.getFee())
            throw new CampusAidException("余额不足");
        request.setSenderId(userId);
        Long targetID = errandMapper.minFreeId();
        request.setId(targetID);
        Errand errand = errandViewsMapper.wrapIntoErrand(request);

        if (targetID != null) {
            errand.setId(targetID);
            errandMapper.insert(errand);
            scheduleAutoConfirmTask(errand, userId);
            return targetID;
        }
        throw new CampusAidException("跑腿数据库记录数异常");
    }

    @Override
    public List<ErrandOrderPreview> listOrders(Long userId) {
        return errandMapper.selectUnacceptedOrderPreviews(userId);
    }

    @Override
    public List<ErrandOrderPreview> listUserHistoricalOrders(Long userId) {
        return errandMapper.selectHistoricalOrders(userId).stream()
                .map(errand -> ErrandViewsMapper.getInstance().getPreview(errand))
                .toList();
    }

    @Override
    public Errand getOrderDetail(Long id, Long userId) {
        Errand errand = getErrand(id, userId);
        if (errand.getSenderId().equals(userId)) return errand;
        if (Objects.equals(errand.getAcceptorId(), userId)) return errand;
        adminConfig.verifyIsAdmin(userId);
        return errand;
    }

    @Override
    public String acceptOrder(Long id, Long runnerId) {
        Errand errand = getErrand(id, runnerId);
        User user = (errand.getAcceptorId() != null)
                ? userMapper.getUserById(errand.getAcceptorId())
                : null;
        if (user != null) {
            throw new CampusAidException("已有用户接单，您的接单无法接受");
        }
        if (errand.getSenderId().equals(runnerId)) {
            throw new CampusAidException("暂时不能接下自己的单子");
        }
        errand.setAcceptorId(runnerId);
        errandMapper.updateAcceptorId(id, runnerId);
        return runnerId + "--接单成功 单号--" + id;
    }

    @Override
    public String confirmOrder(Long id, Long userId) {
        Errand errand = getErrand(id, userId);
        ErrandOrderStatus status;
        if (Objects.equals(errand.getAcceptorId(), userId)) {
            status = ErrandOrderStatus.CONFIRMED;
        } else if (Objects.equals(errand.getSenderId(), userId)) {
            status = ErrandOrderStatus.COMPLETED;
        } else {
            throw new CampusAidException("无此权限");
        }
        errandMapper.updateErrand(id, status);

        // 提交一个延迟30分钟执行的任务
        executorService.schedule(() -> {
            try {
                verifyState(userId, errand);
                errandMapper.updateErrand(id, ErrandOrderStatus.AUTO_CONFIRMED);
                // 自动确认订单逻辑
                System.out.println("自动确认订单: " + id);
                // 调用相关方法更新状态或其他操作
            } catch (Exception e) {
                System.out.println("自动确认订单失败: " + id);
                if (e instanceof CampusAidException campusAidException) {
                    throw campusAidException;
                }
            }
        }, 30, TimeUnit.MINUTES);

        return userId + "--确认成功 单号--" + id;
    }

    @Override
    public String cancelOrder(Long id, Long userId) {
        Errand errand = getErrand(id, userId);
        verifyPublisher(userId, errand);
        if (errand.getAcceptorId() != null && userMapper.getUserById(errand.getAcceptorId()) != null)
            throw new CampusAidException("该订单已被接单，无法取消");
        if (Objects.equals(errand.getSenderId(), userId)) {
            errand.setStatus(ErrandOrderStatus.CANCELLED);
            errandMapper.updateErrand(id, ErrandOrderStatus.CANCELLED);
            return userId + "--取消成功 单号--" + id;
        }
        throw new CampusAidException(id + "不是发布者 无此权限");
    }

    @NotNull
    private Errand getErrand(Long id, Long userId) {
        Errand errand = errandMapper.selectById(id);
        verifyState(userId, errand);
        return errand;
    }

    private void scheduleAutoConfirmTask(Errand errand, long userId) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(errand.getLatestArrivalTime())) throw new CampusAidException("请给我表演一下怎么回到过去送达");
        LocalDateTime timeOut = errand.getLatestArrivalTime().plusHours(3);
        long delay = Duration.between(LocalDateTime.now(), timeOut).toMillis();
        executorService.schedule(() -> {
            Errand updatedErrand = getErrand(errand.getId(), userId);
            // 执行自动确认逻辑，例如调用 confirmOrder 或更新状态
            System.out.println("自动确认订单: " + updatedErrand.getId());
            errandMapper.updateErrand(updatedErrand.getId(), ErrandOrderStatus.AUTO_CONFIRMED);
        }, delay, TimeUnit.MILLISECONDS);
    }

}