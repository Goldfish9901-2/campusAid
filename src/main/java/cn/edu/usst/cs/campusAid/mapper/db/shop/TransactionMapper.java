package cn.edu.usst.cs.campusAid.mapper.db.shop;

import cn.edu.usst.cs.campusAid.dto.shop.ProductTransaction;
import cn.edu.usst.cs.campusAid.model.shop.Good;
import cn.edu.usst.cs.campusAid.model.shop.GoodTransaction;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 交易映射器接口，用于处理与交易相关的数据库操作
 */
@Mapper
public interface TransactionMapper {
    /**
     * 插入一个新的商品交易记录
     *
     * @param build 要插入的商品交易记录
     */
    void insert(GoodTransaction build);

    /**
     * 获取当前空闲交易ID中的最小值
     *
     * @return 当前空闲交易ID中的最小值
     */
    Long minFreeID();

    /**
     * 根据商店名称选择商品
     *
     * @param shopName 商店名称
     * @return 商店的商品列表
     */
    List<ProductTransaction> selectGoodsByShop(String shopName);

    /**
     * <p>由商品的总进货量减去总购买量</p>
     * <p>获得指定商品的存量</p>
     *
     * @param matchingCandidate 指定的商品
     * @return 商品的存量
     * @see TransactionMapper#getSupplies(Good)
     * @see TransactionMapper#getPurchases(Good)
     */
    default Long getStock(Good matchingCandidate) {
        return getSupplies(matchingCandidate) - getPurchases(matchingCandidate);
    }

    /**
     * <p>获得指定商品的供应量</p>
     * <p>由所有供货记录相加得出</p>
     *
     * @param good 指定商品
     * @return 供应量
     */
    Long getSupplies(Good good);

    /**
     * 获得指定商品的购买量
     * <p>
     * 此方法用于获取特定商品的总购买数量
     *
     * @param good 指定商品
     * @return 购买量
     */
    Long getPurchases(Good good);

    /**
     * 获取指定用户的交易历史
     *
     * @param userId 用户ID
     * @return 用户的交易历史列表
     */
    List<ProductTransaction> getHistory(Long userId);
}
