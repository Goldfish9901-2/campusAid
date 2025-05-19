package cn.edu.usst.cs.campusAid.mapper.db.shop;

import cn.edu.usst.cs.campusAid.model.shop.Shop;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ShopMapper {
    /**
     * 验证用户名密码
     * @param name 用户名
     * @param password  密码
     * @return 用户名参数
     * @throws RuntimeException 用户名密码错误
     */
    default String verify(@Param("name") String name, @Param("password") String password) {
        String result = verifyInternal(name, password);
        if (result == null) {
            throw new RuntimeException("用户名或密码错误");
        }
        return result;
    }

    /**
     * 内部验证方法
     */
    String verifyInternal(@Param("name") String name, @Param("password") String password);

    Shop getShopByName(String shopName);

    void insert(Shop shop);

    void deleteByName(String name);
}
