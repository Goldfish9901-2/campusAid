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
     * 测试模式
     */
    @Value("${campus_aid.test}")
    private String test;

    /**
     * 验证用户是否为管理员
     *
     * @see AdminConfig#verifyIsAdmin(String)
     */

    public void verifyIsAdmin(Long userId) {
        verifyIsAdmin(String.valueOf(userId));
    }

    /**
     * 验证用户是否为管理员
     *
     * @param userId 用户id 一般建议传入{@link Long} 或者 {@link String}
     * @throws CampusAidException 用户无权限
     */
    public void verifyIsAdmin(String userId) {
        if (!userId.equals(admin))
            throw new CampusAidException("用户无权限");
    }

    public boolean isInTestMode(){
        return Boolean.parseBoolean(test);
    }

}
