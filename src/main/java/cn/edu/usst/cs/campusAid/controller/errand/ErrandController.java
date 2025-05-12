package cn.edu.usst.cs.campusAid.controller.errand;

import cn.edu.usst.cs.campusAid.controller.SessionKeys;
import cn.edu.usst.cs.campusAid.dto.errand.*;
import cn.edu.usst.cs.campusAid.service.errand.ErrandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/errand")
public class ErrandController {
//    @Autowired
    private ErrandService errandService;

    /**
     * 给想要别的用户跑腿的用户：
     * 用户发布跑腿订单（功能点 1 / 5）
     */
    @PostMapping("/order")
    public ResponseEntity<String> publishOrder(
            @RequestBody ErrandOrderRequest request,
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId) {
        errandService.publishOrder(request, userId);
        // 可以使用 userId 来关联当前登录用户
        return ResponseEntity.ok("订单发布成功");
    }

    /**
     * 给有意愿接单的用户：
     * 获取跑腿订单列表，默认按时间排序
     */
    @GetMapping("/orders")
    public ResponseEntity<List<ErrandOrderPreview>> listOrders(
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId
    ) {
        List<ErrandOrderPreview> orders = errandService.listOrders(userId);
        return ResponseEntity.ok().body(orders);
    }

    /**
     * 获取单个订单详细信息
     */
    @GetMapping("/order")
    public ResponseEntity<ErrandOrderView> getOrderDetail(
            @RequestParam("post") Long id,
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId
    ) {
        ErrandOrderView orderDetail = errandService.getOrderDetail(id, userId);
        return ResponseEntity.ok().body(orderDetail);
    }

    /**
     * 跑腿员接单
     * @param runnerId  接单用户id 接单前需验证
     * @param errandID 订单id
     */
    @PostMapping("/order/accept")
    public ResponseEntity<String> acceptOrder(
            @RequestParam("accept") Long errandID,
            @SessionAttribute(SessionKeys.LOGIN_ID) Long runnerId) {
        errandService.acceptOrder(errandID, runnerId);
        return ResponseEntity.ok("接单成功");
    }


    /**
     * 用户手动确认完成
     */
    @PostMapping("/order/{id}/confirm")
    public ResponseEntity<String> confirmOrder(
            @PathVariable Long id,
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId) {
        errandService.confirmOrder(id, userId);
        return ResponseEntity.ok("确认成功");
    }

    /**
     * 用户取消订单
     * <h2>接单后不可调用</h2>
     */
    @PostMapping("/order/{id}/cancel")
    public ResponseEntity<String> cancelOrder(
            @PathVariable Long id,
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId) {
        errandService.cancelOrder(id, userId);
        return ResponseEntity.ok("取消成功");
    }
}
