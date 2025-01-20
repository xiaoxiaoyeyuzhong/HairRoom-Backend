package com.fdt.common.model.dto.staff;

import com.fdt.common.api.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper =true)
@Data
public class StaffQueryRequest extends PageRequest implements Serializable {


    private static final long serialVersionUID = -6239216971435328083L;

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
     * 创建用户id
     */
    private Long userId;

    /**
     * 所属门店id
     */
    private Long storeId;

    /**
     * 是否为店长
     */
    private Integer isStoreManager;
}
