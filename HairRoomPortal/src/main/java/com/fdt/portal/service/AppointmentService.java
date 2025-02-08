package com.fdt.portal.service;

import com.fdt.common.model.dto.Appointment.AppointmentQueryRequest;
import com.fdt.common.model.entity.Appointment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fdt.common.model.vo.ScheduleVO;

import java.util.List;

/**
* @author 冯德田
* @description 针对表【appointment(预约)】的数据库操作Service
* @createDate 2025-01-31 13:30:50
*/
public interface AppointmentService extends IService<Appointment> {

    /**
     * 查看可预约的员工排班信息
     * @param appointmentQueryRequest 预约查询请求体
     * @return List<ScheduleVO> 可预约的员工排班信息
     */
    List<ScheduleVO> canAppointmentByDay(AppointmentQueryRequest appointmentQueryRequest);
}
