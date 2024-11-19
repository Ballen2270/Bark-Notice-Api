package com.bark.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @Author Ballen  2024/10/23 15:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {
    private Integer code;
    private String message;
    private long timestamp;
}
