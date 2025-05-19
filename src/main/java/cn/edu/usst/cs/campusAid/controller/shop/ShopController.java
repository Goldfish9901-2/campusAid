package cn.edu.usst.cs.campusAid.controller.shop;

import cn.edu.usst.cs.campusAid.controller.SessionKeys;
import cn.edu.usst.cs.campusAid.dto.shop.OrderDTO;
import cn.edu.usst.cs.campusAid.dto.shop.ProductTransaction;
import cn.edu.usst.cs.campusAid.dto.shop.ShopInfo;
import cn.edu.usst.cs.campusAid.service.CampusAidException;
import cn.edu.usst.cs.campusAid.service.auth.UserService;
import cn.edu.usst.cs.campusAid.service.shop.ShopService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shop")
public class ShopController {

    private final UserService userService;
    private final ShopService shopService;

    // 构造器注入
    public ShopController(ShopService shopService, UserService userService) {
        this.shopService = shopService;
        this.userService = userService;
    }

    /**
     * 获取商户信息
     */
    @GetMapping("/info/{shopName}")
    public ResponseEntity<ShopInfo> getShopInfo(
            @SessionAttribute(value = SessionKeys.SHOP_NAME, required = false) String shopNameStored,
            @PathVariable String shopName
    ) {
        return ResponseEntity.ok(shopService.getShopInfo(shopName, shopNameStored));
    }

    /**
     * 提交订单（余额扣减）
     */
    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(
            @SessionAttribute(SessionKeys.LOGIN_ID) String userId,
            @RequestBody OrderDTO orderDTO
    ) {
        if (!userId.equals(String.valueOf(orderDTO.getUserId()))) {
            throw new CampusAidException("用户ID不匹配，请本人登陆下单");
        }
        return ResponseEntity.ok(shopService.checkout(orderDTO));
    }

    /**
     * 获取用户历史订单
     */
    @GetMapping("/history")
    public List<ProductTransaction> getHistory(
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId,
            @RequestParam(required = false) Long targetUserId
    ) {
        Long userIdToSearch = userService.getTargetUserId(userId, targetUserId);
        return shopService.getHistory(userIdToSearch);
    }
}
