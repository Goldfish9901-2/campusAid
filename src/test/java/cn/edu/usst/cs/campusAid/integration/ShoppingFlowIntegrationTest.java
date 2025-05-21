package cn.edu.usst.cs.campusAid.integration;

import cn.edu.usst.cs.campusAid.dto.shop.ProductTransaction;
import cn.edu.usst.cs.campusAid.dto.shop.ShopInfo;
import cn.edu.usst.cs.campusAid.model.User;
import cn.edu.usst.cs.campusAid.model.shop.Shop;
import cn.edu.usst.cs.campusAid.model.shop.Good;
import cn.edu.usst.cs.campusAid.model.shop.ShopOrder;
import cn.edu.usst.cs.campusAid.model.shop.GoodTransaction;
import cn.edu.usst.cs.campusAid.mapper.db.UserMapper;
import cn.edu.usst.cs.campusAid.mapper.db.shop.ShopMapper;
import cn.edu.usst.cs.campusAid.mapper.db.shop.GoodMapper;
import cn.edu.usst.cs.campusAid.mapper.db.shop.OrderMapper;
import cn.edu.usst.cs.campusAid.mapper.db.shop.TransactionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(properties = {
    "jdk.instrument.traceUsage=false",
    "jdk.instrument.ignoreAgent=true"
})
public class ShoppingFlowIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private GoodMapper goodMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private TransactionMapper transactionMapper;

    private User testUser;
    private Shop testShop;
    private Good testGood;
    private String userSessionCookie;

    @BeforeEach
    void setUp() {
        // 创建测试用户
        testUser = createTestUser("测试用户", 13800138000L);

        // 登录获取Session
        userSessionCookie = loginAndGetSessionCookie(String.valueOf(testUser.getPhoneNumber()), "123456");

        // 创建测试商店
        testShop = Shop.builder()
                .name("测试商店")
                .description("这是一个测试商店")
                .password("test123")
                .build();
        shopMapper.insert(testShop);

        // 创建测试商品
        testGood = Good.builder()
                .id(generateUniqueId())
                .name("测试商品")
                .description("这是一个测试商品")
                .shop(testShop.getName())
                .price(99.9)
                .build();
        goodMapper.insert(testGood);

        // 添加商品库存
        GoodTransaction transaction = GoodTransaction.builder()
                .id(generateUniqueId())
                .goodId(testGood.getId())
                .amount(10L)
                .build();
        transactionMapper.insert(transaction);
    }

    @Test
    void testCompleteShoppingFlow() {
        // 1. 用户浏览商品
        HttpEntity<?> browseEntity = new HttpEntity<>(createSessionHeaders(userSessionCookie));
        ResponseEntity<ShopInfo> browseResponse = restTemplate.exchange(
            getBaseUrl() + "/api/shop/info/" + testShop.getName(),
            HttpMethod.GET,
            browseEntity,
            ShopInfo.class
        );
        assertEquals(HttpStatus.OK, browseResponse.getStatusCode());
        assertNotNull(browseResponse.getBody());
        ShopInfo shopInfo = browseResponse.getBody();
        assertNotNull(shopInfo.getName());
        assertEquals(testShop.getName(), shopInfo.getName());
        assertNotNull(shopInfo.getDescription());
        assertEquals(testShop.getDescription(), shopInfo.getDescription());
        assertNotNull(shopInfo.getProducts());
        assertFalse(shopInfo.getProducts().isEmpty());

        // 2. 用户下单
        ShopOrder order = ShopOrder.builder()
                .id(generateUniqueId())
                .shopperId(testUser.getId())
                .shop(testShop.getName())
                .build();
        orderMapper.insert(order);

        // 3. 验证订单创建
        HttpEntity<?> orderListEntity = new HttpEntity<>(createSessionHeaders(userSessionCookie));
        ResponseEntity<List<ProductTransaction>> orderListResponse = restTemplate.exchange(
            getBaseUrl() + "/api/shop/orders",
            HttpMethod.GET,
            orderListEntity,
            new ParameterizedTypeReference<List<ProductTransaction>>() {}
        );
        assertEquals(HttpStatus.OK, orderListResponse.getStatusCode());
        assertNotNull(orderListResponse.getBody());
        assertFalse(orderListResponse.getBody().isEmpty());

        // 4. 验证订单状态
        List<ProductTransaction> orderDetails = orderListResponse.getBody();
        assertNotNull(orderDetails);
        assertFalse(orderDetails.isEmpty());
        ProductTransaction orderDetail = orderDetails.get(0);
        assertEquals(testShop.getName(), orderDetail.getShopName());
    }

    @Test
    void testCancelShoppingFlow() {
        // 1. 创建订单
        Long orderId = generateUniqueId();
        ShopOrder order = ShopOrder.builder()
                .id(orderId)
                .shopperId(testUser.getId())
                .shop(testShop.getName())
                .build();
        orderMapper.insert(order);

        // 2. 取消订单
        HttpEntity<?> cancelEntity = new HttpEntity<>(createSessionHeaders(userSessionCookie));
        ResponseEntity<String> cancelResponse = restTemplate.exchange(
            getBaseUrl() + "/api/shop/order/" + orderId + "/cancel",
            HttpMethod.POST,
            cancelEntity,
            String.class
        );
        assertEquals(HttpStatus.OK, cancelResponse.getStatusCode());
        assertNotNull(cancelResponse.getBody());

        // 3. 验证订单状态
        HttpEntity<?> orderDetailEntity = new HttpEntity<>(createSessionHeaders(userSessionCookie));
        ResponseEntity<ShopOrder> orderDetailResponse = restTemplate.exchange(
            getBaseUrl() + "/api/shop/order/" + orderId,
            HttpMethod.GET,
            orderDetailEntity,
            ShopOrder.class
        );
        assertEquals(HttpStatus.OK, orderDetailResponse.getStatusCode());
        assertNotNull(orderDetailResponse.getBody());
        assertEquals(orderId, orderDetailResponse.getBody().getId());
        assertEquals(testUser.getId(), orderDetailResponse.getBody().getShopperId());
        assertEquals(testShop.getName(), orderDetailResponse.getBody().getShop());
    }
} 