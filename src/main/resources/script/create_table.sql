DROP TABLE IF EXISTS `t_member`;
CREATE TABLE `t_member`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` char(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '房号',
  `sex` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '性别',
  `tel` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `idcard` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '身份证号',
  `room_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联t_room',
  `status` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'status：0新增 1 租住中 2已退租',
  `isdelete` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '0正常 1删除 ',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 47 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `t_room`;
CREATE TABLE `t_room`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `number` char(4) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '房号',
  `status` tinyint(3) NOT NULL DEFAULT 0 COMMENT '房间状态  0待租  1出租中  2待处理',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '描述',
  `enable` tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '0禁用 1启用',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_number`(`number`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 61 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`
(
    `id`      bigint UNSIGNED NOT NULL AUTO_INCREMENT,
    `number`  varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NULL DEFAULT NULL,
    `name`    varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NULL DEFAULT NULL,
    `phone`   varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NULL DEFAULT NULL,
    `pwd`     varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NULL DEFAULT NULL,
    `role_id` bigint NULL DEFAULT NULL COMMENT '关联t_role表',
    `status`  tinyint NULL DEFAULT NULL COMMENT '0：正常  1：禁用',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_unicode_ci ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `t_lease`;
CREATE TABLE `t_lease`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '业务编码',
  `lease_type` tinyint(4) NOT NULL COMMENT '租住类型  1：按日租  2：按月租  3：按年租 ',
  `unit` int(11) NOT NULL COMMENT '租住时长数量单位',
  `rent_amount` decimal(10, 2) UNSIGNED NOT NULL COMMENT '租金额',
  `start_date` datetime NULL DEFAULT NULL COMMENT '开始日期',
  `end_date` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `last_update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `create_user_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '创建用户',
  `last_update_user_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '最后更新用户',
  `room_id` bigint(20) NOT NULL DEFAULT 0,
  `member_id` bigint(20) NOT NULL DEFAULT 0,
  `effective` tinyint(1) UNSIGNED NULL DEFAULT 1 COMMENT '1生效 0无效',
  `isdelete` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '1删除 0正常',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;