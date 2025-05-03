package cn.edu.usst.cs.campusAid.dto.errand;

public enum ErrandOrderStatus {
    PUBLISHED,     // 已发布，待接单
    ACCEPTED,      // 已接单
    COMPLETED,     // 完成，等待确认
    CONFIRMED,     // 用户已确认完成
    AUTO_CONFIRMED,// 系统自动确认完成
    EXPIRED,       // 过期无人接单
    CANCELLED      // 被取消
}
