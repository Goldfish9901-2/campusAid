package cn.edu.usst.cs.campusAid.interceptor.api;

import cn.edu.usst.cs.campusAid.interceptor.BasicInterceptor;
import cn.edu.usst.cs.campusAid.service.CampusAidException;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;

/**
 * 拦截后端请求
 * 回复体均为json
 */
public abstract class ApiInterceptor extends BasicInterceptor {
    @Override
    protected void processResponse(HttpServletResponse response, int code, String message) {
        response.setStatus(code);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("message", message);
        try {
            response.getWriter().print(jsonObject);
        } catch (IOException e) {
            throw new CampusAidException(e);
        }
    }
}
