package cn.edu.usst.cs.campusAid.mapper.db.errand;

import cn.edu.usst.cs.campusAid.model.errand.Errand;
import cn.edu.usst.cs.campusAid.dto.errand.ErrandOrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ErrandMapperTest {

    @Autowired
    private ErrandMapper errandMapper;

    private static final AtomicLong counter = new AtomicLong(0);

    private Long generateUniqueId() {
        return LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(8)) * 1000000 + 
               counter.incrementAndGet();
    }

    private Errand createTestErrand(Long id, Long senderId, ErrandOrderStatus status) {
        return Errand.builder()
                .id(id)
                .senderId(senderId)
                .title("测试跑腿订单")
                .errandDescription("这是一个测试跑腿订单")
                .fee(10.0)
                .startLocation("起点")
                .endLocation("终点")
                .latestArrivalTime(LocalDateTime.now().plusHours(1))
                .status(status)
                .build();
    }

    @Test
    public void testInsertAndSelectById() {
        // 准备测试数据
        Long errandId = generateUniqueId();
        Errand errand = createTestErrand(errandId, 1L, ErrandOrderStatus.NORMAL);

        // 测试插入
        errandMapper.insert(errand);

        // 测试查询
        Errand retrievedErrand = errandMapper.selectById(errandId);

        // 验证结果
        assertNotNull(retrievedErrand);
        assertEquals(errand.getId(), retrievedErrand.getId());
        assertEquals(errand.getSenderId(), retrievedErrand.getSenderId());
        assertEquals(errand.getTitle(), retrievedErrand.getTitle());
        assertEquals(errand.getErrandDescription(), retrievedErrand.getErrandDescription());
        assertEquals(errand.getFee(), retrievedErrand.getFee());
        assertEquals(errand.getStartLocation(), retrievedErrand.getStartLocation());
        assertEquals(errand.getEndLocation(), retrievedErrand.getEndLocation());
        assertEquals(errand.getStatus(), retrievedErrand.getStatus());
    }

    @Test
    public void testSelectUnacceptedOrders() {
        // 准备测试数据
        Long errandId1 = generateUniqueId();
        Errand errand1 = createTestErrand(errandId1, 1L, ErrandOrderStatus.NORMAL);
        errandMapper.insert(errand1);

        Long errandId2 = generateUniqueId();
        Errand errand2 = createTestErrand(errandId2, 2L, ErrandOrderStatus.NORMAL);
        errandMapper.insert(errand2);

        // 测试查询未被接单的订单
        List<Errand> unacceptedOrders = errandMapper.selectUnacceptedOrders(1L);

        // 验证结果
        assertNotNull(unacceptedOrders);
        assertTrue(unacceptedOrders.stream().anyMatch(e -> e.getId().equals(errandId2)));
        assertFalse(unacceptedOrders.stream().anyMatch(e -> e.getId().equals(errandId1)));
    }

    @Test
    public void testSelectErrandsAcceptedByUser() {
        // 准备测试数据
        Long errandId = generateUniqueId();
        Errand errand = createTestErrand(errandId, 1L, ErrandOrderStatus.NORMAL);
        errandMapper.insert(errand);

        // 更新接受者ID
        errandMapper.updateAcceptorId(errandId, 2L);
        errandMapper.updateErrand(errandId, ErrandOrderStatus.CONFIRMED);

        // 测试查询用户接受的订单
        List<Errand> acceptedErrands = errandMapper.selectErrandsAcceptedByUser(2L);

        // 验证结果
        assertNotNull(acceptedErrands);
        assertTrue(acceptedErrands.stream().anyMatch(e -> e.getId().equals(errandId)));
    }

    @Test
    public void testSelectErrandsPublishedByUser() {
        // 准备测试数据
        Long errandId = generateUniqueId();
        Errand errand = createTestErrand(errandId, 1L, ErrandOrderStatus.NORMAL);
        errandMapper.insert(errand);

        // 测试查询用户发布的订单
        List<Errand> publishedErrands = errandMapper.selectErrandsPublishedByUser(1L);

        // 验证结果
        assertNotNull(publishedErrands);
        assertTrue(publishedErrands.stream().anyMatch(e -> e.getId().equals(errandId)));
    }

    @Test
    public void testUpdateErrandStatus() {
        // 准备测试数据
        Long errandId = generateUniqueId();
        Errand errand = createTestErrand(errandId, 1L, ErrandOrderStatus.NORMAL);
        errandMapper.insert(errand);

        // 测试更新状态
        errandMapper.updateErrand(errandId, ErrandOrderStatus.CONFIRMED);

        // 验证结果
        Errand updatedErrand = errandMapper.selectById(errandId);
        assertNotNull(updatedErrand);
        assertEquals(ErrandOrderStatus.CONFIRMED, updatedErrand.getStatus());
    }

    @Test
    public void testUpdateAcceptorId() {
        // 准备测试数据
        Long errandId = generateUniqueId();
        Errand errand = createTestErrand(errandId, 1L, ErrandOrderStatus.NORMAL);
        errandMapper.insert(errand);

        // 测试更新接受者ID
        errandMapper.updateAcceptorId(errandId, 2L);

        // 验证结果
        Errand updatedErrand = errandMapper.selectById(errandId);
        assertNotNull(updatedErrand);
        assertEquals(2L, updatedErrand.getAcceptorId());
    }
} 