package com.fdt.portal.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.easysdk.factory.Factory;
import com.fdt.common.api.BaseResponse;
import com.fdt.common.api.ErrorCode;
import com.fdt.common.api.ResultUtils;
import com.fdt.common.model.dto.pay.AliPay;
import com.fdt.common.model.dto.pay.AliPayRefund;
import com.fdt.common.model.entity.Bill;
import com.fdt.common.model.entity.Customer;
import com.fdt.common.model.entity.Staff;
import com.fdt.portal.config.AliPayConfig;
import com.fdt.portal.exception.BusinessException;
import com.fdt.portal.service.AliPayService;
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
    AliPayService aliPayService;

    private static final String GATEWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private static final String FORMAT = "JSON";
    private static final String CHARSET = "UTF-8";
    private static final String SIGN_TYPE = "RSA2";

    @GetMapping("/pay")
    public void pay(AliPay aliPay, HttpServletResponse httpServletResponse) throws Exception{

        log.info("===支付宝支付接口调用开始===");

        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);

        // 电脑端
        // AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();

        // 手机端
        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        // 设置notifyUrl和returnUrl，即回调的地址和返回的地址
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        request.setReturnUrl(aliPayConfig.getReturnUrl());

        // 关于product_code，PC：FAST_INSTANT_TRADE_PAY 跳转到支付宝或网页：QUICK_WAP_WAY
        String productCode = "QUICK_WAP_WAY";
        request.setBizContent("{" +
                "\"out_trade_no\":\"" + aliPay.getOutTradeNo() + "\","
                + "\"subject\":\"" + aliPay.getBillName() + "\","
                + "\"total_amount\":\"" + aliPay.getBillAmount() + "\","
                + "\"product_code\":\"" + productCode + "\""
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
    public BaseResponse<String> payNotify(HttpServletRequest request) throws Exception{
        log.info("===支付宝异步回调===");
        if("TRADE_SUCCESS".equals(request.getParameter("trade_status"))){
            String result = aliPayService.payNotify(request);
            return ResultUtils.success(result);
        }else{
            log.info("订单支付失败");
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "订单支付失败");
        }
    }

}
