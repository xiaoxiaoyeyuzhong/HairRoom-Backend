package com.fdt.common.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 排班
 * @TableName schedule
 */
@Data
@TableName("schedule")
public class Schedule implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long staffId;
    private Long storeId;
    private Integer weekDay;
    private Integer timeInterval;
    private Integer haveAppointedSlots;
    private Integer appointSlots;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableLogic
    private Integer isDelete;  // 逻辑删除字段

    private static final long serialVersionUID = 1L;
}