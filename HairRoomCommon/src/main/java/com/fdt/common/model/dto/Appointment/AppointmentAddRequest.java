package com.fdt.common.model.dto.Appointment;

import java.time.LocalDate;

public class AppointmentAddRequest {

    /**
     * 客户id
     */
    private Long customerId;

    /**
     * 员工id
     */
    private Long staffId;

    /**
     * 预约日期
     */
    private LocalDate appointmentTime;

    /**
     * 预约时间段
     */
    private Integer timeSlot;
}
