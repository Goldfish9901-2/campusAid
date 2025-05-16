package cn.edu.usst.cs.campusAid;


import cn.edu.usst.cs.campusAid.dto.errand.ErrandOrderPreview;
import cn.edu.usst.cs.campusAid.dto.errand.ErrandOrderStatus;
import cn.edu.usst.cs.campusAid.mapper.db.errand.ErrandMapper;
import cn.edu.usst.cs.campusAid.model.errand.Errand;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ErrandMapper 单元测试类（不依赖 beforeEach 插入数据）
 * <p>
 * 测试目标：
 * - 验证所有 Mapper 方法是否能正常执行
 * - 所有测试依赖 testInsertOrder 的插入结果
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // 按照 @Order 注解排序
public class ErrandMapperTest {

    private static final Logger log = LoggerFactory.getLogger(ErrandMapperTest.class);

    @Autowired
    private ErrandMapper errandMapper;

    private static Long testUserId = 2235062128L;
    private static Long testOrderId = 1L;

    /**
     * 测试插入订单（后续测试依赖该订单ID）
     */
    @Test
    @Order(1)
    public void testInsertOrder() {
        log.info("【测试方法】testInsertOrder()");

        LocalDateTime now = LocalDateTime.now().plusHours(2);

        Errand errand = Errand.builder()
                .id(testOrderId)
                .senderId(testUserId)
                .title("测试跑腿单")
                .errandDescription("这是一个测试订单描述")
                .fee(10.0)
                .startLocation("起点")
                .endLocation("终点")
                .latestArrivalTime(now)
                .status(ErrandOrderStatus.NORMAL)
                .build();

        errandMapper.insert(errand);
        testOrderId = errand.getId();

        assertNotNull(testOrderId, "插入失败，订单ID为空");
        log.info("【测试结果】插入订单成功，ID: {}", testOrderId);
    }

    /**
     * 测试方法：获取最小可用ID
     */
    @Test
    @Order(2)
    public void testMinFreeId() {
        log.info("【测试方法】minFreeId()");
        Long freeId = errandMapper.minFreeId();
        System.out.println(freeId);
        assertNotNull(freeId, "应存在可复用的订单ID");
        log.info("【测试结果】找到最小可用ID: {}", freeId);
    }

    /**
     * 测试方法：根据ID查询订单
     */
    @Test
    @Order(3)
    public void testSelectById() {
        log.info("【测试方法】selectById()");
        Errand found = errandMapper.selectById(testOrderId);
        assertNotNull(found, "未找到预期的订单");
        assertEquals(testOrderId, found.getId(), "订单ID不匹配");
        log.info("【测试结果】成功查到订单 ID: {}", testOrderId);
    }

    /**
     * 测试方法：查询用户未接单的订单列表
     */
    @Test
    @Order(4)
    public void testSelectUnacceptedOrders() {
        log.info("【测试方法】selectUnacceptedOrders()");
        List<Errand> orders = errandMapper.selectUnacceptedOrders(testUserId);
        assertNotNull(orders, "返回结果为空");
        assertTrue(orders.size() > 0, "没有未接单的订单");
        log.info("【测试结果】找到 {} 条未接单记录", orders.size());
    }

    /**
     * 测试方法：查询用户未接单的订单预览信息
     */
    @Test
    @Order(5)
    public void testSelectUnacceptedOrderPreviews() {
        log.info("【测试方法】selectUnacceptedOrderPreviews()");
        List<ErrandOrderPreview> previews = errandMapper.selectUnacceptedOrderPreviews(testUserId);
        assertNotNull(previews, "预览结果为空");
        assertTrue(previews.size() > 0, "没有未接单的订单预览");
        log.info("【测试结果】找到 {} 条预览记录", previews.size());
    }

    /**
     * 测试方法：更新订单状态
     */
    @Test
    @Order(6)
    public void testUpdateErrand() {
        log.info("【测试方法】updateErrand()");
        errandMapper.updateErrand(testOrderId, ErrandOrderStatus.COMPLETED);
        Errand updated = errandMapper.selectById(testOrderId);
        assertEquals(ErrandOrderStatus.COMPLETED, updated.getStatus(), "状态更新失败");
        log.info("【测试结果】订单状态已更新为 COMPLETED");
    }

    /**
     * 测试方法：更新接受者ID
     */
    @Test
    @Order(7)
    public void testUpdateAcceptorId() {
        log.info("【测试方法】updateAcceptorId()");
        Long acceptorId = 2235062121L;
        errandMapper.updateAcceptorId(testOrderId, acceptorId);
        Errand updated = errandMapper.selectById(testOrderId);
        assertEquals(acceptorId, updated.getAcceptorId(), "接受者ID更新失败");
        log.info("【测试结果】接受者ID更新成功: {}", acceptorId);
    }

    /**
     * 清理测试数据（可选，手动调用或最后执行）
     */
    @Test
    @Order(8)
    public void testCleanup() {
        log.info("【清理】删除测试订单 ID: {}", testOrderId);
        errandMapper.updateErrand(testOrderId, ErrandOrderStatus.CANCELLED);
        log.info("【清理】订单状态更新为 CANCELLED");
    }
}
