/*
Navicat MySQL Data Transfer

Source Server         : 本地数据库
Source Server Version : 50624
Source Host           : 127.0.0.1:3306
Source Database       : fukun

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2019-07-08 16:53:01
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for broker_message_log
-- ----------------------------
DROP TABLE IF EXISTS `broker_message_log`;
CREATE TABLE `broker_message_log` (
  `message_id` varchar(128) NOT NULL,
  `message` varchar(4000) DEFAULT NULL,
  `try_count` int(4) NOT NULL DEFAULT '0',
  `status` varchar(10) DEFAULT '',
  `next_retry` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of broker_message_log
-- ----------------------------
INSERT INTO `broker_message_log` VALUES ('1562575451774$8bdc57bf-7fd0-489c-9b09-99dd6cdb6db0', '{\"id\":\"1562575451774$6d2848df-f3d3-419c-a393-42bba02613c0\",\"messageId\":\"1562575451774$8bdc57bf-7fd0-489c-9b09-99dd6cdb6db0\",\"name\":\"订单\"}', '3', '2', '2019-07-08 16:45:12', '2019-07-08 16:44:12', '2019-07-08 16:45:43');
