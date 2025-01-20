package com.fdt.portal.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.fdt.portal.annotation.AuthCheck;
import com.fdt.common.api.BaseResponse;
import com.fdt.common.api.DeleteRequest;
import com.fdt.common.api.ErrorCode;
import com.fdt.common.api.ResultUtils;
import com.fdt.common.constant.UserConstant;
import com.fdt.portal.exception.BusinessException;
import com.fdt.common.model.dto.staff.StaffAddRequest;
import com.fdt.common.model.dto.staff.StaffQueryRequest;
import com.fdt.common.model.dto.staff.StaffUpdateRequest;
import com.fdt.common.model.entity.Staff;
import com.fdt.common.model.vo.StaffVO;
import com.fdt.portal.service.StaffService;
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
@RequestMapping("/staff")
public class StaffController {

    @Resource
    private StaffService staffService;

    /**
     * 添加账单
     * @param staffAddRequest
     * @return 账单id
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.MANAGER_ROLE)
    public BaseResponse<Long> addStaff(@RequestBody StaffAddRequest staffAddRequest) {
        if (staffAddRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long staffId = staffService.addStaff(staffAddRequest);

        return ResultUtils.success(staffId);

    }

    /**
     * 删除账单
     * @param deleteRequest
     * @return 删除是否成功
     */

    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.MANAGER_ROLE)
    public BaseResponse<Boolean> deleteStaff(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = staffService.removeById(deleteRequest.getId());
        if (!result){
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(result);
    }

    /**
     * 修改账单
     * @param staffUpdateRequest
     * @return 修改是否成功
     */

    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.MANAGER_ROLE)
    public BaseResponse<Boolean> updateStaff(@RequestBody StaffUpdateRequest staffUpdateRequest) {
        if (staffUpdateRequest == null || staffUpdateRequest.getId() <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Staff staff = new Staff();
        BeanUtils.copyProperties(staffUpdateRequest, staff);
        Boolean result = staffService.updateById(staff);
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
    public BaseResponse<Staff> getStaffById(int id, HttpServletRequest request) {
        if (id <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Staff staff = staffService.getStaffById(id,request);

        return ResultUtils.success(staff);
    }

    @PostMapping("/list")
    @AuthCheck(anyRole = {UserConstant.STAFF_ROLE,UserConstant.MANAGER_ROLE})
    public BaseResponse<Page<StaffVO>> listStaffByPage(@RequestBody StaffQueryRequest staffQueryRequest, HttpServletRequest request) {

        long current = 1;
        long size = 10;
        Staff staffQuery = new Staff();
        if (staffQueryRequest != null) {
            BeanUtils.copyProperties(staffQueryRequest, staffQuery);
            current = staffQueryRequest.getCurrent();
            size = staffQueryRequest.getPageSize();
        }
        QueryWrapper<Staff> queryWrapper = new QueryWrapper<>(staffQuery);
        Page<Staff> staffPage = staffService.page(new Page<>(current, size), queryWrapper);
        Page<StaffVO> staffVOPage = new PageDTO<>(staffPage.getCurrent(), staffPage.getSize(), staffPage.getTotal());
        List<StaffVO> staffVOList = staffPage.getRecords().stream().map(staff -> {
            StaffVO staffVO = new StaffVO();
            BeanUtils.copyProperties(staff, staffVO);
            return staffVO;
        }).collect(Collectors.toList());
        staffVOPage.setRecords(staffVOList);
        return ResultUtils.success(staffVOPage);
    }
}
