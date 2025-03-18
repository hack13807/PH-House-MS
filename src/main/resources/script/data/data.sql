-- 添加默认管理员用户
INSERT INTO t_user (number, name, pwd, role_id, status, create_user)
VALUES ('admin', '管理员', 'admin', 1, '1', 'system')
ON DUPLICATE KEY UPDATE last_update_time = CURRENT_TIMESTAMP;

-- 添加示例房间数据
INSERT INTO t_room (number, description, status, enable, create_user)
VALUES ('101', '一楼单人房', 0, 1, 'system'),
       ('102', '一楼双人房', 0, 1, 'system'),
       ('201', '二楼单人房', 0, 1, 'system'),
       ('202', '二楼双人房', 0, 1, 'system')
ON DUPLICATE KEY UPDATE last_update_time = CURRENT_TIMESTAMP; 