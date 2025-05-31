package com.fdt.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 员工评价表
 * @TableName staff_evaluation
 */
@TableName(value ="staff_evaluation")
@Data
public class StaffEvaluation implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

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
     * 关联的账单id
     */
    private Long billId;

    /**
     * 评价内容
     */
    private String evaluation;

    /**
     * 评价分数
     */
    private Integer evaluationScore;

    /**
     * 评价状态
     */
    private Integer evaluationSituation;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}