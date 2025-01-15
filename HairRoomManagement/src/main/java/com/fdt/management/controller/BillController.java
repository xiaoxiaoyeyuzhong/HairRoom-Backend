package com.fdt.management.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.fdt.management.annotation.AuthCheck;
import com.fdt.management.common.BaseResponse;
import com.fdt.management.common.DeleteRequest;
import com.fdt.management.common.ErrorCode;
import com.fdt.management.common.ResultUtils;
import com.fdt.management.constant.UserConstant;
import com.fdt.management.exception.BusinessException;
import com.fdt.management.model.dto.bill.BillAddRequest;
import com.fdt.management.model.dto.bill.BillQueryRequest;
import com.fdt.management.model.dto.bill.BillUpdateRequest;
import com.fdt.management.model.entity.Bill;
import com.fdt.management.model.vo.BillVO;
import com.fdt.management.service.BillService;
import com.fdt.management.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/Bill")
public class BillController {

    @Resource
    private BillService billService;

    /**
     * 添加账单
     * @param billAddRequest
     * @return 账单id
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.MANAGER_ROLE)
    public BaseResponse<Long> addBill(@RequestBody BillAddRequest billAddRequest) {
        if (billAddRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long billId = billService.addBill(billAddRequest);

        return ResultUtils.success(billId);

    }

    /**
     * 删除账单
     * @param deleteRequest
     * @return 删除是否成功
     */

    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.MANAGER_ROLE)
    public BaseResponse<Boolean> deleteBill(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = billService.removeById(deleteRequest.getId());
        if (!result){
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(result);
    }

    /**
     * 修改账单
     * @param billUpdateRequest
     * @return 修改是否成功
     */

    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.MANAGER_ROLE)
    public BaseResponse<Boolean> updateBill(@RequestBody BillUpdateRequest billUpdateRequest) {
        if (billUpdateRequest == null || billUpdateRequest.getId() <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Bill bill = new Bill();
        BeanUtils.copyProperties(billUpdateRequest, bill);
        Boolean result = billService.updateById(bill);
        if (!result){
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(result);

    }

    /**
     * 根据id查询账单
     * @param id
     * @return
     */
    @PostMapping("/get")
    @AuthCheck(anyRole = {UserConstant.STAFF_ROLE,UserConstant.MANAGER_ROLE})
    public BaseResponse<Bill> getBillById(int id, HttpServletRequest request) {
        if (id <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Bill bill = billService.getBillById(id,request);

        return ResultUtils.success(bill);
    }

    @PostMapping("/list")
    @AuthCheck(anyRole = {UserConstant.STAFF_ROLE,UserConstant.MANAGER_ROLE})
    public BaseResponse<Page<BillVO>> listBillByPage(@RequestBody BillQueryRequest billQueryRequest, HttpServletRequest request) {

        long current = 1;
        long size = 10;
        Bill billQuery = new Bill();
        if (billQueryRequest != null) {
            BeanUtils.copyProperties(billQueryRequest, billQuery);
            current = billQueryRequest.getCurrent();
            size = billQueryRequest.getPageSize();
        }
        QueryWrapper<Bill> queryWrapper = new QueryWrapper<>(billQuery);
        Page<Bill> billPage = billService.page(new Page<>(current, size), queryWrapper);
        Page<BillVO> billVOPage = new PageDTO<>(billPage.getCurrent(), billPage.getSize(), billPage.getTotal());
        List<BillVO> billVOList = billPage.getRecords().stream().map(bill -> {
            BillVO billVO = new BillVO();
            BeanUtils.copyProperties(bill, billVO);
            return billVO;
        }).collect(Collectors.toList());
        billVOPage.setRecords(billVOList);
        return ResultUtils.success(billVOPage);
    }

}
