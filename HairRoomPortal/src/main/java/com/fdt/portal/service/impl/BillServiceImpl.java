package com.fdt.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fdt.common.api.ErrorCode;
import com.fdt.common.constant.UserConstant;
import com.fdt.portal.exception.BusinessException;
import com.fdt.common.model.dto.bill.BillAddRequest;
import com.fdt.common.model.dto.bill.BillQueryRequest;
import com.fdt.common.model.entity.Bill;
import com.fdt.common.model.entity.User;
import com.fdt.portal.service.BillService;
import com.fdt.portal.mapper.BillMapper;
import com.fdt.portal.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
* @author 冯德田
* @description 针对表【bill】的数据库操作Service实现
* @createDate 2025-01-10 15:51:27
*/
@Service
public class BillServiceImpl extends ServiceImpl<BillMapper, Bill>
    implements BillService {

    @Resource
    private UserService userService;

    @Override
    public QueryWrapper<Bill> getQueryWrapper(BillQueryRequest billQueryRequest) {
        return null;
    }

    @Override
    public Bill getBillById(int id, HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        Bill bill = this.getById(id);
        if (bill == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        if (!userService.isAdmin(request) && !user.getId().equals(bill.getStaffId())){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        return bill;
    }

    @Override
    public Long addBill(BillAddRequest billAddRequest) {
        Bill bill = new Bill();
        BeanUtils.copyProperties(billAddRequest, bill);
        if(bill.getBillAmount()== null || bill.getBillAmount().compareTo(BigDecimal.ZERO) < 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "金额不能为空且必须大于0");
        }
        // 验证员工和客户是否存在,注意验证角色
        User customer = userService.getById(bill.getCustomerId());
        User staff = userService.getById(bill.getStaffId());
        if (customer == null || !UserConstant.DEFAULT_ROLE.equals(customer.getUserRole())){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "客户不存在");
        }
        if (staff == null || !UserConstant.STAFF_ROLE.equals(staff.getUserRole())){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "员工不存在");
        }
        boolean result = this.save(bill);
        if (!result){
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return bill.getId();
    }

    @Override
    public Boolean updateBill(Bill bill) {
        UpdateWrapper<Bill> updateWrapper = new UpdateWrapper<>();

        Long id = bill.getId();
        String tradeNo = bill.getTradeNo();
        String outTradeNo = bill.getOutTradeNo();
        Integer paySituation = bill.getPaySituation();
        String billName = bill.getBillName();
        BigDecimal billAmount = bill.getBillAmount();
        String billType = bill.getBillType();
        String billDesc = bill.getBillDesc();
        Long CustomerId = bill.getCustomerId();
        Long StaffId = bill.getStaffId();

        if(id != null && id > 0){
            updateWrapper.eq("id", id);
        }
        if(tradeNo != null && tradeNo.length() > 0){
            updateWrapper.eq("tradeNo", tradeNo);
        }
        if(outTradeNo != null && outTradeNo.length() > 0){
            updateWrapper.eq("outTradeNo", outTradeNo);
        }
        if(paySituation != null && paySituation > 0){
            updateWrapper.set("paySituation", paySituation);
        }
        if(billName != null && billName.length() > 0){
            updateWrapper.set("billName", billName);
        }
        if(billAmount != null && !BigDecimal.ZERO.equals(billAmount)){
            updateWrapper.set("billAmount", billAmount);
        }
        if(billType != null && billType.length() > 0){
            updateWrapper.set("billType", billType);
        }
        if(billDesc != null && billDesc.length() > 0){
            updateWrapper.set("billDesc", billDesc);
        }
        if(CustomerId != null && CustomerId > 0){
            updateWrapper.set("CustomerId", CustomerId);
        }
        if(StaffId != null && StaffId > 0){
            updateWrapper.set("StaffId", StaffId);
        }
        boolean updated = this.update(updateWrapper);
        if (!updated) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新账单信息失败");
        }
        return updated;
    }
}




