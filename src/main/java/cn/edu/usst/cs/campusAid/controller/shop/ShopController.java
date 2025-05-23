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

/**
 * 购物者用商家信息
 */
@RestController
@RequestMapping("/api/shop")
public class ShopController {
    private final UserService userService;
    private ShopService shopService;

    public ShopController(ShopService shopService, UserService userService) {
        this.shopService = shopService;
        this.userService = userService;
    }

    /**
     * 商户门户，陈列所有可购买的商品
     *
     * @param shopNameStored 10位是学生，5位是教师（工号），其他查商户数据库
     * @param shopName       商户名 如果商户数据库有记录，则视为商户查看自己信息
     * @return 商户信息
     */

    @GetMapping("/info/{shopName}")
    public ResponseEntity<ShopInfo> getShopInfo(
            @SessionAttribute(value = SessionKeys.SHOP_NAME, required = false) String shopNameStored,
            @PathVariable String shopName
    ) {
        return ResponseEntity.ok(shopService.getShopInfo(shopName, shopNameStored));
    }
    @GetMapping("/products/{shopName}")
    public List<ProductTransaction> getProducts(@PathVariable String shopName) {
        return shopService.getShopInfo(shopName, null).getProducts();
    }


    /**
     * 普通用户提交订单
     * <p>如果余额不足，会返回失败回应</p>
     * <p>如果余额足够，会直接扣除</p>
     * <h1>前端发送请求前 务必让用户确认</h1>
     *
     * @param userId   用户ID
     * @param orderDTO 所购买的商品 {@link OrderDTO#getItems()} (也就是{@link ProductTransaction})
     *                 <p></p>只需填写商品名{@link ProductTransaction#getName()}
     *                 <p></p>和数量{@link ProductTransaction#getAmount()}
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
            @RequestParam(required = false) Long targetUserId
    ) {
        Long userIdToSearch = userService.getTargetUserId(userId, targetUserId);
        return shopService.getHistory(userIdToSearch);
    }
}
