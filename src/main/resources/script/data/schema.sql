-- 创建用户表
CREATE TABLE IF NOT EXISTS t_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    number VARCHAR(50) NOT NULL COMMENT '用户编号',
    name VARCHAR(50) NOT NULL COMMENT '用户名称',
    phone VARCHAR(20) COMMENT '电话',
    pwd VARCHAR(100) NOT NULL COMMENT '密码',
    role_id BIGINT COMMENT '角色ID',
    status VARCHAR(10) COMMENT '状态',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_user VARCHAR(50) COMMENT '创建用户',
    last_update_time TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    last_update_user VARCHAR(50) COMMENT '最后更新用户'
);

-- 创建房间表
CREATE TABLE IF NOT EXISTS t_room (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    number VARCHAR(50) NOT NULL COMMENT '房间号',
    description VARCHAR(500) COMMENT '房间描述',
    status INT DEFAULT 0 COMMENT '房间状态：0-空闲，1-已租',
    enable INT DEFAULT 1 COMMENT '是否可用：0-不可用，1-可用',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_user VARCHAR(50) COMMENT '创建用户',
    last_update_time TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    last_update_user VARCHAR(50) COMMENT '最后更新用户'
);

-- 创建会员表
CREATE TABLE IF NOT EXISTS t_member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL COMMENT '会员姓名',
    sex INT COMMENT '性别：0-男，1-女',
    tel VARCHAR(20) COMMENT '电话号码',
    idcard VARCHAR(50) COMMENT '身份证号',
    status INT DEFAULT 0 COMMENT '状态：0-正常，1-注销',
    isdelete INT DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_user VARCHAR(50) COMMENT '创建用户',
    last_update_time TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    last_update_user VARCHAR(50) COMMENT '最后更新用户'
);

-- 创建租约表
CREATE TABLE IF NOT EXISTS t_lease (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    lease_type INT COMMENT '租约类型',
    unit INT COMMENT '租金单位：1-月，2-季，3-年',
    rent_amount DECIMAL(10,2) COMMENT '租金金额',
    start_date TIMESTAMP COMMENT '开始日期',
    end_date TIMESTAMP COMMENT '结束日期',
    room_id BIGINT COMMENT '房间ID',
    number VARCHAR(50) NOT NULL COMMENT '租约编号',
    effective INT DEFAULT 1 COMMENT '是否有效：0-无效，1-有效',
    isdelete INT DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_user VARCHAR(50) COMMENT '创建用户',
    last_update_time TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    last_update_user VARCHAR(50) COMMENT '最后更新用户'
);

-- 创建租约-会员关系表
CREATE TABLE IF NOT EXISTS t_lease_member_rel (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    lease_id BIGINT NOT NULL COMMENT '租约ID',
    member_id BIGINT NOT NULL COMMENT '会员ID',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_user VARCHAR(50) COMMENT '创建用户',
    last_update_time TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    last_update_user VARCHAR(50) COMMENT '最后更新用户'
);

-- 创建收据表
CREATE TABLE IF NOT EXISTS t_receipt (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    receipt_type INT COMMENT '收据类型',
    pay_type INT COMMENT '支付类型',
    number VARCHAR(50) NOT NULL COMMENT '收据编号',
    lease_number VARCHAR(50) COMMENT '租约编号',
    room_no VARCHAR(50) COMMENT '房间号',
    member_name VARCHAR(50) COMMENT '会员姓名',
    member_idcard VARCHAR(50) COMMENT '会员身份证号',
    member_tel VARCHAR(20) COMMENT '会员电话',
    amount DECIMAL(10,2) COMMENT '金额',
    biz_date TIMESTAMP COMMENT '业务日期',
    effective INT DEFAULT 1 COMMENT '是否有效：0-无效，1-有效',
    isdelete INT DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_user VARCHAR(50) COMMENT '创建用户',
    last_update_time TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    last_update_user VARCHAR(50) COMMENT '最后更新用户'
);

-- 创建邮件提醒表
CREATE TABLE IF NOT EXISTS return_reminder (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    detail VARCHAR(500) COMMENT '提醒详情'
);

-- 创建邮件到达提醒表
CREATE TABLE IF NOT EXISTS arrive_reminder (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    detail VARCHAR(500) COMMENT '提醒详情'
); 