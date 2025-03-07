package com.bark.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Ballen  3/7/25 11:01 AM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusRes {

    /**
     * api服务器状态
     */
    private String status;

    private Integer activeDeviceNum;

    private Integer allDeviceNum;
}
