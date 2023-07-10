DROP TABLE IF EXISTS `t_member`;
CREATE TABLE `t_member` (
    `id` bigint(20) AUTO_INCREMENT NOT NULL,
    `name` char(4) DEFAULT '' NOT NULL COMMENT '房号',
    `tel` varchar(20) DEFAULT '' NOT NULL COMMENT '关联t_member',
    `idcard` varchar(30) DEFAULT '' NOT NULL COMMENT '身份证号',
    `room_id` bigint(20) DEFAULT 0 NOT NULL COMMENT '关联t_room',
    `status` tinyint(3) DEFAULT 0 NOT NULL COMMENT 'status：0正常  1退租',
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `t_room`;
CREATE TABLE `t_room` (
  `id` bigint(20) AUTO_INCREMENT NOT NULL,
  `number` char(4) DEFAULT '' NOT NULL COMMENT '房号',
  `status` tinyint(3) DEFAULT 0 NOT NULL COMMENT '房间状态  0待租  1出租中  2待处理',
  `member_id` bigint(20) DEFAULT 0 NOT NULL COMMENT '关联t_member',
  `description` varchar(255) DEFAULT '' NOT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint AUTO_INCREMENT NOT NULL,
  `number` varchar(20) DEFAULT NULL NULL,
  `name` varchar(20) DEFAULT NULL NULL,
  `phone` varchar(20) DEFAULT NULL NULL,
  `pwd` varchar(20) DEFAULT NULL NULL,
  `role_id` bigint DEFAULT NULL NULL COMMENT '关联t_role表',
  `status` tinyint DEFAULT NULL NULL COMMENT '0：正常  1：禁用',
  PRIMARY KEY (`id`)
);
