package com.fdt.common.model.dto.refund;

import com.fdt.common.api.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true) // 让lombok生成equals和hashCode方法时考虑父类的属性
@Data
public class RefundQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = -7169219525469130736L;

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
}
