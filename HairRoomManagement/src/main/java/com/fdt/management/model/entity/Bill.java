package com.fdt.management.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName bill
 */
@TableName(value ="bill")
@Data
public class Bill implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

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
    private Long CustomerId;

    /**
     * 员工id
     */
    private Long StaffId;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}