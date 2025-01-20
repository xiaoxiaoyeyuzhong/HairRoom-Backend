package com.fdt.common.model.dto.bill;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class BillAddRequest implements Serializable {


    private static final long serialVersionUID = -2057535838179458589L;
    // 账单名称
    private String billName;

    // 账单描述
    private String billDesc;

    // 账单类型
    private String billType;

    // 账单金额
    private BigDecimal billAmount;

    // 账单关联的客户id
    private Long customerId;

    //账单关联的员工id
    private Long staffId;

}
