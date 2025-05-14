package cn.edu.usst.cs.campusAid.dto.shop;

import com.google.common.util.concurrent.AtomicDouble;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Order {
    Long id;
    Long buyerID;
    String shopName;
    List<ProductTransaction> products;
    double price;
}
