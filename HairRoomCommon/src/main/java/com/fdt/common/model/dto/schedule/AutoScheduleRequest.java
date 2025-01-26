package com.fdt.common.model.dto.schedule;

import lombok.Data;

@Data
public class AutoScheduleRequest {

    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 可预约数
     */
    private Integer appointSlots;
}
