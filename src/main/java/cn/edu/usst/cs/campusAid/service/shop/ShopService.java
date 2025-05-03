package cn.edu.usst.cs.campusAid.service.shop;

import cn.edu.usst.cs.campusAid.dto.shop.ProductTransaction;
import cn.edu.usst.cs.campusAid.dto.shop.ShopInfo;

import java.util.List;

public interface ShopService {
    // 验证商家身份
    void verify(String name, String password);

    ShopInfo getShopInfo(String shopName);

    // 商家添加商品到店铺
    void addProductToShop(String shopName, ProductTransaction product);

    // 获取商店中的商品列表
    List<ProductTransaction> getProductsByShop(String shopName);

}
