package com.fdt.common.model.dto.Appointment;

import lombok.Data;

import java.time.LocalDate;

@Data
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
    private String appointmentTime;

    /**
     * 预约时间段
     */
    private Integer timeSlot;
}
