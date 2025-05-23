package cn.edu.usst.cs.campusAid.dto.shop;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductTransaction {
    private String shopName;
    private String name;
    private String description;
    private Float price;
    private Long amount; // ⚠️注意字段名和数据库查询一致
}
