package cn.edu.usst.cs.campusAid.service.impl;

import cn.edu.usst.cs.campusAid.mapper.ExceptionMapper;
import cn.edu.usst.cs.campusAid.service.ExceptionService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class ExceptionServiceImpl implements ExceptionService {
    @Autowired
    ExceptionMapper exceptionMapper;

    private Logger logger = org.slf4j.LoggerFactory.getLogger(ExceptionServiceImpl.class);

    /**
     * 根据异常信息提交异常，并返回异常id
     *
     * @param exception 异常信息
     * @return 异常id
     */

    @Override
    public long reportException(Exception exception) {
        StringWriter sw = new StringWriter();
        exception.printStackTrace(new PrintWriter(sw));
        logger.atError().log("{}", sw.toString());
        return exceptionMapper.submitException(sw.getBuffer().toString());
    }

    /**
     * 根据id查询异常
     *
     * @param id 异常id
     * @return 异常信息
     */
    @Override
    public String queryException(long id) {
        return exceptionMapper.queryException(id);
    }

}
