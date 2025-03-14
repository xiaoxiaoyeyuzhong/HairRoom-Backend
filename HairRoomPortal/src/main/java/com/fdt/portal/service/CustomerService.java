package com.fdt.portal.service;

import com.fdt.common.model.entity.Customer;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 冯德田
* @description 针对表【customer(客户)】的数据库操作Service
* @createDate 2025-03-13 16:48:25
*/
public interface CustomerService extends IService<Customer> {

    /**
     * 通过用户id获取客户id
     * @param UserId 用户id
     * @return 客户id
     */
    Long getCustomerIdByUserId(Long UserId);

}
