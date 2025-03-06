/*
 Navicat Premium Data Transfer

 Source Server         : mysql8-local
 Source Server Type    : MySQL
 Source Server Version : 80404 (8.4.4)
 Source Host           : 127.0.0.1:3306
 Source Schema         : notice-api

 Target Server Type    : MySQL
 Target Server Version : 80404 (8.4.4)
 File Encoding         : 65001

 Date: 05/03/2025 15:08:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for api_conf
-- ----------------------------
DROP TABLE IF EXISTS `api_conf`;
CREATE TABLE `api_conf` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(40) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '名称',
  `local_url` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '本地url地址',
  `url` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '在线url地址',
  `rec_create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `rec_update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for device_conf
-- ----------------------------
DROP TABLE IF EXISTS `device_conf`;
CREATE TABLE `device_conf` (
  `device_token` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'apple device Id',
  `name` varchar(80) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '设备名称',
  `device_key` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'api key',
  `algorithm` varchar(16) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '加密算法',
  `model` varchar(8) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '加密模式',
  `padding` varchar(16) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '填充方式',
  `encode_key` varchar(40) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '密钥',
  `iv` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '偏移向量',
  `status` varchar(16) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'ACTIVE:启用;STOP:停止;',
  `rec_create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `rec_update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`device_token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for notice_log
-- ----------------------------
DROP TABLE IF EXISTS `notice_log`;
CREATE TABLE `notice_log` (
  `id` int NOT NULL AUTO_INCREMENT,
  `device_key` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'api key',
  `url` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求url',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '标题',
  `body` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '消息体',
  `group` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '消息分组',
  `result` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '返回结果',
  `status` varchar(16) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'SUCCESS:成功;FAILED:失败;',
  `rec_create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `rec_update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统用户表';

SET FOREIGN_KEY_CHECKS = 1;
