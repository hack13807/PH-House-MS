-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(255) NOT NULL COMMENT '密码',
  `real_name` VARCHAR(50) COMMENT '真实姓名',
  `phone` VARCHAR(20) COMMENT '手机号',
  `email` VARCHAR(100) COMMENT '邮箱',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `avatar` VARCHAR(255) COMMENT '头像URL',
  `role` VARCHAR(20) DEFAULT 'USER' COMMENT '角色：ADMIN-管理员，USER-普通用户',
  `last_login_time` DATETIME COMMENT '最后登录时间',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '用户表';

-- 用户备份表
CREATE TABLE IF NOT EXISTS `userbak` (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '备份ID',
  `user_id` INT NOT NULL COMMENT '用户ID',
  `backup_data` TEXT NOT NULL COMMENT '备份数据',
  `backup_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '备份时间'
) COMMENT '用户备份表';

-- 房间表
CREATE TABLE IF NOT EXISTS `room` (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '房间ID',
  `room_number` VARCHAR(50) NOT NULL COMMENT '房间号',
  `floor` VARCHAR(10) COMMENT '楼层',
  `area` DECIMAL(10,2) COMMENT '面积（平方米）',
  `type` VARCHAR(50) COMMENT '房型',
  `status` VARCHAR(20) DEFAULT 'AVAILABLE' COMMENT '状态：AVAILABLE-可用，LEASED-已租，MAINTENANCE-维护中',
  `monthly_rent` DECIMAL(10,2) COMMENT '月租金',
  `deposit` DECIMAL(10,2) COMMENT '押金',
  `description` TEXT COMMENT '描述',
  `facilities` TEXT COMMENT '设施配套',
  `images` TEXT COMMENT '图片URLs（JSON格式）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '房间表';

-- 租客表
CREATE TABLE IF NOT EXISTS `member` (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '租客ID',
  `name` VARCHAR(50) NOT NULL COMMENT '姓名',
  `gender` VARCHAR(10) COMMENT '性别',
  `age` INT COMMENT '年龄',
  `id_card` VARCHAR(20) COMMENT '身份证号',
  `phone` VARCHAR(20) NOT NULL COMMENT '手机号',
  `emergency_contact` VARCHAR(50) COMMENT '紧急联系人',
  `emergency_phone` VARCHAR(20) COMMENT '紧急联系人电话',
  `status` VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE-在租，INACTIVE-历史租客',
  `occupation` VARCHAR(100) COMMENT '职业',
  `work_place` VARCHAR(255) COMMENT '工作单位',
  `notes` TEXT COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '租客表';

-- 租约表
CREATE TABLE IF NOT EXISTS `lease` (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '租约ID',
  `room_id` INT NOT NULL COMMENT '房间ID',
  `member_id` INT NOT NULL COMMENT '租客ID',
  `start_date` DATE NOT NULL COMMENT '开始日期',
  `end_date` DATE NOT NULL COMMENT '结束日期',
  `monthly_rent` DECIMAL(10,2) NOT NULL COMMENT '月租金',
  `deposit` DECIMAL(10,2) COMMENT '押金',
  `deposit_paid` TINYINT DEFAULT 0 COMMENT '押金是否已支付：0-否，1-是',
  `deposit_return_date` DATE COMMENT '押金退还日期',
  `lease_term` INT COMMENT '租期（月）',
  `payment_method` VARCHAR(50) COMMENT '支付方式',
  `payment_day` INT COMMENT '每月付款日',
  `contract_file` VARCHAR(255) COMMENT '合同文件URL',
  `status` VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE-有效，EXPIRED-已过期，TERMINATED-已终止',
  `notes` TEXT COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '租约表';

-- 收据表
CREATE TABLE IF NOT EXISTS `receipt` (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '收据ID',
  `lease_id` INT NOT NULL COMMENT '租约ID',
  `member_id` INT NOT NULL COMMENT '租客ID',
  `room_id` INT NOT NULL COMMENT '房间ID',
  `amount` DECIMAL(10,2) NOT NULL COMMENT '金额',
  `payment_date` DATE NOT NULL COMMENT '支付日期',
  `payment_method` VARCHAR(50) COMMENT '支付方式',
  `type` VARCHAR(20) NOT NULL COMMENT '类型：RENT-租金，DEPOSIT-押金，OTHER-其他',
  `status` VARCHAR(20) DEFAULT 'PAID' COMMENT '状态：PAID-已支付，REFUNDED-已退款',
  `description` TEXT COMMENT '描述',
  `period_start` DATE COMMENT '费用开始日期',
  `period_end` DATE COMMENT '费用结束日期',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '收据表';

-- 邮件记录表
CREATE TABLE IF NOT EXISTS `mail` (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '邮件ID',
  `recipient` VARCHAR(100) NOT NULL COMMENT '收件人',
  `subject` VARCHAR(255) NOT NULL COMMENT '主题',
  `content` TEXT NOT NULL COMMENT '内容',
  `send_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  `status` VARCHAR(20) DEFAULT 'SENT' COMMENT '状态：SENT-已发送，FAILED-发送失败',
  `error_message` TEXT COMMENT '错误信息',
  `related_id` INT COMMENT '相关业务ID',
  `related_type` VARCHAR(50) COMMENT '相关业务类型'
) COMMENT '邮件记录表';

-- 创建索引
CREATE INDEX IF NOT EXISTS `idx_user_username` ON `user` (`username`);
CREATE INDEX IF NOT EXISTS `idx_room_status` ON `room` (`status`);
CREATE INDEX IF NOT EXISTS `idx_member_phone` ON `member` (`phone`);
CREATE INDEX IF NOT EXISTS `idx_lease_room_id` ON `lease` (`room_id`);
CREATE INDEX IF NOT EXISTS `idx_lease_member_id` ON `lease` (`member_id`);
CREATE INDEX IF NOT EXISTS `idx_receipt_lease_id` ON `receipt` (`lease_id`);
CREATE INDEX IF NOT EXISTS `idx_mail_recipient` ON `mail` (`recipient`); 