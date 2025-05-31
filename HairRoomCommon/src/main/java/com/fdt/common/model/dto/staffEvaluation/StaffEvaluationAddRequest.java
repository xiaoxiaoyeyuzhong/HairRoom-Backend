package com.fdt.common.model.dto.staffEvaluation;

import lombok.Data;

@Data
public class StaffEvaluationAddRequest {


    /**
     * 客户id
     */
    private Long customerId;

    /**
     * 员工id
     */
    private Long staffId;

    /**
     * 关联的预约id
     */
    private Long appointmentId;

    /**
     * 评价内容
     */
    private String evaluation;

    /**
     * 评价分数
     */
    private Integer evaluationScore;

}
