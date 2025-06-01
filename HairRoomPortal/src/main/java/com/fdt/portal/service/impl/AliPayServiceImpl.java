package com.fdt.portal.service.impl;

import com.alipay.easysdk.factory.Factory;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fdt.common.api.ErrorCode;
import com.fdt.common.constant.BillConstant;
import com.fdt.common.model.entity.*;
import com.fdt.portal.exception.BusinessException;
import com.fdt.portal.service.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@Log4j2
public class AliPayServiceImpl implements AliPayService {

    @Resource
    private CustomerService customerService;

    @Resource
    private StaffService staffService;

    @Resource
    private AppointmentService appointmentService;

    @Resource
    BillService billService;

    @Resource
    private StaffEvaluationService staffEvaluationService;

    @Resource
    private BusinessSituationService businessSituationService;

    @Resource
    private StoreService storeService;

    @Override
    public String payNotify(HttpServletRequest request) throws Exception {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            params.put(name, request.getParameter(name));
        }
        String billName = params.get("subject");
        BigDecimal billAmount = new BigDecimal(params.get("buyer_pay_amount"));
        String tradeNo = params.get("trade_no");
        String outTradeNo = params.get("out_trade_no");
        String stringBusinessAmount = params.get("total_amount");

        log.info("交易名称: " + billName);
        log.info("交易状态: " + params.get("trade_status"));
        log.info("支付宝交易凭证号: " + tradeNo);
        log.info("商户订单号: " + outTradeNo);
        log.info("交易金额: " + params.get("total_amount"));
        log.info("买家在支付宝唯一id: " + params.get("buyer_id"));
        log.info("买家付款时间: " + params.get("gmt_payment"));
        log.info("买家付款金额: " + billAmount);

        //  签名认证通过，保存账单信息
        //  拆分billOutId，获取客户的用户id和员工id,验证时间戳是否过期
        int deadline = 1000 * 60 * 60 * 24;
        String result = this.checkOutTradeNo(outTradeNo, deadline);

        String[] parts = result.split("_");
        Long customerId = Long.valueOf(parts[0]);
        Long staffId = Long.valueOf(parts[1]);
        Long billId = Long.valueOf(parts[2]);

        // 修改对应billId的账单信息。
        Bill bill = new Bill();
        bill.setId(billId);
        bill.setBillName(billName);
        bill.setBillAmount(billAmount);
        bill.setTradeNo(tradeNo);
        bill.setOutTradeNo(outTradeNo);
        bill.setCustomerId(customerId);
        bill.setStaffId(staffId);
        // todo 获取账单类型，替换固定的类型,可以考虑放入billOutId中
        bill.setBillType("洗剪吹");
        // 支付宝签名验证
        if (Factory.Payment.Common().verifyNotify(params)) {

            bill.setPaySituation(BillConstant.BILL_PAY_STATUS_SUCCESS);
            billService.updateById(bill);

            // 支付成功后，生成空评价，关联客户id，员工id，预约id, 账单id
            StaffEvaluation staffEvaluation = new StaffEvaluation();
            staffEvaluation.setCustomerId(customerId);
            staffEvaluation.setStaffId(staffId);
            staffEvaluation.setBillId(billId);
            QueryWrapper<Appointment> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("billId", billId);
            Appointment appointment = appointmentService.getOne(queryWrapper);
            Long appointmentId = appointment.getId();

            if (appointmentId >= 0) {
                staffEvaluation.setAppointmentId(appointmentId);
                staffEvaluationService.save(staffEvaluation);
            }

            // 修改Business_ situation表，修改营业额

            // 通过staffId查询门店id
            QueryWrapper<Staff> staffQueryWrapper = new QueryWrapper<>();
            staffQueryWrapper.eq("id", staffId);
            Staff staff = staffService.getOne(staffQueryWrapper);

            Long storeId = staff.getStoreId();

            LocalDate businessDay = appointment.getAppointmentTime();
            // 先查询是否存在该门店营业额记录
            QueryWrapper<BusinessSituation> businessSituationQueryWrapper = new QueryWrapper<>();
            businessSituationQueryWrapper.eq("storeId", storeId);
            businessSituationQueryWrapper.eq("businessDay", businessDay);
            BusinessSituation businessSituation = businessSituationService.getOne(businessSituationQueryWrapper);
            if (businessSituation == null) {
                businessSituation = new BusinessSituation();
                businessSituation.setStoreId(storeId);
                businessSituation.setBusinessDay(businessDay);
                BigDecimal businessAmount = new BigDecimal(stringBusinessAmount);
                businessSituation.setBusinessAmount(businessAmount);
                // 查看门店有多少员工
                int staffCount = storeService.getStaffCount(storeId);
                BigDecimal  businessCost = new BigDecimal(staffCount * 300);
                businessSituation.setBusinessCost(businessCost);
                businessSituation.setBusinessProfit(businessAmount.subtract(businessCost));
                businessSituationService.save(businessSituation);
            } else {
                UpdateWrapper<BusinessSituation> businessSituationUpdateWrapper = new UpdateWrapper<>();
                if (staff.getStoreId() != null && storeId >= 0) {
                    businessSituationUpdateWrapper.eq("businessDay", businessDay);
                    businessSituationUpdateWrapper.eq("storeId", storeId);
                    BigDecimal businessAmount = new BigDecimal(stringBusinessAmount);
                    BigDecimal allBusinessAmount = businessSituation.getBusinessAmount().add(businessAmount);
                    businessSituationUpdateWrapper.set("businessAmount", allBusinessAmount);
                    BigDecimal businessProfit = businessSituation.getBusinessProfit().add(businessAmount);
                    businessSituationUpdateWrapper.set("businessProfit", businessProfit);
                }
            }

            log.info("支付宝支付回调完成");

        } else {
            log.info("支付宝签名认证失败");
            // 如果订单 支付失败，则修改账单状态为支付失败
            bill.setPaySituation(BillConstant.BILL_PAY_STATUS_FAILED);
            billService.updateById(bill);

        }
        return "订单支付成功";
    }

    @Override
    public String checkOutTradeNo(String outTradeNo, int deadline) {
        // 按照 _ 切分字符串
        String[] parts = outTradeNo.split("_");
        Long customerUserId = Long.valueOf(parts[0]);
        Long staffId = Long.valueOf(parts[1]);
        Long billId = Long.valueOf(parts[2]);
        Long timeStamp = Long.valueOf(parts[3]);
        Customer customer = customerService.getCustomerByUserId(customerUserId);
        if (customer == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "客户不存在");
        }
        // 验证员工是否存在
        Staff staff = staffService.getById(staffId);
        if (staff == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "员工不存在");
        }

        // 验证账单是否存在
        Bill bill = billService.getById(billId);
        if (bill == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账单不存在");
        }

        // 验证时间戳是否过期
        if (System.currentTimeMillis() - timeStamp > deadline) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "时间戳过期");
        }
        return customer.getId() + "_" + staffId + "_" + billId;
    }
}
