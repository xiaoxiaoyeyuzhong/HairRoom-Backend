package com.fdt.portal.controller;

import com.fdt.common.api.BaseResponse;
import com.fdt.common.api.ErrorCode;
import com.fdt.common.api.ResultUtils;
import com.fdt.common.model.dto.schedule.ScheduleQueryRequest;
import com.fdt.common.model.entity.Schedule;
import com.fdt.common.model.vo.ScheduleVO;
import com.fdt.portal.exception.BusinessException;
import com.fdt.portal.service.ScheduleService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Resource
    private ScheduleService scheduleService;

    // 查看排班信息
    @PostMapping("/get/day")
    public BaseResponse<List<ScheduleVO>> getScheduleByDay(@RequestBody ScheduleQueryRequest scheduleQueryRequest) {

        if (scheduleQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 加入RPC，使用管理系统的查询方法查询排班信息
        return ResultUtils.success(scheduleService.getDay(scheduleQueryRequest));

    }
}
