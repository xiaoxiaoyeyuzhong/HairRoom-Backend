package com.fdt.common.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class BillVO implements Serializable {

    private static final long serialVersionUID = -4618131047228449519L;
    /**
     * id
     */
    private Long id;

    /**
     * 支付宝交易凭证号
     */
    private String tradeNo;

    /**
     * 账单名称
     */
    private String billName;

    /**
     * 账单金额
     * 若数据库存储的金额小数位数少于输入值，会进行四舍五入
     */
    private BigDecimal billAmount;

    /**
     * 账单类型
     */
    private String billType;

    /**
     * 账单描述
     */
    private String billDesc;

    /**
     *  客户id
     */
    private Long customerId;

    /**
     * 员工id
     */
    private Long staffId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
