package cn.edu.usst.cs.campusAid.dto.shop;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class ProductTransaction {
    String shopName;
    String name;
    String description;
    /**
     * <h2>价格</h2>
     * <p>可以为空，表示维持现价</p>
     */
    Float price;
    Long amount;
}
