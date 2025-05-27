package cn.edu.usst.cs.campusAid.controller.errand;

import cn.edu.usst.cs.campusAid.controller.SessionKeys;
import cn.edu.usst.cs.campusAid.dto.ApiResponse;
import cn.edu.usst.cs.campusAid.dto.errand.ErrandOrderPreview;
import cn.edu.usst.cs.campusAid.dto.errand.ErrandOrderRequest;
import cn.edu.usst.cs.campusAid.dto.errand.ErrandOrderStatus;
import cn.edu.usst.cs.campusAid.model.errand.Errand;
import cn.edu.usst.cs.campusAid.service.UploadFileSystemService;
import cn.edu.usst.cs.campusAid.service.errand.ErrandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ErrandControllerTest {

    @Mock
    private ErrandService errandService;

    @Mock
    private UploadFileSystemService uploadFileSystemService;

    @InjectMocks
    private ErrandController errandController;

    private static final Long TEST_USER_ID = 2235062129L;
    private static final Long TEST_RUNNER_ID = 2235062130L;
    private static final Long TEST_ORDER_ID = 1L;

    private ErrandOrderRequest testRequest;
    private Errand testErrand;
    private List<ErrandOrderPreview> testPreviews;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        testRequest = ErrandOrderRequest.builder()
                .title("Test Errand")
                .description("Test Description")
                .fee(10.0)
                .startLocation("Start")
                .endLocation("End")
                .latestArrivalTime(LocalDateTime.now().plusDays(1))
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

        testPreviews = new ArrayList<>();
        testPreviews.add(ErrandOrderPreview.builder()
                .id(TEST_ORDER_ID)
                .startLocation("Start")
                .endLocation("End")
                .fee(10.0)
//                .publishTime(LocalDateTime.now())
                .status(ErrandOrderStatus.NORMAL)
                .build());
    }

    @Test
    void testPublishOrder() {
        // 准备测试数据
        when(errandService.publishOrder(any(ErrandOrderRequest.class), eq(TEST_USER_ID)))
                .thenReturn(TEST_ORDER_ID);

        // 执行测试
        ResponseEntity<ApiResponse<String>> response = errandController.publishOrder(testRequest, TEST_USER_ID);

        // 验证结果
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().getData().contains("订单发布成功"));
        assertTrue(response.getBody().getData().contains(TEST_ORDER_ID.toString()));

        // 验证服务调用
        verify(errandService).publishOrder(testRequest, TEST_USER_ID);
    }

    @Test
    void testListOrders() {
        // 准备测试数据
        when(errandService.listOrders(TEST_USER_ID)).thenReturn(testPreviews);

        // 执行测试
        ResponseEntity<ApiResponse<List<ErrandOrderPreview>>> response = errandController.listOrders(TEST_USER_ID);

        // 验证结果
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testPreviews, response.getBody().getData());

        // 验证服务调用
        verify(errandService).listOrders(TEST_USER_ID);
    }

    @Test
    void testGetOrderDetail() {
        // 准备测试数据
        when(errandService.getOrderDetail(TEST_ORDER_ID, TEST_USER_ID)).thenReturn(testErrand);

        // 执行测试
        ResponseEntity<ApiResponse<Errand>> response = errandController.getOrderDetail(TEST_ORDER_ID, TEST_USER_ID);

        // 验证结果
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testErrand, response.getBody().getData());

        // 验证服务调用
        verify(errandService).getOrderDetail(TEST_ORDER_ID, TEST_USER_ID);
    }

    @Test
    void testAcceptOrder() {
        // 准备测试数据
        String successMessage = TEST_RUNNER_ID + "--接单成功 单号--" + TEST_ORDER_ID;
        when(errandService.acceptOrder(TEST_ORDER_ID, TEST_RUNNER_ID)).thenReturn(successMessage);

        // 执行测试
        ResponseEntity<ApiResponse<String>> response = errandController.acceptOrder(TEST_ORDER_ID, TEST_RUNNER_ID);

        // 验证结果
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().getData().contains("接单成功"));
        assertTrue(response.getBody().getData().contains(TEST_ORDER_ID.toString()));

        // 验证服务调用
        verify(errandService).acceptOrder(TEST_ORDER_ID, TEST_RUNNER_ID);
    }

    @Test
    void testConfirmOrder() {
        // 准备测试数据
        String successMessage = TEST_USER_ID + "--确认成功 单号--" + TEST_ORDER_ID;
        when(errandService.confirmOrder(TEST_ORDER_ID, TEST_USER_ID)).thenReturn(successMessage);

        // 执行测试
        ResponseEntity<ApiResponse<String>> response = errandController.confirmOrder(TEST_ORDER_ID, TEST_USER_ID);

        // 验证结果
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().getData().contains("确认成功"));
        assertTrue(response.getBody().getData().contains(TEST_ORDER_ID.toString()));

        // 验证服务调用
        verify(errandService).confirmOrder(TEST_ORDER_ID, TEST_USER_ID);
    }

    @Test
    void testCancelOrder() {
        // 准备测试数据
        String successMessage = TEST_USER_ID + "--取消成功 单号--" + TEST_ORDER_ID;
        when(errandService.cancelOrder(TEST_ORDER_ID, TEST_USER_ID)).thenReturn(successMessage);

        // 执行测试
        ResponseEntity<ApiResponse<String>> response = errandController.cancelOrder(TEST_ORDER_ID, TEST_USER_ID);

        // 验证结果
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().getData().contains("取消成功"));
        assertTrue(response.getBody().getData().contains(TEST_ORDER_ID.toString()));

        // 验证服务调用
        verify(errandService).cancelOrder(TEST_ORDER_ID, TEST_USER_ID);
    }

    @Test
    void testUploadImage() throws Exception {
        // 准备测试数据
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );
        File errandDir = new File("test/errand/" + TEST_ORDER_ID);
        String uploadedLocation = "test/errand/" + TEST_ORDER_ID + "/test.jpg";

        when(uploadFileSystemService.getErrandDir(TEST_ORDER_ID)).thenReturn(errandDir);
        when(uploadFileSystemService.uploadFile(any(File.class), any(MultipartFile.class))).thenReturn(uploadedLocation);

        // 执行测试
        ResponseEntity<String> response = errandController.uploadImage(file, TEST_USER_ID, TEST_ORDER_ID);

        // 验证结果
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("上传成功"));
        assertTrue(response.getBody().contains(uploadedLocation));

        // 验证服务调用
        verify(uploadFileSystemService).getErrandDir(TEST_ORDER_ID);
        verify(uploadFileSystemService).uploadFile(errandDir, file);
        verify(errandService).verifyPublisher(TEST_USER_ID, TEST_ORDER_ID);
    }

    @Test
    void testGetUploadedImage() {
        // 准备测试数据
        File errandDir = new File("test/errand/" + TEST_ORDER_ID);
        String[] fileUrls = new String[]{"test/errand/1/file1.jpg", "test/errand/1/file2.jpg"};

        when(uploadFileSystemService.getErrandDir(TEST_ORDER_ID)).thenReturn(errandDir);
        when(uploadFileSystemService.listFiles(errandDir)).thenReturn(fileUrls);

        // 执行测试
        ResponseEntity<List<String>> response = errandController.getUploadedImage(TEST_USER_ID, TEST_ORDER_ID);

        // 验证结果
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(List.of(fileUrls), response.getBody());

        // 验证服务调用
        verify(uploadFileSystemService).getErrandDir(TEST_ORDER_ID);
        verify(uploadFileSystemService).listFiles(errandDir);
    }
} 