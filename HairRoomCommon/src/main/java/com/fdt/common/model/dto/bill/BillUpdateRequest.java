package com.fdt.common.model.dto.bill;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class BillUpdateRequest implements Serializable {


    private static final long serialVersionUID = -1906291302565184178L;
    /**
     * id
     */
    private Long id;

    // 账单名称
    private String billName;

    // 账单描述
    private String billDesc;

    // 账单类型
    private String billType;

    // 账单金额,BigDecimal类型，不要用double，并使用copyProperties复制属性，因为会导致复制失败
    private BigDecimal billAmount;

    // 账单关联的客户id
    private Long customerId;

    // 账单关联的员工id
    private Long staffId;

}
