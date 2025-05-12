package cn.edu.usst.cs.campusAid.mapper.db.shop;

import cn.edu.usst.cs.campusAid.model.shop.ShopOrder;

public interface OrderMapper {
    Long minFreeID();

    void insert(ShopOrder shopOrder);
}
