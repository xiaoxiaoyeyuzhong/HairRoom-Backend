package com.fdt.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fdt.common.model.entity.Customer;
import com.fdt.portal.service.CustomerService;
import com.fdt.portal.mapper.CustomerMapper;
import org.springframework.stereotype.Service;

/**
* @author 冯德田
* @description 针对表【customer(客户)】的数据库操作Service实现
* @createDate 2025-03-13 16:48:25
*/
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer>
    implements CustomerService{

    @Override
    public Customer getCustomerByUserId(Long UserId) {
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", UserId);
        return this.getOne(queryWrapper);
    }
}




