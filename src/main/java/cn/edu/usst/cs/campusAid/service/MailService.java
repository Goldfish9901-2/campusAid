package cn.edu.usst.cs.campusAid.service;

import org.springframework.http.HttpRequest;

public interface MailService {
    void sendVerificationMail(String id, String code);
}
