package cn.edu.usst.cs.campusAid.dto.shop;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
@Builder
public class OrderDTO {
    @NonNull
    private List<ProductTransaction> items;
    private Long userId;
    private String shopName;
}
   