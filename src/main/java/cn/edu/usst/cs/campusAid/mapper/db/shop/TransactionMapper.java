package cn.edu.usst.cs.campusAid.mapper.db.shop;

import cn.edu.usst.cs.campusAid.dto.shop.ProductTransaction;
import cn.edu.usst.cs.campusAid.model.shop.GoodTransaction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TransactionMapper {
    void insert(GoodTransaction build);

    Long minFreeID();

//    List<ProductTransaction> selectGoodsByShop(String shopName);


    /**
     * 获取指定商品的进货量（order_id IS NULL）
     */
    Long getAllSupplyHistory(Long id);

    /**
     * 获取指定商品的销售量（order_id IS NOT NULL）
     */
    Long getTotalSold(Long id);

    /**
     * 获取指定商品的库存 = 进货量 - 销售量
     */
    Long getAvailableStock(Long id);
    /**
     * 执行一次进货操作（即插入一条 order_id 为 null 的交易记录）
     * @param goodId 商品ID
     * @param amount 进货数量
     */
    void restock(@Param("goodId") Long goodId, @Param("amount") Float amount);

    /**
     *    获取用户的历史订单
     * @param userId
     * @return
     */
    List<ProductTransaction> getHistory(Long userId);
}
