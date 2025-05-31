package com.fdt.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fdt.common.api.DeleteRequest;
import com.fdt.common.api.ErrorCode;
import com.fdt.common.constant.BillConstant;
import com.fdt.common.model.dto.appointment.AppointmentAddRequest;
import com.fdt.common.model.dto.appointment.AppointmentQueryRequest;
import com.fdt.common.model.dto.schedule.ScheduleQueryRequest;
import com.fdt.common.model.entity.*;
import com.fdt.common.model.vo.AppointmentVO;
import com.fdt.common.model.vo.ScheduleVO;
import com.fdt.common.utils.DateToWeekUtil;
import com.fdt.portal.exception.BusinessException;
import com.fdt.portal.mapper.AppointmentMapper;
import com.fdt.portal.mapper.BillMapper;
import com.fdt.portal.service.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 冯德田
 * @description 针对表【appointment(预约)】的数据库操作Service实现
 * @createDate 2025-01-31 13:30:50
 */
@Service
@Log4j2
public class AppointmentServiceImpl extends ServiceImpl<AppointmentMapper, Appointment>
        implements AppointmentService {

    @Resource
    private ScheduleService scheduleService;

    @Resource
    private CustomerService customerService;

    @Resource
    private StaffService staffService;

    @Resource
    private StoreService storeService;

    @Resource
    private BillService billService;

    @Resource
    private BillMapper billMapper;

    @Override
    public List<ScheduleVO> canAppointmentByDay(AppointmentQueryRequest appointmentQueryRequest) {

        // 将日期类型转换成数字类型的星期几
        LocalDate appointmentDate = DateToWeekUtil.getDate(appointmentQueryRequest.getAppointmentTime());
        String week = DateToWeekUtil.getWeek(appointmentDate);

        // 对应查询星期几有什么员工排班
        ScheduleQueryRequest scheduleQueryRequest = new ScheduleQueryRequest();
        scheduleQueryRequest.setWeekDay(Integer.parseInt(week));
        List<ScheduleVO> scheduleVOList = scheduleService.getDay(scheduleQueryRequest);
        return scheduleVOList;

    }

    @Override
    public Long addAppointment(AppointmentAddRequest appointmentAddRequest) {
        Appointment appointment = new Appointment();
        BeanUtils.copyProperties(appointmentAddRequest, appointment);
        // 前端传递的是一个字符串类型的时间，要保存到数据库及后续操作，我们要转换为日期类型
        LocalDate appointmentDate = DateToWeekUtil.getDate(appointmentAddRequest.getAppointmentTime());
        appointment.setAppointmentTime(appointmentDate);

        //不能预约过时的日期
         if (appointmentDate.isBefore(LocalDate.now())) {
              throw new BusinessException(ErrorCode.PARAMS_ERROR, "不能预约过时的日期");
         }
        // 不能预约过时的时间段
        // 如果是当天预约，需要检查时间段是否可用
        Integer timeInterval = appointment.getTimeInterval();
        this.checkTimeInterval(appointmentDate,timeInterval);

        // 根据前端传递的用户id获取用户在客户表的id
        Customer customer = customerService.getCustomerByUserId(appointmentAddRequest.getCustomerUserId());
        Long customerId = customer.getId();
        appointment.setCustomerId(customerId);
//        // 验证客户是否存在,注意验证角色
//        User customer = userService.getById(appointment.getCustomerId());
//        if (customer == null || !UserConstant.DEFAULT_ROLE.equals(customer.getUserRole())){
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "客户不存在");
//        }

        // 验证用户要预约的时间，是否有对应的员工排班信息
        Long staffId = appointment.getStaffId();
        DayOfWeek dayOfWeek = appointment.getAppointmentTime().getDayOfWeek();
        Integer weekDay = dayOfWeek.getValue();
        ScheduleQueryRequest scheduleQueryRequest = new ScheduleQueryRequest();
        scheduleQueryRequest.setStaffId(staffId);
        scheduleQueryRequest.setWeekDay(weekDay);
        scheduleQueryRequest.setTimeInterval(timeInterval);
        List<ScheduleVO> scheduleVOList = scheduleService.getDay(scheduleQueryRequest);
        if (scheduleVOList.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "该时段没有该员工排班信息");
        }

        ScheduleVO scheduleVO = scheduleVOList.get(0);
        Long id = scheduleVO.getId();

        // todo 防止重复预约

        // 生成新的的账单项，设置账单的客户id和员工id，然后把账单id保存到预约信息中
        Bill bill = new Bill();
        bill.setCustomerId(customerId);
        bill.setStaffId(staffId);
        // 插入账单后要获得账单id
        billMapper.insert(bill);
        long billId = bill.getId();
        appointment.setBillId(billId);
        // 保存预约信息
        boolean result = this.save(appointment);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }

        // 更新员工排班信息，修改已预约人数和可预约人数
        scheduleService.updateAppointmentInfo(id);

        return appointment.getId();
    }

    @Override
    public List<AppointmentVO> listAppointmentByUserId(Long userId) {

        if (userId == null || userId == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Customer customer = customerService.getCustomerByUserId(userId);
        Long customerId = customer.getId();

        QueryWrapper<Appointment> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("customerId", customerId);
        List<Appointment> appointmentList = this.list(queryWrapper);
        return getAppointmentVOByAppointment(appointmentList);

    }

    @Override
    public List<AppointmentVO> getAppointmentVOByAppointment(List<Appointment> appointmentList) {
        return appointmentList.stream().map(appointment -> {
            AppointmentVO appointmentVO = new AppointmentVO();
            BeanUtils.copyProperties(appointment, appointmentVO);

            // 根据staffId和timeInterval查询店铺名称和员工名称
            // 记得QueryWrapper设置id而不是storeId，要和数据库保持一致。
            QueryWrapper<Store> storeNameQueryWrapper = new QueryWrapper<>();
            log.info("storeId:" + appointmentVO.getStoreId());
            storeNameQueryWrapper.eq("id", appointmentVO.getStoreId());
            Store store = storeService.getOne(storeNameQueryWrapper);

            QueryWrapper<Staff> staffNameQueryWrapper = new QueryWrapper<>();
            log.info("staffId:" + appointmentVO.getStaffId());
            staffNameQueryWrapper.eq("id", appointmentVO.getStaffId());
            Staff staff = staffService.getOne(staffNameQueryWrapper);

            // 补充需要的店铺名称和员工名称
            appointmentVO.setStoreName(store.getStoreName());
            appointmentVO.setStaffName(staff.getStaffName());


            // 如果预约对应的账单已支付，根据账单id查询商户订单号和支付宝交易号，否则不用查询
            if (appointmentVO.getBillId() != null && appointmentVO.getBillId() != 0) {
                QueryWrapper<Bill> billQueryWrapper = new QueryWrapper<>();
                billQueryWrapper.eq("id", appointmentVO.getBillId());
                Bill bill = billService.getOne(billQueryWrapper);
                String tradeNo = bill.getTradeNo();
                String outTradeNo = bill.getOutTradeNo();
                Integer paySituation = bill.getPaySituation();
                if(bill != null){
                    if(paySituation != 0){
                        appointmentVO.setTradeNo(tradeNo);
                        appointmentVO.setOutTradeNo(outTradeNo);
                    }
                    // 补充账单支付状态信息
                    appointmentVO.setPaySituation(bill.getPaySituation());
                }
            }

            return appointmentVO;
        }).collect(Collectors.toList());
    }


    @Override
    public boolean cancelAppointment(DeleteRequest deleteRequest) {

        // 验证参数
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 查询出对应的预约项
        Appointment appointment = this.getById(deleteRequest.getId());

        // 如果已经有已支付的订单，不允许删除
        if (appointment.getBillId() != null && appointment.getBillId() != 0) {
            Bill bill = billMapper.selectById(appointment.getBillId());
            if (bill != null) {
                if(bill.getPaySituation().equals(BillConstant.BILL_PAY_STATUS_WAIT)){
                    // 删除生成的空账单
                    billMapper.deleteById(appointment.getBillId());
                } else if(bill.getPaySituation().equals(BillConstant.BILL_PAY_STATUS_SUCCESS)){
                    throw new BusinessException(ErrorCode.OPERATION_ERROR, "该预约项已支付，不允许删除");
                }

            }
        }

        if (this.removeById(deleteRequest.getId())) {
            // todo 订单过期后不需要修改。
            // 更新员工排班信息，修改已预约人数和可预约人数
            Long staffId = appointment.getStaffId();
            Integer timeInterval = appointment.getTimeInterval();
            Integer weekDay = appointment.getAppointmentTime().getDayOfWeek().getValue();
            QueryWrapper<Schedule> scheduleQueryWrapper = new QueryWrapper<>();
            scheduleQueryWrapper.eq("staffId", staffId);
            scheduleQueryWrapper.eq("timeInterval", timeInterval);
            scheduleQueryWrapper.eq("weekDay", weekDay);
            Schedule schedule = scheduleService.getOne(scheduleQueryWrapper);
            schedule.setHaveAppointedSlots(schedule.getHaveAppointedSlots() - 1);
            schedule.setAppointSlots(schedule.getAppointSlots() + 1);
            return scheduleService.updateSchedule(schedule);

        }
        return true;
    }

    @Override
    public void checkTimeInterval(LocalDate appointmentDate,Integer timeInterval) {
        // 如果是当天预约，需要检查时间段是否可用
        if (appointmentDate.isEqual(LocalDate.now())) {
            // 获取当前时间
            LocalTime now = LocalTime.now();

            // 根据时间段设置结束时间
            LocalTime endTime;
            switch (timeInterval) {
                case 1:
                    endTime = LocalTime.of(11, 30);
                    break;
                case 2:
                    endTime = LocalTime.of(17, 30);
                    break;
                case 3:
                    endTime = LocalTime.of(22, 30);
                    break;
                default:
                    throw new BusinessException(ErrorCode.PARAMS_ERROR, "无效的时间段");
            }

            // 检查时间规则
            if (now.isAfter(endTime)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "该时间段已结束，无法预约");
            }

            // 检查是否在15分钟内结束
            if (Duration.between(now, endTime).toMinutes() < 15) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "距离该时间段结束不足15分钟，无法预约");
            }
        }
    }


}




