package cn.edu.usst.cs.campusAid.controller;

import cn.edu.usst.cs.campusAid.dto.shop.OrderDTO;
import cn.edu.usst.cs.campusAid.dto.shop.ProductTransaction;
import cn.edu.usst.cs.campusAid.dto.shop.ShopInfo;
import cn.edu.usst.cs.campusAid.service.shop.OrderService;
import cn.edu.usst.cs.campusAid.service.shop.ProductService;
import cn.edu.usst.cs.campusAid.service.shop.ShopService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

//@RestController
//@RequestMapping("/api/shop")
public  class ShopController {
//    @Autowired
    private ShopService shopService;

//    @Autowired
    private OrderService orderService;
//    @Autowired
    private ProductService productService;
    /**
     * 商户登录
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestParam String shopName,
            @RequestParam String password,
            HttpSession session
    ){
        shopService.verify(shopName, password);
        session.setAttribute(SessionKeys.SHOP_ID, shopName);
        return ResponseEntity.ok("登录成功");
    }

    /**
     *
     * @param userId
     * @param shopName
     * @return
     */

    @PostMapping("/")
    public ResponseEntity<ShopInfo> getShopInfo(
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId,
            @RequestParam String shopName
    ) {
        return ResponseEntity.ok(shopService.getShopInfo(shopName));
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody OrderDTO orderDTO) {
        return ResponseEntity.ok(orderService.checkout(orderDTO));
    }

    @PostMapping("/order/complete")
    public ResponseEntity<?> completeOrder(@RequestParam Long orderId) {
        orderService.completeOrder(orderId);
        return ResponseEntity.ok().body(
                "订单已完成"
        );
    }

}
