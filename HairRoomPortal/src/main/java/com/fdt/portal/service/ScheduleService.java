package com.fdt.portal.service;

import com.fdt.common.model.dto.schedule.ScheduleQueryRequest;
import com.fdt.common.model.vo.ScheduleVO;

import java.util.List;

public interface ScheduleService {
    List<ScheduleVO> getDay(ScheduleQueryRequest scheduleQueryRequest);
}
