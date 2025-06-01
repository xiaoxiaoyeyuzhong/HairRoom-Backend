package com.fdt.management.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fdt.common.api.BaseResponse;
import com.fdt.common.api.ErrorCode;
import com.fdt.common.api.ResultUtils;
import com.fdt.common.constant.BillConstant;
import com.fdt.common.constant.RefundConstant;
import com.fdt.common.constant.UserConstant;
import com.fdt.common.model.dto.pay.AliPayRefund;
import com.fdt.common.model.entity.*;
import com.fdt.management.annotation.AuthCheck;
import com.fdt.management.config.AliPayConfig;
import com.fdt.management.exception.BusinessException;
import com.fdt.management.service.*;

import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/alipay")
@Transactional(rollbackFor = Exception.class)
@Log4j2
public class AliPayController {

    @Resource
    private AliPayConfig aliPayConfig;

    @Resource
    private BillService billService;

    @Resource
    private RefundService refundService;

    @Resource
    private AppointmentService appointmentService;

    @Resource
    private ScheduleService scheduleService;

    @Resource
    private StaffService staffService;

    @Resource
    private BusinessSituationService businessSituationService;

    private static final String GATEWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private static final String FORMAT = "JSON";
    private static final String CHARSET = "UTF-8";
    private static final String SIGN_TYPE = "RSA2";

    @PostMapping("/refund")
    @AuthCheck(mustRole = UserConstant.MANAGER_ROLE)
    public BaseResponse<String> refund(@RequestBody AliPayRefund aliPayRefund) {
        log.info("===支付宝退款接口调用开始===");

        try {
            AlipayClient alipayClient = new DefaultAlipayClient(
                    GATEWAY_URL,
                    aliPayConfig.getAppId(),
                    aliPayConfig.getAppPrivateKey(),
                    FORMAT,
                    CHARSET,
                    aliPayConfig.getAlipayPublicKey(),
                    SIGN_TYPE
            );


            // 构造退款请求
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
            request.setBizContent("{" +
                    "\"trade_no\":\"" + aliPayRefund.getTradeNo() + "\"," +
                    "\"out_trade_no\":\"" + aliPayRefund.getOutTradeNo() + "\"," +
                    "\"refund_amount\":\"" + aliPayRefund.getRefundAmount() + "\"," +
                    "\"refund_reason\":\"" + aliPayRefund.getRefundReason() + "\"" +
                    "}");

            // 执行退款
            AlipayTradeRefundResponse response = alipayClient.execute(request);

            BigDecimal businessAmount = aliPayRefund.getRefundAmount();
            Bill bill = billService.getByOutAndTradeNo(aliPayRefund.getTradeNo(), aliPayRefund.getOutTradeNo());

            // 更新退款情况
            Refund refund = new Refund();
            refund.setTradeNo(aliPayRefund.getTradeNo());
            refund.setOutTradeNo(aliPayRefund.getOutTradeNo());
            refund.setRefundAmount(aliPayRefund.getRefundAmount());
            refund.setRefundReason(aliPayRefund.getRefundReason());

            // 退款成功，更新数据库
            if (response.isSuccess()) {
                log.info("===支付宝退款接口调用成功===");

                if (bill != null) {
                    // 如果客户选择退款，证明客户不想要服务了，需要更新排班信息，增加对应项的可预约人数，减少已预约人数
                    QueryWrapper<Appointment> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("billId", bill.getId());
                    Appointment appointment = appointmentService.getOne(queryWrapper);
                    if (appointment != null) {
                        // 要根据预约中的员工id、时间段和预约时间修改对应的排班信息
                        // 首先要转换预约时间和星期几
                        int weekDay = appointment.getAppointmentTime().getDayOfWeek().getValue();
                        int timeInterval = appointment.getTimeInterval();
                        Long staffId = appointment.getStaffId();
                        QueryWrapper<Schedule> scheduleQueryWrapper = new QueryWrapper<>();
                        scheduleQueryWrapper.eq("weekDay", weekDay);
                        scheduleQueryWrapper.eq("staffId", staffId);
                        scheduleQueryWrapper.eq("timeInterval", timeInterval);
                        Schedule schedule = scheduleService.getOne(scheduleQueryWrapper);
                        schedule.setHaveAppointedSlots(schedule.getHaveAppointedSlots() - 1);
                        schedule.setAppointSlots(schedule.getAppointSlots() + 1);
                        if (!scheduleService.updateSchedule(schedule)) {
                            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新排班信息失败");
                        }
                        // 设置账单的支付状态为已退款
                        bill.setPaySituation(BillConstant.BILL_PAY_STATUS_REFUNDED);
                        refund.setRefundSituation(RefundConstant.REFUND_SITUATION_SUCCESS);
                        billService.updateById(bill);
                        refundService.updateRefund(refund);
                        log.info("订单退款成功，订单号：{}", aliPayRefund.getOutTradeNo());

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
                        UpdateWrapper<BusinessSituation> businessSituationUpdateWrapper = new UpdateWrapper<>();
                        if (storeId != null && storeId >= 0) {
                            businessSituationUpdateWrapper.eq("businessDay", businessDay);
                            businessSituationUpdateWrapper.eq("storeId", storeId);
                            BigDecimal allBusinessAmount = businessSituation.getBusinessAmount().subtract(businessAmount);
                            businessSituationUpdateWrapper.set("businessAmount", allBusinessAmount);
                            BigDecimal businessProfit = businessSituation.getBusinessProfit().subtract(businessAmount);
                            businessSituationUpdateWrapper.set("businessProfit", businessProfit);

                        }
                    }

                }
                return ResultUtils.success("退款成功");
            } else { // 退款失败，更新数据库
                // 设置账单的支付状态为退款失败
                bill.setPaySituation(BillConstant.BILL_PAY_STATUS_REFUND_FAILED);
                refund.setRefundSituation(RefundConstant.REFUND_SITUATION_FAIL);
                billService.updateById(bill);
                refundService.updateRefund(refund);
                log.error("支付宝退款失败，原因：{}", response.getSubMsg());
                return ResultUtils.error(ErrorCode.OPERATION_ERROR, "退款失败：" + response.getSubMsg());
            }

        } catch (AlipayApiException e) {
            log.error("支付宝退款接口异常", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "退款处理失败");
        }
    }
}
