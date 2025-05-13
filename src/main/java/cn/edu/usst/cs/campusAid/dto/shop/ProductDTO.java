package cn.edu.usst.cs.campusAid.dto.shop;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Builder
public class ProductDTO {
    private String name;
    private float price;
    private String description;
    private String imageUrl;
    // Getters and setters
}
   