package cn.edu.usst.cs.campusAid.mapper.mapstruct;

import cn.edu.usst.cs.campusAid.dto.shop.ProductTransaction;
import cn.edu.usst.cs.campusAid.model.shop.GoodTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface ProductToGoodTransaction {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "goodId", ignore = true),
            @Mapping(target = "orderId", ignore = true)
    })
    GoodTransaction fromProductTransaction(ProductTransaction productTransaction);

}
