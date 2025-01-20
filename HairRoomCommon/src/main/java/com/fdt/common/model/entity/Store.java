package com.fdt.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 门店
 * @TableName store
 */
@TableName(value ="store")
@Data
public class Store implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = -5068492416614008328L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 门店名称
     */
    private String storeName;

    /**
     * 门店地址
     */
    private String storeAddress;

    /**
     * 门店电话
     */
    private String storePhone;

    /**
     * 门店邮箱
     */
    private String storeEmail;

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


}