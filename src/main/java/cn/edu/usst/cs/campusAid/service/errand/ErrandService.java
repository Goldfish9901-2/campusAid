package cn.edu.usst.cs.campusAid.service.errand;

import cn.edu.usst.cs.campusAid.dto.errand.*;

import java.util.List;

/**
 * 跑腿订单业务逻辑接口
 */
public interface ErrandService {

    /**
     * 用户发布跑腿订单
     */
    String publishOrder(ErrandOrderRequest request, Long userId);

    /**
     * 获取所有跑腿订单列表（默认按时间排序）
     */
    List<ErrandOrderPreview> listOrders();

    /**
     * 获取单个订单详细信息
     */
    ErrandOrderView getOrderDetail(Long id, Long userId);

    /**
     * 跑腿员接单
     */
    String acceptOrder(Long id, Long runnerId);


    /**
     * 用户手动确认完成订单
     */
    String confirmOrder(Long id, Long userId);

    /**
     * 用户取消订单（仅限接单前）
     */
    String cancelOrder(Long id, Long userId);
}
