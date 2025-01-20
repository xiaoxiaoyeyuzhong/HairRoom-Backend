package com.fdt.common.model.vo;

import java.io.Serializable;
import java.util.Date;

public class StaffVO implements Serializable {

    private static final long serialVersionUID = 7876703986053545536L;
    /**
     * 主键id
     */
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

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
