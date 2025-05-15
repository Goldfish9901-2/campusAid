package cn.edu.usst.cs.campusAid.mapper.db.charge;

import cn.edu.usst.cs.campusAid.model.charge.Charge;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChargeMapper {
    void insertCharge(Charge charge);

    List<Charge> listCharges(Long realId);
}
