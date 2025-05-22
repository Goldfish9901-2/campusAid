package cn.edu.usst.cs.campusAid.interceptor;

import cn.edu.usst.cs.campusAid.mapper.db.complaint.BanMapper;
import cn.edu.usst.cs.campusAid.model.complaint.BanBlock;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
public class ErrandInterceptor extends BanInterceptor {
    private final SpringTemplateEngine templateEngine;
    private final BanMapper banMapper;

    public ErrandInterceptor(SpringTemplateEngine templateEngine, BanMapper banMapper) {
        this.templateEngine = templateEngine;
        this.banMapper = banMapper;
    }


    @Override
    protected TemplateEngine getTemplateEngine() {
        return templateEngine;
    }

    @Override
    protected @NonNull BanMapper getBanMapper() {
        return banMapper;
    }

    @NotNull
    @Override
    protected BanBlock getBlock() {
        return BanBlock.ERRAND;
    }
}
