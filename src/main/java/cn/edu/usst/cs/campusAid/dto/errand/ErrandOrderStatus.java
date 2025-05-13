package cn.edu.usst.cs.campusAid.dto.errand;

/**
 * 跑腿订单状态
 */
public enum ErrandOrderStatus {
    /**
     * 单子在走流程
     */
    NORMAL,
    /**
     * 完成，等待确认
     */
    COMPLETED,
    /**
     * 用户已确认完成
     */
    CONFIRMED,

    /**
     * 系统自动确认完成
     */
    AUTO_CONFIRMED,

    /**
     * 过期无人接单
     */
    EXPIRED,

    /**
     * 被取消
     */
    CANCELLED
}
