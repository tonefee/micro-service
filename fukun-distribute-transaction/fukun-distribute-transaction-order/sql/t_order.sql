/*
Navicat MySQL Data Transfer

Source Server         : 测试环境
Source Server Version : 50621
Source Host           : 192.168.0.43:3306
Source Database       : fukun_order

Target Server Type    : MYSQL
Target Server Version : 50621
File Encoding         : 65001

Date: 2019-07-03 17:40:19
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_order
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `order_no` varchar(64) DEFAULT NULL COMMENT '订单号',
  `create_time` varchar(50) NOT NULL,
  `update_time` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_order
-- ----------------------------
INSERT INTO `t_order` VALUES ('2bae9729023f4eadb5ee38ad5908ee7f', '123456789', '2019-07-02 14:47:37.902', '2019-07-02 14:47:37.902');
INSERT INTO `t_order` VALUES ('3fd8ed397ddc4225b1061255aff487a7', '123456789', '2019-07-02 14:52:13.1', '2019-07-02 14:52:13.1');
INSERT INTO `t_order` VALUES ('f34f6961e5274789a3fd40b208e7eb0b', '123456789', '2019-07-03 17:35:56.099', '2019-07-03 17:35:56.099');
