package cn.edu.usst.cs.campusAid.service.impl;

import cn.edu.usst.cs.campusAid.config.AdminConfig;
import cn.edu.usst.cs.campusAid.dto.errand.ErrandOrderStatus;
import cn.edu.usst.cs.campusAid.dto.shop.ProductTransaction;
import cn.edu.usst.cs.campusAid.mapper.db.UserMapper;
import cn.edu.usst.cs.campusAid.mapper.db.errand.ErrandMapper;
import cn.edu.usst.cs.campusAid.mapper.db.shop.OrderMapper;
import cn.edu.usst.cs.campusAid.mapper.db.shop.ShopMapper;
import cn.edu.usst.cs.campusAid.mapper.db.shop.TransactionMapper;
import cn.edu.usst.cs.campusAid.model.User;
import cn.edu.usst.cs.campusAid.model.charge.Charge;
import cn.edu.usst.cs.campusAid.model.errand.Errand;
import cn.edu.usst.cs.campusAid.service.CampusAidException;
import cn.edu.usst.cs.campusAid.service.auth.UserService;
import cn.edu.usst.cs.campusAid.service.charge.ChargeService;
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
class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;
    @Mock
    private AdminConfig adminConfig;
    @Mock
    private ChargeService chargeService;
    @Mock
    private ErrandMapper errandMapper;
    @Mock
    private ShopMapper shopMapper;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private static final Long TEST_USER_ID = 2235062129L;
    private static final Long TEST_ADMIN_ID = 2235062128L;
    private static final Long TEST_NORMAL_USER_ID = 2235062130L;

    @BeforeEach
    void setUp() {
        // 初始化测试环境
    }

    @Test
    void testGetUserById_WhenUserExists() {
        // 准备测试数据
        User expectedUser = User.builder()
                .id(TEST_USER_ID)
                .build();
        when(userMapper.getUserById(TEST_USER_ID)).thenReturn(expectedUser);

        // 执行测试
        User result = userService.getUserById(TEST_USER_ID);

        // 验证结果
        assertNotNull(result);
        assertEquals(TEST_USER_ID, result.getId());
    }

    @Test
    void testGetUserById_WhenUserDoesNotExist() {
        // 准备测试数据
        when(userMapper.getUserById(TEST_USER_ID)).thenReturn(null);

        // 执行测试并验证异常
        assertThrows(CampusAidException.class, () -> userService.getUserById(TEST_USER_ID));
    }

    @Test
    void testIsAdmin_WhenUserIsAdmin() {
        // 准备测试数据
        User adminUser = User.builder()
                .id(TEST_ADMIN_ID)
                .build();
        when(userMapper.getUserById(TEST_ADMIN_ID)).thenReturn(adminUser);
        when(adminConfig.getAdmin()).thenReturn(TEST_ADMIN_ID.toString());

        // 执行测试
        boolean result = userService.isAdmin(TEST_ADMIN_ID);

        // 验证结果
        assertTrue(result);
    }

    @Test
    void testIsAdmin_WhenUserIsNotAdmin() {
        // 准备测试数据
        User normalUser = User.builder()
                .id(TEST_NORMAL_USER_ID)
                .build();
        when(userMapper.getUserById(TEST_NORMAL_USER_ID)).thenReturn(normalUser);
        when(adminConfig.getAdmin()).thenReturn(TEST_ADMIN_ID.toString());

        // 执行测试
        boolean result = userService.isAdmin(TEST_NORMAL_USER_ID);

        // 验证结果
        assertFalse(result);
    }

    @Test
    void testGetTargetUserId_WhenUserIsAdmin() {
        // 准备测试数据
        doNothing().when(adminConfig).verifyIsAdmin(TEST_ADMIN_ID);

        // 执行测试
        Long result = userService.getTargetUserId(TEST_ADMIN_ID, TEST_NORMAL_USER_ID);

        // 验证结果
        assertEquals(TEST_NORMAL_USER_ID, result);
    }

    @Test
    void testGetTargetUserId_WhenUserIsNotAdmin() {
        // 准备测试数据
        doThrow(new CampusAidException("Not admin")).when(adminConfig).verifyIsAdmin(TEST_NORMAL_USER_ID);

        // 执行测试
        Long result = userService.getTargetUserId(TEST_NORMAL_USER_ID, TEST_ADMIN_ID);

        // 验证结果
        assertEquals(TEST_NORMAL_USER_ID, result);
    }

    @Test
    void testGetBalance() {
        // 准备测试数据
        List<Charge> charges = new ArrayList<>();
        Charge charge = new Charge();
        charge.setAmount(100.0);
        charges.add(charge);

        List<Errand> acceptedErrands = new ArrayList<>();
        Errand acceptedErrand = Errand.builder()
                .id(1L)
                .senderId(TEST_USER_ID)
                .title("Test Errand")
                .errandDescription("Test Description")
                .fee(50.0)
                .startLocation("Start")
                .endLocation("End")
                .latestArrivalTime(LocalDateTime.now())
                .status(ErrandOrderStatus.CONFIRMED)
                .build();
        acceptedErrands.add(acceptedErrand);

        List<Errand> publishedErrands = new ArrayList<>();
        Errand publishedErrand = Errand.builder()
                .id(2L)
                .senderId(TEST_USER_ID)
                .title("Test Errand")
                .errandDescription("Test Description")
                .fee(30.0)
                .startLocation("Start")
                .endLocation("End")
                .latestArrivalTime(LocalDateTime.now())
                .status(ErrandOrderStatus.CONFIRMED)
                .build();
        publishedErrands.add(publishedErrand);

        List<ProductTransaction> purchases = new ArrayList<>();
        ProductTransaction purchase = ProductTransaction.builder()
                .price(20.0f)
                .amount(2L)
                .build();
        purchases.add(purchase);

        when(chargeService.getHistory(TEST_USER_ID)).thenReturn(charges);
        when(errandMapper.selectErrandsAcceptedByUser(TEST_USER_ID)).thenReturn(acceptedErrands);
        when(errandMapper.selectErrandsPublishedByUser(TEST_USER_ID)).thenReturn(publishedErrands);
        when(transactionMapper.getHistory(TEST_USER_ID)).thenReturn(purchases);

        // 执行测试
        double result = userService.getBalance(TEST_USER_ID);

        // 验证结果
        // 100.0 (charge) + 50.0 (accepted errand) - 30.0 (published errand) - 40.0 (purchase) = 80.0
        assertEquals(80.0, result, 0.001);
    }
} 