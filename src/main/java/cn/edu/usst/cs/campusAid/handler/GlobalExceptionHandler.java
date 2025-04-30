package cn.edu.usst.cs.campusAid.handler;

import cn.edu.usst.cs.campusAid.model.ApiResponse;
import cn.edu.usst.cs.campusAid.service.ExceptionService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import cn.edu.usst.cs.campusAid.service.CampusAidException;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ExceptionService exceptionService;

    public GlobalExceptionHandler(ExceptionService exceptionService) {
        this.exceptionService = exceptionService;
    }

    @ExceptionHandler({CampusAidException.class})
    public ApiResponse<?> handleCampusAidException(CampusAidException e) {
        return ApiResponse.badRequest(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleOtherExceptions(Exception e) {
        var reportID =
                exceptionService.reportException(e);
        return ApiResponse.interServerError("服务器内部错误。编号 " + reportID);
    }
}
