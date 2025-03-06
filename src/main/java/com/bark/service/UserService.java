package com.bark.service;

import java.util.List;

import com.bark.domain.User;
import com.bark.dto.CheckInitRes;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @param captcha 验证码
     * @param captchaKey 验证码key
     * @param ip 登录IP
     * @return 登录用户信息
     */
    User login(String username, String password, String captcha, String captchaKey, String ip);
    
    /**
     * 注册用户
     * @param user 用户信息
     * @return 注册结果
     */
    User register(User user);
    
    /**
     * 修改密码
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 修改结果
     */
    boolean changePassword(Long userId, String oldPassword, String newPassword);
    
    /**
     * 激活用户账号
     * @param userId 用户ID
     * @return 激活结果
     */
    boolean activateUser(Long userId);
    
    /**
     * 禁用用户账号
     * @param userId 用户ID
     * @return 禁用结果
     */
    boolean deactivateUser(Long userId);
    
    /**
     * 获取所有用户
     * @return 用户列表
     */
    List<User> getAllUsers();
    
    /**
     * 获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    User getUserById(Long userId);
    
    /**
     * 根据用户名获取用户
     * @param username 用户名
     * @return 用户信息
     */
    User getUserByUsername(String username);


    /**
     * 检查是否初始化
     * @return
     */
    CheckInitRes checkInit();
} 