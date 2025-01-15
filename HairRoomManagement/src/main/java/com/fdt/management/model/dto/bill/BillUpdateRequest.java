package com.fdt.management.model.dto.bill;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class BillUpdateRequest implements Serializable {

    private static final long serialVersionUID = -4134139143495987372L;
    // 账单id
    private Long id;

    // 账单名称
    private String billName;

    // 账单描述
    private String billDesc;

    // 账单类型
    private String billType;

    // 账单金额
    private Double billAmount;

    // 账单关联的客户id
    private Long customerId;

    // 账单关联的员工id
    private Long staffId;

}
