package com.fdt.management.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.fdt.common.api.ErrorCode;
import com.fdt.common.constant.UserConstant;
import com.fdt.common.model.dto.pay.AliPayRefund;
import com.fdt.common.model.entity.Bill;
import com.fdt.management.annotation.AuthCheck;
import com.fdt.management.config.AliPayConfig;
import com.fdt.management.exception.BusinessException;
import com.fdt.management.service.BillService;
import com.fdt.management.service.CustomerService;
import com.fdt.management.service.StaffService;
import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/alipay")
@Transactional(rollbackFor = Exception.class)
@Log4j2
public class AliPayController {

    @Resource
    AliPayConfig aliPayConfig;

    @Resource
    CustomerService customerService;

    @Resource
    StaffService staffService;

    @Resource
    BillService billService;

    private static final String GATEWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private static final String FORMAT = "JSON";
    private static final String CHARSET = "UTF-8";
    private static final String SIGN_TYPE = "RSA2";

    @PostMapping("/refund")
    @AuthCheck(mustRole = UserConstant.MANAGER_ROLE)
    public String refund(@RequestBody AliPayRefund aliPayRefund) {
        log.info("===支付宝退款接口调用开始===");

        try {
            AlipayClient alipayClient = new DefaultAlipayClient(
                    GATEWAY_URL,
                    aliPayConfig.getAppId(),
                    aliPayConfig.getAppPrivateKey(),
                    FORMAT,
                    CHARSET,
                    aliPayConfig.getAlipayPublicKey(),
                    SIGN_TYPE
            );

            // 构造退款请求
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
            request.setBizContent("{" +
                    "\"trade_no\":\"" + aliPayRefund.getTradeNo() + "\"," +
                    "\"out_trade_no\":\"" + aliPayRefund.getOutTradeNo() + "\"," +
                    "\"refund_amount\":\"" + aliPayRefund.getRefundAmount() + "\"," +
                    "\"refund_reason\":\"" + aliPayRefund.getRefundReason() + "\"" +
                    "}");

            // 执行退款
            AlipayTradeRefundResponse response = alipayClient.execute(request);

            if (response.isSuccess()) {
                log.info("===支付宝退款接口调用成功===");
                // 退款成功，更新数据库
//                Bill bill = billService.getByOutTradeNo(aliPayRefund.getOutTradeNo());
//                if (bill != null) {
//                    bill.setRefundAmount(aliPayRefund.getRefundAmount());
//                    bill.setStatus("REFUNDED");
//                    billService.updateById(bill);
//                    log.info("订单退款成功，订单号：{}", aliPayRefund.getOutTradeNo());
//                }
                return "退款成功";
            } else {
                log.error("支付宝退款失败，原因：{}", response.getSubMsg());
                return "退款失败：" + response.getSubMsg();
            }
        } catch (AlipayApiException e) {
            log.error("支付宝退款接口异常", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "退款处理失败");
        }
    }
}
