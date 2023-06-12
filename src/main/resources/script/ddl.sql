ALTER TABLE `t_room`
DROP COLUMN `description`,
ADD COLUMN `description` varchar(255) NOT NULL DEFAULT '' COMMENT '描述' AFTER `member_id`;

ALTER TABLE `hms`.`t_room`
DROP COLUMN `description`,
ADD COLUMN `status` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '房间状态  0待租  1出租中  2待处理' AFTER `number`,
ADD COLUMN `description` varchar(255) NOT NULL DEFAULT '' COMMENT '描述' AFTER `member_id`;