package com.fdt.common.model.dto.refund;

import lombok.Data;


import java.io.Serializable;


@Data
public class RefundUpdateRequest implements Serializable {

    private static final long serialVersionUID = 8497921349207873313L;

    /**
     * id
     */
    private Long id;

    /**
     * 支付宝交易号
     */
    private String tradeNo;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 审核状态
     */
    private Integer auditSituation;


}
