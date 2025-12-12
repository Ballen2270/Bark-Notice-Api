package com.bark.service.impl;

import com.bark.mapper.UserMapper;
import com.bark.core.BasicError;
import com.bark.core.ErrorCode;
import com.bark.domain.User;
import com.bark.dto.CheckInitRes;
import com.bark.service.UserService;
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

    private final UserMapper userMapper;

    private final StringRedisTemplate redisTemplate;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserServiceImpl(UserMapper userMapper, StringRedisTemplate redisTemplate) {
        this.userMapper = userMapper;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 验证码缓存前缀
     */
    private static final String CAPTCHA_PREFIX = "captcha:";

    /**
     * 验证码有效期（分钟）
     */
    private static final long CAPTCHA_EXPIRATION = 10;

    /**
     * IP封锁相关常量
     */
    private static final String LOGIN_FAIL_PREFIX = "login:fail:";
    private static final String LOGIN_BLOCK_PREFIX = "login:block:";
    private static final int MAX_FAIL_COUNT = 3;
    private static final long BLOCK_DURATION = 30; // 天
    private static final long FAIL_DURATION = 24; // 小时

    @Override
    public User login(String username, String password, String captcha, String captchaKey, String ip) {
        // 检查IP是否被封锁
        checkIpBlocked(ip);

        // 验证验证码
        String key = CAPTCHA_PREFIX + captchaKey;
        String cachedCaptcha = redisTemplate.opsForValue().get(key);

        if (cachedCaptcha == null) {
            handleLoginFailure(ip, "验证码已过期，请重新获取");
        }
        if (!cachedCaptcha.equalsIgnoreCase(captcha)) {
            handleLoginFailure(ip, "验证码错误，请重新输入");
        }

        // 删除已使用的验证码
        redisTemplate.delete(key);

        // 查询用户
        User user = userMapper.findByUsername(username);
        if (user == null) {
            handleLoginFailure(ip, "用户名或密码错误");
        }

        // 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            handleLoginFailure(ip, "用户名或密码错误");
        }

        // 验证账号状态
        if (!"ACTIVE".equals(user.getStatus())) {
            handleLoginFailure(ip, "账号已被禁用，请联系管理员");
        }

        // 登录成功：清除失败计数
        clearFailCount(ip);

        // 更新登录信息
        userMapper.updateLoginInfo(user.getId(), ip);

        // 不返回密码
        user.setPassword(null);

        return user;
    }

    /**
     * 检查IP是否被封锁
     *
     * @param ip IP地址
     */
    private void checkIpBlocked(String ip) {
        String blockKey = LOGIN_BLOCK_PREFIX + ip;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(blockKey))) {
            throw new BasicError(ErrorCode.IP_BLOCKED, "IP已被封锁，请30天后再试");
        }
    }

    /**
     * 处理登录失败
     *
     * @param ip IP地址
     * @param errorMessage 错误信息
     */
    private void handleLoginFailure(String ip, String errorMessage) {
        String failKey = LOGIN_FAIL_PREFIX + ip;

        // 使用原子操作增加失败计数
        Long failCount = redisTemplate.opsForValue().increment(failKey);

        // 设置失败计数的过期时间（仅在第一次设置时）
        if (failCount != null && failCount == 1) {
            redisTemplate.expire(failKey, FAIL_DURATION, TimeUnit.HOURS);
        }

        // 检查是否达到封锁阈值
        if (failCount != null && failCount >= MAX_FAIL_COUNT) {
            // 封锁IP
            String blockKey = LOGIN_BLOCK_PREFIX + ip;
            redisTemplate.opsForValue().set(blockKey, "blocked", BLOCK_DURATION, TimeUnit.DAYS);

            // 删除失败计数key（因为已经被封锁）
            redisTemplate.delete(failKey);

            throw new BasicError(ErrorCode.IP_BLOCKED, "IP已被封锁，请30天后再试");
        }

        // 抛出原始错误信息
        throw new BasicError(ErrorCode.SYSTEM_ERROR, errorMessage);
    }

    /**
     * 清除失败计数（登录成功时调用）
     *
     * @param ip IP地址
     */
    private void clearFailCount(String ip) {
        String failKey = LOGIN_FAIL_PREFIX + ip;
        redisTemplate.delete(failKey);
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
     * 
     * @param captchaKey  验证码key
     * @param captchaCode 验证码
     */
    public void saveCaptcha(String captchaKey, String captchaCode) {
        String key = CAPTCHA_PREFIX + captchaKey;
        redisTemplate.opsForValue().set(
                key,
                captchaCode,
                CAPTCHA_EXPIRATION,
                TimeUnit.MINUTES);
        System.out.println("验证码已保存: key=" + key + ", code=" + captchaCode + ", 有效期=" + CAPTCHA_EXPIRATION + "分钟");
    }

    @Override
    public CheckInitRes checkInit() {
        Integer count = userMapper.countUser();
        if (count >= 1) {
            return new CheckInitRes(true);
        }
        return new CheckInitRes(false);
    }
}