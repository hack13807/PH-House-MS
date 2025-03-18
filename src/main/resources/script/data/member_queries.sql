-- 查询所有租客
SELECT m.id, m.name, m.sex, m.tel, m.idcard, m.status, m.isdelete
FROM t_member m
WHERE m.isdelete = 0;

-- 查询租客和房间关系
SELECT 
    m.id, m.name, m.sex, m.tel, m.idcard, m.status,
    r.id as room_id, r.number as room_no,
    l.id as lease_id, l.number as lease_no
FROM t_member m
LEFT JOIN t_lease_member_rel rel ON rel.member_id = m.id
LEFT JOIN t_lease l ON l.id = rel.lease_id AND l.effective = 1 AND l.isdelete = 0
LEFT JOIN t_room r ON l.room_id = r.id
WHERE m.isdelete = 0
ORDER BY m.id;

-- 根据房间号查询租客
SELECT 
    m.id, m.name, m.sex, m.tel, m.idcard, m.status,
    r.id as room_id, r.number as room_no
FROM t_member m
LEFT JOIN t_lease_member_rel rel ON rel.member_id = m.id
LEFT JOIN t_lease l ON l.id = rel.lease_id AND l.effective = 1 AND l.isdelete = 0
LEFT JOIN t_room r ON l.room_id = r.id
WHERE r.number = ? AND m.isdelete = 0;

-- 根据租客姓名、电话或身份证模糊查询
SELECT 
    m.id, m.name, m.sex, m.tel, m.idcard, m.status,
    r.id as room_id, r.number as room_no
FROM t_member m
LEFT JOIN t_lease_member_rel rel ON rel.member_id = m.id
LEFT JOIN t_lease l ON l.id = rel.lease_id AND l.effective = 1 AND l.isdelete = 0
LEFT JOIN t_room r ON l.room_id = r.id
WHERE 
    (m.name LIKE CONCAT('%', ?, '%') OR 
     m.tel LIKE CONCAT('%', ?, '%') OR 
     m.idcard LIKE CONCAT('%', ?, '%'))
    AND m.isdelete = 0;

-- 检查租客状态
SELECT id, name, sex, tel, idcard, status
FROM t_member
WHERE id IN (?) AND status = 1;

-- 统计每个房间的租客数量
SELECT 
    r.id as room_id, 
    r.number as room_no, 
    COUNT(DISTINCT m.id) as member_count
FROM t_room r
LEFT JOIN t_lease l ON l.room_id = r.id AND l.effective = 1 AND l.isdelete = 0
LEFT JOIN t_lease_member_rel rel ON rel.lease_id = l.id
LEFT JOIN t_member m ON m.id = rel.member_id AND m.isdelete = 0
GROUP BY r.id, r.number;

-- 租客身份证唯一性检查
SELECT id, name, idcard
FROM t_member
WHERE idcard = ? AND isdelete = 0 AND id != ?; 