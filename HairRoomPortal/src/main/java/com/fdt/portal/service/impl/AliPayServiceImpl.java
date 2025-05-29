package com.fdt.portal.service.impl;

import com.alipay.easysdk.factory.Factory;
import com.fdt.common.api.ErrorCode;
import com.fdt.common.constant.BillConstant;
import com.fdt.common.model.entity.Bill;
import com.fdt.common.model.entity.Customer;
import com.fdt.common.model.entity.Staff;
import com.fdt.portal.exception.BusinessException;
import com.fdt.portal.service.AliPayService;
import com.fdt.portal.service.BillService;
import com.fdt.portal.service.CustomerService;
import com.fdt.portal.service.StaffService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
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
    BillService billService;

    @Override
    public String payNotify(HttpServletRequest request) throws Exception {
        Map<String,String> params = new HashMap<>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for(String name : requestParams.keySet()){
            params.put(name, request.getParameter(name));
        }
        // 支付宝签名验证
        if(Factory.Payment.Common().verifyNotify(params)) {

            String billName = params.get("subject");
            BigDecimal billAmount = new BigDecimal(params.get("buyer_pay_amount"));
            String tradeNo = params.get("trade_no");
            String outTradeNo = params.get("out_trade_no");

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
            String result = this.checkOutTradeNo(outTradeNo,deadline);

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
            bill.setPaySituation(BillConstant.BILL_PAY_STATUS_SUCCESS);

            billService.updateById(bill);
            log.info("支付宝支付回调完成");

        }else{
            log.info("支付宝签名认证失败");
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
        if (customer == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "客户不存在");
        }
        // 验证员工是否存在
        Staff staff = staffService.getById(staffId);
        if (staff == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "员工不存在");
        }

        // 验证账单是否存在
        Bill bill = billService.getById(billId);
        if (bill == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账单不存在");
        }

        // 验证时间戳是否过期
        if (System.currentTimeMillis() - timeStamp > deadline){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "时间戳过期");
        }
        return customer.getId() + "_" + staffId + "_" + billId;
    }
}
