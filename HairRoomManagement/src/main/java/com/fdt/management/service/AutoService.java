package com.fdt.management.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fdt.common.model.entity.Schedule;
import com.fdt.management.mapper.ScheduleMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;

@Service
@Log4j2
public class AutoService {

    @Resource
    private ScheduleMapper scheduleMapper;

    // 每天23:30执行（CRON表达式：秒 分 时 日 月 周）
    @Scheduled(cron = "0 30 23 * * ?")
    public void resetScheduleSlotsDaily() {
        UpdateWrapper<Schedule> updateWrapper = new UpdateWrapper<>();
        LocalDate  now = LocalDate.now();
        int weekDay = now.getDayOfWeek().getValue();
        updateWrapper
                .set("haveAppointedSlots", 0)   // 重置已预约数为0
                .set("appointSlots", 16)// 重置可预约数为16
                .eq( "weekDay", weekDay)
                .eq("isDelete", 0); // 只更新未删除的记录

        int updatedRows = scheduleMapper.update(null, updateWrapper);
        log.info("[" + new java.util.Date() + "] 排班表预约数已重置，更新行数: " + updatedRows);
    }


}
