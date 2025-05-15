package cn.edu.usst.cs.campusAid.model.charge;

import cn.edu.usst.cs.campusAid.util.BizContentField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Charge implements Serializable {
    @BizContentField(key = "subject")
    public final long interfaceInfoId = 294389472934L;
    @BizContentField(key = "product_code")
    public final String productCode = "FAST_INSTANT_TRADE_PAY";
    @BizContentField(key = "body")
    public final String paymentMethod = "支付宝";
    @BizContentField(key = "out_trade_info")
    private Long id;
    private Long userId;
    private LocalDateTime chargeTime;
    @BizContentField(key = "total_amount")
    private double amount;
}
