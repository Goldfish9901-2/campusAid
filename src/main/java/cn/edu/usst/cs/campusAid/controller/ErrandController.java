package cn.edu.usst.cs.campusAid.controller;

import cn.edu.usst.cs.campusAid.dto.errand.*;
import cn.edu.usst.cs.campusAid.service.ErrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@RestController
//@RequestMapping("/api/errand")
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
     * 获取跑腿订单列表（功能点 4），默认按时间排序
     */
    @GetMapping("/orders")
    public ResponseEntity<List<ErrandOrderPreview>> listOrders() {
        List<ErrandOrderPreview> orders = errandService.listOrders();
        return ResponseEntity.ok().body(orders);

    }

    /**
     * 获取单个订单详细信息
     */
    @GetMapping("/order/{id}")
    public ResponseEntity<ErrandOrderView> getOrderDetail(
            @PathVariable Long id,
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId
    ) {
        ErrandOrderView orderDetail = errandService.getOrderDetail(id, userId);
        return ResponseEntity.ok().body(orderDetail);
//        return ResponseEntity.ok().build(); // 后续可增加权限校验逻辑
    }

    /**
     * 跑腿员接单（功能点 7）
     * @param runnerId  接单用户id 接单前需验证
     * @param id 订单id
     */
    @PostMapping("/order/{id}/accept")
    public ResponseEntity<String> acceptOrder(
            @PathVariable Long id,
            @SessionAttribute(SessionKeys.LOGIN_ID) Long runnerId) {
        errandService.acceptOrder(id, runnerId);
        return ResponseEntity.ok("接单成功");
    }


    /**
     * 用户手动确认完成（功能点 9）
     */
    @PostMapping("/order/{id}/confirm")
    public ResponseEntity<String> confirmOrder(
            @PathVariable Long id,
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId) {
        errandService.confirmOrder(id, userId);
        return ResponseEntity.ok("确认成功");
    }

    /**
     * 用户取消订单（补充：在接单前允许取消）
     */
    @PostMapping("/order/{id}/cancel")
    public ResponseEntity<String> cancelOrder(
            @PathVariable Long id,
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId) {
        errandService.cancelOrder(id, userId);
        return ResponseEntity.ok("取消成功");
    }
}
