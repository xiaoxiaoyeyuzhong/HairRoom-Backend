package com.fdt.portal.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fdt.common.api.ErrorCode;
import com.fdt.common.constant.UserConstant;
import com.fdt.common.model.dto.Appointment.AppointmentAddRequest;
import com.fdt.common.model.dto.Appointment.AppointmentQueryRequest;
import com.fdt.common.model.dto.schedule.ScheduleQueryRequest;
import com.fdt.common.model.entity.Schedule;
import com.fdt.common.model.entity.User;
import com.fdt.common.model.vo.ScheduleVO;
import com.fdt.common.utils.DateToWeekUtil;
import com.fdt.portal.exception.BusinessException;
import com.fdt.portal.mapper.AppointmentMapper;
import com.fdt.portal.service.AppointmentService;
import com.fdt.common.model.entity.Appointment;
import com.fdt.portal.service.ScheduleService;
import com.fdt.portal.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author 冯德田
* @description 针对表【appointment(预约)】的数据库操作Service实现
* @createDate 2025-01-31 13:30:50
*/
@Service
public class AppointmentServiceImpl extends ServiceImpl<AppointmentMapper, Appointment>
    implements AppointmentService {

    @Resource
    private ScheduleService scheduleService;

    @Resource
    private UserService userService;

    @Override
    public List<ScheduleVO> canAppointmentByDay(AppointmentQueryRequest appointmentQueryRequest) {

        // 将日期类型转换成数字类型的星期几
        LocalDate appointmentDate = DateToWeekUtil.getDate(appointmentQueryRequest.getAppointmentTime());
        String week = DateToWeekUtil.getWeek(appointmentDate);

        // 对应查询星期几有什么员工排班
        ScheduleQueryRequest scheduleQueryRequest = new ScheduleQueryRequest();
        scheduleQueryRequest.setWeekDay(Integer.parseInt(week));
        List<ScheduleVO> scheduleVOList = scheduleService.getDay(scheduleQueryRequest);
        return scheduleVOList;

    }

    @Override
    public Long addAppointment(AppointmentAddRequest appointmentAddRequest) {
        Appointment appointment = new Appointment();
        BeanUtils.copyProperties(appointmentAddRequest, appointment);
        // 前端传递的是一个字符串类型的时间，要保存到数据库及后续操作，我们要转换为日期类型
        LocalDate appointmentDate = DateToWeekUtil.getDate(appointmentAddRequest.getAppointmentTime());
        appointment.setAppointmentTime(appointmentDate);

        // 验证客户是否存在,注意验证角色
        User customer = userService.getById(appointment.getCustomerId());
        if (customer == null || !UserConstant.DEFAULT_ROLE.equals(customer.getUserRole())){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "客户不存在");
        }

        // 验证用户要预约的时间，是否有对应的员工排班信息
        Long staffId = appointment.getStaffId();
        DayOfWeek dayOfWeek = appointment.getAppointmentTime().getDayOfWeek();
        Integer weekDay = dayOfWeek.getValue();
        Integer timeSlot = appointment.getTimeSlot();
        ScheduleQueryRequest scheduleQueryRequest = new ScheduleQueryRequest();
        scheduleQueryRequest.setStaffId(staffId);
        scheduleQueryRequest.setWeekDay(weekDay);
        scheduleQueryRequest.setTimeSlot(timeSlot);
        List<ScheduleVO> scheduleVOList = scheduleService.getDay(scheduleQueryRequest);
        if (scheduleVOList.isEmpty()){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "该时段没有该员工排班信息");
        }

        ScheduleVO scheduleVO = scheduleVOList.get(0);
        Long id = scheduleVO.getId();
        // 保存预约信息
        boolean result = this.save(appointment);
        if (!result){
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }

        // 更新员工排班信息，修改已预约人数和可预约人数
        scheduleService.updateAppointmentInfo(id);

        return appointment.getId();
    }


}




