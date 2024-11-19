package com.bark.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Ballen  2024/10/24 10:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse extends ApiResponse{
    private RegisterData data;
}
