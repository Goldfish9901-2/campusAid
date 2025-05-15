package cn.edu.usst.cs.campusAid.service.charge;

import cn.edu.usst.cs.campusAid.model.charge.Charge;

import java.util.List;

public interface ChargeService {
    void recordCharge(Charge charge);

    List<Charge> getHistory(Long realId);
}
