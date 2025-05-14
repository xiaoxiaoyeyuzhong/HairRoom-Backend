package com.fdt.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
}




