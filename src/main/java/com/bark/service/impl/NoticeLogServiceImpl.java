package com.bark.service.impl;

import com.bark.domain.NoticeLog;
import com.bark.mapper.NoticeLogMapper;
import com.bark.service.NoticeLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
    public PageInfo<NoticeLog> queryByConditionPage(String deviceKey, String group,
                                                  String beginTime, String endTime,
                                                  String status, Integer pageNum, Integer pageSize) {
        // 参数验证，确保分页参数有效
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;  // 默认第一页
        }
        if (pageSize == null || pageSize < 1 || pageSize > 100) {
            pageSize = 10;  // 默认每页10条，并限制最大条数
        }
        
        // 设置分页参数
        PageHelper.startPage(pageNum, pageSize);
        // 执行查询
        List<NoticeLog> list = noticeLogMapper.selectByCondition(deviceKey, group, beginTime, endTime, status);
        // 返回分页结果
        return new PageInfo<>(list);
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