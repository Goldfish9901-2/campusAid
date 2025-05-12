package cn.edu.usst.cs.campusAid.mapper.db.shop;

import cn.edu.usst.cs.campusAid.dto.shop.ProductTransaction;
import cn.edu.usst.cs.campusAid.model.shop.Good;
import cn.edu.usst.cs.campusAid.model.shop.GoodTransaction;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TransactionMapper {
    void insert(GoodTransaction build);

    Long minFreeID();

    List<ProductTransaction> selectGoodsByShop(String shopName);

    /**
     * 获得指定商品的存量
     * {@link }
     * @param matchingCandidate
     * @return
     */
    Long getStock(Good matchingCandidate);
}
