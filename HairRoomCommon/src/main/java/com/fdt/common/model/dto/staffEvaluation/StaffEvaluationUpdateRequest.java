package com.fdt.common.model.dto.staffEvaluation;

import lombok.Data;

@Data
public class StaffEvaluationUpdateRequest {

    /**
     * 评价id
     */
    private Long id;

    /**
     * 评价内容
     */
    private String evaluation;

    /**
     * 评价分数
     */
    private Integer evaluationScore;
}

