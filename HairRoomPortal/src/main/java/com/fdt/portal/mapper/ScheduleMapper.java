package com.fdt.portal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fdt.common.model.entity.Schedule;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;


/**
* @author 冯德田
* @description 针对表【schedule(排班)】的数据库操作Mapper
* @createDate 2025-01-23 16:09:38
*/
public interface ScheduleMapper extends BaseMapper<Schedule> {

    @Update("UPDATE schedule SET haveAppointedSlots = #{haveAppointedSlots}" +
            ",appointSlots = #{appointSlots} WHERE id = #{id}")
    boolean updateAppointmentInfo(@Param("id") Long id,
                                  @Param("haveAppointedSlots") Integer haveAppointedSlots,
                                  @Param("appointSlots") Integer appointSlots);
}




