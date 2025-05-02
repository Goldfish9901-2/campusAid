package cn.edu.usst.cs.campusAid.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class VerifyRequest {
    @NotBlank
    private String code;
    // getters/setters
}
