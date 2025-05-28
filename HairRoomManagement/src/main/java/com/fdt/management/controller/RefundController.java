package com.fdt.management.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fdt.common.api.BaseResponse;
import com.fdt.common.api.ErrorCode;
import com.fdt.common.api.ResultUtils;
import com.fdt.common.constant.UserConstant;
import com.fdt.common.model.dto.refund.RefundQueryRequest;
import com.fdt.common.model.dto.refund.RefundUpdateRequest;
import com.fdt.common.model.vo.RefundVO;
import com.fdt.management.annotation.AuthCheck;
import com.fdt.management.exception.BusinessException;
import com.fdt.management.service.RefundService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/refund")
@Slf4j
public class RefundController {

    @Resource
    private RefundService refundService;

    @PostMapping("check")
    @AuthCheck(mustRole = UserConstant.MANAGER_ROLE)
    public BaseResponse<Boolean> checkRefund(@RequestBody RefundUpdateRequest refundUpdateRequest) {
        if(refundUpdateRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean result = refundService.checkRefund(refundUpdateRequest);
        return ResultUtils.success(result);

    }

    @PostMapping("list")
    @AuthCheck(mustRole = UserConstant.MANAGER_ROLE)
    public BaseResponse<Page<RefundVO>> listRefundByPage(RefundQueryRequest refundQueryRequest, HttpServletRequest request) {
        if(refundQueryRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return refundService.listRefundByPage(refundQueryRequest);

    }
}
