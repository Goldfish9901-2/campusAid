package cn.edu.usst.cs.campusAid.service;

import cn.edu.usst.cs.campusAid.CampusAidException;
import org.springframework.http.HttpRequest;

public interface MailService {
    void sendVerificationMail(String id, String code)throws CampusAidException;
}
