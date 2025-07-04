package cn.edu.usst.cs.campusAid.service.impl.auth;

import cn.edu.usst.cs.campusAid.service.CampusAidException;
import cn.edu.usst.cs.campusAid.service.auth.MailService;
import jakarta.mail.MessagingException;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Properties;

@Service
public class MailServiceImpl implements MailService {
    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;

    static final Logger logger = org.apache.logging.log4j.LogManager.getLogger(MailServiceImpl.class);

    @Value("${spring.mail.username}")
    private String sender_addr;

    public MailServiceImpl(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        configureMailSender();
    }

    private void configureMailSender() {
        // 移除不必要的类型转换和配置
        // 邮件配置已经在application.yml中完成
    }

    @Override
    public void sendVerificationMail(
            String id,
            String code
    )
            throws CampusAidException
    {
        if (sender_addr == null)
            throw new CampusAidException("无法加载发件地址");
        try {
            String receiver_addr = String.format("%s@st.usst.edu.cn", id);
            MimeMessageHelper helper = new MimeMessageHelper(javaMailSender.createMimeMessage(), true);
            helper.setTo(receiver_addr);
            helper.setFrom(sender_addr);
            helper.setSubject("Campus Aid校园互助平台: 认证消息");
            String content;

            Context context = new Context();
            context.setVariable("id", id);
            context.setVariable("code", code);
            content = templateEngine.process("mail", context);
            helper.setText(content, true);

            javaMailSender.send(helper.getMimeMessage());
        } catch (MessagingException | MailException e) {
            throw new CampusAidException(e);
        }
    }
}
