package com.fdt.portal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fdt.common.model.dto.refund.RefundAddRequest;
import com.fdt.common.model.entity.Refund;

/**
* @author 冯德田
* @description 针对表【refund(退款)】的数据库操作Service
* @createDate 2025-05-25 23:20:08
*/
public interface RefundService extends IService<Refund> {

    /**
     * 添加退款申请
     * @param refundAddRequest
     * @return
     */
    boolean addRefund(RefundAddRequest refundAddRequest);

    /**
     * 根据交易号和商户订单号查询退款记录
     * @param tradeNo
     * @param outTradeNo
     * @return
     */
    Refund getByOutAndTradeNo(String tradeNo, String outTradeNo);
}
