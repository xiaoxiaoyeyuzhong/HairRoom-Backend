package com.fdt.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fdt.common.model.entity.Store;
import com.fdt.portal.service.StoreService;
import com.fdt.portal.mapper.StoreMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 冯德田
* @description 针对表【store(门店)】的数据库操作Service实现
* @createDate 2025-01-15 11:32:15
*/

@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store>
    implements StoreService{

    @Resource
    private  StoreMapper storeMapper;

    @Override
    public int getStaffCount(Long storeId) {
        QueryWrapper<Store> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",storeId);
        return storeMapper.selectList(queryWrapper).size();

    }
}




