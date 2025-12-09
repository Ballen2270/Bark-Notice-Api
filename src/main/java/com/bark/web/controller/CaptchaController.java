package com.bark.web.controller;

import com.bark.core.BasicResponse;
import com.bark.service.impl.UserServiceImpl;
import com.bark.utils.CaptchaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 验证码控制器
 */
@RestController
@RequestMapping("/auth/captcha")
public class CaptchaController {

    @Autowired
    private UserServiceImpl userService;

    /**
     * 生成验证码
     * 
     * @return 验证码图片和key
     */
    @GetMapping("/generate")
    public BasicResponse<Map<String, String>> generateCaptcha() {
        try {
            // 生成验证码
            String captchaCode = CaptchaUtils.generateCode();

            // 生成唯一的key
            String captchaKey = UUID.randomUUID().toString();

            // 将验证码保存到Redis
            userService.saveCaptcha(captchaKey, captchaCode);

            // 生成验证码图片
            String captchaImage = CaptchaUtils.generateCaptchaImage(captchaCode);

            // 返回验证码图片和key
            Map<String, String> data = new HashMap<>();
            data.put("key", captchaKey);
            data.put("image", captchaImage);

            return BasicResponse.successToClient("获取验证码成功", data);
        } catch (IOException e) {
            e.printStackTrace();
            return BasicResponse.errorToClient("生成验证码失败", null);
        }
    }
}