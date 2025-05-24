package com.fdt.common.model.dto.pay;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AliPayRefund {

    private String TradeNo; // 支付宝交易号
    private String outTradeNo;  // 商户订单号
    private BigDecimal refundAmount; // 退款金额
    private String refundReason; // 退款原因

}
