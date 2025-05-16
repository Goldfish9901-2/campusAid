package cn.edu.usst.cs.campusAid.controller.charge;

import cn.edu.usst.cs.campusAid.config.AlipayTemplate;
import cn.edu.usst.cs.campusAid.controller.SessionKeys;
import cn.edu.usst.cs.campusAid.model.charge.Charge;
import cn.edu.usst.cs.campusAid.service.CampusAidException;
import cn.edu.usst.cs.campusAid.service.auth.UserService;
import cn.edu.usst.cs.campusAid.service.charge.ChargeService;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

/**
 * 支付宝接口
 */
@Slf4j
@RestController
@RequestMapping("/api/alipay")
public class AlipayController {

    private final SpringTemplateEngine templateEngine;
    private final ChargeService chargeService;
    private final UserService userService;
    @Resource
    AlipayTemplate alipayTemplate;

    public AlipayController(SpringTemplateEngine templateEngine, ChargeService chargeService, UserService userService) {
        this.templateEngine = templateEngine;
        this.chargeService = chargeService;
        this.userService = userService;
    }

    private static long getGeneratedId(LocalDateTime chargeTime) {
        long generatedId = chargeTime.toEpochSecond(ZoneOffset.ofHours(8));
        generatedId = (long) (generatedId * 1e9);
        generatedId += chargeTime.getNano();
        return generatedId;
    }

    @GetMapping(
            value = "",
            produces = "text/html"
    )
    public String hello(
            @RequestParam(name = "timestamp", required = false) String timestamp,
            @RequestParam(name = "total_amount", required = false) String total_amount,
            @RequestParam Map<String, String> params
    ) {
        params.forEach((key, value) ->
                log.info(" -*RET*- {}:{}", key, value));
        if (timestamp == null && total_amount == null) return jumpLink();
        Context context = new Context();
        context.setVariable("timestamp", timestamp);
        context.setVariable("total_amount", total_amount);
        String httpContent = templateEngine.process("alipay", context);
        log.info(httpContent);
        return httpContent;

    }

    @GetMapping(value = "/pay", produces = "text/html")
    @ResponseBody
    public String pay(
            @RequestParam double price,
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId,
            @SessionAttribute(value = SessionKeys.PENDING_CHARGE_REQUEST, required = false)
            Charge existingCharge,
            HttpSession session
    ) throws Exception {
        if (existingCharge != null)
            throw new CampusAidException("请先完成之前的充值");
        LocalDateTime chargeTime = LocalDateTime.now();
        Charge.ChargeBuilder builder = Charge.builder();
        long generatedId = getGeneratedId(chargeTime);
        builder.id(generatedId);
        builder.userId(userId);
        builder.amount(price);
        builder.chargeTime(chargeTime);
        Charge charge = builder.build();
        session.setAttribute(SessionKeys.PENDING_CHARGE_REQUEST, charge);
        AlipayTradePagePayResponse response = alipayTemplate.pay(charge);
        String body = response.getBody();
        if (body == null)
            throw new CampusAidException("支付失败");
//        body = body.replace("null://", "");
        log.info(" 支付宝页面： {}", body);
        return body;
    }

    @GetMapping("/cancel")
    public String cancel(
            HttpSession session
    ) {
        session.removeAttribute(SessionKeys.PENDING_CHARGE_REQUEST);
        return jumpLink();
    }

    @PostMapping("/notify")
    public String payNotify(
            @RequestParam(name = "trade_status", required = false) String tradeStatus,
            @RequestParam(name = "out_trade_no", required = false) String outTradeNo,
            @RequestParam(name = "gmt_payment", required = false) String gmtPayment,
            @RequestParam(name = "trade_no", required = false) String alipayTradeNo,
            @RequestParam(name = "subject", required = false) String subject,
            @RequestParam(name = "total_amount", required = false) Double totalAmount,
            @RequestParam(name = "buyer_id", required = false) String buyerId,
            @RequestParam(name = "buyer_pay_amount", required = false) String buyerPayAmount,
            @RequestParam Map<String, String> params,
            HttpSession session
    ) throws Exception {
        try {
            log.warn("=========异步回调========");

            if (!"TRADE_SUCCESS".equals(tradeStatus))
                throw new CampusAidException("支付失败");

            log.info("=========支付宝异步回调========");
            // 支付宝验签
            if (!Factory.Payment.Common().verifyNotify(params)) {
                throw new CampusAidException("验签失败");
            } else {
                // 验签通过
                params.forEach((key, value) ->
                        log.info(" * NOTIFY * {}:{}", key, value));
            }
            log.warn("*** \n verify success \n  ***");
            Charge charge = (Charge) session.getAttribute(SessionKeys.PENDING_CHARGE_REQUEST);


            // 更新订单状态等业务逻辑
            if (charge != null) {
                log.info("更新订单状态: {}", charge);
                chargeService.recordCharge(charge);
            } else {
                Long tradeId = Long.parseLong(outTradeNo);
                charge = AlipayTemplate.getUserId(tradeId);
                log.warn("rebuilding charge: {}", charge);
                chargeService.recordCharge(charge);
            }
        } finally {
            session.removeAttribute(SessionKeys.PENDING_CHARGE_REQUEST);
            log.warn("=========异步回调========");
        }

        return "success";

    }

    @GetMapping("/history")
    public List<Charge> getHistory(
            @SessionAttribute(SessionKeys.LOGIN_ID) Long userId,
            @RequestParam(required = false) Long targetUserId
    ) {
        Long realId = userService.getTargetUserId(userId, targetUserId);
        if (realId == null)
            throw new CampusAidException("未找到用户");

        return chargeService.getHistory(realId);

    }

    private String jumpLink() {
        return "<script>window.location.href='" + "/" + "'</script>";
    }

}

