package com.fdt.management.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fdt.common.model.entity.Customer;
import com.fdt.management.mapper.CustomerMapper;
import com.fdt.management.service.CustomerService;
import org.springframework.stereotype.Service;

/**
* @author 冯德田
* @description 针对表【customer(客户)】的数据库操作Service实现
* @createDate 2025-03-13 16:48:25
*/
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer>
    implements CustomerService {

    @Override
    public Long getCustomerIdByUserId(Long UserId) {
        Customer customer = this.getById(UserId);
        return customer.getId();
    }
}




