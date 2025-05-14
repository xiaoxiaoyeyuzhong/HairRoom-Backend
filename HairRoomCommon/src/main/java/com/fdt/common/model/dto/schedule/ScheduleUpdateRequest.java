package com.fdt.common.model.dto.schedule;

public class ScheduleUpdateRequest {

    /**
     * id
     */
    private Long id;

    /**
     * 员工id
     */
    private Long staffId;

    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 星期几
     */
    private Integer weekDay;

    /**
     * 时间段
     */
    private Integer timeInterval;

    /**
     * 已预约数
     */
    private Integer haveAppointedSlots;

    /**
     * 可预约数
     */
    private Integer appointSlots;


}
