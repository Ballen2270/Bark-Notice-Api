-- MySQL 版本建表脚本

-- 创建表：device_conf
CREATE TABLE IF NOT EXISTS `device_conf` (
    `device_token` varchar(64) NOT NULL COMMENT 'apple device Id',
    `name` varchar(80) DEFAULT NULL COMMENT '设备名称',
    `device_key` varchar(64) DEFAULT NULL COMMENT 'api key',
    `algorithm` varchar(16) DEFAULT NULL COMMENT '加密算法',
    `model` varchar(8) DEFAULT NULL COMMENT '加密模式',
    `padding` varchar(16) DEFAULT NULL COMMENT '填充方式',
    `encode_key` varchar(40) DEFAULT NULL COMMENT '密钥',
    `iv` varchar(20) DEFAULT NULL COMMENT '偏移向量',
    `status` varchar(16) DEFAULT NULL COMMENT 'ACTIVE:启用;STOP:停止;',
    `rec_create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `rec_update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`device_token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建表：notice_log
CREATE TABLE IF NOT EXISTS `notice_log` (
    `id` int NOT NULL AUTO_INCREMENT,
    `device_key` varchar(64) DEFAULT NULL COMMENT 'api key',
    `url` varchar(255) DEFAULT NULL COMMENT '请求url',
    `title` varchar(100) DEFAULT NULL COMMENT '标题',
    `body` varchar(1000) DEFAULT NULL COMMENT '消息体',
    `group_name` varchar(20) DEFAULT NULL COMMENT '消息分组',
    `result` varchar(1000) DEFAULT NULL COMMENT '返回结果',
    `status` varchar(16) DEFAULT NULL COMMENT 'SUCCESS:成功;FAILED:失败;',
    `rec_create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `rec_update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建表：sys_user
CREATE TABLE IF NOT EXISTS `sys_user` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `username` varchar(50) NOT NULL COMMENT '用户名',
    `password` varchar(100) NOT NULL COMMENT '密码(加密)',
    `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
    `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
    `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
    `status` varchar(10) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态(ACTIVE-激活,INACTIVE-禁用)',
    `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
    `last_login_ip` varchar(50) DEFAULT NULL COMMENT '最后登录IP',
    `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_notice_log_device_key ON notice_log(device_key);
CREATE INDEX IF NOT EXISTS idx_sys_user_username ON sys_user(username);
