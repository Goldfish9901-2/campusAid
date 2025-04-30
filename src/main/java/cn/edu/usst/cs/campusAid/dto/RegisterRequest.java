package cn.edu.usst.cs.campusAid.dto;

// src/main/java/cn/edu/usst/cs/campusAid/dto/RegisterRequest.java
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotNull
    @Positive
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    @Pattern(regexp= "^1\\d{10}$", message="非法手机号")
    private String phone;
}

