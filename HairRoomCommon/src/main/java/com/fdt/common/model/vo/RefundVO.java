package com.fdt.common.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class RefundVO {

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
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 退款原因
     */
    private String refundReason;

    /**
     * 审核情况,0-未审核，1-审核通过，2-审核不通过
     */
    private Integer auditSituation;

    /**
     * 退款情况，0-未退款，1-已退款，2-退款失败
     */
    private Integer refundSituation;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
