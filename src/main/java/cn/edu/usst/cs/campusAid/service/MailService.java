package cn.edu.usst.cs.campusAid.service;

public interface MailService {
    void sendVerificationMail(String id, String code)throws CampusAidException;
}
