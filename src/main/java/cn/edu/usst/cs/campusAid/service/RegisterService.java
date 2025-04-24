package cn.edu.usst.cs.campusAid.service;

import cn.edu.usst.cs.campusAid.CampusAidException;
import jakarta.servlet.http.HttpServletRequest;

public interface RegisterService {
    void requestAdd(HttpServletRequest request, long id, String name, long phone)throws CampusAidException;

    void verifyAndAdd(HttpServletRequest request,long code)throws CampusAidException;
}
