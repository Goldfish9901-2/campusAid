package cn.edu.usst.cs.campusAid.dto.complaint;

import java.util.logging.Logger;

/**
 * 该投诉针对的是哪个板块的内容
 */
public enum ComplaintBlock {
    /**
     * 论坛帖子(楼)
     */
    FORUM_BLOG,
    /**
     * 论坛回复
     */
    FORUM_REPLY,
    /**
     * 商家购物
     */
    SHOP,
    /**
     * 跑腿
     */
    ERRAND;

//    private final String dbValue;
//
//    ComplaintBlock(String dbValue) {
//        this.dbValue = dbValue;
//    }
//
//    public String getDbValue() {
//        return dbValue;
//    }
//
//    /**
//     * 根据数据库值获取对应的枚举
//     */
//    public static ComplaintBlock fromDbValue(String dbValue) {
//        if (dbValue == null) {
//            return null;
//        }
//        for (ComplaintBlock value : values()) {
//            if (value.dbValue.equals(dbValue)) {
//                return value;
//            }
//        }
//        // 记录警告日志，而不是直接抛异常
//        Logger.getLogger(ComplaintBlock.class.getName())
//                .warning("Unknown complaint_block value: " + dbValue);
//        return null;
//    }
}
