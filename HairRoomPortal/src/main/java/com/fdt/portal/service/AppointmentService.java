package com.fdt.portal.service;

import com.fdt.common.api.DeleteRequest;
import com.fdt.common.model.dto.appointment.AppointmentAddRequest;
import com.fdt.common.model.dto.appointment.AppointmentQueryRequest;
import com.fdt.common.model.entity.Appointment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fdt.common.model.vo.AppointmentVO;
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

    /**
     * 用户预约
     * @param appointmentAddRequest 预约添加请求体
     * @return
     */
    Long addAppointment(AppointmentAddRequest appointmentAddRequest);

    /**
     * 根据用户id查看预约记录
     * @param userId
     * @return List<AppointmentVO> 预约记录
     */
    List<AppointmentVO> listAppointmentByUserId(Long userId);

    /**
     * 根据预约实体获取预约视图
     * @param appointmentList 预约实体列表
     * @return AppointmentVOList 预约视图列表
     */
    List<AppointmentVO> getAppointmentVOByAppointment(List<Appointment> appointmentList);

    /**
     * 删除预约
     * @param deleteRequest 删除请求体
     * @return
     */
    boolean cancelAppointment(DeleteRequest deleteRequest);
}
