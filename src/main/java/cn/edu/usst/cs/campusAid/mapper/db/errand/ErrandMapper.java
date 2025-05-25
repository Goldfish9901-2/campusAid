package cn.edu.usst.cs.campusAid.mapper.db.errand;

import cn.edu.usst.cs.campusAid.dto.errand.ErrandOrderPreview;
import cn.edu.usst.cs.campusAid.dto.errand.ErrandOrderStatus;
import cn.edu.usst.cs.campusAid.mapper.mapstruct.ErrandViewsMapper;
import cn.edu.usst.cs.campusAid.model.errand.Errand;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ErrandMapper {

    /**
     * 获取最小的可用订单ID（用于复用已删除或取消的订单ID）
     */
    Long minFreeId();

    /**
     * 插入新的跑腿订单
     */
    void insert(Errand errand);

    /**
     * 根据订单ID查询跑腿订单详情
     */
    Errand selectById(Long id);

    /**
     * 查询用户未被接单的跑腿订单列表
     */
    List<Errand> selectUnacceptedOrders(Long userId);

    /**
     * 查询用户未被接单的跑腿订单预览信息（包含转换逻辑）
     */
    default List<ErrandOrderPreview> selectUnacceptedOrderPreviews(Long userId) {
        var errands = selectUnacceptedOrders(userId);
        return errands.stream()
                .map(errand -> ErrandViewsMapper.getInstance().getPreview(errand))
                .toList();
    }

    /**
     * 查询用户已接受的跑腿订单列表
     */
    List<Errand> selectErrandsAcceptedByUser(Long userId);

    /**
     * 查询用户发布的所有跑腿订单列表
     */
    List<Errand> selectErrandsPublishedByUser(Long userId);

    /**
     * 查询用户的历史跑腿订单（包括已完成、已取消等状态的订单）
     *
     * @param userId 用户 ID
     * @return 用户的历史跑腿订单列表
     */
    List<Errand> selectHistoricalOrders(Long userId);

    /**
     * 更新跑腿订单的状态
     */
    void updateErrand(Long id, ErrandOrderStatus status);

    /**
     * 更新订单的接受者ID（即接单操作）
     */
    void updateAcceptorId(Long id, Long runnerId);
}
