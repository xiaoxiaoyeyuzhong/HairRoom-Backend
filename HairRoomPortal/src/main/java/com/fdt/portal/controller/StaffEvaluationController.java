package com.fdt.portal.controller;

import com.fdt.common.api.BaseResponse;
import com.fdt.common.api.ErrorCode;
import com.fdt.common.api.ResultUtils;
import com.fdt.common.model.dto.staffEvaluation.StaffEvaluationAddRequest;
import com.fdt.common.model.dto.staffEvaluation.StaffEvaluationQueryRequest;
import com.fdt.common.model.dto.staffEvaluation.StaffEvaluationUpdateRequest;
import com.fdt.common.model.entity.StaffEvaluation;
import com.fdt.common.model.vo.StaffEvaluationVO;
import com.fdt.portal.exception.BusinessException;
import com.fdt.portal.service.StaffEvaluationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/staffEvaluation")
public class StaffEvaluationController {

    @Resource
    private StaffEvaluationService staffEvaluationService;

     @PostMapping("/list")
     public BaseResponse<List<StaffEvaluationVO>> listStaffEvaluation(@RequestBody StaffEvaluationQueryRequest staffEvaluationQueryRequest){
         if(staffEvaluationQueryRequest == null){
             throw new BusinessException(ErrorCode.PARAMS_ERROR);
         }
         return ResultUtils.success(staffEvaluationService.listStaffEvaluation(staffEvaluationQueryRequest));
     }

     @PostMapping("/update")
    public BaseResponse<Boolean> updateStaffEvaluation(@RequestBody StaffEvaluationUpdateRequest staffEvaluationUpdateRequest){
          if(staffEvaluationUpdateRequest == null){
             throw new BusinessException(ErrorCode.PARAMS_ERROR);
         }
         Boolean result = staffEvaluationService.updateStaffEvaluation(staffEvaluationUpdateRequest);
         return ResultUtils.success(result);
     }
}
