package cn.edu.usst.cs.campusAid.model.charge;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 接口次数订单表
 * @TableName order
 */
@TableName(value ="`order`")
@Data
public class Order implements Serializable {
    /**
     * 订单Id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private Long id;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 接口Id
     */
    private Long interfaceInfoId;

    /**
     * 支付金额
     */
    private Double money;

    /**
     * 支付方式
     */
    private String paymentMethod;

    /**
     * 0 - 未支付 1 - 已支付
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
