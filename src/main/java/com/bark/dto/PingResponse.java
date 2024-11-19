package com.bark.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Ballen  2024/10/24 13:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PingResponse {
    private Integer code;
    private String message;
    private long timestamp;
}
