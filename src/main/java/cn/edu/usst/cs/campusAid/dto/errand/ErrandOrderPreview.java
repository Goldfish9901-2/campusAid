package cn.edu.usst.cs.campusAid.dto.errand;

import lombok.Data;
import lombok.Builder;
import cn.edu.usst.cs.campusAid.model.errand.Errand;
import java.time.LocalDateTime;

@Data
@Builder
public class ErrandOrderPreview {
    /**{@link Errand#getId()}
     */
    private Long id;
    /**
     * {@link Errand#getStartLocation()}
     */
    private String startLocation;
    /**
     * {@link Errand#getEndLocation()}
     */
    private String endLocation;
    /**
     * {@link Errand#getFee()}
     */
    private Double fee;
    /**
     * {@link Errand#getLatestArrivalTime()}
     */
    private LocalDateTime publishTime;
    /**
     * {@link Errand#getStatus()}
     */
    private ErrandOrderStatus status;
}
