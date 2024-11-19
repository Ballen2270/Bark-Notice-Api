package com.bark.web.controller;

import com.alibaba.fastjson.JSON;
import com.bark.core.BasicResponse;
import com.bark.domain.NoticeLog;
import com.bark.domain.DeviceConf;
import com.bark.dto.*;
import com.bark.mapper.DeviceConfMapper;
import com.bark.mapper.NoticeLogMapper;
import com.bark.properties.ApiUrlProperties;
import com.bark.utils.AESCodec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Ballen  2024/2/6 12:38
 */
@Slf4j
@RequestMapping("/")
@RestController
public class NoticeController {
    @Autowired
    RestTemplate restTemplate;
    @Resource
    DeviceConfMapper deviceConfMapper;
    @Resource
    NoticeLogMapper noticeLogMapper;

    @Autowired
    ApiUrlProperties apiUrlProperties;

    public static final String REGISTER_PRE = "/register?devicetoken=";
    public static final String REGISTER_KEY_PRE = "&key=";
    public static final String PING = "ping";
    public static final String URL_PATH_SEPARATOR = "/";
    public static final String FAILED = "FAILED";
    public static final String SUCCESS = "SUCCESS";
    public static final Integer API_SUCCESS = 200;
    public final static String ACTIVE = "ACTIVE";

    @GetMapping("ping")
    private PingResponse ping(){
        return restTemplate.getForObject(apiUrlProperties.getUrl() + URL_PATH_SEPARATOR + PING, PingResponse.class);
    }

    @GetMapping("register")
    private RegisterResponse register(@RequestParam String devicetoken, @RequestParam String key){
        String registerUrl = genRegisterUrl(devicetoken, key);
        RegisterResponse res = restTemplate.getForObject(registerUrl, RegisterResponse.class);
        if (res == null || !API_SUCCESS.equals(res.getCode())){
            return res;
        }
        DeviceConf deviceConf = deviceConfMapper.selectByPrimaryKey(devicetoken);
        if (deviceConf != null){
            deviceConf.setDeviceKey(res.getData().getDeviceKey());
            deviceConfMapper.updateByPrimaryKey(deviceConf);
        }else {
            DeviceConf newConf = new DeviceConf();
            newConf.setDeviceToken(devicetoken);
            newConf.setDeviceKey(res.getData().getDeviceKey());
            newConf.setStatus(ACTIVE);
            deviceConfMapper.insert(newConf);
        }
        return res;
    }

    private String genRegisterUrl(String devicetoken, String key) {
        StringBuilder sb = new StringBuilder(apiUrlProperties.getUrl()).append(REGISTER_PRE).append(devicetoken);;
        if (!StringUtils.isEmpty(key)){
            sb.append(REGISTER_KEY_PRE).append(key);
        }
        return sb.toString();
    }


    @GetMapping("notice")
    private BasicResponse notice(@RequestParam String title, @RequestParam String body, @RequestParam(required = false) String group){
        return BasicResponse.successToClient("发送成功", noticeAll(new ApiParam(title, body, group)));
    }

    @GetMapping("notice/{title}/{body}")
    private BasicResponse noticePathVariable(@PathVariable("title") String title, @PathVariable("body") String body, @RequestParam(required = false) String group){
        return BasicResponse.successToClient("发送成功", noticeAll(new ApiParam(title, body, group)));
    }


    @PostMapping("notice")
    private BasicResponse notice(@Valid @RequestBody ApiParam param){
        return BasicResponse.successToClient("发送成功", noticeAll(param));
    }
    NoticeResponse noticeAll(ApiParam param){
        List<DeviceConf> devices = deviceConfMapper.selectAll();
        List<DeviceConf> success = devices.stream().parallel().filter(device -> notify(device.getDeviceKey(), device.getEncodeKey(), device.getIv(), param)).collect(Collectors.toList());
        return new NoticeResponse(devices.size(), success.size());
    }
    boolean notify(String deviceKey, String key, String iv, ApiParam param){
        String url = apiUrlProperties.getUrl() + URL_PATH_SEPARATOR + deviceKey;
        AESCodec aesCodec = new AESCodec();
        NoticeLog noticeLog = new NoticeLog();
        noticeLog.setDeviceKey(deviceKey);
        noticeLog.setUrl(url);
        noticeLog.setTitle(param.getTitle());
        noticeLog.setBody(param.getBody());
        noticeLog.setGroup(param.getGroup());
        String res= null;
        try{
            String ciphertext = aesCodec.encrypt(JSON.toJSONString(param), key, iv);
            res = restTemplate.postForObject(url, JSON.toJSON(new NoticeApiParam(ciphertext, iv)), String.class);
            log.info("notice deviceKey:{}, res:{}", deviceKey, res);
            noticeLog.setResult(res);
        }catch (Exception e){
            log.warn("notice失败, deviceKey:{}, e:{}",deviceKey, e);
            noticeLog.setStatus(FAILED);
            noticeLog.setResult(res);
            noticeLogMapper.insert(noticeLog);
            return false;
        }
        if (!StringUtils.isEmpty(res) && res.contains(String.valueOf(API_SUCCESS))){
            noticeLog.setStatus(SUCCESS);
            noticeLogMapper.insert(noticeLog);
            return true;
        }
        noticeLog.setStatus(FAILED);
        noticeLogMapper.insert(noticeLog);
        return false;
    }
}
