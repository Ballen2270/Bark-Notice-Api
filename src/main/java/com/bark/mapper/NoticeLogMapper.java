package com.bark.mapper;

import com.bark.domain.NoticeLog;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface NoticeLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(NoticeLog record);

    NoticeLog selectByPrimaryKey(Integer id);

    List<NoticeLog> selectAll();

    int updateByPrimaryKey(NoticeLog record);
    
    // 按条件查询通知日志
    List<NoticeLog> selectByCondition(@Param("deviceKey") String deviceKey, 
                                     @Param("group") String group,
                                     @Param("beginTime") String beginTime,
                                     @Param("endTime") String endTime,
                                     @Param("status") String status);
    
    // 按日期统计通知数量
    List<Map<String, Object>> countByDate(@Param("dateType") String dateType, 
                                         @Param("beginTime") String beginTime,
                                         @Param("endTime") String endTime);
    
    // 按状态统计
    List<Map<String, Object>> countByStatus(@Param("beginTime") String beginTime,
                                          @Param("endTime") String endTime);
    
    // 按分组统计
    List<Map<String, Object>> countByGroup(@Param("beginTime") String beginTime,
                                         @Param("endTime") String endTime);
                                         
    // 按设备统计
    List<Map<String, Object>> countByDevice(@Param("beginTime") String beginTime,
                                          @Param("endTime") String endTime);
}