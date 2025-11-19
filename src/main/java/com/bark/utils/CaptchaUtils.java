package com.bark.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
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
     * 
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
     * 
     * @param code 验证码字符串
     * @return Base64编码的图片字符串
     */
    public static String generateCaptchaImage(String code) throws IOException {
        // 创建图片缓冲区
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // 设置背景色
        g2d.setColor(new Color(240, 240, 240));
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        // 绘制干扰线
        for (int i = 0; i < 20; i++) {
            g2d.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            int x1 = random.nextInt(WIDTH);
            int y1 = random.nextInt(HEIGHT);
            int x2 = random.nextInt(WIDTH);
            int y2 = random.nextInt(HEIGHT);
            g2d.drawLine(x1, y1, x2, y2);
        }

        // 绘制噪点
        float yawpRate = 0.05f; // 噪声率
        int area = (int) (yawpRate * WIDTH * HEIGHT);
        for (int i = 0; i < area; i++) {
            int x = random.nextInt(WIDTH);
            int y = random.nextInt(HEIGHT);
            int rgb = getRandomIntColor();
            image.setRGB(x, y, rgb);
        }

        // 扭曲图片
        shear(g2d, WIDTH, HEIGHT, Color.WHITE);

        // 设置字体
        g2d.setFont(new Font("Arial", Font.BOLD, 28));

        // 绘制验证码
        for (int i = 0; i < code.length(); i++) {
            AffineTransform affine = new AffineTransform();
            affine.setToRotation(Math.PI / 4 * random.nextDouble() * (random.nextBoolean() ? 1 : -1),
                    (WIDTH / code.length()) * i + (HEIGHT - 4) / 2, HEIGHT / 2);
            g2d.setTransform(affine);
            g2d.setColor(new Color(random.nextInt(100), random.nextInt(100), random.nextInt(100)));
            g2d.drawString(code.charAt(i) + "", (i * WIDTH / code.length()) + 5, HEIGHT - 10);
        }

        g2d.dispose();

        // 转为Base64字符串
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] bytes = baos.toByteArray();
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes);
    }

    private static int getRandomIntColor() {
        int[] color = { 0, 0, 0 };
        for (int i = 0; i < color.length; i++) {
            color[i] = random.nextInt(256);
        }
        return (color[0] << 16) | (color[1] << 8) | color[2];
    }

    private static void shear(Graphics g, int w1, int h1, Color color) {
        shearX(g, w1, h1, color);
        shearY(g, w1, h1, color);
    }

    private static void shearX(Graphics g, int w1, int h1, Color color) {
        int period = random.nextInt(2);
        boolean borderGap = true;
        int frames = 1;
        int phase = random.nextInt(2);

        for (int i = 0; i < h1; i++) {
            double d = (double) (period >> 1)
                    * Math.sin((double) i / (double) period
                            + (6.2831853071795862D * (double) phase)
                                    / (double) frames);
            g.copyArea(0, i, w1, 1, (int) d, 0);
            if (borderGap) {
                g.setColor(color);
                g.drawLine((int) d, i, 0, i);
                g.drawLine((int) d + w1, i, w1, i);
            }
        }
    }

    private static void shearY(Graphics g, int w1, int h1, Color color) {
        int period = random.nextInt(40) + 10; // 50;
        boolean borderGap = true;
        int frames = 20;
        int phase = 7;
        for (int i = 0; i < w1; i++) {
            double d = (double) (period >> 1)
                    * Math.sin((double) i / (double) period
                            + (6.2831853071795862D * (double) phase)
                                    / (double) frames);
            g.copyArea(i, 0, 1, h1, 0, (int) d);
            if (borderGap) {
                g.setColor(color);
                g.drawLine(i, (int) d, i, 0);
                g.drawLine(i, (int) d + h1, i, h1);
            }
        }
    }
}