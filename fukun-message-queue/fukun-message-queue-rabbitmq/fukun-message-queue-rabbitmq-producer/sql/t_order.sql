/*
Navicat MySQL Data Transfer

Source Server         : 本地数据库
Source Server Version : 50624
Source Host           : 127.0.0.1:3306
Source Database       : fukun

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2019-07-08 16:53:09
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_order
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order` (
  `id` varchar(128) NOT NULL,
  `name` varchar(128) DEFAULT NULL,
  `message_id` varchar(128) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_order
-- ----------------------------
INSERT INTO `t_order` VALUES ('1562575451774$6d2848df-f3d3-419c-a393-42bba02613c0', '订单', '1562575451774$8bdc57bf-7fd0-489c-9b09-99dd6cdb6db0');
