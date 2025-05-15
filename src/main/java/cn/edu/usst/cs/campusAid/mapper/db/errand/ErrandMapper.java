package cn.edu.usst.cs.campusAid.mapper.db.errand;

import cn.edu.usst.cs.campusAid.dto.errand.ErrandOrderPreview;
import cn.edu.usst.cs.campusAid.dto.errand.ErrandOrderStatus;
import cn.edu.usst.cs.campusAid.mapper.mapstruct.ErrandViewsMapper;
import cn.edu.usst.cs.campusAid.model.errand.Errand;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ErrandMapper {

    Long minFreeId();

    void insert(Errand errand);

    Errand selectById(Long id);

    List<Errand> selectUnacceptedOrders(Long userId);

    default List<ErrandOrderPreview> selectUnacceptedOrderPreviews(Long userId) {
        return selectUnacceptedOrders(userId).stream()
                .map(errandOrderPreview -> {
                    return ErrandViewsMapper.getInstance().getPreview(errandOrderPreview);
                })
                .toList();
    }

    void updateErrand(Long id, ErrandOrderStatus status);

    void updateAcceptorId(Long id, Long runnerId);
}
