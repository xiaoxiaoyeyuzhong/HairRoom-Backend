package com.fdt.management.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fdt.common.api.ErrorCode;
import com.fdt.management.exception.BusinessException;
import com.fdt.common.model.dto.staff.StaffAddRequest;
import com.fdt.common.model.entity.Staff;
import com.fdt.management.service.StaffService;
import com.fdt.management.mapper.StaffMapper;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
* @author 冯德田
* @description 针对表【staff(员工)】的数据库操作Service实现
* @createDate 2025-01-15 10:31:30
*/
@Service
public class StaffServiceImpl extends ServiceImpl<StaffMapper, Staff>
    implements StaffService{

    // todo 注意外键，需要检查门店是否存在,检查店长是否已经存在
    // todo 员工添加时暂不填写用户id。
    @Override
    public Long addStaff(StaffAddRequest staffAddRequest) {
        if(staffAddRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if(staffAddRequest.getStoreId() == null || staffAddRequest.getStoreId() <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请选择门店");
        }
        // 查询门店是否存在


        // 查询门店店长是否存在

        return null;
    }

    @Override
    public Staff getStaffById(int id, HttpServletRequest request) {
        return null;
    }
}




