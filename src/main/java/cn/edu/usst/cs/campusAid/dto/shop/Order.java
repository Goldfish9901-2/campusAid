package cn.edu.usst.cs.campusAid.dto.shop;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Order {
    Long buyerID;
    String shopID;
    List<ProductTransaction> products;
}
