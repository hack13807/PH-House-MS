CREATE TABLE `t_member`
(
  `id`      bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name`    char(4) CHARACTER SET utf8 COLLATE utf8_unicode_ci     NOT NULL DEFAULT '' COMMENT '房号',
  `phone`   varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '关联t_member',
  `idcard`  varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '身份证号',
  `room_id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联t_room',
  `status`  tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'status：0正常  1退租',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `t_room`;
CREATE TABLE `t_room`
(
  `id`          bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `number`      char(4) CHARACTER SET utf8 COLLATE utf8_unicode_ci      NOT NULL DEFAULT '' COMMENT '房号',
  `status`      tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '房间状态  0待租  1出租中  2待处理',
  `member_id`   bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联t_member',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 175 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

