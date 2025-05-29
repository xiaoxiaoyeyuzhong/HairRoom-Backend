package com.fdt.management.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fdt.common.model.entity.Refund;
import org.apache.ibatis.annotations.Update;

/**
* @author 冯德田
* @description 针对表【refund(退款)】的数据库操作Mapper
* @createDate 2025-05-25 23:20:08
* @Entity generator.domain.Refund
*/
public interface RefundMapper extends BaseMapper<Refund> {

    @Update("update refund set auditSituation = #{auditSituation} " +
            "where id = #{id}")
    Boolean checkRefund(Refund refund);

}




