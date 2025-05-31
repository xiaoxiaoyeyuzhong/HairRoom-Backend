package com.fdt.common.model.dto.staffEvaluation;

import lombok.Data;

@Data
public class StaffEvaluationQueryRequest {


    private Long customerUserId;

    private Integer evaluationSituation;
}
