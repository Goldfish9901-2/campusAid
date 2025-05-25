package cn.edu.usst.cs.campusAid.service.errand;

import cn.edu.usst.cs.campusAid.dto.errand.*;
import cn.edu.usst.cs.campusAid.model.errand.Errand;
import cn.edu.usst.cs.campusAid.service.CampusAidException;

import java.util.List;
import java.util.Objects;

/**
 * 跑腿订单业务逻辑接口
 */
public interface ErrandService {

    /**
     * 用户发布跑腿订单
     */
    Long publishOrder(ErrandOrderRequest request, Long userId);

    /**
     * 获取所有跑腿订单列表（默认按时间排序）
     */
    List<ErrandOrderPreview> listOrders(Long userId);

    /**
     * 获取用户的历史跑腿订单
     *
     * @param userId 指定查询的用户 ID
     * @return 用户的历史订单预览列表
     */
    List<ErrandOrderPreview> listUserHistoricalOrders(Long userId);

    /**
     * 获取单个订单详细信息
     */
    Errand getOrderDetail(Long id, Long userId);

    /**
     * 跑腿员接单
     */
    String acceptOrder(Long id, Long runnerId);


    /**
     * 确认订单
     * <h2>跑腿员完成跑腿，用户手动确认（在待确认之后30分钟未确认的订单由平台自动确认）</h2>
     *
     * @param id 订单id
     * @param userId 有效（发布者或跑腿员）的用户ID
     * @return 确认成功信息
     */
    String confirmOrder(Long id, Long userId);

    /**
     * 用户取消订单（仅限接单前）
     */
    String cancelOrder(Long id, Long userId);

    default void verifyPublisher(Long userId,Long errandId){
        Errand errand = getOrderDetail(errandId, userId);
        if (!Objects.equals(errand.getSenderId(), userId))
            throw new CampusAidException(errandId + "不是发布者 无此权限");
    }
}
