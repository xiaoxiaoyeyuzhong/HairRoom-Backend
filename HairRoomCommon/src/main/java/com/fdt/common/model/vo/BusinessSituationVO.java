package com.fdt.common.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
public class BusinessSituationVO {
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
}
