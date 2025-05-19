package cn.edu.usst.cs.campusAid.service.impl.errand;

import cn.edu.usst.cs.campusAid.config.AdminConfig;
import cn.edu.usst.cs.campusAid.dto.errand.ErrandOrderPreview;
import cn.edu.usst.cs.campusAid.dto.errand.ErrandOrderRequest;
import cn.edu.usst.cs.campusAid.dto.errand.ErrandOrderStatus;
import cn.edu.usst.cs.campusAid.mapper.db.UserMapper;
import cn.edu.usst.cs.campusAid.mapper.db.errand.ErrandMapper;
import cn.edu.usst.cs.campusAid.mapper.mapstruct.ErrandViewsMapper;
import cn.edu.usst.cs.campusAid.model.User;
import cn.edu.usst.cs.campusAid.model.errand.Errand;
import cn.edu.usst.cs.campusAid.service.CampusAidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ErrandServiceImplTest {

    @Mock
    private ErrandViewsMapper errandViewsMapper;

    @Mock
    private ErrandMapper errandMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private ScheduledExecutorService executorService;

    @Mock
    private AdminConfig adminConfig;

    @InjectMocks
    private ErrandServiceImpl errandService;

    private static final Long TEST_USER_ID = 2235062129L;
    private static final Long TEST_RUNNER_ID = 2235062130L;
    private static final Long TEST_ORDER_ID = 1L;

    private Errand testErrand;
    private ErrandOrderRequest testRequest;
    private User testUser;

    @BeforeEach
    void setUp() throws Exception {
        // 初始化测试数据
        testUser = User.builder()
                .id(TEST_USER_ID)
                .build();

        testErrand = Errand.builder()
                .id(TEST_ORDER_ID)
                .senderId(TEST_USER_ID)
                .title("Test Errand")
                .errandDescription("Test Description")
                .fee(10.0)
                .startLocation("Start")
                .endLocation("End")
                .latestArrivalTime(LocalDateTime.now().plusDays(1))
                .status(ErrandOrderStatus.NORMAL)
                .build();

        testRequest = ErrandOrderRequest.builder()
                .title("Test Errand")
                .description("Test Description")
                .fee(10.0)
                .startLocation("Start")
                .endLocation("End")
                .latestArrivalTime(LocalDateTime.now().plusDays(1))
                .build();

        // 反射注入mock adminConfig
        Field field = ErrandServiceImpl.class.getDeclaredField("adminConfig");
        field.setAccessible(true);
        field.set(errandService, adminConfig);
    }

    @Test
    void testPublishOrder_Success() {
        // 准备测试数据
        when(errandMapper.minFreeId()).thenReturn(TEST_ORDER_ID);
        when(errandViewsMapper.wrapIntoErrand(any(ErrandOrderRequest.class))).thenReturn(testErrand);
        doNothing().when(errandMapper).insert(any(Errand.class));
        when(executorService.schedule(any(Runnable.class), anyLong(), any())).thenReturn(null);

        // 执行测试
        Long result = errandService.publishOrder(testRequest, TEST_USER_ID);

        // 验证结果
        assertEquals(TEST_ORDER_ID, result);
        verify(errandMapper, times(1)).minFreeId();
        verify(errandViewsMapper, times(1)).wrapIntoErrand(any(ErrandOrderRequest.class));
        verify(errandMapper, times(1)).insert(any(Errand.class));
        verify(executorService, times(1)).schedule(any(Runnable.class), anyLong(), any());
    }

    @Test
    void testPublishOrder_WhenNoFreeId() {
        // 准备测试数据
        when(errandMapper.minFreeId()).thenReturn(null);

        // 执行测试并验证异常
        assertThrows(CampusAidException.class, () -> errandService.publishOrder(testRequest, TEST_USER_ID));
        verify(errandMapper, times(1)).minFreeId();
    }

    @Test
    void testListOrders() {
        // 准备测试数据
        List<ErrandOrderPreview> expectedPreviews = new ArrayList<>();
        when(errandMapper.selectUnacceptedOrderPreviews(TEST_USER_ID)).thenReturn(expectedPreviews);

        // 执行测试
        List<ErrandOrderPreview> result = errandService.listOrders(TEST_USER_ID);

        // 验证结果
        assertNotNull(result);
        assertEquals(expectedPreviews, result);
        verify(errandMapper, times(1)).selectUnacceptedOrderPreviews(TEST_USER_ID);
    }

    @Test
    void testGetOrderDetail_WhenUserIsSender() {
        // 准备测试数据
        when(errandMapper.selectById(TEST_ORDER_ID)).thenReturn(testErrand);

        // 执行测试
        Errand result = errandService.getOrderDetail(TEST_ORDER_ID, TEST_USER_ID);

        // 验证结果
        assertNotNull(result);
        assertEquals(testErrand, result);
        verify(errandMapper, times(1)).selectById(TEST_ORDER_ID);
    }

    @Test
    void testGetOrderDetail_WhenUserIsAcceptor() {
        // 准备测试数据
        testErrand.setAcceptorId(TEST_RUNNER_ID);
        when(errandMapper.selectById(TEST_ORDER_ID)).thenReturn(testErrand);

        // 执行测试
        Errand result = errandService.getOrderDetail(TEST_ORDER_ID, TEST_RUNNER_ID);

        // 验证结果
        assertNotNull(result);
        assertEquals(testErrand, result);
        verify(errandMapper, times(1)).selectById(TEST_ORDER_ID);
    }

    @Test
    void testGetOrderDetail_WhenUserIsNeitherPublisherNorAcceptor() {
        // 准备测试数据
        Long otherUserId = 999L; // 既不是sender也不是acceptor
        testErrand.setAcceptorId(TEST_RUNNER_ID);
        when(errandMapper.selectById(TEST_ORDER_ID)).thenReturn(testErrand);
        doNothing().when(adminConfig).verifyIsAdmin(otherUserId);

        // 执行测试
        Errand result = errandService.getOrderDetail(TEST_ORDER_ID, otherUserId);

        // 验证结果
        assertNotNull(result);
        assertEquals(testErrand, result);
        verify(errandMapper, times(1)).selectById(TEST_ORDER_ID);
        verify(adminConfig, times(1)).verifyIsAdmin(otherUserId);
    }

    @Test
    void testGetOrderDetail_WhenUserIsNeitherPublisherNorAcceptorAndNotAdmin() {
        // 准备测试数据
        Long otherUserId = 999L; // 既不是sender也不是acceptor
        testErrand.setAcceptorId(TEST_RUNNER_ID);
        when(errandMapper.selectById(TEST_ORDER_ID)).thenReturn(testErrand);
        doThrow(new CampusAidException("用户无权限")).when(adminConfig).verifyIsAdmin(anyLong());

        // 执行测试并验证异常
        CampusAidException exception = assertThrows(CampusAidException.class,
            () -> errandService.getOrderDetail(TEST_ORDER_ID, otherUserId));
        assertEquals("用户无权限", exception.getMessage());

        verify(errandMapper, times(1)).selectById(TEST_ORDER_ID);
        verify(adminConfig, times(1)).verifyIsAdmin(otherUserId);
    }

    @Test
    void testAcceptOrder_Success() {
        // 准备测试数据
        when(errandMapper.selectById(TEST_ORDER_ID)).thenReturn(testErrand);
        doNothing().when(errandMapper).updateAcceptorId(TEST_ORDER_ID, TEST_RUNNER_ID);

        // 执行测试
        String result = errandService.acceptOrder(TEST_ORDER_ID, TEST_RUNNER_ID);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.contains("接单成功"));
        verify(errandMapper, times(1)).selectById(TEST_ORDER_ID);
        verify(errandMapper, times(1)).updateAcceptorId(TEST_ORDER_ID, TEST_RUNNER_ID);
    }

    @Test
    void testAcceptOrder_WhenAlreadyAccepted() {
        // 准备测试数据
        testErrand.setAcceptorId(TEST_RUNNER_ID);
        when(errandMapper.selectById(TEST_ORDER_ID)).thenReturn(testErrand);
        when(userMapper.getUserById(TEST_RUNNER_ID)).thenReturn(testUser);

        // 执行测试并验证异常
        assertThrows(CampusAidException.class, () -> errandService.acceptOrder(TEST_ORDER_ID, TEST_RUNNER_ID));
        verify(errandMapper, times(1)).selectById(TEST_ORDER_ID);
        verify(userMapper, times(1)).getUserById(TEST_RUNNER_ID);
    }

    @Test
    void testAcceptOrder_WhenUserIsSender() {
        // 准备测试数据
        when(errandMapper.selectById(TEST_ORDER_ID)).thenReturn(testErrand);

        // 执行测试并验证异常
        assertThrows(CampusAidException.class, () -> errandService.acceptOrder(TEST_ORDER_ID, TEST_USER_ID));
        verify(errandMapper, times(1)).selectById(TEST_ORDER_ID);
    }

    @Test
    void testConfirmOrder_WhenUserIsAcceptor() {
        // 准备测试数据
        testErrand.setAcceptorId(TEST_RUNNER_ID);
        when(errandMapper.selectById(TEST_ORDER_ID)).thenReturn(testErrand);
        doNothing().when(errandMapper).updateErrand(TEST_ORDER_ID, ErrandOrderStatus.CONFIRMED);
        when(executorService.schedule(any(Runnable.class), anyLong(), any())).thenReturn(null);

        // 执行测试
        String result = errandService.confirmOrder(TEST_ORDER_ID, TEST_RUNNER_ID);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.contains("确认成功"));
        verify(errandMapper, times(1)).selectById(TEST_ORDER_ID);
        verify(errandMapper, times(1)).updateErrand(TEST_ORDER_ID, ErrandOrderStatus.CONFIRMED);
        verify(executorService, times(1)).schedule(any(Runnable.class), anyLong(), any());
    }

    @Test
    void testConfirmOrder_WhenUserIsSender() {
        // 准备测试数据
        testErrand.setAcceptorId(TEST_RUNNER_ID);
        when(errandMapper.selectById(TEST_ORDER_ID)).thenReturn(testErrand);
        doNothing().when(errandMapper).updateErrand(TEST_ORDER_ID, ErrandOrderStatus.COMPLETED);
        when(executorService.schedule(any(Runnable.class), anyLong(), any())).thenReturn(null);

        // 执行测试
        String result = errandService.confirmOrder(TEST_ORDER_ID, TEST_USER_ID);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.contains("确认成功"));
        verify(errandMapper, times(1)).selectById(TEST_ORDER_ID);
        verify(errandMapper, times(1)).updateErrand(TEST_ORDER_ID, ErrandOrderStatus.COMPLETED);
        verify(executorService, times(1)).schedule(any(Runnable.class), anyLong(), any());
    }

    @Test
    void testCancelOrder_Success() {
        // 准备测试数据
        when(errandMapper.selectById(TEST_ORDER_ID)).thenReturn(testErrand);
        doNothing().when(errandMapper).updateErrand(TEST_ORDER_ID, ErrandOrderStatus.CANCELLED);

        // 执行测试
        String result = errandService.cancelOrder(TEST_ORDER_ID, TEST_USER_ID);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.contains("取消成功"));
        verify(errandMapper, times(1)).selectById(TEST_ORDER_ID);
        verify(errandMapper, times(1)).updateErrand(TEST_ORDER_ID, ErrandOrderStatus.CANCELLED);
    }

    @Test
    void testCancelOrder_WhenAlreadyAccepted() {
        // 准备测试数据
        testErrand.setAcceptorId(TEST_RUNNER_ID);
        when(errandMapper.selectById(TEST_ORDER_ID)).thenReturn(testErrand);
        when(userMapper.getUserById(TEST_RUNNER_ID)).thenReturn(testUser);

        // 执行测试并验证异常
        assertThrows(CampusAidException.class, () -> errandService.cancelOrder(TEST_ORDER_ID, TEST_USER_ID));
        verify(errandMapper, times(1)).selectById(TEST_ORDER_ID);
        verify(userMapper, times(1)).getUserById(TEST_RUNNER_ID);
    }

    @Test
    void testCancelOrder_WhenUserIsNotPublisher() {
        // 准备测试数据
        when(errandMapper.selectById(TEST_ORDER_ID)).thenReturn(testErrand);

        // 执行测试并验证异常
        assertThrows(CampusAidException.class, () -> errandService.cancelOrder(TEST_ORDER_ID, TEST_RUNNER_ID));
        verify(errandMapper, times(1)).selectById(TEST_ORDER_ID);
    }

    @Test
    void testScheduleAutoConfirmTask_Success() {
        // 准备测试数据
        LocalDateTime now = LocalDateTime.now();
        testErrand.setLatestArrivalTime(now.plusHours(1));
        when(errandMapper.minFreeId()).thenReturn(TEST_ORDER_ID);
        when(errandViewsMapper.wrapIntoErrand(any(ErrandOrderRequest.class))).thenReturn(testErrand);
        doNothing().when(errandMapper).insert(any(Errand.class));
        when(executorService.schedule(any(Runnable.class), anyLong(), any())).thenReturn(null);

        // 执行测试
        Long result = errandService.publishOrder(testRequest, TEST_USER_ID);

        // 验证结果
        assertEquals(TEST_ORDER_ID, result);
        verify(errandMapper, times(1)).minFreeId();
        verify(errandViewsMapper, times(1)).wrapIntoErrand(any(ErrandOrderRequest.class));
        verify(errandMapper, times(1)).insert(any(Errand.class));
        verify(executorService, times(1)).schedule(any(Runnable.class), anyLong(), any());
    }

    @Test
    void testScheduleAutoConfirmTask_WhenLatestArrivalTimePassed() {
        // 准备测试数据
        LocalDateTime now = LocalDateTime.now();
        testErrand.setLatestArrivalTime(now.minusHours(1));
        when(errandMapper.minFreeId()).thenReturn(TEST_ORDER_ID);
        when(errandViewsMapper.wrapIntoErrand(any(ErrandOrderRequest.class))).thenReturn(testErrand);

        // 执行测试并验证异常
        assertThrows(CampusAidException.class, () -> errandService.publishOrder(testRequest, TEST_USER_ID));
    }
} 