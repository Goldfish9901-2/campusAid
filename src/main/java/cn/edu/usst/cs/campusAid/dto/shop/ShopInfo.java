package cn.edu.usst.cs.campusAid.dto.shop;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ShopInfo {
    private String name;
    private String description;
    private List<ProductTransaction> products;
}
