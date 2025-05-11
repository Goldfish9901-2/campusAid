package cn.edu.usst.cs.campusAid.dto.complaint;

import lombok.Data;

@Data
public class ComplaintDetail {
    /**
     * 投诉id 由后端生成
     */
    private Long id;
    /**
     * 投诉人id
     */
    private Long userId;
    /**
     * 投诉的板块
     */
    private ComplaintBlock block;
    /**
     * 投诉内容在板块相应数据库中的id
     */
    private Long complainedID;
    /**
     * 标题
     */
    private String title;
    /**
     * 投诉内容
     */
    private String content;
    /**
     * 处理结果
     */
    private String result;
}
