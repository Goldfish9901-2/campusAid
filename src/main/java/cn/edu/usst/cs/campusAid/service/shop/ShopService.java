package cn.edu.usst.cs.campusAid.service.shop;

import cn.edu.usst.cs.campusAid.dto.shop.Order;
import cn.edu.usst.cs.campusAid.dto.shop.OrderDTO;
import cn.edu.usst.cs.campusAid.dto.shop.ProductTransaction;
import cn.edu.usst.cs.campusAid.dto.shop.ShopInfo;

import java.util.List;

public interface ShopService {

    /**
     *
     * @param shopName 商家名
     * @param userID 访问者，可决定是否隐藏某些信息(如卖完的商品)
     * @return 商家信息
     */
    ShopInfo getShopInfo(String shopName,String userID);
    /**
     * 提交订单
     * <strong>如果有商品存货不够，抛出异常</strong>
     * <strong>如果余额不足，抛出异常</strong>
     * @param orderDTO
     * @return
     */
    Order checkout(OrderDTO orderDTO);


}
