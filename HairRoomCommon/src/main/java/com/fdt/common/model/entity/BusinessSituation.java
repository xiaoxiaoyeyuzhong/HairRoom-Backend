package com.fdt.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

/**
 * 营业情况
 * @TableName business_situation
 */
@TableName(value ="business_situation")
@Data
public class BusinessSituation implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 营业额
     */
    private BigDecimal businessAmount;

    /**
     * 成本
     */
    private BigDecimal businessCost;

    /**
     * 利润
     */
    private BigDecimal businessProfit;

    /**
     * 营业日期
     */
    private LocalDate businessDay;

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