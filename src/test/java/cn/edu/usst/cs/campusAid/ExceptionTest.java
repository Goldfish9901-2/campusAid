package cn.edu.usst.cs.campusAid;

import cn.edu.usst.cs.campusAid.service.ExceptionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ExceptionTest {
    @Autowired
    ExceptionService exceptionService;
    @Test
    public void testException() {
        Exception exception=new IllegalArgumentException("q is not a number ");
        System.out.println(exceptionService.queryException(exceptionService.reportException(exception)));
    }
}
