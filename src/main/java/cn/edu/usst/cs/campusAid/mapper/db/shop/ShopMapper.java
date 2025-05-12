package cn.edu.usst.cs.campusAid.mapper.db.shop;

import cn.edu.usst.cs.campusAid.model.shop.Shop;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShopMapper {
    /**
     * 验证用户名密码
     * @param name 用户名
     * @param password  密码
     * @return 用户名参数
     * @throws cn.edu.usst.cs.campusAid.service.CampusAidException 用户名密码错误
     */
    String verify(String name, String password);

    Shop getShopByName(String shopName);
}
