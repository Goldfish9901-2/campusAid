package cn.edu.usst.cs.campusAid.integration;

import cn.edu.usst.cs.campusAid.dto.errand.ErrandOrderRequest;
import cn.edu.usst.cs.campusAid.dto.errand.ErrandOrderStatus;
import cn.edu.usst.cs.campusAid.model.User;
import cn.edu.usst.cs.campusAid.model.errand.Errand;
import cn.edu.usst.cs.campusAid.mapper.db.UserMapper;
import cn.edu.usst.cs.campusAid.mapper.db.errand.ErrandMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(properties = {
    "jdk.instrument.traceUsage=false",
    "jdk.instrument.ignoreAgent=true"
})
public class ErrandFlowIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ErrandMapper errandMapper;

    private User testUser;
    private User testRunner;
    private String userSessionCookie;
    private String runnerSessionCookie;

    @BeforeEach
    void setUp() {
        // 创建测试用户（发布者）
        testUser = createTestUser("测试用户", 13800138000L);

        // 创建测试跑腿员
        testRunner = createTestUser("测试跑腿员", 13800138001L);

        // 登录获取Session
        userSessionCookie = loginAndGetSessionCookie(String.valueOf(testUser.getPhoneNumber()), "123456");
        runnerSessionCookie = loginAndGetSessionCookie(String.valueOf(testRunner.getPhoneNumber()), "123456");
    }

    @Test
    void testCompleteErrandFlow() {
        // 1. 创建跑腿订单请求
        ErrandOrderRequest request = ErrandOrderRequest.builder()
                .title("测试跑腿订单")
                .description("这是一个测试跑腿订单")
                .fee(50.0)
                .startLocation("起点")
                .endLocation("终点")
                .latestArrivalTime(LocalDateTime.now().plusHours(1))
                .build();

        // 2. 发布订单
        HttpEntity<ErrandOrderRequest> publishEntity = new HttpEntity<>(request, createSessionHeaders(userSessionCookie));
        ResponseEntity<String> publishResponse = restTemplate.exchange(
            getBaseUrl() + "/api/errand/order",
            HttpMethod.POST,
            publishEntity,
            String.class
        );
        assertEquals(HttpStatus.OK, publishResponse.getStatusCode());
        assertNotNull(publishResponse.getBody());

        // 3. 获取订单列表
        HttpEntity<?> listEntity = new HttpEntity<>(createSessionHeaders(userSessionCookie));
        ResponseEntity<List> listResponse = restTemplate.exchange(
            getBaseUrl() + "/api/errand/orders",
            HttpMethod.GET,
            listEntity,
            List.class
        );
        assertEquals(HttpStatus.OK, listResponse.getStatusCode());
        assertNotNull(listResponse.getBody());
        assertFalse(listResponse.getBody().isEmpty());

        // 4. 跑腿员接单
        HttpEntity<?> acceptEntity = new HttpEntity<>(createSessionHeaders(runnerSessionCookie));
        ResponseEntity<String> acceptResponse = restTemplate.exchange(
            getBaseUrl() + "/api/errand/order/1/accept",
            HttpMethod.POST,
            acceptEntity,
            String.class
        );
        assertEquals(HttpStatus.OK, acceptResponse.getStatusCode());
        assertNotNull(acceptResponse.getBody());

        // 5. 确认订单完成
        HttpEntity<?> confirmEntity = new HttpEntity<>(createSessionHeaders(userSessionCookie));
        ResponseEntity<String> confirmResponse = restTemplate.exchange(
            getBaseUrl() + "/api/errand/order/1/confirm",
            HttpMethod.POST,
            confirmEntity,
            String.class
        );
        assertEquals(HttpStatus.OK, confirmResponse.getStatusCode());
        assertNotNull(confirmResponse.getBody());

        // 6. 验证订单状态
        Errand updatedErrand = errandMapper.selectById(1L);
        assertNotNull(updatedErrand);
        assertEquals(testRunner.getId(), updatedErrand.getAcceptorId());
        assertEquals(ErrandOrderStatus.CONFIRMED, updatedErrand.getStatus());
    }

    @Test
    void testCancelErrandFlow() {
        // 1. 创建跑腿订单
        Long errandId = generateUniqueId();
        Errand errand = Errand.builder()
                .id(errandId)
                .senderId(testUser.getId())
                .title("测试跑腿订单")
                .errandDescription("这是一个测试跑腿订单")
                .fee(50.0)
                .startLocation("起点")
                .endLocation("终点")
                .latestArrivalTime(LocalDateTime.now().plusHours(1))
                .status(ErrandOrderStatus.NORMAL)
                .build();
        errandMapper.insert(errand);

        // 2. 取消订单
        HttpEntity<?> cancelEntity = new HttpEntity<>(createSessionHeaders(userSessionCookie));
        ResponseEntity<String> cancelResponse = restTemplate.exchange(
            getBaseUrl() + "/api/errand/order/" + errandId + "/cancel",
            HttpMethod.POST,
            cancelEntity,
            String.class
        );
        assertEquals(HttpStatus.OK, cancelResponse.getStatusCode());
        assertNotNull(cancelResponse.getBody());

        // 3. 验证订单状态
        Errand cancelledErrand = errandMapper.selectById(errandId);
        assertNotNull(cancelledErrand);
        assertEquals(ErrandOrderStatus.CANCELLED, cancelledErrand.getStatus());
    }
} 