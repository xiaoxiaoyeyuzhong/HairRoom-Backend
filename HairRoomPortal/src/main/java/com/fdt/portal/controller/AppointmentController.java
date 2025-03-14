package com.fdt.portal.controller;

import com.fdt.common.api.BaseResponse;
import com.fdt.common.api.ErrorCode;
import com.fdt.common.api.ResultUtils;
import com.fdt.common.constant.UserConstant;
import com.fdt.common.model.dto.Appointment.AppointmentAddRequest;
import com.fdt.common.model.dto.Appointment.AppointmentQueryRequest;
import com.fdt.common.model.entity.User;
import com.fdt.common.model.vo.AppointmentVO;
import com.fdt.common.model.vo.ScheduleVO;
import com.fdt.portal.annotation.AuthCheck;
import com.fdt.portal.exception.BusinessException;
import com.fdt.portal.service.AppointmentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    @Resource
    private AppointmentService appointmentService;

    /**
     * 用户查看某天可以预约的员工信息(有排班的员工)
     * @param appointmentQueryRequest
     * @return
     */
    @PostMapping("/can/day")
    public BaseResponse<List<ScheduleVO>> canAppointmentByDay(@RequestBody AppointmentQueryRequest appointmentQueryRequest) {

        if (appointmentQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<ScheduleVO> scheduleVOList = appointmentService.canAppointmentByDay(appointmentQueryRequest);

        return ResultUtils.success(scheduleVOList);
    }

    @PostMapping("/add")
    @AuthCheck(anyRole = {UserConstant.DEFAULT_ROLE,UserConstant.STAFF_ROLE,UserConstant.MANAGER_ROLE})
    public BaseResponse<Long> addAppointment(@RequestBody AppointmentAddRequest appointmentAddRequest) {
        if (appointmentAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long appointmentId = appointmentService.addAppointment(appointmentAddRequest);
        return ResultUtils.success(appointmentId);
    }

    @GetMapping("/listByUserId")
    public BaseResponse<List<AppointmentVO>> listAppointmentByUserId(Long userId){
        if (userId ==null || userId == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<AppointmentVO> appointmentVOList = appointmentService.listAppointmentByUserId(userId);
        return ResultUtils.success(appointmentVOList);
    }

    @GetMapping("/test")
    public BaseResponse<String> test(String date) {

        // todo 只允许预约包括当前日期的7天

        // 按照 . 切分字符串
        String[] parts = date.split("\\.");

        String yearPart = "";
        String monthPart = "";
        String dayPart = "";

        // 根据切分结果分配年、月、日
        if (parts.length == 3) {
            // 格式为 yyyy.M.d
            yearPart = parts[0];
            monthPart = parts[1];
            dayPart = parts[2];
        } else if (parts.length == 2) {
            // 格式为 M.d
            monthPart = parts[0];
            dayPart = parts[1];
        }

        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        int currentMonth = currentDate.getMonthValue();

        // 补全年份逻辑
        int year;
        if (yearPart.isEmpty()) {
            // 如果年份为空
            int month = Integer.parseInt(monthPart);
            if (month == currentMonth) {
                // 月份与当前月份相同，使用当前年份
                year = currentYear;
            } else if (month < currentMonth) {
                // 月份小于当前月份，使用当前年份 + 1
                year = currentYear + 1;
            } else {
                // 月份大于当前月份，使用当前年份
                year = currentYear;
            }
        } else {
            // 如果年份不为空，直接使用输入的年份
            year = Integer.parseInt(yearPart);
        }

        date = year + "." + monthPart + "." + dayPart;


        // 日期匹配格式只写一个M（小写m代表分钟）和一个d，就可以匹配不加零和加零的月，日了
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.M.d");

        LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);

        DayOfWeek dayOfWeek = localDate.getDayOfWeek();

        int dayOfWeekValue = dayOfWeek.getValue();

        return ResultUtils.success(String.valueOf(dayOfWeekValue));

    }
}
