package com.bark.dto;

import com.bark.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录响应
 * @author ballen
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private User userInfo;
    private String token;
}
