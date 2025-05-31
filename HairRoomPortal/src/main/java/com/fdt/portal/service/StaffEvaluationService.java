package com.fdt.portal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fdt.common.model.dto.staffEvaluation.StaffEvaluationAddRequest;
import com.fdt.common.model.dto.staffEvaluation.StaffEvaluationQueryRequest;
import com.fdt.common.model.dto.staffEvaluation.StaffEvaluationUpdateRequest;
import com.fdt.common.model.entity.StaffEvaluation;
import com.fdt.common.model.vo.StaffEvaluationVO;

import java.util.List;

/**
* @author 冯德田
* @description 针对表【staff_evaluation(员工评价表)】的数据库操作Service
* @createDate 2025-05-31 10:08:18
*/
public interface StaffEvaluationService extends IService<StaffEvaluation> {

    /**
     * 添加员工评价
     * @param staffEvaluationAddRequest 员工评价
     * @return  添加结果
     */
    boolean addStaffEvaluation(StaffEvaluationAddRequest staffEvaluationAddRequest);

     /**
     * 获取员工评价列表
     * @param staffEvaluationQueryRequest 员工评价查询参数
     * @return 员工评价列表
     */
     List<StaffEvaluationVO> listStaffEvaluation(StaffEvaluationQueryRequest staffEvaluationQueryRequest);

    /**
     * 获取staffEvaluationVO
     */
    List<StaffEvaluationVO> getStaffEvaluationVO(List<StaffEvaluation> staffEvaluationList);

     /**
     * 修改员工评价
     * @param staffEvaluationUpdateRequest 员工评价
     * @return 修改结果
     */
    Boolean updateStaffEvaluation(StaffEvaluationUpdateRequest staffEvaluationUpdateRequest);
}
