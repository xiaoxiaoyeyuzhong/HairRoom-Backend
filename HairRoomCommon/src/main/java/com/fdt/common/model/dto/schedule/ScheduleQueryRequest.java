package com.fdt.common.model.dto.schedule;

import lombok.Data;

@Data
public class ScheduleQueryRequest {

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
    private Integer timeInterval;
}
