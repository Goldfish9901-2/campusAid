package cn.edu.usst.cs.campusAid.config;

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
}
