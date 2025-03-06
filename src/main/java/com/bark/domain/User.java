package com.bark.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String password;
    private String realName;
    private String email;
    private String phone;
    private String status; // ACTIVE 或 INACTIVE
    private Date lastLoginTime;
    private String lastLoginIp;
    private Date createdTime;
    private Date updatedTime;
} 