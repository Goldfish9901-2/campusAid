package cn.edu.usst.cs.campusAid.service;

/**
 * 异常服务接口<br/>
 * 将无法预测的异常上报到服务器，录入数据库，便于后续处理
 */
public interface ExceptionService {
    /**
     * 上报异常
     * @param exception 异常信息
     * @return 异常id
     */
    long reportException(Exception exception);

    /**
     * 根据给出的异常id查询对应的异常信息
     * @param id 异常id
     * @return 异常信息
     */
    String queryException(long id);
}
