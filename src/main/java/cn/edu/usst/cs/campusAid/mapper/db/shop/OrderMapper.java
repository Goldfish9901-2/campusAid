package cn.edu.usst.cs.campusAid.mapper.db.shop;

import cn.edu.usst.cs.campusAid.dto.shop.ProductTransaction;
import cn.edu.usst.cs.campusAid.model.shop.ShopOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {
    Long minFreeID();

    void insert(ShopOrder shopOrder);

    /**
     * 查询用户的所有订单列表
     */
    List<ProductTransaction> selectAllOrdersFromUser(Long userId);
}
