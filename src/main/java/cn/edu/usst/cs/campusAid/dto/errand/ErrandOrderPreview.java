package cn.edu.usst.cs.campusAid.dto.errand;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ErrandOrderPreview {
    private Long id;
    private String startLocation;
    private String endLocation;
    private Double fee;
    private LocalDateTime publishTime;
    private ErrandOrderStatus status;
}
