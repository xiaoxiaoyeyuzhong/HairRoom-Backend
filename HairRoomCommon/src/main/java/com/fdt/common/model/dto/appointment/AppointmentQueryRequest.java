package com.fdt.common.model.dto.appointment;

import lombok.Data;

@Data
public class AppointmentQueryRequest {

    /**
     * 客户id
     */
    private Long customerUserId;

    /**
     * 员工id
     */
    private Long staffId;

    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 账单id
     */
    private Long billId;

    /**
     * 预约日期
     */
    private String appointmentTime;

    /**
     * 预约时间段
     */
    private Integer timeInterval;
}
