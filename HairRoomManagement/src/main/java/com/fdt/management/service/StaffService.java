package com.fdt.management.service;

import com.fdt.common.model.dto.staff.StaffAddRequest;
import com.fdt.common.model.entity.Staff;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author 冯德田
* @description 针对表【staff(员工)】的数据库操作Service
* @createDate 2025-01-15 10:31:30
*/
public interface StaffService extends IService<Staff> {

    /**
     * 添加员工
     * @param staffAddRequest
     * @return 添加的员工id
     */
    Long addStaff(StaffAddRequest staffAddRequest);

    Staff getStaffById(int id, HttpServletRequest request);
}
