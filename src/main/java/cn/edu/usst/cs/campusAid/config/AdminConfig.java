package cn.edu.usst.cs.campusAid.config;

import cn.edu.usst.cs.campusAid.service.CampusAidException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties()
public class AdminConfig {
    /**
     * 管理员账号
     */
    @Value("${campus_aid.admin}")
    String admin;

    /**
     *
     * @param userId 用户id 一般建议传入{@link Long} 或者 {@link String}
     * @throws CampusAidException 用户无权限
     */
    public void verifyIsAdmin(Object userId) {
        if (!userId.toString().equals(admin))
            throw new CampusAidException("用户无权限");
    }
}
