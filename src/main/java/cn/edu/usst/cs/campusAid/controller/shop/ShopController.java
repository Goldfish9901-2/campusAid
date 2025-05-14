package cn.edu.usst.cs.campusAid.controller.shop;

import cn.edu.usst.cs.campusAid.config.AdminConfig;
import cn.edu.usst.cs.campusAid.controller.SessionKeys;
import cn.edu.usst.cs.campusAid.dto.shop.OrderDTO;
import cn.edu.usst.cs.campusAid.dto.shop.ProductTransaction;
import cn.edu.usst.cs.campusAid.dto.shop.ShopInfo;
import cn.edu.usst.cs.campusAid.service.CampusAidException;
import cn.edu.usst.cs.campusAid.service.shop.ShopService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物者用商家信息
 */
@RestController
@RequestMapping("/api/shop")
public class ShopController {
    private final AdminConfig adminConfig;
    private ShopService shopService;

    public ShopController(ShopService shopService, AdminConfig adminConfig) {
        this.shopService = shopService;
        this.adminConfig = adminConfig;
    }

    /**
     * 商户门户，陈列所有可购买的商品
     *
     * @param shopNameStored 10位是学生，5位是教师（工号），其他查商户数据库
     * @param shopName       商户名 如果商户数据库有记录，则视为商户查看自己信息
     * @return 商户信息
     */

    @PostMapping("/")
    public ResponseEntity<ShopInfo> getShopInfo(
            @SessionAttribute(value = SessionKeys.SHOP_NAME, required = false) String shopNameStored,
            @RequestParam String shopName
    ) {
        return ResponseEntity.ok(shopService.getShopInfo(shopName, shopNameStored));
    }

    /**
     * 普通用户提交订单
     * <p>如果余额不足，会返回失败回应</p>
     * <p>如果余额足够，会直接扣除</p>
     * <h1>前端发送请求前 务必让用户确认</h1>
     *
     * @param userId   用户ID
     * @param orderDTO 所购买的商品
     * @return 结算结果
     */
    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(
            @SessionAttribute(SessionKeys.LOGIN_ID) String userId,
            @RequestBody OrderDTO orderDTO
    ) {
        if (!userId.equals(String.valueOf(orderDTO.getUserId())))
            throw new CampusAidException("用户ID不匹配，请本人登陆下单");
        return ResponseEntity.ok(shopService.checkout(orderDTO));
    }

    /**
     * 难不成还想偷窥别人买了什么（
     */
    @GetMapping("/history")
    List<ProductTransaction> getHistory(
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId,
            @RequestParam(required = false, defaultValue = "") Long targetUserId
    ) {
        Long userIdToSearch = userId;
        if (targetUserId != null) {
            adminConfig.verifyIsAdmin(userId);
            userIdToSearch = targetUserId;
        }
        return shopService.getHistory(userIdToSearch);
    }
}
