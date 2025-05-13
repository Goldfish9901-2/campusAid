package cn.edu.usst.cs.campusAid.model.complaint;

import cn.edu.usst.cs.campusAid.dto.complaint.ComplaintBlock;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class Complaint {
    /**
     * 投诉id 由后端生成
     */
    private Long id;
    /**
     * 投诉人id
     */
    @NonNull
    private Long senderId;
    /**
     * 投诉的板块
     */
    @NonNull
    private ComplaintBlock block;
    /**
     * 投诉内容在板块相应数据库中的id
     */
    @NonNull
    private Long complainedID;
    /**
     * 标题
     */
    @Size(min = 1, max = 20)
    private String title;
    /**
     * 投诉内容
     */
    @NonNull
    private String description;
    /**
     * 处理结果
     */
    private String result;
}
