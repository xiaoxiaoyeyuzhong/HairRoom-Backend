package com.fdt.management.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fdt.common.model.entity.BusinessSituation;

import com.fdt.management.mapper.BusinessSituationMapper;
import com.fdt.management.service.BusinessSituationService;
import org.springframework.stereotype.Service;

/**
* @author 冯德田
* @description 针对表【business_situation(营业情况)】的数据库操作Service实现
* @createDate 2025-05-31 21:14:09
*/
@Service
public class BusinessSituationServiceImpl extends ServiceImpl<BusinessSituationMapper, BusinessSituation>
    implements BusinessSituationService {

}




