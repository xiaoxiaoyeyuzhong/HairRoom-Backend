package com.fdt.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fdt.common.api.ErrorCode;
import com.fdt.common.model.dto.schedule.ScheduleQueryRequest;
import com.fdt.common.model.entity.Schedule;
import com.fdt.common.model.entity.Staff;
import com.fdt.common.model.entity.Store;
import com.fdt.common.model.vo.ScheduleVO;
import com.fdt.portal.exception.BusinessException;
import com.fdt.portal.mapper.ScheduleMapper;
import com.fdt.portal.service.ScheduleService;
import com.fdt.portal.service.StaffService;
import com.fdt.portal.service.StoreService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ScheduleServiceImpl extends ServiceImpl<ScheduleMapper, Schedule>
        implements ScheduleService {

    @Resource
    private ScheduleMapper scheduleMapper;

    @Resource
    private StaffService staffService;

    @Resource
    private StoreService storeService;

    @Override
    public List<ScheduleVO> getDay(ScheduleQueryRequest scheduleQueryRequest) {
        QueryWrapper<Schedule> queryWrapper = new QueryWrapper<>();
        Long staffId = scheduleQueryRequest.getStaffId();
        Integer timeInterval = scheduleQueryRequest.getTimeInterval();
        Integer weekDay = scheduleQueryRequest.getWeekDay();
        queryWrapper.eq(ObjectUtils.isNotEmpty(staffId),"staffId", staffId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(timeInterval),"timeInterval", timeInterval);
        queryWrapper.eq("weekDay", weekDay);
        List<Schedule> scheduleList = scheduleMapper.selectList(queryWrapper);
        try {
            return scheduleList.stream().map(schedule -> {
                ScheduleVO scheduleVO = new ScheduleVO();
                BeanUtils.copyProperties(schedule, scheduleVO);
                // todo 根据staffId和timeInterval查询店铺名称和员工名称
                // 记得QueryWrapper设置id而不是storeId，要和数据库保持一致。
                QueryWrapper<Store> storeNameQueryWrapper = new QueryWrapper<>();
                log.info("storeId:"+scheduleVO.getStoreId());
                storeNameQueryWrapper.eq("id",scheduleVO.getStoreId());
                Store store = storeService.getOne(storeNameQueryWrapper);

                QueryWrapper<Staff> staffNameQueryWrapper = new QueryWrapper<>();
                log.info("staffId:"+scheduleVO.getStaffId());
                staffNameQueryWrapper.eq("id",scheduleVO.getStaffId());
                Staff staff = staffService.getOne(staffNameQueryWrapper);

                // todo 补充需要的店铺名称和员工名称
                scheduleVO.setStoreName(store.getStoreName());
                scheduleVO.setStaffName(staff.getStaffName());
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
