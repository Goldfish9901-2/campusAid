package cn.edu.usst.cs.campusAid.service.impl.charge;

import cn.edu.usst.cs.campusAid.mapper.db.charge.ChargeMapper;
import cn.edu.usst.cs.campusAid.model.charge.Charge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChargeServiceImplTest {

    @Mock
    private ChargeMapper chargeMapper;

    @InjectMocks
    private ChargeServiceImpl chargeService;

    private static final Long TEST_USER_ID = 2235062129L;
    private static final double TEST_AMOUNT = 100.0;

    private Charge testCharge;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        testCharge = Charge.builder()
                .id(1L)
                .userId(TEST_USER_ID)
                .amount(TEST_AMOUNT)
                .chargeTime(LocalDateTime.now())
                .build();
    }

    @Test
    void testRecordCharge() {
        // 准备测试数据
        doNothing().when(chargeMapper).insertCharge(any(Charge.class));

        // 执行测试
        chargeService.recordCharge(testCharge);

        // 验证结果
        verify(chargeMapper, times(1)).insertCharge(testCharge);
    }

    @Test
    void testGetHistory_WhenChargesExist() {
        // 准备测试数据
        List<Charge> expectedCharges = new ArrayList<>();
        expectedCharges.add(testCharge);
        when(chargeMapper.listCharges(TEST_USER_ID)).thenReturn(expectedCharges);

        // 执行测试
        List<Charge> result = chargeService.getHistory(TEST_USER_ID);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testCharge, result.get(0));
        verify(chargeMapper, times(1)).listCharges(TEST_USER_ID);
    }

    @Test
    void testGetHistory_WhenNoCharges() {
        // 准备测试数据
        when(chargeMapper.listCharges(TEST_USER_ID)).thenReturn(new ArrayList<>());

        // 执行测试
        List<Charge> result = chargeService.getHistory(TEST_USER_ID);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(chargeMapper, times(1)).listCharges(TEST_USER_ID);
    }
} 