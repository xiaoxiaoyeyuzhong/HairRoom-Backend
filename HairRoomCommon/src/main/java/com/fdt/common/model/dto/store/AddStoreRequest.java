package com.fdt.common.model.dto.store;

import lombok.Data;

@Data
public class AddStoreRequest {

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
}
