package com.bark.web.controller;

import com.bark.domain.DeviceConf;
import com.bark.dto.PingResponse;
import com.bark.dto.StatusRes;
import com.bark.mapper.DeviceConfMapper;
import com.bark.properties.ApiUrlProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

/**
 * endpoint
 * @author ballen
 */
@RestController
@RequestMapping("/status")
public class StatusController {

    public static final String PING = "ping";
    public static final String URL_PATH_SEPARATOR = "/";

    @Autowired
    DeviceConfMapper deviceConfMapper;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ApiUrlProperties apiUrlProperties;

    /**
     * 获取所有用户
     *
     * @return 用户列表
     */
    @GetMapping("/endpoint")
    public StatusRes endpoint(@RequestHeader("API-TOKEN") String apiToken) {
        if (StringUtils.isEmpty(apiUrlProperties.getToken()) || !apiUrlProperties.getToken().equals(apiToken)){
            return new StatusRes("未授权", 0, 0);
        }
        List<DeviceConf> all = deviceConfMapper.selectAll();
        int activeDeviceNum = (int) all.stream().filter(deviceConfs -> "ACTIVE".equals(deviceConfs.getStatus())).count();
        return new StatusRes(pingServer() ? "在线" : "离线", activeDeviceNum, all.size());
    }

    boolean pingServer(){
        try{
            PingResponse res = restTemplate.getForObject(apiUrlProperties.getUrl() + URL_PATH_SEPARATOR + PING, PingResponse.class);
            return res != null && res.getCode() == 200;
        }catch (Exception e){
            return false;
        }
    }
}