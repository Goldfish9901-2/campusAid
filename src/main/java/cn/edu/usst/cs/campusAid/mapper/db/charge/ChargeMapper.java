package cn.edu.usst.cs.campusAid.mapper.db.charge;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import cn.edu.usst.cs.campusAid.model.charge.Charge;

@Mapper
public interface ChargeMapper {
    void insertCharge(Charge charge);

    List<Charge> listCharges(Long realId);
}
