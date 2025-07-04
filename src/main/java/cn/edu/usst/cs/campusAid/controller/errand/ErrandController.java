package cn.edu.usst.cs.campusAid.controller.errand;

import cn.edu.usst.cs.campusAid.config.AdminConfig;
import cn.edu.usst.cs.campusAid.controller.SessionKeys;
import cn.edu.usst.cs.campusAid.dto.ApiResponse;
import cn.edu.usst.cs.campusAid.dto.errand.ErrandOrderPreview;
import cn.edu.usst.cs.campusAid.dto.errand.ErrandOrderRequest;
import cn.edu.usst.cs.campusAid.model.errand.Errand;
import cn.edu.usst.cs.campusAid.service.UploadFileSystemService;
import cn.edu.usst.cs.campusAid.service.errand.ErrandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * 跑腿服务控制器
 * 提供跑腿订单相关的 REST 接口，包括发布、查看、接单、确认完成和取消订单
 */
@RestController
@RequestMapping("/api/errand")
public class ErrandController {

    private final ErrandService errandService;
    private final UploadFileSystemService uploadFileSystemService;
    private final AdminConfig adminConfig;

    public ErrandController(ErrandService errandService, UploadFileSystemService uploadFileSystemService, AdminConfig adminConfig) {
        this.errandService = errandService;
        this.uploadFileSystemService = uploadFileSystemService;
        this.adminConfig = adminConfig;
    }

    /**
     * 发布跑腿订单
     *
     * @param request 请求体，包含订单信息
     * @param userId  当前登录用户 ID（从 Session 获取）
     * @return 成功响应
     */
    @PostMapping("/order")
    public ResponseEntity<ApiResponse<String>> publishOrder(
            @RequestBody ErrandOrderRequest request,
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId) {
        Long orderId = errandService.publishOrder(request, userId);
        return ResponseEntity.ok(ApiResponse.success("订单发布成功" + orderId));
    }

    /**
     * 获取所有 未被接单的 跑腿订单列表
     *
     * @param userId 当前登录用户 ID（用于鉴权）
     * @return 订单预览列表
     */
    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<List<ErrandOrderPreview>>> listOrders(
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId) {
        List<ErrandOrderPreview> orders = errandService.listOrders(userId);
        System.out.println("获取到的订单数据: " + orders);  // 添加调试日志
        return ResponseEntity.ok(ApiResponse.success(orders));
    }

    /**
     * 获取用户的历史跑腿订单（仅限用户自己或管理员调用）
     *
     * @param userId      当前登录用户 ID（用于鉴权）
     * @param queryUserId 指定查询的用户 ID（当为 null 时默认为当前用户）
     * @return 用户的历史订单预览列表
     */
    @GetMapping("/orders/history")
    public ResponseEntity<ApiResponse<List<ErrandOrderPreview>>> listUserHistoricalOrders(
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId,
            @RequestParam(name = "userId", required = false) Long queryUserId) {
        Long targetUserId = queryUserId;
        try {
            Objects.requireNonNull(targetUserId);
            adminConfig.verifyIsAdmin(userId);
        } catch (RuntimeException runtimeException) {
            //  非有效管理员查询 回退到自己的信息
            targetUserId = userId;
        }

        List<ErrandOrderPreview> historicalOrders = errandService.listUserHistoricalOrders(targetUserId);
        return ResponseEntity.ok(ApiResponse.success(historicalOrders));
    }

    /**
     * 获取指定订单的详细信息
     *
     * @param id     订单 ID
     * @param userId 当前登录用户 ID
     * @return 订单详情
     */
    @GetMapping("/order/{id}")
    public ResponseEntity<ApiResponse<Errand>> getOrderDetail(
            @PathVariable Long id,
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId) {
        Errand order = errandService.getOrderDetail(id, userId);
        return ResponseEntity.ok(ApiResponse.success(order));
    }

    /**
     * 跑腿员接单
     *
     * @param id       订单 ID
     * @param runnerId 当前登录用户 ID（跑腿员）
     * @return 接单结果
     */
    @PostMapping("/order/{id}/accept")
    public ResponseEntity<ApiResponse<String>> acceptOrder(
            @PathVariable Long id,
            @SessionAttribute(SessionKeys.LOGIN_ID) Long runnerId) {
        String message = errandService.acceptOrder(id, runnerId);
        return ResponseEntity.ok(ApiResponse.success("接单成功" + message));
    }

    /**
     * 用户手动确认订单完成
     *
     * @param id     订单 ID
     * @param userId 当前登录用户 ID
     * @return 确认结果
     */
    @PostMapping("/order/{id}/confirm")
    public ResponseEntity<ApiResponse<String>> confirmOrder(
            @PathVariable Long id,
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId) {
        String message = errandService.confirmOrder(id, userId);
        return ResponseEntity.ok(ApiResponse.success("确认成功" + message));
    }

    /**
     * 用户取消订单（仅限未接单时调用）
     *
     * @param id     订单 ID
     * @param userId 当前登录用户 ID
     * @return 取消结果
     */
    @PostMapping("/order/{id}/cancel")
    public ResponseEntity<ApiResponse<String>> cancelOrder(
            @PathVariable Long id,
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId) {
        String message = errandService.cancelOrder(id, userId);
        return ResponseEntity.ok(ApiResponse.success("取消成功" + message));
    }

    /**
     * 上传附件（物品描述等）
     *
     * @param file     上传的文件
     * @param userId   当前登录用户 ID
     * @param errandId 跑腿单号
     * @return 上传结果
     */

    @PostMapping("/order/{errandId}/upload")
    public ResponseEntity<String> uploadImage(
            @RequestParam("file") MultipartFile file,
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId,
            @PathVariable Long errandId
    ) {
        File uri = uploadFileSystemService.getErrandDir(errandId);
        errandService.verifyPublisher(userId, errandId);
        var location = uploadFileSystemService.uploadFile(uri, file);
        return ResponseEntity.ok("上传成功 可在 " + location + " 查看");
    }

    /**
     * 查看跑腿所有附件
     */
    @GetMapping("/order/{errandId}/uploaded")
    public ResponseEntity<List<String>> getUploadedImage(
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId,
            @PathVariable Long errandId
    ) {
        File uri = uploadFileSystemService.getErrandDir(errandId);
        String[] urls = uploadFileSystemService.listFiles(uri);
        return ResponseEntity.ok(List.of(urls));
    }

}
