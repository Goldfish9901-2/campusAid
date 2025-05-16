package cn.edu.usst.cs.campusAid.model.shop;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Builder
@EqualsAndHashCode(of = {"name"})
@ToString
public class Shop {
    private String name;
    private String description;
    private String password;
}
