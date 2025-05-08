package cn.edu.usst.cs.campusAid.dto.shop;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderDTO {
    private List<CartItemDTO> items;
    private Long userId;
    // Getters and setters
}
   