package com.bark.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

/**
 * 验证码工具类
 */
public class CaptchaUtils {
    
    // 验证码字符集
    private static final String CODES = "0123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz";
    
    // 验证码图片宽度
    private static final int WIDTH = 120;
    
    // 验证码图片高度
    private static final int HEIGHT = 40;
    
    // 验证码字符个数
    private static final int CODE_LENGTH = 4;
    
    private static final Random random = new Random();
    
    /**
     * 生成随机验证码
     * @return 验证码字符串
     */
    public static String generateCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(CODES.charAt(random.nextInt(CODES.length())));
        }
        return sb.toString();
    }
    
    /**
     * 生成验证码图片
     * @param code 验证码字符串
     * @return Base64编码的图片字符串
     */
    public static String generateCaptchaImage(String code) throws IOException {
        // 创建图片缓冲区
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        
        // 设置背景色
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);
        
        // 绘制干扰线
        g2d.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < 60; i++) {
            int x1 = random.nextInt(WIDTH);
            int y1 = random.nextInt(HEIGHT);
            int x2 = x1 + random.nextInt(20) - 10;
            int y2 = y1 + random.nextInt(20) - 10;
            g2d.drawLine(x1, y1, x2, y2);
        }
        
        // 绘制噪点
        for (int i = 0; i < 160; i++) {
            int x = random.nextInt(WIDTH);
            int y = random.nextInt(HEIGHT);
            image.setRGB(x, y, random.nextInt(0xFFFFFF));
        }
        
        // 设置字体
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        
        // 绘制验证码
        for (int i = 0; i < code.length(); i++) {
            g2d.setColor(new Color(random.nextInt(128), random.nextInt(128), random.nextInt(128)));
            g2d.drawString(code.charAt(i) + "", i * WIDTH / code.length() + 10, HEIGHT - 10);
        }
        
        g2d.dispose();
        
        // 转为Base64字符串
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] bytes = baos.toByteArray();
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes);
    }
} 