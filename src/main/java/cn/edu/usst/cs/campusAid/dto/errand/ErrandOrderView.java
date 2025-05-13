package cn.edu.usst.cs.campusAid.dto.errand;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ErrandOrderView {
    private Long id;                         // 订单 ID
    private Long requesterId;                // 发布者 ID
    private String startLocation;            // 起点
    private String endLocation;              // 终点
    private String description;              // 描述文字
    private List<String> imageUrls;          // 图片（可选）
    private LocalDateTime latestArrivalTime; // 最晚送达时间
    private Double fee;                      // 费用
    private LocalDateTime publishTime;       // 发布时间
    private ErrandOrderStatus status;        // 订单状态（见下）
}
