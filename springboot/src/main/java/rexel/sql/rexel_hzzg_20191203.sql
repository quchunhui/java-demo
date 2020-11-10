/*
 Navicat Premium Data Transfer

 Source Server         : localmysql
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : localhost:3306
 Source Schema         : rexel_hzzg

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 03/12/2019 11:24:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP DATABASE IF EXISTS rexel_hzzg;
CREATE DATABASE rexel_hzzg;
USE rexel_hzzg;

-- ----------------------------
-- Table structure for access_token
-- ----------------------------
DROP TABLE IF EXISTS `access_token`;
CREATE TABLE `access_token`  (
  `accessId` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一标识',
  `accessKey` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '访问密钥',
  `deviceId` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '设备唯一标示',
  `token` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '访问令牌',
  `insertTime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for collect_switch
-- ----------------------------
DROP TABLE IF EXISTS `collect_switch`;
CREATE TABLE `collect_switch`  (
  `collect` int(1) NOT NULL DEFAULT 1 COMMENT '是否接入数据（true：可接入、false：不接入）',
  PRIMARY KEY (`collect`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dview_var_name_dic
-- ----------------------------
DROP TABLE IF EXISTS `dview_var_name_dic`;
CREATE TABLE `dview_var_name_dic`  (
  `apiItemName` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '接口字段名称',
  `dviewVarType` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'DView字段类型（AI,AO,AR,VA,DI,DO,DR,VD,VT）',
  `dviewVarName` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'DView字典名称',
  `comment` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '说明',
  PRIMARY KEY (`apiItemName`, `dviewVarType`, `dviewVarName`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dview_var_name_dic
-- ----------------------------
INSERT INTO `dview_var_name_dic` VALUES ('aluGrade', 'AI', 'aluGrade', '板的牌号');
INSERT INTO `dview_var_name_dic` VALUES ('aluState', 'AI', 'aluState', '板的合金状态');
INSERT INTO `dview_var_name_dic` VALUES ('cnt', 'AI', 'cnt', '板的张数');
INSERT INTO `dview_var_name_dic` VALUES ('isFilm', 'AI', 'isFilm', '板是否覆膜（1:覆膜、0:不覆膜）');
INSERT INTO `dview_var_name_dic` VALUES ('length', 'AI', 'length', '板的长度');
INSERT INTO `dview_var_name_dic` VALUES ('thickness', 'AI', 'thickness', '板的厚度');
INSERT INTO `dview_var_name_dic` VALUES ('width', '', 'width', NULL);

-- ----------------------------
-- Table structure for processed_data
-- ----------------------------
DROP TABLE IF EXISTS `processed_data`;
CREATE TABLE `processed_data`  (
  `autoId` int(20) NOT NULL AUTO_INCREMENT COMMENT '自增唯一ID',
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '订单标识',
  `deviceUnique` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '设备唯一标识',
  `collectDate` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接入时间(yyyy-MM-dd)',
  `status` int(1) NOT NULL DEFAULT 0 COMMENT '加工状态（0:待加工、1：加工中、2：已完成）',
  `aluGrade` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '板的牌号',
  `aluState` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '板的合金状态',
  `thickness` float(10, 0) NOT NULL DEFAULT 0 COMMENT '板的厚度',
  `width` float(10, 0) NOT NULL DEFAULT 0 COMMENT '板的宽度',
  `length` float(10, 0) NOT NULL DEFAULT 0 COMMENT '板的长度',
  `isFilm` int(1) NOT NULL DEFAULT 0 COMMENT '板是否覆膜（1:覆膜、0:不覆膜）',
  `cnt` int(10) NOT NULL DEFAULT 0 COMMENT '板的张数',
  `insertTime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
  `insertUser` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'rexel' COMMENT '插入用户',
  `updateTime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次更新时间',
  `updateUser` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'rexel' COMMENT '最后一次更新用户',
  PRIMARY KEY (`autoId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
