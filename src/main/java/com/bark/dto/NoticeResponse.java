package com.bark.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Ballen  2024/10/24 16:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeResponse {
    private Integer sendNum;
    private Integer successNum;
}
