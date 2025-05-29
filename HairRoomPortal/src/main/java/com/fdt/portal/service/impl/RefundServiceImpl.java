package com.fdt.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fdt.common.api.ErrorCode;
import com.fdt.common.model.dto.refund.RefundAddRequest;
import com.fdt.common.model.entity.Customer;
import com.fdt.common.model.entity.Refund;

import com.fdt.common.model.entity.Staff;
import com.fdt.portal.exception.BusinessException;
import com.fdt.portal.mapper.RefundMapper;
import com.fdt.portal.service.AliPayService;
import com.fdt.portal.service.CustomerService;
import com.fdt.portal.service.RefundService;
import com.fdt.portal.service.StaffService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 冯德田
* @description 针对表【refund(退款)】的数据库操作Service实现
* @createDate 2025-05-25 23:20:08
*/
@Service
public class RefundServiceImpl extends ServiceImpl<RefundMapper, Refund>
    implements RefundService {

    @Resource
    private AliPayService aliPayService;


    @Override
    public boolean addRefund(RefundAddRequest refundAddRequest) {

        String tradeNo = refundAddRequest.getTradeNo();
        String outTradeNo = refundAddRequest.getOutTradeNo();
        // 检查数据库是否存在与请求参数重复的支付宝订单号和外部订单号。
        Refund oldRefund = this.getByOutAndTradeNo(tradeNo, outTradeNo);
        if(oldRefund != null){
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该退款申请已存在");
        }
        // 检查外部订单号构成是否合法-是否存在相应的客户，员工，时间戳是否过期。
        int deadline = 1000 * 60 * 60 * 24 * 7;
        aliPayService.checkOutTradeNo(outTradeNo,deadline);

        // 若请求合法，则复制请求参数，创建退款对象，保存到数据库中。
        Refund refund = new Refund();
        BeanUtils.copyProperties(refundAddRequest, refund);
        return this.save(refund);
    }

    @Override
    public Refund getByOutAndTradeNo(String tradeNo, String outTradeNo) {
        QueryWrapper<Refund> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tradeNo", tradeNo);
        queryWrapper.eq("outTradeNo", outTradeNo);
        return this.getOne(queryWrapper);
    }
}




