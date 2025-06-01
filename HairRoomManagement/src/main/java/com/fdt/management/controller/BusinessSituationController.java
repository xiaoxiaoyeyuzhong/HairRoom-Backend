package com.fdt.management.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.fdt.common.api.BaseResponse;
import com.fdt.common.api.ResultUtils;
import com.fdt.common.model.dto.businessSituation.BusinessSituationQueryRequest;
import com.fdt.common.model.dto.user.UserQueryRequest;
import com.fdt.common.model.entity.BusinessSituation;
import com.fdt.common.model.entity.User;
import com.fdt.common.model.vo.BusinessSituationVO;
import com.fdt.common.model.vo.UserVO;
import com.fdt.management.service.BusinessSituationService;
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
@RequestMapping("/businessSituation")
public class BusinessSituationController {

    @Resource
    private BusinessSituationService businessSituationService;

    @PostMapping("/list")
    public BaseResponse<List<BusinessSituationVO>> listBusinessSituation(@RequestBody BusinessSituationQueryRequest businessSituationQueryRequest, HttpServletRequest request) {
        BusinessSituation  businessSituation = new BusinessSituation();
        if (businessSituationQueryRequest != null) {
            BeanUtils.copyProperties(businessSituationQueryRequest, businessSituation);
        }
        QueryWrapper<BusinessSituation> queryWrapper = new QueryWrapper<>(businessSituation);
        List<BusinessSituation> businessSituationList = businessSituationService.list(queryWrapper);
        List<BusinessSituationVO> businessSituationVOList = businessSituationList.stream().map(business -> {
            BusinessSituationVO businessSituationVO = new BusinessSituationVO();
            BeanUtils.copyProperties(business, businessSituationVO);
            return businessSituationVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(businessSituationVOList);
    }

    @PostMapping("/list/page")
    public BaseResponse<Page<BusinessSituationVO>> listBusinessSituationByPage(@RequestBody BusinessSituationQueryRequest businessSituationQueryRequest, HttpServletRequest request) {
        long current = 1;
        long size = 10;
        BusinessSituation businessSituationQuery =  new BusinessSituation();
        if (businessSituationQueryRequest != null) {
            BeanUtils.copyProperties(businessSituationQueryRequest, businessSituationQuery);
            current = businessSituationQueryRequest.getCurrent();
            size = businessSituationQueryRequest.getPageSize();
        }
        QueryWrapper<BusinessSituation> queryWrapper = new QueryWrapper<>(businessSituationQuery);
        Page<BusinessSituation> businessSituationPage = businessSituationService.page(new Page<>(current, size), queryWrapper);
        Page<BusinessSituationVO> businessSituationVOPage = new PageDTO<>(businessSituationPage.getCurrent(), businessSituationPage.getSize(), businessSituationPage.getTotal());
        List<BusinessSituationVO> businessSituationVOList = businessSituationPage.getRecords().stream().map(businessSituation -> {
            BusinessSituationVO businessSituationVO = new BusinessSituationVO();
            BeanUtils.copyProperties(businessSituation, businessSituationVO);
            return businessSituationVO;
        }).collect(Collectors.toList());
        businessSituationVOPage.setRecords(businessSituationVOList);
        return ResultUtils.success(businessSituationVOPage);
    }

}
