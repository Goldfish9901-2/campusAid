package cn.edu.usst.cs.campusAid.service;

import jakarta.servlet.http.HttpServletRequest;

public interface RegisterService {
    void requestAdd(HttpServletRequest request, long id, String name, long phone);

    void verifyAndAdd(HttpServletRequest request,long code);
}
