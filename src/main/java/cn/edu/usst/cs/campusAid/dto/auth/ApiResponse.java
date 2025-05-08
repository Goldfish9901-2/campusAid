package cn.edu.usst.cs.campusAid.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.of(200, "success", data);
    }

    public static ApiResponse<?> fail(int code, String message) {
        return ApiResponse.of(code, message, null);
    }

    public static ApiResponse<String> badRequest(String message) {
        return ApiResponse.of(400, message, null);
    }

    public static ApiResponse<String> interServerError(String message) {
        return ApiResponse.of(500, message, null);
    }

    public static ApiResponse<?> unauthorized(String message) {
        return ApiResponse.of(401, message, null);
    }
}
