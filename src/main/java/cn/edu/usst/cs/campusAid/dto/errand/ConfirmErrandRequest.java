package cn.edu.usst.cs.campusAid.dto.errand;

import lombok.Data;

@Data
public class ConfirmErrandRequest {
    private Long orderId;
    private Long userId;
}
