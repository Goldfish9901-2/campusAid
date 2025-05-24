package cn.edu.usst.cs.campusAid.handler;

import cn.edu.usst.cs.campusAid.dto.ApiResponse;
import cn.edu.usst.cs.campusAid.service.ExceptionService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import cn.edu.usst.cs.campusAid.service.CampusAidException;
import org.springframework.web.bind.MissingRequestValueException;

import java.sql.SQLException;
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ExceptionService exceptionService;

    public GlobalExceptionHandler(ExceptionService exceptionService) {
        this.exceptionService = exceptionService;
    }

    @ExceptionHandler({CampusAidException.class})
    public ResponseEntity<?> handleCampusAidException(CampusAidException e) {
        log.error("CampusAidException:", e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    @ExceptionHandler({ServletRequestBindingException.class})
    public ResponseEntity<?> handleServletRequestBindingException(ServletRequestBindingException e) {
        log.error("ServletRequestBindingException: ", e);
        return ResponseEntity.badRequest().body("是不是没有登陆（会话参数错误）\n" + e.getMessage());
    }

    @ExceptionHandler({HttpMessageConversionException.class, MissingRequestValueException.class})
    public ResponseEntity<?> handleHttpMessageConversionException(HttpMessageConversionException e) {
        log.error("HttpMessageConversionException: {}", e.getMessage());
        return ResponseEntity.badRequest().body("参数错误\n" + e.getMessage());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> handleOtherExceptions(Exception e) {
        var reportID =
                exceptionService.reportException(e);
        StringBuilder message = new StringBuilder("服务器内部错误。编号 " + reportID);
        for(Throwable t = e; t != null; t = t.getCause()){
            if(t instanceof SQLException sqlException){
                message
                        .append("\n")
                        .append("包含数据库错误--")
                        .append(sqlException.getMessage());
            }
        }
        return ResponseEntity.internalServerError().body(message.toString());
    }


}
