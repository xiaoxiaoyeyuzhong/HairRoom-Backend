package com.fdt.portal.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fdt.common.model.dto.Appointment.AppointmentQueryRequest;
import com.fdt.common.model.vo.ScheduleVO;
import com.fdt.portal.mapper.AppointmentMapper;
import com.fdt.portal.service.AppointmentService;
import com.fdt.common.model.entity.Appointment;
import com.fdt.portal.service.ScheduleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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

    @Override
    public List<ScheduleVO> canAppointmentByDay(AppointmentQueryRequest appointmentQueryRequest) {

        // 将日期类型转换成数字类型的星期几

        // 对应查询星期几有什么员工排班

        return null;
    }


}




