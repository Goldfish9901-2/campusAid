package cn.edu.usst.cs.campusAid.dto.errand;

import jakarta.mail.Multipart;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ErrandOrderRequest {
    /**
     * 订单ID，后端生成，请求不必填写
     */
    private Long id;
    @NonNull
    private String startLocation;
    @NonNull
    private String endLocation;
    @NonNull
    private String title;
    /**
     * 请求方，从会话中读取，所以请求中不必填入
     */
    private Long senderId;
    @NonNull
    private String description;
    @NonNull
    private LocalDateTime latestArrivalTime;
    @NonNull
    private Double fee;
}
