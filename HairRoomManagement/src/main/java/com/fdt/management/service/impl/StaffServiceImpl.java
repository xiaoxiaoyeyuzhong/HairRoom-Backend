package com.fdt.management.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fdt.management.model.entity.Staff;
import com.fdt.management.service.StaffService;
import com.fdt.management.mapper.StaffMapper;
import org.springframework.stereotype.Service;

/**
* @author 冯德田
* @description 针对表【staff(员工)】的数据库操作Service实现
* @createDate 2025-01-15 10:31:30
*/
@Service
public class StaffServiceImpl extends ServiceImpl<StaffMapper, Staff>
    implements StaffService{

}




