DROP TABLE IF EXISTS `t_member`;
CREATE TABLE `t_member` (
    `id` bigint(20) AUTO_INCREMENT NOT NULL,
    `name` char(10) DEFAULT '' NOT NULL COMMENT '房号',
    `sex` tinyint(1) DEFAULT 0 NOT NULL COMMENT '性别',
    `tel` varchar(20) DEFAULT '' NOT NULL,
    `idcard` varchar(30) DEFAULT '' NOT NULL COMMENT '身份证号',
    `room_id` bigint(20) DEFAULT 0 NOT NULL COMMENT '关联t_room',
    `status` tinyint(1) DEFAULT 0 NOT NULL COMMENT 'status：0新增 1 租住中 2已退租',
    `isdelete` tinyint(1) DEFAULT 0 NOT NULL COMMENT '0正常 1删除 ',
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `t_room`;

CREATE TABLE `t_room` (
  `id` bigint(20) AUTO_INCREMENT NOT NULL,
  `number` char(4) DEFAULT '' NOT NULL COMMENT '房号',
  `status` tinyint(3) DEFAULT 0 NOT NULL COMMENT '房间状态  0待租  1出租中  2待处理',
  `description` varchar(255) DEFAULT '' NOT NULL COMMENT '描述',
  `enable` tinyint(1) DEFAULT 1 NOT NULL COMMENT '0禁用 1启用',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_number3334813690500`(`number`)
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

DROP TABLE IF EXISTS `t_lease`;

CREATE TABLE `t_lease` (
   `id` bigint(20) AUTO_INCREMENT NOT NULL,
   `number` varchar(20) DEFAULT '' NOT NULL COMMENT '业务编码',
   `lease_type` tinyint(4) NOT NULL COMMENT '租住类型  1：按日租  2：按月租  3：按年租 ',
   `unit` int(11) NOT NULL COMMENT '租住时长数量单位',
   `rent_amount` decimal(10, 2) NOT NULL COMMENT '租金额',
   `start_date` datetime DEFAULT NULL NULL COMMENT '开始日期',
   `end_date` datetime DEFAULT NULL NULL COMMENT '结束时间',
   `create_time` datetime DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建日期',
   `last_update_time` datetime DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '最后更新时间',
   `create_user_id` bigint(20) DEFAULT 0 NOT NULL COMMENT '创建用户',
   `last_update_user_id` bigint(20) DEFAULT 0 NOT NULL COMMENT '最后更新用户',
   `room_id` bigint(20) DEFAULT 0 NOT NULL,
   `member_id` bigint(20) DEFAULT 0 NOT NULL,
   `effective` tinyint(1) DEFAULT 1 NULL COMMENT '1生效 0无效',
   `isdelete` tinyint(1) DEFAULT 0 NULL COMMENT '1删除 0正常',
   PRIMARY KEY (`id`)
);
