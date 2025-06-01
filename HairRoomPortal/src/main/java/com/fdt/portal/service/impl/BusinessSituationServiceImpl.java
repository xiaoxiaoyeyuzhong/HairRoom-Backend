package com.fdt.portal.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fdt.common.model.entity.BusinessSituation;
import com.fdt.portal.service.BusinessSituationService;
import com.fdt.portal.mapper.BusinessSituationMapper;
import org.springframework.stereotype.Service;

/**
* @author 冯德田
* @description 针对表【business_situation(营业情况)】的数据库操作Service实现
* @createDate 2025-06-01 18:43:51
*/
@Service
public class BusinessSituationServiceImpl extends ServiceImpl<BusinessSituationMapper, BusinessSituation>
    implements BusinessSituationService{

}




