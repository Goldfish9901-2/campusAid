package cn.edu.usst.cs.campusAid.service.shop;

import cn.edu.usst.cs.campusAid.dto.shop.Order;
import cn.edu.usst.cs.campusAid.dto.shop.OrderDTO;
import cn.edu.usst.cs.campusAid.dto.shop.ProductTransaction;

import java.util.*;

public interface OrderService {
    Order checkout(OrderDTO orderDTO);

    void completeOrder(Long orderId);
}
