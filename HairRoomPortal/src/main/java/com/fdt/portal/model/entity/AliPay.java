package com.fdt.portal.model.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AliPay {

    // 订单编号
    private Long billId;

    // 商品金额
    private BigDecimal billAmount;

    // 订单名称
    private String billName;
}
