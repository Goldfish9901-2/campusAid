package cn.edu.usst.cs.campusAid.dto.errand;

import lombok.Data;
import lombok.Builder;
import cn.edu.usst.cs.campusAid.model.errand.Errand;
import java.time.LocalDateTime;

@Data
@Builder
public class ErrandOrderPreview {
    /**{@link Errand#getTitle()} ()}
     */
    private String title;
    /**
     * {@link Errand#getErrandDescription()} ()}
     */
    private String errandDescription;
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
    private LocalDateTime latestArrivalTime;
}
