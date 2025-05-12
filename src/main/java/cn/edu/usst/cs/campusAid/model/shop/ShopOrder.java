package cn.edu.usst.cs.campusAid.model.shop;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Builder
@EqualsAndHashCode(of = {"id"})
@ToString
public class ShopOrder {
    Long id;
    Long shopperId;
    String shop;
}
