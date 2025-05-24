package com.fdt.common.model.dto.pay;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AliPay {

    // 订单编号
    private String outTradeNo;

    // 商品金额
    private BigDecimal billAmount;

    // 订单名称
    private String billName;
}
