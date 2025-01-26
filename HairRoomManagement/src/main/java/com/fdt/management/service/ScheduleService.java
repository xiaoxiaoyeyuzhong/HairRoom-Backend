package com.fdt.management.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fdt.common.model.dto.schedule.AutoScheduleRequest;
import com.fdt.common.model.entity.Schedule;


/**
* @author 冯德田
* @description 针对表【schedule(排班)】的数据库操作Service
* @createDate 2025-01-23 16:09:38
*/
public interface ScheduleService extends IService<Schedule> {

    boolean autoSchedule(AutoScheduleRequest autoScheduleRequest);
}
