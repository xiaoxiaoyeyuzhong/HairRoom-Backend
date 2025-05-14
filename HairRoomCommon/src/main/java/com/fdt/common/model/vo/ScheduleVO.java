package com.fdt.common.model.vo;

import lombok.Data;

@Data
public class ScheduleVO {

    /**
     * 排班id
     */
    private Long id;

    /**
     * 门店id
     */
    private Long storeId;

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

    /**
     * 已预约数
     */
    private Integer haveAppointedSlots;

    /**
     * 可预约数
     */
    private Integer appointSlots;

    /**
     * 门店名称
     */
    private String storeName;

    /**
     * 员工名称
     */
    private String staffName;
}
