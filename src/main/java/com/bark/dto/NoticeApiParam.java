package com.bark.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Ballen  2024/10/23 16:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeApiParam {
    private String ciphertext;
    private String iv;
}
