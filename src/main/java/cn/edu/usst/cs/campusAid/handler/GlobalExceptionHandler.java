package cn.edu.usst.cs.campusAid.handler;

import cn.edu.usst.cs.campusAid.dto.auth.ApiResponse;
import cn.edu.usst.cs.campusAid.service.ExceptionService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import cn.edu.usst.cs.campusAid.service.CampusAidException;
import org.springframework.web.bind.MissingRequestValueException;

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

    @ExceptionHandler(MissingRequestValueException.class)
    public ApiResponse<?> handleMissingSessionAttribute(MissingRequestValueException e) {
        return ApiResponse.unauthorized("请先登录");
    }


    @ExceptionHandler({Exception.class})
    public ApiResponse<?> handleOtherExceptions(Exception e) {
        var reportID =
                exceptionService.reportException(e);
        return ApiResponse.interServerError("服务器内部错误。编号 " + reportID);
    }
}
