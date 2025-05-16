package cn.edu.usst.cs.campusAid.dto.shop;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ShopInfo {
    private String name;
    private String description;
    /**
     * 商户所能提供的所有商品信息
     */
    private List<ProductTransaction> products;
}
