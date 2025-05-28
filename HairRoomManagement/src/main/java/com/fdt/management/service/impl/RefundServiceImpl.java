package com.fdt.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fdt.common.api.BaseResponse;
import com.fdt.common.api.ErrorCode;
import com.fdt.common.api.ResultUtils;
import com.fdt.common.model.dto.refund.RefundQueryRequest;
import com.fdt.common.model.dto.refund.RefundUpdateRequest;
import com.fdt.common.model.entity.Bill;
import com.fdt.common.model.entity.Refund;
import com.fdt.common.model.vo.BillVO;
import com.fdt.common.model.vo.RefundVO;
import com.fdt.management.exception.BusinessException;
import com.fdt.management.mapper.RefundMapper;
import com.fdt.management.service.RefundService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author 冯德田
* @description 针对表【refund(退款)】的数据库操作Service实现
* @createDate 2025-05-25 23:20:08
*/
@Service
public class RefundServiceImpl extends ServiceImpl<RefundMapper, Refund>
    implements RefundService {

    @Resource
    private RefundMapper refundMapper;

    @Override
    public Boolean checkRefund(RefundUpdateRequest refundUpdateRequest) {
        if(refundUpdateRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Refund refund = this.getById(refundUpdateRequest.getId());

        // 当退款状态不是已通过时，才允许修改审核状态
        if(refund.getRefundSituation() == 1){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "退款状态已通过，不能修改审核状态");
        }

        // 设置审核状态
        refund.setAuditSituation(refundUpdateRequest.getAuditSituation());
        // 调用编写的mapper方法审核退款请求
        return refundMapper.checkRefund(refund);


    }

    @Override
    public BaseResponse<Page<RefundVO>> listRefundByPage(RefundQueryRequest refundQueryRequest) {

        long current = 1;
        long size = 10;
        Refund refundQuery = new Refund();
        if (refundQueryRequest != null) {
            BeanUtils.copyProperties(refundQueryRequest, refundQuery);
            current = refundQueryRequest.getCurrent();
            size = refundQueryRequest.getPageSize();
        }
        QueryWrapper<Refund> queryWrapper = new QueryWrapper<>(refundQuery);
        Page<Refund> refundPage = this.page(new Page<>(current, size), queryWrapper);
        Page<RefundVO> refundVOPage = new PageDTO<>(refundPage.getCurrent(), refundPage.getSize(), refundPage.getTotal());
        List<RefundVO> refundVOList = refundPage.getRecords().stream().map(refund -> {
            RefundVO refundVO = new RefundVO();
            BeanUtils.copyProperties(refund, refundVO);
            return refundVO;
        }).collect(Collectors.toList());
        refundVOPage.setRecords(refundVOList);
        return ResultUtils.success(refundVOPage);
    }
}




