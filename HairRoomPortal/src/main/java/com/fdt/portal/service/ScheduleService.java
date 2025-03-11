package com.fdt.portal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fdt.common.model.dto.schedule.ScheduleQueryRequest;
import com.fdt.common.model.dto.schedule.ScheduleUpdateRequest;
import com.fdt.common.model.entity.Schedule;
import com.fdt.common.model.entity.User;
import com.fdt.common.model.vo.ScheduleVO;

import java.util.List;

public interface ScheduleService extends IService<Schedule> {

    /**
     * 根据星期几获取排班信息
     * @param scheduleQueryRequest
     * @return
     */
    List<ScheduleVO> getDay(ScheduleQueryRequest scheduleQueryRequest);

    /**
     * 修改排班信息中的可预约人数和已预约人数
     * @param id 排班id
     * @return
     */
    boolean updateAppointmentInfo(Long id);
}
