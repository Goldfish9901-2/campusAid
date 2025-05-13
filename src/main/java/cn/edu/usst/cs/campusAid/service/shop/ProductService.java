package cn.edu.usst.cs.campusAid.service.shop;

import cn.edu.usst.cs.campusAid.dto.shop.ProductTransaction;


import java.util.List;

public interface ProductService {
    ProductTransaction addProduct(ProductTransaction productDTO);

    ProductTransaction getProductById(Long productId);
}
