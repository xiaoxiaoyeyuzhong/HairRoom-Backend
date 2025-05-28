package com.fdt.portal.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.easysdk.factory.Factory;
import com.fdt.common.api.ErrorCode;
import com.fdt.common.model.dto.pay.AliPay;
import com.fdt.common.model.dto.pay.AliPayRefund;
import com.fdt.common.model.entity.Bill;
import com.fdt.common.model.entity.Customer;
import com.fdt.common.model.entity.Staff;
import com.fdt.portal.config.AliPayConfig;
import com.fdt.portal.exception.BusinessException;
import com.fdt.portal.service.BillService;
import com.fdt.portal.service.CustomerService;
import com.fdt.portal.service.StaffService;
import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/alipay")
@Transactional(rollbackFor = Exception.class)
@Log4j2
public class AliPayController {

    @Resource
    AliPayConfig aliPayConfig;

    @Resource
    CustomerService customerService;

    @Resource
    StaffService staffService;

    @Resource
    BillService billService;

    private static final String GATEWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private static final String FORMAT = "JSON";
    private static final String CHARSET = "UTF-8";
    private static final String SIGN_TYPE = "RSA2";

    @GetMapping("/pay")
    public void pay(AliPay aliPay, HttpServletResponse httpServletResponse) throws Exception{

        log.info("===支付宝支付接口调用开始===");

        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);

        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        // 设置notifyUrl和returnUrl，即回调的地址和返回的地址
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        request.setReturnUrl(aliPayConfig.getReturnUrl());

        // 关于product_code，PC：FAST_INSTANT_TRADE_PAY 手机浏览器：MOBILE_WAP_PAY
        request.setBizContent("{" +
                "\"out_trade_no\":\"" + aliPay.getOutTradeNo() + "\","
                + "\"subject\":\"" + aliPay.getBillName() + "\","
                + "\"total_amount\":\"" + aliPay.getBillAmount() + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\""
                + "}");
        // 跳转到指定页面
//        request.setReturnUrl("");

        String form = "";
        try{
            // 调用SDK生成表单
            form = alipayClient.pageExecute(request).getBody();
        } catch (AlipayApiException e){
            e.printStackTrace();
        }
        httpServletResponse.setContentType("text/html;charset=" + CHARSET);
        // 将表单html输出到浏览器页面中
        httpServletResponse.getWriter().write(form);
        httpServletResponse.getWriter().flush();
        httpServletResponse.getWriter().close();
    }

    // 注意接口类型，是post
    @PostMapping("/notify")
    public String payNotify(HttpServletRequest request) throws Exception{
        log.info("===支付宝异步回调===");
        if("TRADE_SUCCESS".equals(request.getParameter("trade_status"))){

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

                // 按照 _ 切分字符串
                String[] parts = outTradeNo.split("_");
                Long customerUserId = Long.valueOf(parts[0]);
                Long staffId = Long.valueOf(parts[1]);
                Long timeStamp = Long.valueOf(parts[2]);

                // 通过客户的用户id拿到客户id，验证客户是否存在
                Customer customer = customerService.getCustomerByUserId(customerUserId);
                if (customer == null){
                    throw new BusinessException(ErrorCode.PARAMS_ERROR, "客户不存在");
                }

                // 验证员工是否存在
                Staff staff = staffService.getById(staffId);
                if (staff == null){
                    throw new BusinessException(ErrorCode.PARAMS_ERROR, "员工不存在");
                }

                // 验证时间戳是否过期
                if (System.currentTimeMillis() - timeStamp > 1000 * 60 * 60 * 24){
                    throw new BusinessException(ErrorCode.PARAMS_ERROR, "时间戳过期");
                }

                // todo 重要！记录订单的同时记录预约信息

                Bill bill = new Bill();
                bill.setBillName(billName);
                bill.setBillAmount(billAmount);
                bill.setTradeNo(tradeNo);
                bill.setOutTradeNo(outTradeNo);
                bill.setCustomerId(customer.getId());
                bill.setStaffId(staffId);
                // todo 获取账单类型，替换固定的类型,可以考虑放入billOutId中
                bill.setBillType("洗剪吹");
                bill.setPaySituation(1);

                billService.save(bill);
                log.info("支付宝支付回调完成");

            }else{
                log.info("支付宝签名认证失败");
            }
        }else{
            log.info("订单支付失败");
        }
        return "success";
    }

}
