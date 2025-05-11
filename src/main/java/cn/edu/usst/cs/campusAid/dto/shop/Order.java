package cn.edu.usst.cs.campusAid.dto.shop;

import com.google.common.util.concurrent.AtomicDouble;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Order {
    Long buyerID;
    String shopID;
    List<ProductTransaction> products;

    public double getTotalPrice() {
        if(products == null)
            return .0;
        return products.stream().mapToDouble(ProductTransaction::getPrice).sum();
    }
}
