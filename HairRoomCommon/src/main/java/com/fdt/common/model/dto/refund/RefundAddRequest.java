package com.fdt.common.model.dto.refund;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RefundAddRequest {

    /**
     * 支付宝交易号
     */
    private String tradeNo;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 退款原因
     */
    private String refundReason;
}
