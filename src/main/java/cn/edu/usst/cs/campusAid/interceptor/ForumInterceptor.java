package cn.edu.usst.cs.campusAid.interceptor;

import cn.edu.usst.cs.campusAid.mapper.db.complaint.BanMapper;
import cn.edu.usst.cs.campusAid.model.complaint.BanBlock;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class ForumInterceptor extends BanInterceptor {

    private final BanMapper banMapper;

    public ForumInterceptor(BanMapper banMapper) {
        super();
        this.banMapper = banMapper;
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
