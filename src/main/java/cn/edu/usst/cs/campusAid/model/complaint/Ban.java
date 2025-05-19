package cn.edu.usst.cs.campusAid.model.complaint;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
@Builder
public class Ban {
    @NonNull
    private BanBlock block;
    @NonNull
    private Long userId;
    @NonNull
    private LocalDateTime releaseTime;
    private int lengthByDay;
    @NonNull
    private String reason;
}
