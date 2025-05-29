package com.fdt.portal.service;


import javax.servlet.http.HttpServletRequest;

public interface AliPayService {


    String payNotify(HttpServletRequest request) throws Exception;

    /**
     * 检查商户订单号是否合法
     * @param outTradeNo
     * @param deadline
     */
    String checkOutTradeNo(String outTradeNo, int deadline);
}
