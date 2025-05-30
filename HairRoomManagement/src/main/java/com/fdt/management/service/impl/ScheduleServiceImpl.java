package com.fdt.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fdt.common.api.ErrorCode;
import com.fdt.common.model.dto.schedule.AutoScheduleRequest;
import com.fdt.common.model.entity.Schedule;
import com.fdt.common.model.entity.Staff;
import com.fdt.common.utils.generateWorkDays;
import com.fdt.management.exception.BusinessException;
import com.fdt.management.mapper.ScheduleMapper;
import com.fdt.management.service.ScheduleService;
import com.fdt.management.service.StaffService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 冯德田
* @description 针对表【schedule(排班)】的数据库操作Service实现
* @createDate 2025-01-23 16:09:38
*/
@Service
public class ScheduleServiceImpl extends ServiceImpl<ScheduleMapper, Schedule>
    implements ScheduleService {

    @Resource
    private StaffService staffService;

    @Override
    public boolean autoSchedule(AutoScheduleRequest autoScheduleRequest) {
        // 获取对应门店的员工
        Long storeId = autoScheduleRequest.getStoreId();
        List<Staff> staffList = staffService.list(new QueryWrapper<Staff>().eq("storeId", storeId));

        int staffNum = staffList.size();

        int num_day = 5;
        int num_slot = 2;
        int range_day = 7;
        int range_slot = 3;
        // 每个员工每星期值班5天，每天两个时间段
        for(int i =0;i < staffNum; i++){
            // 随机生成周几值班的数据
            List<Integer> workDaysList = generateWorkDays.generate(num_day,range_day);

            for(int j = 0; j < num_day; j++){

                // 随机生成时间段数据
                List<Integer> timeIntervalList = generateWorkDays.generate(num_slot,range_slot);

                for(int k = 0; k < num_slot; k++){
                    Schedule schedule = new Schedule();
                    schedule.setStaffId(staffList.get(i).getId());
                    schedule.setStoreId(storeId);
                    schedule.setWeekDay(workDaysList.get(j));
                    schedule.setTimeInterval(timeIntervalList.get(k));
                    schedule.setAppointSlots(autoScheduleRequest.getAppointSlots());
                    try {
                        this.save(schedule);

                    }catch (Exception e){
                        throw new BusinessException(ErrorCode.SYSTEM_ERROR, String.valueOf(e));
                    }

                }
            }
        }

        return true;
    }

    @Override
    public boolean updateSchedule(Schedule schedule) {
        UpdateWrapper<Schedule> updateWrapper = new UpdateWrapper<>();
        Long id = schedule.getId();
        Long staffId = schedule.getStaffId();
        Long storeId = schedule.getStoreId();
        Integer weekDay = schedule.getWeekDay();
        Integer timeInterval = schedule.getTimeInterval();
        Integer haveAppointedSlots = schedule.getHaveAppointedSlots();
        Integer appointSlots = schedule.getAppointSlots();

        if(id != null && id >= 0){
            updateWrapper.eq("id", id);
        }
        if(staffId != null && staffId >= 0){
            updateWrapper.eq("staffId", staffId);
        }
        if(weekDay != null && weekDay >= 0){
            updateWrapper.eq("weekDay", weekDay);
        }
        if(timeInterval != null && timeInterval >= 0){
            updateWrapper.eq("timeInterval", timeInterval);
        }
        if(storeId != null && storeId >= 0){
            updateWrapper.set("storeId", storeId);
        }
        if(haveAppointedSlots != null && haveAppointedSlots >= 0){
            updateWrapper.set("haveAppointedSlots", haveAppointedSlots);
        }
        if(appointSlots != null && appointSlots >= 0){
            updateWrapper.set("appointSlots", appointSlots);
        }
        return this.update(updateWrapper);
    }
}




