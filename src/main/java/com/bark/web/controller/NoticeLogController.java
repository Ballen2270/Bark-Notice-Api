package com.bark.web.controller;

import com.bark.core.BasicResponse;
import com.bark.domain.NoticeLog;
import com.bark.service.NoticeLogService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 通知日志控制器
 */
@RestController
@RequestMapping("/log/")
public class NoticeLogController {

    @Autowired
    private NoticeLogService noticeLogService;

    /**
     * 按条件查询通知日志（分页）
     */
    @GetMapping("/list")
    public BasicResponse<?> getLogList(
            @RequestParam(required = false) String group,
            @RequestParam(required = false) String beginTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false) String status,
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        // 验证参数有效性
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1 || pageSize > 100) {
            pageSize = 10;
        }

        // 使用分页查询
        PageInfo<NoticeLog> pageInfo = noticeLogService.queryByConditionPage(
                null, group, beginTime, endTime, status, pageNum, pageSize);

        // 构建返回结果
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("data", pageInfo.getList());
        resultMap.put("total", pageInfo.getTotal());
        resultMap.put("pages", pageInfo.getPages());
        resultMap.put("pageNum", pageInfo.getPageNum());
        resultMap.put("pageSize", pageInfo.getPageSize());

        return BasicResponse.successToClient("获取日志列表成功", resultMap);
    }

    /**
     * 按日期统计
     */
    @GetMapping("/count/date")
    public BasicResponse<?> countByDate(
            @RequestParam(defaultValue = "day") String dateType,
            @RequestParam(required = false) String beginTime,
            @RequestParam(required = false) String endTime) {

        List<Map<String, Object>> data = noticeLogService.countByDate(
                dateType, beginTime, endTime);

        return BasicResponse.successToClient("按日期统计成功", data);
    }

    /**
     * 按状态统计
     */
    @GetMapping("/count/status")
    public BasicResponse<?> countByStatus(
            @RequestParam(required = false) String beginTime,
            @RequestParam(required = false) String endTime) {

        List<Map<String, Object>> data = noticeLogService.countByStatus(beginTime, endTime);

        return BasicResponse.successToClient("按状态统计成功", data);
    }

    /**
     * 按分组统计
     */
    @GetMapping("/count/group")
    public BasicResponse<?> countByGroup(
            @RequestParam(required = false) String beginTime,
            @RequestParam(required = false) String endTime) {

        List<Map<String, Object>> data = noticeLogService.countByGroup(beginTime, endTime);

        return BasicResponse.successToClient("按分组统计成功", data);
    }

    /**
     * 按设备统计（使用设备名称而非敏感的deviceKey）
     */
    @GetMapping("/count/device")
    public BasicResponse<?> countByDevice(
            @RequestParam(required = false) String beginTime,
            @RequestParam(required = false) String endTime) {

        List<Map<String, Object>> data = noticeLogService.countByDevice(beginTime, endTime);

        return BasicResponse.successToClient("按设备统计成功", data);
    }
}