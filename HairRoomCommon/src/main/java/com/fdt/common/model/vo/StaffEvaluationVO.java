package com.fdt.common.model.vo;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class StaffEvaluationVO {

    /**
     * id
     */
    private Long id;

    /**
     * 客户id
     */
    private Long customerId;

    /**
     * 员工id
     */
    private Long staffId;

    /**
     * 关联的预约id
     */
    private Long appointmentId;

    /**
     * 评价内容
     */
    private String evaluation;

    /**
     * 评价分数
     */
    private Integer evaluationScore;

    /**
     * 评价状态
     */
    private Integer evaluationSituation;

    /**
     * 门店名称
     */
    private String storeName;

    /**
     * 员工名称
     */
    private String staffName;

    /**
     * 预约时间
     */
    private LocalDate appointmentTime;

    /**
     * 预约时间段
     */
    private Integer timeInterval;

}
