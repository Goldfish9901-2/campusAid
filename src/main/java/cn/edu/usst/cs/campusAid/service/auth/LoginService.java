package cn.edu.usst.cs.campusAid.service.auth;

import cn.edu.usst.cs.campusAid.service.CampusAidException;

/**
 * 登录服务接口
 */
public interface LoginService {
    String LAST_VERIFY_TIME = "last_verify_time";

    /**
     * 生成验证码并发送到用户邮箱
     *
     * @param id 用户的学号
     * @return 生成的验证码
     * @throws CampusAidException 如果用户不存在或发送失败
     */
    String generateAndSendCode(Long id) throws CampusAidException;

    /**
     * 验证用户提交的验证码是否正确
     *
     * @param id           用户的学号
     * @param inputCode    用户输入的验证码
     * @param expectedCode 服务器生成并发送的验证码
     * @throws CampusAidException 如果验证码不匹配或其他错误
     */
    void verifyCode(Long id, String inputCode, String expectedCode) throws CampusAidException;

    /**
     * 检查用户是否已经注册
     *
     * @param id 用户的学号
     * @return 如果用户已注册，返回 true；否则，返回 false
     * @throws CampusAidException 如果查询失败
     */
    boolean isUserRegistered(Long id) throws CampusAidException;
}
