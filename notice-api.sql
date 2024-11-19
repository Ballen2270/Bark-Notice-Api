/*
 Navicat Premium Data Transfer

 Source Server         : mysql8-local
 Source Server Type    : MySQL
 Source Server Version : 80300 (8.3.0)
 Source Host           : 127.0.0.1:3306
 Source Schema         : notice-api

 Target Server Type    : MySQL
 Target Server Version : 80300 (8.3.0)
 File Encoding         : 65001

 Date: 30/10/2024 10:54:50
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

SET FOREIGN_KEY_CHECKS = 1;
