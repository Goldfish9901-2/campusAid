package cn.edu.usst.cs.campusAid.handler;

import cn.edu.usst.cs.campusAid.dto.ApiResponse;
import cn.edu.usst.cs.campusAid.service.ExceptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import cn.edu.usst.cs.campusAid.service.CampusAidException;
import org.springframework.web.bind.MissingRequestValueException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ExceptionService exceptionService;
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public GlobalExceptionHandler(ExceptionService exceptionService) {
        this.exceptionService = exceptionService;
    }

    @ExceptionHandler({CampusAidException.class})
    public ApiResponse<?> handleCampusAidException(CampusAidException e) {
        return ApiResponse.badRequest(e.getMessage());
    }

    @ExceptionHandler({HttpMessageConversionException.class, MissingRequestValueException.class})
    public ApiResponse<?> handleHttpMessageConversionException(HttpMessageConversionException e) {
        return ApiResponse.badRequest("参数错误\n" + e.getMessage());
    }

    @ExceptionHandler({Exception.class})
    public ApiResponse<?> handleOtherExceptions(Exception e) {
        var reportID =
                exceptionService.reportException(e);
        return ApiResponse.interServerError("服务器内部错误。编号 " + reportID);
    }


}
