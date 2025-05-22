package cn.edu.usst.cs.campusAid.interceptor;

import cn.edu.usst.cs.campusAid.mapper.db.complaint.BanMapper;
import cn.edu.usst.cs.campusAid.model.complaint.BanBlock;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class ErrandInterceptor extends BanInterceptor {
    BanMapper banMapper;

    public ErrandInterceptor(BanMapper banMapper) {
        this.banMapper = banMapper;
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
