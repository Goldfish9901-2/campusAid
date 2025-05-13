package cn.edu.usst.cs.campusAid.dto.errand;

import jakarta.mail.Multipart;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ErrandOrderRequest {
    private String startLocation;
    private String endLocation;
    private String title;
    private Long senderId;
    private String description;
    private LocalDateTime latestArrivalTime;
    private Double fee;
}
