package com.fdt.common.model.dto.staff;


import lombok.Data;

import java.io.Serializable;

@Data
public class StaffUpdateRequest implements Serializable {

    private static final long serialVersionUID = -8640172881766154578L;

    private Long id;

    /**
     * 员工姓名
     */
    private String staffName;

    /**
     *  员工性别
     */
    private String staffSex;

    /**
     * 员工年龄
     */
    private Long staffAge;

    /**
     * 员工手机号
     */
    private String staffPhone;

    /**
     * 员工邮箱
     */
    private String staffEmail;

    /**
     * 所属门店id
     */
    private Long storeId;

    /**
     * 是否为店长
     */
    private Integer isStoreManager;
}
