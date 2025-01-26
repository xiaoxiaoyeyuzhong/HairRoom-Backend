package com.fdt.common.model.vo;

import lombok.Data;

@Data
public class ScheduleVO {

    /**
     * 员工id
     */
    private Long staffId;

    /**
     * 星期几
     */
    private Integer weekDay;

    /**
     * 时间段
     */
    private Integer timeSlot;

    /**
     * 已预约数
     */
    private Integer haveAppointedSlots;

    /**
     * 可预约数
     */
    private Integer appointSlots;
}
