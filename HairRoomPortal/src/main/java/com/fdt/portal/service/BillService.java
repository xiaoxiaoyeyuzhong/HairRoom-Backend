package com.fdt.portal.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fdt.common.model.dto.bill.BillAddRequest;
import com.fdt.common.model.dto.bill.BillQueryRequest;
import com.fdt.common.model.entity.Bill;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author 冯德田
* @description 针对表【bill】的数据库操作Service
* @createDate 2025-01-10 15:51:27
*/
public interface BillService extends IService<Bill> {

    /**
     *
     * @param billQueryRequest
     * @return
     */
    QueryWrapper<Bill> getQueryWrapper(BillQueryRequest billQueryRequest);

    Bill getBillById(int id, HttpServletRequest request);

    /**
     * 添加账单
     * @param billAddRequest
     * @return Long 账单id
     */
    Long addBill(BillAddRequest billAddRequest);
}
