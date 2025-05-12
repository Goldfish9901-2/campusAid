package cn.edu.usst.cs.campusAid.model.shop;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * <strong>数据库实体类</strong>
 * <p>商品的一次转移</p>
 * <p>可以是进货，也可以是下单</p>
 * <p>参见{@link GoodTransaction#orderId}</p>
 */
@Data
@Builder
@EqualsAndHashCode(of = {"id"})
@ToString
@Schema(description = "交易记录实体类")
public class GoodTransaction implements Serializable {

    private Long id;
    /**
     *
     */
    private Long goodId;

    @Positive
    private Float amount;
    /**
     * 若指向商品表有效记录，则表示为下单操作<br/>
     * 若为空则为为补货操作
     */
    private Long orderId;
}

