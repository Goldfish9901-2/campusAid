package cn.edu.usst.cs.campusAid.mapper.db.shop;

import cn.edu.usst.cs.campusAid.dto.shop.ProductTransaction;
import cn.edu.usst.cs.campusAid.model.shop.Good;

import java.util.List;


/**
 * GoodMapper接口定义了与商品相关的数据操作方法
 * 它提供了根据不同条件查询商品、插入新商品等功能
 */
public interface GoodMapper {

    /**
     * 根据商品ID查询商品信息
     *
     * @param targetGoodID 目标商品的ID
     * @return 返回查询到的商品对象，如果找不到则返回null
     */
    Good selectGoodByID(Long targetGoodID);

    /**
     * 根据商店名称和商品名称查询商品信息
     *
     * @param shopName 商店名称
     * @param name     商品名称
     * @return 返回查询到的商品对象，如果找不到则返回null
     */
    Good selectGoodByShopAndProductName(String shopName, String name);

    /**
     * 插入一个新的商品记录
     *
     * @param build 要插入的商品对象，包含商品的所有必要信息
     */
    void insert(Good build);

    /**
     * 获取当前系统中商品ID的最小可用值
     *
     * @return 返回最小可用的商品ID
     */
    Long minFreeId();

    /**
     * <p>查找该商店所有已经上架过的商品</p>
     * <strong> {@link ProductTransaction#getAmount()}字段是该</strong>
     *
     * @param shopName 商店名称
     * @return 返回该商店所有已上架商品的交易信息列表，包含商品名、价格、库存等信息
     */
    List<ProductTransaction> selectGoodsByShopName(String shopName);

    /**
     * 查找该商店当前可购买的商品
     *
     * @param shopName 商店名称
     * @return 返回该商店当前可购买商品的交易信息列表，包含商品名、价格、库存等信息
     */
     List<ProductTransaction> selectAvalibleGoodsByShopName(String shopName);

    void deleteByShopAndName(String shop, String name);

}
