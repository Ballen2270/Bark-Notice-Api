package com.bark.service.impl;

import com.bark.domain.NoticeLog;
import com.bark.mapper.NoticeLogMapper;
import com.bark.service.NoticeLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 通知日志服务实现类
 */
@Service
public class NoticeLogServiceImpl implements NoticeLogService {

    @Autowired
    private NoticeLogMapper noticeLogMapper;

    @Override
    public List<NoticeLog> queryByCondition(String deviceKey, String group, 
                                           String beginTime, String endTime, 
                                           String status) {
        return noticeLogMapper.selectByCondition(deviceKey, group, beginTime, endTime, status);
    }

    @Override
    public List<Map<String, Object>> countByDate(String dateType, String beginTime, String endTime) {
        return noticeLogMapper.countByDate(dateType, beginTime, endTime);
    }

    @Override
    public List<Map<String, Object>> countByStatus(String beginTime, String endTime) {
        return noticeLogMapper.countByStatus(beginTime, endTime);
    }

    @Override
    public List<Map<String, Object>> countByGroup(String beginTime, String endTime) {
        return noticeLogMapper.countByGroup(beginTime, endTime);
    }

    @Override
    public List<Map<String, Object>> countByDevice(String beginTime, String endTime) {
        return noticeLogMapper.countByDevice(beginTime, endTime);
    }
} 