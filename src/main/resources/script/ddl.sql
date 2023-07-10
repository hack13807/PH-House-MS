ALTER TABLE `t_room` DROP COLUMN `description`;
ALTER TABLE `t_room` ADD (`description` varchar(255) DEFAULT '' NOT NULL COMMENT '描述');

ALTER TABLE `t_room` DROP COLUMN `description`;
ALTER TABLE `t_room` ADD (`description` varchar(255) DEFAULT '' NOT NULL COMMENT '描述');

ALTER TABLE `t_member` ADD (`sex` tinyint(1) NOT NULL COMMENT '性别');
ALTER TABLE `t_member` ALTER COLUMN `status` tinyint(1) DEFAULT 0 NOT NULL COMMENT 'status：0正常  1退租';

ALTER TABLE `t_member` ALTER COLUMN `sex` tinyint(1) DEFAULT 0 NOT NULL COMMENT '性别';