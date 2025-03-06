package com.bark.service.impl;

import com.bark.mapper.UserMapper;
import com.bark.core.BasicError;
import com.bark.core.ErrorCode;
import com.bark.domain.User;
import com.bark.dto.CheckInitRes;
import com.bark.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    /**
     * 验证码缓存前缀
     */
    private static final String CAPTCHA_PREFIX = "captcha:";
    
    /**
     * 验证码有效期（分钟）
     */
    private static final long CAPTCHA_EXPIRATION = 5;
    
    @Override
    public User login(String username, String password, String captcha, String captchaKey, String ip) {
        // 验证验证码
        String cachedCaptcha = redisTemplate.opsForValue().get(CAPTCHA_PREFIX + captchaKey);
        if (cachedCaptcha == null || !cachedCaptcha.equalsIgnoreCase(captcha)) {
            throw new BasicError(ErrorCode.SYSTEM_ERROR, "验证码错误或已过期");
        }
        
        // 删除已使用的验证码
        redisTemplate.delete(CAPTCHA_PREFIX + captchaKey);
        
        // 查询用户
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new BasicError(ErrorCode.SYSTEM_ERROR, "用户名或密码错误");
        }
        
        // 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BasicError(ErrorCode.SYSTEM_ERROR, "用户名或密码错误");
        }
        
        // 验证账号状态
        if (!"ACTIVE".equals(user.getStatus())) {
            throw new BasicError(ErrorCode.SYSTEM_ERROR, "账号已被禁用，请联系管理员");
        }
        
        // 更新登录信息
        userMapper.updateLoginInfo(user.getId(), ip);
        
        // 不返回密码
        user.setPassword(null);
        
        return user;
    }
    
    @Override
    @Transactional
    public User register(User user) {
        // 验证用户名是否已存在
        User existingUser = userMapper.findByUsername(user.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // 设置状态为激活
        user.setStatus("ACTIVE");
        
        // 保存用户
        userMapper.insert(user);
        
        // 不返回密码
        user.setPassword(null);
        
        return user;
    }
    
    @Override
    @Transactional
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        // 查询用户
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new BasicError(ErrorCode.SYSTEM_ERROR, "用户不存在");
        }
        
        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BasicError(ErrorCode.SYSTEM_ERROR, "原密码错误");
        }
        
        // 更新密码
        User updateUser = new User();
        updateUser.setId(userId);
        updateUser.setPassword(passwordEncoder.encode(newPassword));
        
        return userMapper.update(updateUser) > 0;
    }
    
    @Override
    @Transactional
    public boolean activateUser(Long userId) {
        return userMapper.updateStatus(userId, "ACTIVE") > 0;
    }
    
    @Override
    @Transactional
    public boolean deactivateUser(Long userId) {
        return userMapper.updateStatus(userId, "INACTIVE") > 0;
    }
    
    @Override
    public List<User> getAllUsers() {
        List<User> users = userMapper.findAll();
        // 不返回密码
        for (User user : users) {
            user.setPassword(null);
        }
        return users;
    }
    
    @Override
    public User getUserById(Long userId) {
        User user = userMapper.findById(userId);
        if (user != null) {
            // 不返回密码
            user.setPassword(null);
        }
        return user;
    }
    
    @Override
    public User getUserByUsername(String username) {
        User user = userMapper.findByUsername(username);
        if (user != null) {
            // 不返回密码
            user.setPassword(null);
        }
        return user;
    }
    
    /**
     * 保存验证码到Redis
     * @param captchaKey 验证码key
     * @param captchaCode 验证码
     */
    public void saveCaptcha(String captchaKey, String captchaCode) {
        redisTemplate.opsForValue().set(
                CAPTCHA_PREFIX + captchaKey, 
                captchaCode, 
                CAPTCHA_EXPIRATION, 
                TimeUnit.MINUTES
        );
    }

    @Override
    public CheckInitRes checkInit(){
        Integer count = userMapper.countUser();
        if (count >= 1) {
            return new CheckInitRes(true);
        }
        return new CheckInitRes(false);
    }
} 