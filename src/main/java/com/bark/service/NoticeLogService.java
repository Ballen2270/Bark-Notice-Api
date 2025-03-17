package com.bark.service;

import com.bark.domain.NoticeLog;
import java.util.List;
import java.util.Map;

/**
 * 通知日志服务接口
 */
public interface NoticeLogService {
    
    /**
     * 根据条件查询通知日志
     * @param deviceKey 设备key
     * @param group 分组
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @param status 状态
     * @return 日志列表
     */
    List<NoticeLog> queryByCondition(String deviceKey, String group, 
                                    String beginTime, String endTime,
                                    String status);
    
    /**
     * 按日期统计通知数量
     * @param dateType 日期类型：year,month,day
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return 统计数据
     */
    List<Map<String, Object>> countByDate(String dateType, String beginTime, String endTime);
    
    /**
     * 按状态统计
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return 统计数据
     */
    List<Map<String, Object>> countByStatus(String beginTime, String endTime);
    
    /**
     * 按分组统计
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return 统计数据
     */
    List<Map<String, Object>> countByGroup(String beginTime, String endTime);
    
    /**
     * 按设备统计
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return 统计数据
     */
    List<Map<String, Object>> countByDevice(String beginTime, String endTime);
} 