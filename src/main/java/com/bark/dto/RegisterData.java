package com.bark.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Ballen  2024/10/23 15:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterData {
    private String key;
    @JsonProperty("device_key")
    private String deviceKey;
    @JsonProperty("device_token")
    private String deviceToken;
}
