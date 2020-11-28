/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50130
Source Host           : localhost:3306
Source Database       : crawler

Target Server Type    : MYSQL
Target Server Version : 50130
File Encoding         : 65001

Date: 2020-11-28 15:12:25
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for jd_item
-- ----------------------------
DROP TABLE IF EXISTS `jd_item`;
CREATE TABLE `jd_item` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `spu` bigint(15) DEFAULT NULL,
  `sku` bigint(15) DEFAULT NULL,
  `title` varchar(100) DEFAULT NULL,
  `price` bigint(10) DEFAULT NULL,
  `pic` varchar(200) DEFAULT NULL,
  `url` varchar(200) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jd_item
-- ----------------------------
