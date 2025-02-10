package com.fdt.portal.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.easysdk.factory.Factory;
import com.fdt.portal.config.AliPayConfig;
import com.fdt.portal.model.entity.AliPay;
import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/alipay")
@Transactional(rollbackFor = Exception.class)
@Log4j2
public class AliPayController {

    @Resource
    AliPayConfig aliPayConfig;

    private static final String GATEWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private static final String FORMAT = "JSON";
    private static final String CHARSET = "UTF-8";
    private static final String SIGN_TYPE = "RSA2";

    @GetMapping("/pay")
    public void pay(AliPay aliPay, HttpServletResponse httpServletResponse) throws Exception{

        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);

        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setBizContent("{" +
                "\"out_trade_no\":\"" + aliPay.getBillId() + "\","
                + "\"subject\":\"" + aliPay.getBillName() + "\","
                + "\"total_amount\":\"" + aliPay.getBillAmount() + "\","
                + "}");
        // 跳转到指定页面
        request.setReturnUrl("");

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
        if(request.getParameter("trade_status").equals("TRADE_SUCCESS")){
            log.info("===支付宝异步回调===");
            Map<String,String> params = new HashMap<>();
            Map<String,String[]> requestParams = request.getParameterMap();
            for(String name : requestParams.keySet()){
                params.put(name, request.getParameter(name));
            }
            // 支付宝签名验证
            if(Factory.Payment.Common().verifyNotify(params)) {
                log.info("交易名称: " + params.get("subject"));
                log.info("交易状态: " + params.get("trade_status"));
                log.info("支付宝交易凭证号: " + params.get("trade_no"));
                log.info("商户订单号: " + params.get("out_trade_no"));
                log.info("交易金额: " + params.get("total_amount"));
                log.info("买家在支付宝唯一id: " + params.get("buyer_id"));
                log.info("买家付款时间: " + params.get("gmt_payment"));
                log.info("买家付款金额: " + params.get("buyer_pay_amount"));

            }
        }
        return "success";
    }

}
