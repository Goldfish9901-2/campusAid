package cn.edu.usst.cs.campusAid.dto.shop;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class ProductTransaction {
    String name;
    String description;
    float price;
    float amount;
    MultipartFile image;
}
