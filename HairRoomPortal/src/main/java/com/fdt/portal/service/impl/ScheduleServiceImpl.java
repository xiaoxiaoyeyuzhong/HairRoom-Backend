package com.fdt.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fdt.common.api.ErrorCode;
import com.fdt.common.model.dto.schedule.ScheduleQueryRequest;
import com.fdt.common.model.dto.schedule.ScheduleUpdateRequest;
import com.fdt.common.model.entity.Schedule;
import com.fdt.common.model.vo.ScheduleVO;
import com.fdt.portal.exception.BusinessException;
import com.fdt.portal.mapper.ScheduleMapper;
import com.fdt.portal.service.ScheduleService;
import org.apache.commons.lang3.ObjectUtils;
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
        Long staffId = scheduleQueryRequest.getStaffId();
        Integer timeSlot = scheduleQueryRequest.getTimeSlot();
        Integer weekDay = scheduleQueryRequest.getWeekDay();
        queryWrapper.eq(ObjectUtils.isNotEmpty(staffId),"staffId", staffId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(timeSlot),"timeSlot", timeSlot);
        queryWrapper.eq("weekDay", weekDay);
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

    @Override
    public boolean updateAppointmentInfo(Long id) {

            Schedule oldSchedule = scheduleMapper.selectById(id);
            if (oldSchedule == null){
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
            }

            Integer haveAppointedSlots = oldSchedule.getHaveAppointedSlots();
            Integer appointSlots = oldSchedule.getAppointSlots();

            // todo 考虑并发问题，加入分布式锁保证数据正常-可预约人数不能小于0
            boolean result = scheduleMapper.updateAppointmentInfo(id,haveAppointedSlots+1,appointSlots-1);
            if (!result){
                throw new BusinessException(ErrorCode.OPERATION_ERROR);
            }
            return result;

    }

}
