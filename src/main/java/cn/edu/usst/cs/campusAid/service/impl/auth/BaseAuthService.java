package cn.edu.usst.cs.campusAid.service.impl.auth;

import cn.edu.usst.cs.campusAid.service.CampusAidException;
import cn.edu.usst.cs.campusAid.service.ExceptionService;
import cn.edu.usst.cs.campusAid.service.auth.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


public abstract class BaseAuthService {

    protected MailService mailService;

    protected final ExceptionService exceptionService;


    @Autowired
    public BaseAuthService(MailService mailService,ExceptionService exceptionService) {
        this.mailService = mailService;
        this.exceptionService = exceptionService;
    }

    /**
     * 生成验证码
     *
     * @return 生成的验证码
     */
    protected String generateVerificationCode() {
        int length = 6;

        StringBuffer buffer = new StringBuffer();
        // 生成一个基于当前时间戳的随机验证码
        var raw = String.valueOf((long) (Math.random() * System.currentTimeMillis()) % 1000000);
        while (buffer.length() + raw.length() < length) {
            buffer.append("0");
        }
        buffer.append(raw);
        return buffer.toString();
    }

    /**
     * 发送验证码到用户邮箱
     *
     * @param id   用户ID
     * @param code 验证码
     */
    protected void sendVerificationCode(Long id, String code)
            throws CampusAidException {
        mailService.sendVerificationMail(String.valueOf(id), code);
    }
}
