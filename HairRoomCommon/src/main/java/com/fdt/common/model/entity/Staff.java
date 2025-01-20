package com.fdt.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 员工
 * @TableName staff
 */
@TableName(value ="staff")
@Data
public class Staff implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 5887522748604532116L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
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

    /**
     * 是否删除
     */
    private Integer isDelete;


}