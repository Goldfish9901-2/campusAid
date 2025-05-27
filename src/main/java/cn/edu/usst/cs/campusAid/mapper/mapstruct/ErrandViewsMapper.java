package cn.edu.usst.cs.campusAid.mapper.mapstruct;

import cn.edu.usst.cs.campusAid.dto.errand.ErrandOrderPreview;
import cn.edu.usst.cs.campusAid.dto.errand.ErrandOrderRequest;
import cn.edu.usst.cs.campusAid.model.errand.Errand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ErrandViewsMapper {
    ErrandViewsMapper INSTANCE = Mappers.getMapper(ErrandViewsMapper.class);

    static ErrandViewsMapper getInstance() {
        return INSTANCE;
    }

    @Mappings({
//            @Mapping(target = "publishTime", expression = "java(java.time.LocalDateTime.now())")
    })
    ErrandOrderPreview getPreview(Errand view);

    /**
     * <p>由请求生成跑腿订单实体类</p>
     * <p>{@link Errand#getId()} 系统会在插入时自动生成</p>
     * <p>{@link Errand#getStatus()} 默认为 {@link cn.edu.usst.cs.campusAid.dto.errand.ErrandOrderStatus#NORMAL}</p>
     * <p>{@link Errand#getAcceptorId()} 默认为 {@code null}</p>
     *
     * @param request 请求
     *                运行时，{@link Errand#getSenderId()} 会被设置为当前登录用户的 ID
     * @return 跑腿订单实体类
     */
    @Mappings({
            @Mapping(target = "errandDescription", source = "description"),
//            @Mapping(target = "id", ignore = true),
            @Mapping(target = "acceptorId", ignore = true),
            @Mapping(target = "status", expression =
                    "java(cn.edu.usst.cs.campusAid.dto.errand.ErrandOrderStatus.NORMAL)"),
            @Mapping(target = "confirmTime", expression = "java(null)"),
    })
    Errand wrapIntoErrand(ErrandOrderRequest request);
}
