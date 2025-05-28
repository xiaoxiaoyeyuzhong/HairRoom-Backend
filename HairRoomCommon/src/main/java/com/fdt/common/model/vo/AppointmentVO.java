package com.fdt.common.model.vo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AppointmentVO {

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
     * 门店id
     */
    private Long storeId;

    /**
     * 预约日期
     */
    private LocalDate appointmentTime;

    /**
     * 预约时间段
     */
    private Integer timeInterval;

    /**
     * 门店名称
     */
    private String storeName;

    /**
     * 员工名称
     */
    private String staffName;

}
