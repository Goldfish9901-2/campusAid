package cn.edu.usst.cs.campusAid.dto.errand;

import cn.edu.usst.cs.campusAid.model.errand.Errand;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrandOrderPreview {
    /**
     * {@link Errand#getId()}
     */
    @NonNull
    private Long id;
    /**
     * {@link Errand#getTitle()} ()}
     */
    @NonNull
    private String title;
    /**
     * {@link Errand#getErrandDescription()} ()}
     */
    private String errandDescription;
    /**
     * {@link Errand#getStartLocation()}
     */
    @NonNull
    private String startLocation;
    /**
     * {@link Errand#getEndLocation()}
     */
    @NonNull
    private String endLocation;
    /**
     * {@link Errand#getFee()}
     */
    @NonNull
    private Double fee;
    /**
     * {@link Errand#getLatestArrivalTime()}
     */
    @NonNull
    private LocalDateTime latestArrivalTime;
}
