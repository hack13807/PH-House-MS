ALTER TABLE `t_room`
DROP COLUMN `description`,
ADD COLUMN `description` varchar(255) NOT NULL DEFAULT '' COMMENT '描述' AFTER `member_id`;

ALTER TABLE `hms`.`t_room`
DROP COLUMN `description`,
ADD COLUMN `status` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '房间状态  0待租  1出租中  2待处理' AFTER `number`,
ADD COLUMN `description` varchar(255) NOT NULL DEFAULT '' COMMENT '描述' AFTER `member_id`;

ALTER TABLE `hms`.`t_member`
ADD COLUMN `sex` tinyint(1) NOT NULL COMMENT '性别' AFTER `name`,
MODIFY COLUMN `status` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'status：0正常  1退租' AFTER `room_id`;
ALTER TABLE `hms`.`t_member`
MODIFY COLUMN `sex` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '性别' AFTER `name`;