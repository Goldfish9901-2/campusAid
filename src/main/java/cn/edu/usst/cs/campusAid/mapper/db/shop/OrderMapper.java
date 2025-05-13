package cn.edu.usst.cs.campusAid.mapper.db.shop;

import cn.edu.usst.cs.campusAid.model.shop.ShopOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {
    Long minFreeID();

    void insert(ShopOrder shopOrder);
}
