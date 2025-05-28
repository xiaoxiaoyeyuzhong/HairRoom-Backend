package com.fdt.management.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fdt.common.api.BaseResponse;
import com.fdt.common.model.dto.refund.RefundQueryRequest;
import com.fdt.common.model.dto.refund.RefundUpdateRequest;
import com.fdt.common.model.entity.Refund;
import com.fdt.common.model.vo.RefundVO;

/**
* @author 冯德田
* @description 针对表【refund(退款)】的数据库操作Service
* @createDate 2025-05-25 23:20:08
*/
public interface RefundService extends IService<Refund> {

    /**
     * 审核退款信息
     * @param refundUpdateRequest 更新请求
     */
    Boolean checkRefund(RefundUpdateRequest refundUpdateRequest);

    /**
     * 分页获取退款信息列表
     * @param refundQueryRequest 查询请求
     * @return
     */
    BaseResponse<Page<RefundVO>> listRefundByPage(RefundQueryRequest refundQueryRequest);
}
