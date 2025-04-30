package cn.edu.usst.cs.campusAid.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

// 配置类或实现类中定义，而不是接口
@ConfigurationProperties(prefix = "campusaid.security")
public class SecurityProperties {
    private Duration codeTtl = Duration.ofMinutes(5);
    private Duration resendInterval = Duration.ofMinutes(1);
    // getters/setters
}
