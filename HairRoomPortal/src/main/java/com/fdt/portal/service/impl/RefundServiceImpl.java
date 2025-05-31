package com.fdt.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fdt.common.api.ErrorCode;
import com.fdt.common.constant.BillConstant;
import com.fdt.common.model.dto.refund.RefundAddRequest;
import com.fdt.common.model.entity.*;

import com.fdt.portal.exception.BusinessException;
import com.fdt.portal.mapper.RefundMapper;
import com.fdt.portal.service.*;
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

    @Resource
    private BillService billService;

    @Resource
    private StaffEvaluationService staffEvaluationService;

    @Override
    public boolean addRefund(RefundAddRequest refundAddRequest) {

        String tradeNo = refundAddRequest.getTradeNo();
        String outTradeNo = refundAddRequest.getOutTradeNo();
        // 检查数据库是否存在与请求参数重复的支付宝订单号和外部订单号。
        Refund oldRefund = this.getByOutAndTradeNo(tradeNo, outTradeNo);
        if (oldRefund != null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该退款申请已存在");
        }
        // 检查外部订单号构成是否合法-是否存在相应的客户，员工，时间戳是否过期。
        int deadline = 1000 * 60 * 60 * 24 * 7;
        String result = aliPayService.checkOutTradeNo(outTradeNo, deadline);
        String[] parts = result.split("_");
        Long billId = Long.valueOf(parts[2]);
        // 若请求合法，则复制请求参数，创建退款对象，保存到数据库中;接着修改对应账单的支付情况为退款处理中。
        Refund refund = new Refund();
        BeanUtils.copyProperties(refundAddRequest, refund);
        if (!this.save(refund)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "添加退款申请失败");
        }
        Bill bill = new Bill();
        bill.setTradeNo(tradeNo);
        bill.setOutTradeNo(outTradeNo);
        bill.setPaySituation(BillConstant.BILL_PAY_STATUS_REFUNDING);
        if (!billService.updateBill(bill)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "修改账单支付状态失败");
        }
        // 删除账单对应的评价
        QueryWrapper<StaffEvaluation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("billId", billId);
        StaffEvaluation staffEvaluation = staffEvaluationService.getOne(queryWrapper);
        if (staffEvaluation != null) {
            if (!staffEvaluationService.removeById(staffEvaluation)) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "删除评价失败");
            }
        }
        return true;

    }

    @Override
    public Refund getByOutAndTradeNo(String tradeNo, String outTradeNo) {
        QueryWrapper<Refund> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tradeNo", tradeNo);
        queryWrapper.eq("outTradeNo", outTradeNo);
        return this.getOne(queryWrapper);
    }
}




