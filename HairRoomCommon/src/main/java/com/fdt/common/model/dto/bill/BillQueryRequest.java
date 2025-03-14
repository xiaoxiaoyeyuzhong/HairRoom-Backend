package com.fdt.common.model.dto.bill;


import com.fdt.common.api.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true) // 让lombok生成equals和hashCode方法时考虑父类的属性
@Data
public class BillQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 8440376369386158986L;
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


}
