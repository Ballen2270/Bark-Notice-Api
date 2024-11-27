package com.bark.service;

import com.bark.dto.ApiParam;
import com.bark.dto.NoticeResponse;

/**
 * @Author Ballen  2024/11/27 10:55
 */
public interface NotifyService {

    /**
     * 通知设备
     * @param deviceKey
     * @param key
     * @param iv
     * @param param
     * @return
     */
    boolean notify(String deviceKey, String key, String iv, ApiParam param);

    /**
     * 通知所有设备
     * @param param
     * @return
     */
    NoticeResponse noticeAll(ApiParam param);
}
