package com.fdt.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fdt.common.api.ErrorCode;
import com.fdt.common.model.dto.schedule.ScheduleQueryRequest;
import com.fdt.common.model.entity.Schedule;
import com.fdt.common.model.vo.ScheduleVO;
import com.fdt.portal.exception.BusinessException;
import com.fdt.portal.mapper.ScheduleMapper;
import com.fdt.portal.service.ScheduleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl extends ServiceImpl<ScheduleMapper, Schedule>
        implements ScheduleService {

    @Resource
    private ScheduleMapper scheduleMapper;

    @Override
    public List<ScheduleVO> getDay(ScheduleQueryRequest scheduleQueryRequest) {
        QueryWrapper<Schedule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("staffId", scheduleQueryRequest.getStaffId());
        queryWrapper.eq("weekDay", scheduleQueryRequest.getWeekDay());
        List<Schedule> scheduleList = scheduleMapper.selectList(queryWrapper);
        try {
            return scheduleList.stream().map(schedule -> {
                ScheduleVO scheduleVO = new ScheduleVO();
                BeanUtils.copyProperties(schedule, scheduleVO);
                return scheduleVO;
            }).collect(Collectors.toList());
        }catch (Exception e){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
    }
}
