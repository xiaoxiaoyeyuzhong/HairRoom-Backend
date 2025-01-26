package com.fdt.management.controller;

import com.fdt.common.api.BaseResponse;
import com.fdt.common.api.ErrorCode;
import com.fdt.common.api.ResultUtils;
import com.fdt.common.model.dto.schedule.AutoScheduleRequest;
import com.fdt.management.exception.BusinessException;
import com.fdt.management.service.ScheduleService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {


    @Resource
    ScheduleService scheduleService;

    // 自动排班
    @PostMapping("/autoSchedule")
    public BaseResponse<Boolean> autoSchedule(@RequestBody AutoScheduleRequest autoScheduleRequest) {

        if (autoScheduleRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(scheduleService.autoSchedule(autoScheduleRequest));

    }
}
