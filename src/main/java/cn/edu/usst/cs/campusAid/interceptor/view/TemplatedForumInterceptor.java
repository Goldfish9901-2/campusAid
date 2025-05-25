package cn.edu.usst.cs.campusAid.interceptor.view;

import cn.edu.usst.cs.campusAid.config.AdminConfig;
import cn.edu.usst.cs.campusAid.mapper.db.complaint.BanMapper;
import cn.edu.usst.cs.campusAid.model.complaint.BanBlock;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
public class TemplatedForumInterceptor extends TemplatedBanInterceptor {

    private final SpringTemplateEngine templateEngine;
    private final BanMapper banMapper;
    private final AdminConfig adminConfig;

    public TemplatedForumInterceptor(SpringTemplateEngine templateEngine, BanMapper banMapper, AdminConfig adminConfig) {
        this.templateEngine = templateEngine;
        this.banMapper = banMapper;
        this.adminConfig = adminConfig;
    }

    @Override
    public AdminConfig getAdminConfig() {
        return adminConfig;
    }

    @Override
    protected TemplateEngine getTemplateEngine() {
        return templateEngine;
    }

    @NotNull
    @Override
    protected BanMapper getBanMapper() {
        return banMapper;
    }

    @NotNull
    @Override
    protected BanBlock getBlock() {
        return BanBlock.FORUM;
    }

}
