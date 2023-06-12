CREATE TABLE `t_member`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` char(4) NOT NULL DEFAULT '' COMMENT '房号',
  `phone` varchar(20) NOT NULL DEFAULT '' COMMENT '关联t_member',
  `idcard` varchar(30) NOT NULL DEFAULT '' COMMENT '身份证号',
  `room_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联t_room',
  `status` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT 'status：0正常  1退租',
  PRIMARY KEY (`id`)
);

CREATE TABLE `t_room`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `number` char(4) NOT NULL DEFAULT '' COMMENT '房号',
  `member_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联t_member',
  PRIMARY KEY (`id`)
);

