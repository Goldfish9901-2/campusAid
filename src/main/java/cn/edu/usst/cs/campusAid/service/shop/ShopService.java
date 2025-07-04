package cn.edu.usst.cs.campusAid.service.shop;

import cn.edu.usst.cs.campusAid.dto.shop.Order;
import cn.edu.usst.cs.campusAid.dto.shop.OrderDTO;
import cn.edu.usst.cs.campusAid.dto.shop.ProductTransaction;
import cn.edu.usst.cs.campusAid.dto.shop.ShopInfo;

import java.util.List;

/**
 * 代替商店与购物者交互
 */
public interface ShopService {

    /**
     * @param shopName 商家名
     * @param shopNameStored   访问者，可决定是否隐藏某些信息(如卖完的商品)
     * @return 商家信息
     */
    ShopInfo getShopInfo(String shopName, String shopNameStored);

    /**
     * 提交订单
     * <p><strong>如果有商品存货不够，抛出异常</strong></p>
     *
     * <p><strong>如果余额不足，抛出异常</strong></p>
     *
     * @param orderDTO 订单信息
     * @return 后端结算信息
     */
    Order checkout(OrderDTO orderDTO);


    List<ProductTransaction> getHistory(Long userId);
}
