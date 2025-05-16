package cn.edu.usst.cs.campusAid.model.shop;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Builder
@EqualsAndHashCode(of = {"id"})
@ToString
public class Good {
    /**
     *
     */
    private Long id;

    /**
     *
     */
    private String name;

    /**
     *
     */
    private String description;

    /**
     *
     */
    private String shop;

    /**
     *
     */
    private Double price;


}