package cn.edu.usst.cs.campusAid.mapper.db.errand;

import cn.edu.usst.cs.campusAid.dto.errand.ErrandOrderPreview;
import cn.edu.usst.cs.campusAid.dto.errand.ErrandOrderStatus;
import cn.edu.usst.cs.campusAid.model.errand.Errand;

import java.util.List;

public interface ErrandMapper {
    Long minFreeId();

    void insert(Errand errand);

    Errand selectById(Long id);

    List<ErrandOrderPreview> selectUnacceptedOrders(Long userId);

    void updateErrand(Long id, ErrandOrderStatus status);

    void updateAcceptorId(Long id, Long runnerId);
}
