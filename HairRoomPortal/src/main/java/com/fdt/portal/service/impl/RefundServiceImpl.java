package com.fdt.portal.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fdt.common.model.entity.Refund;

import com.fdt.portal.mapper.RefundMapper;
import org.springframework.stereotype.Service;

/**
* @author 冯德田
* @description 针对表【refund(退款)】的数据库操作Service实现
* @createDate 2025-05-25 23:20:08
*/
@Service
public class RefundServiceImpl extends ServiceImpl<RefundMapper, Refund>
    implements IService<Refund> {

}




