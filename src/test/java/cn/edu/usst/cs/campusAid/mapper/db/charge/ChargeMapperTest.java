package cn.edu.usst.cs.campusAid.mapper.db.charge;

import cn.edu.usst.cs.campusAid.model.charge.Charge;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ChargeMapperTest {

    @Autowired
    private ChargeMapper chargeMapper;

    @Test
    public void testInsertAndListCharges() {
        // 准备测试数据
        Charge charge = Charge.builder()
                .id(1L)
                .userId(1L)
                .chargeTime(LocalDateTime.now())
                .amount(100.0)
                .build();

        // 测试插入
        chargeMapper.insertCharge(charge);
        
        // 测试查询
        List<Charge> charges = chargeMapper.listCharges(1L);
        
        // 验证结果
        assertNotNull(charges);
        assertFalse(charges.isEmpty());
        assertEquals(1, charges.size());
        
        Charge retrievedCharge = charges.get(0);
        assertEquals(charge.getId(), retrievedCharge.getId());
        assertEquals(charge.getUserId(), retrievedCharge.getUserId());
        assertEquals(charge.getAmount(), retrievedCharge.getAmount());
    }

    @Test
    public void testListChargesWithNoRecords() {
        // 测试查询不存在的用户充值记录
        List<Charge> charges = chargeMapper.listCharges(999L);
        
        // 验证结果
        assertNotNull(charges);
        assertTrue(charges.isEmpty());
    }
} 