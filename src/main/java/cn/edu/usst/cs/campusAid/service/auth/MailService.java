package cn.edu.usst.cs.campusAid.service.auth;

import cn.edu.usst.cs.campusAid.service.CampusAidException;

public interface MailService {
    void sendVerificationMail(String id, String code)throws CampusAidException;
}
