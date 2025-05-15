package cn.edu.usst.cs.campusAid.service.impl.charge;

import cn.edu.usst.cs.campusAid.mapper.db.charge.ChargeMapper;
import cn.edu.usst.cs.campusAid.model.charge.Charge;
import cn.edu.usst.cs.campusAid.service.charge.ChargeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChargeServiceImpl implements ChargeService {
    private final ChargeMapper chargeMapper;

    public ChargeServiceImpl(ChargeMapper chargeMapper) {
        this.chargeMapper = chargeMapper;
    }

    @Override
    public void recordCharge(Charge charge) {
        chargeMapper.insertCharge(charge);
    }

    @Override
    public List<Charge> getHistory(Long realId) {
        return chargeMapper.listCharges(realId);
    }
}
