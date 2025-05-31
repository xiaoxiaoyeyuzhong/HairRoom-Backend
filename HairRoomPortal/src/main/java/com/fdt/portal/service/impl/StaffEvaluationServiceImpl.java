package com.fdt.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fdt.common.api.ErrorCode;
import com.fdt.common.constant.BillConstant;
import com.fdt.common.model.dto.staffEvaluation.StaffEvaluationAddRequest;
import com.fdt.common.model.dto.staffEvaluation.StaffEvaluationQueryRequest;
import com.fdt.common.model.dto.staffEvaluation.StaffEvaluationUpdateRequest;
import com.fdt.common.model.entity.*;
import com.fdt.common.model.vo.StaffEvaluationVO;
import com.fdt.portal.exception.BusinessException;
import com.fdt.portal.service.*;
import com.fdt.portal.mapper.StaffEvaluationMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 冯德田
 * @description 针对表【staff_evaluation(员工评价表)】的数据库操作Service实现
 * @createDate 2025-05-31 10:08:18
 */
@Service
public class StaffEvaluationServiceImpl extends ServiceImpl<StaffEvaluationMapper, StaffEvaluation>
        implements StaffEvaluationService {

    @Resource
    private AppointmentService appointmentService;

    @Resource
    private BillService billService;

    @Resource
    private StaffService staffService;

    @Resource
    private StoreService storeService;

    @Resource
    private CustomerService customerService;

    @Override
    public boolean addStaffEvaluation(StaffEvaluationAddRequest staffEvaluationAddRequest) {

        // 查看是否有关联的预约id,预约项是否存在
        Long appointmentId = staffEvaluationAddRequest.getAppointmentId();
        if (appointmentId != null && appointmentId != 0) {
            Appointment appointment = appointmentService.getById(appointmentId);
            if (appointment != null && appointment.getBillId() >= 0) {
                Long billId = appointment.getBillId();
                Bill bill = billService.getById(billId);
                // 查看账单项是否存在，支付状态是否为已支付
                if (bill != null && bill.getPaySituation().equals(BillConstant.BILL_PAY_STATUS_SUCCESS)) {
                    StaffEvaluation staffEvaluation = new StaffEvaluation();
                    staffEvaluation.setCustomerId(appointment.getCustomerId());
                    staffEvaluation.setStaffId(appointment.getStaffId());
                    staffEvaluation.setAppointmentId(appointmentId);
                    staffEvaluation.setEvaluation(staffEvaluationAddRequest.getEvaluation());
                    staffEvaluation.setEvaluationScore(staffEvaluationAddRequest.getEvaluationScore());
                    return save(staffEvaluation);
                }
            }
        }

        return false;
    }

    @Override
    public List<StaffEvaluationVO> listStaffEvaluation(StaffEvaluationQueryRequest staffEvaluationQueryRequest) {

        Long customerUserId = staffEvaluationQueryRequest.getCustomerUserId();
        Customer customer = customerService.getCustomerByUserId(customerUserId);
        Long customerId = 0L;
        ;
        if (customer != null && customer.getId() >= 0) {
            customerId = customer.getId();
        }
        Integer evaluationSituation = staffEvaluationQueryRequest.getEvaluationSituation();
        QueryWrapper<StaffEvaluation> queryWrapper = new QueryWrapper<>();
        if (customerId != null && customerId >= 0) {
            queryWrapper.eq("customerId", customerId);
        }
        if (evaluationSituation != null && evaluationSituation >= 0) {
            queryWrapper.eq("evaluationSituation", evaluationSituation);
        }
        List<StaffEvaluation> staffEvaluationList = list(queryWrapper);
        return getStaffEvaluationVO(staffEvaluationList);
    }

    @Override
    public List<StaffEvaluationVO> getStaffEvaluationVO(List<StaffEvaluation> staffEvaluationList) {

        return staffEvaluationList.stream().map(staffEvaluation -> {
            StaffEvaluationVO staffEvaluationVO = new StaffEvaluationVO();

            // 复制已有属性
            BeanUtils.copyProperties(staffEvaluation, staffEvaluationVO);
            // 补充需要的员工名称，门店名称 ，预约时间，预约时间段
            Long staffId = staffEvaluationVO.getStaffId();
            Staff staff = staffService.getById(staffId);
            String staffName = staff.getStaffName();
            staffEvaluationVO.setStaffName(staffName);

            Long storeId = staff.getStoreId();
            Store store = storeService.getById(storeId);
            String storeName = store.getStoreName();
            staffEvaluationVO.setStoreName(storeName);

            Long appointmentId = staffEvaluationVO.getAppointmentId();
            Appointment appointment = appointmentService.getById(appointmentId);
            staffEvaluationVO.setAppointmentTime(appointment.getAppointmentTime());
            staffEvaluationVO.setTimeInterval(appointment.getTimeInterval());

            return staffEvaluationVO;
        }).collect(Collectors.toList());

    }

    @Override
    public Boolean updateStaffEvaluation(StaffEvaluationUpdateRequest staffEvaluationUpdateRequest) {

        Long id = staffEvaluationUpdateRequest.getId();
        if (id != null && id > 0) {
            QueryWrapper<StaffEvaluation> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", id);
            StaffEvaluation staffEvaluation = getOne(queryWrapper);
            if (staffEvaluation != null) {
                UpdateWrapper<StaffEvaluation> updateWrapper = new UpdateWrapper<>();
                boolean hasUpdates = false;
                updateWrapper.eq("id", id);
                String evaluation = staffEvaluationUpdateRequest.getEvaluation();
                Integer evaluationScore = staffEvaluationUpdateRequest.getEvaluationScore();
                // 仅当字段非空时才添加更新
                if (StringUtils.isNotBlank(evaluation)) {
                    updateWrapper.set("evaluation", evaluation);
                    hasUpdates = true;
                }

                if (evaluationScore != null) {
                    updateWrapper.set("evaluationScore",evaluationScore);
                    hasUpdates = true;
                }
                updateWrapper.set("evaluationSituation",1);
                // 如果没有有效更新字段，直接返回false
                if (!hasUpdates) {
                    return false;
                }

                // 执行更新并返回结果
                return update(updateWrapper);
            }

        }
        return false;
    }
}




