package com.fdt.common.model.dto.Appointment;

import lombok.Data;

import java.util.Date;

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
     * 预约日期
     */
    private String appointmentTime;

    /**
     * 预约时间段
     */
    private Integer timeInterval;
}
