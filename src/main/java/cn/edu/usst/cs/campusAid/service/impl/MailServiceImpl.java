package cn.edu.usst.cs.campusAid.service.impl;

import cn.edu.usst.cs.campusAid.CampusAidRuntimeException;
import cn.edu.usst.cs.campusAid.service.MailService;
import jakarta.mail.MessagingException;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

@Service
public class MailServiceImpl implements MailService {
    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;

    private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger(MailServiceImpl.class);


    public MailServiceImpl(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        configureMailSender();
    }

    private void configureMailSender() {
        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) javaMailSender;

        Properties mailProperties = mailSender.getJavaMailProperties();
        mailProperties.put("mail.debug", "true");
    }

    @Override
    public void sendVerificationMail(
            String id,
            String code
    ) {
        try {
            String receiver_addr = String.format("%s@st.usst.edu.cn", id);
            MimeMessageHelper helper = new MimeMessageHelper(javaMailSender.createMimeMessage(), true);
            helper.setTo(receiver_addr);
            helper.setSubject("Campus Aid: Verification Message");
            String content;

            Context context = new Context();
            context.setVariable("id", id);
            context.setVariable("code", code);
            content = templateEngine.process("mail", context);
            helper.setText(content, true);

            javaMailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            throw new CampusAidRuntimeException(e);
        }
    }
}
