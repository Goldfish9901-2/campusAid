package cn.edu.usst.cs.campusAid.model.errand;

import cn.edu.usst.cs.campusAid.dto.errand.ErrandOrderStatus;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;
/**
 * 代表一个跑腿订单的类，包含了订单的相关信息和状态
 */
@Data
@Builder
public class Errand {
    /**
     * 订单ID
     */
    @NonNull
    private Long id;
    /**
     * 发布者ID
     */
    @NonNull
    private Long senderId;
    /**
     * 订单标题
     */
    @NonNull
    private String title;
    /**
     * 跑腿订单的描述
     */
    @NonNull
    private String errandDescription;
    /**
     * 订单的费用
     */
    @NonNull
    private Double fee;
    /**
     * 订单的起始地点
     */
    @NonNull
    private String startLocation;
    /**
     * 订单的目的地
     */
    @NonNull
    private String endLocation;
    /**
     * 订单的最晚到达时间
     */
    @NonNull
    private LocalDateTime latestArrivalTime;
    /**
     * 接受者ID，表示接单者的身份
     * <h2>为空表示没人接单，对想接单的用户可见</h2>
     * <h2>不为空表示有人接单，对想接单的用户不可见，只对该id指向的用户可见</h2>
     */
    @Nullable
    private Long acceptorId;
    /**
     * 订单的状态
     */
    @NonNull
    private ErrandOrderStatus status;

    private LocalDateTime confirmTime;
}
