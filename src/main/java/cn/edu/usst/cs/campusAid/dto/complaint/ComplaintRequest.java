package cn.edu.usst.cs.campusAid.dto.complaint;

import lombok.Data;
@Data
public class ComplaintRequest {
    /**
     * 用户id 由后端生成
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
}