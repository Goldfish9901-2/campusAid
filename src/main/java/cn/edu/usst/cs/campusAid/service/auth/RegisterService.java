package cn.edu.usst.cs.campusAid.service.auth;

import cn.edu.usst.cs.campusAid.model.User;
import cn.edu.usst.cs.campusAid.service.CampusAidException;

public interface RegisterService {
    /**
     * 根据学号生成验证码并发送邮件，返回验证码（用于写入 session）
     * @param id 用户学号
     * @return 验证码字符串
     */
    String generateVerificationCode(long id) throws CampusAidException;

    void completeRegister(User user) throws CampusAidException;
}
