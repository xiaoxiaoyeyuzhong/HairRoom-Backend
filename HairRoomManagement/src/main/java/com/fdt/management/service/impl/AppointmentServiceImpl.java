package com.fdt.management.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fdt.common.model.entity.Appointment;
import com.fdt.management.service.AppointmentService;
import com.fdt.management.mapper.AppointmentMapper;
import org.springframework.stereotype.Service;

/**
* @author 冯德田
* @description 针对表【appointment(预约)】的数据库操作Service实现
* @createDate 2025-05-30 21:24:26
*/
@Service
public class AppointmentServiceImpl extends ServiceImpl<AppointmentMapper, Appointment>
    implements AppointmentService{

}




