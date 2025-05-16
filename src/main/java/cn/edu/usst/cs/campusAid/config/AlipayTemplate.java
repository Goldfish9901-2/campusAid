package cn.edu.usst.cs.campusAid.config;

import cn.edu.usst.cs.campusAid.mapper.mapstruct.AlipayConfigMapper;
import cn.edu.usst.cs.campusAid.model.charge.Charge;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Configuration
@PropertySource("classpath:alipay.properties")
@Data
@Slf4j
public class AlipayTemplate {
    private final static Map<Long, Charge> orderToUser = new HashMap<>();
    private final AlipayConfigMapper alipayConfigMapper;
    /**
     * 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
     */

    @Value("${alipay.sandbox.appId}")
    public String appId;
    /**
     * 应用私钥，就是工具生成的应用私钥
     */
    @Value("${alipay.sandbox.merchantPrivateKey}")
    public String merchantPrivateKey;
    /**
     * 支付宝公钥,对应APPID下的支付宝公钥。
     */
    @Value("${alipay.sandbox.alipayPublicKey}")
    public String alipayPublicKey;
    /**
     * 支付宝会悄悄的给我们发送一个请求，告诉我们支付成功的信息
     */
    @Value("${alipay.sandbox.notifyUrl}")
    public String notifyUrl;
    /**
     * 同步通知，支付成功，一般跳转到成功页
     */
    @Value("${alipay.sandbox.returnUrl}")
    public String returnUrl;
    /**
     * 支付宝网关；<a href=https://openapi-sandbox.dl.alipaydev.com/gateway.do>https://openapi-sandbox.dl.alipaydev.com/gateway.do</a>
     */
    @Value("${alipay.sandbox.gatewayUrl}")
    public String gatewayUrl;
    /**
     * 签名方式
     */
    @Value("${alipay.sandbox.signType}")
    private String signType;

    /**
     * 字符编码格式
     */
    @Value("${alipay.sandbox.charset}")
    private String charset;

    /**
     * 订单超时时间
     */
    @Value("${alipay.sandbox.timeout}")
    private String timeout;

    public AlipayTemplate(AlipayConfigMapper alipayConfigMapper) {
        this.alipayConfigMapper = alipayConfigMapper;
    }

    public static Charge getUserId(Long orderId) {
        return orderToUser.get(orderId);
    }

    @PostConstruct
    public void initAlipaySDK() {
        for (Field field : getClass().getFields()) {
            Value value = field.getAnnotation(Value.class);
            if (value == null) {
                log.debug("skipping field {}", field.getName());
                continue;
            }
            try {
                field.setAccessible(true);
                log.debug("loaded {} into field {} : {}",
                        value.value(), field.getName(),
                        field.get(this)
                );
            } catch (IllegalAccessException e) {
                log.error("failed to load {} into field {}",
                        value.value(), field.getName(),
                        e
                );
            }
        }
        Factory.setOptions(alipayConfigMapper.toConfig(this));
    }

    public AlipayTradePagePayResponse pay(Charge order) throws Exception {
        registerOrder(order.getId(), order);
        // 使用 EasySDK 发起支付请求
        var response = Factory.Payment.Page().pay(
                "校园充值",  // 商品标题
                String.valueOf(order.getId()),  // 商户订单号
                String.format("%.2f", order.getAmount()),  // 金额
                returnUrl  // 同步回调地址
        );
        log.info("支付宝返回页面内容: {}", response);
        return response;
    }

    public void registerOrder(Long orderId, Charge charge) {
        orderToUser.put(orderId, charge);
    }


}
