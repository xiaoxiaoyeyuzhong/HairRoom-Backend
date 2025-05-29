package com.fdt.portal.controller;

import com.fdt.common.api.BaseResponse;
import com.fdt.common.api.ErrorCode;
import com.fdt.common.api.ResultUtils;
import com.fdt.common.model.dto.refund.RefundAddRequest;
import com.fdt.common.model.vo.RefundVO;
import com.fdt.portal.exception.BusinessException;
import com.fdt.portal.service.RefundService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/refund")
public class RefundController {

    @Resource
    private RefundService refundService;

    // 添加退款申请
    @PostMapping("/add")
    public BaseResponse<Boolean> addRefund(@RequestBody RefundAddRequest refundAddRequest) {
        if(refundAddRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result =  refundService.addRefund(refundAddRequest);
        return ResultUtils.success(result);
    }
}
