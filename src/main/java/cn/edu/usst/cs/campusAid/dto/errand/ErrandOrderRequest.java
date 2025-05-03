package cn.edu.usst.cs.campusAid.dto.errand;

import jakarta.mail.Multipart;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ErrandOrderRequest {
    private String startLocation;
    private String endLocation;
    private String description;
    private List<Multipart> extras;
    private LocalDateTime latestArrivalTime;
    private Double fee;
}
