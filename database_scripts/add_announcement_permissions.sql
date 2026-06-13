-- ============================================
-- 添加公告管理相关权限
-- 用于控制公告的查看、创建、编辑、删除等操作
-- ============================================

-- 1. 插入公告管理权限
-- 注意：根据实际权限表结构，字段名是 permission_type 而不是 type
-- 如果表中有 status 字段，会自动使用默认值（通常是 1）
INSERT INTO permissions (permission_name, permission_key, description, permission_type, order_num) VALUES
-- 公告管理菜单权限
('消息管理', 'announcement:view', '查看消息中心', 'MENU', 100),
-- 公告操作权限
('发布公告', 'announcement:create', '创建并发布公告', 'BUTTON', 101),
('编辑公告', 'announcement:edit', '编辑已发布的公告', 'BUTTON', 102),
('删除公告', 'announcement:delete', '删除公告', 'BUTTON', 103),
('撤回公告', 'announcement:revoke', '撤回已发布的公告', 'BUTTON', 104),
('查看公告详情', 'announcement:detail', '查看公告详情', 'BUTTON', 105)
ON CONFLICT (permission_key) DO NOTHING;

-- 2. 为管理员角色分配所有公告权限
-- 获取管理员角色ID
DO $$
DECLARE
    admin_role_id INTEGER;
    perm_ids INTEGER[];
BEGIN
    -- 获取管理员角色ID
    SELECT id INTO admin_role_id FROM roles WHERE role_key = 'ADMIN';
    
    IF admin_role_id IS NOT NULL THEN
        -- 获取所有公告相关权限ID
        SELECT ARRAY_AGG(id) INTO perm_ids
        FROM permissions
        WHERE permission_key IN (
            'announcement:view',
            'announcement:create',
            'announcement:edit',
            'announcement:delete',
            'announcement:revoke',
            'announcement:detail'
        );
        
        -- 为管理员角色分配权限
        INSERT INTO role_permissions (role_id, permission_id)
        SELECT admin_role_id, unnest(perm_ids)
        WHERE NOT EXISTS (
            SELECT 1 FROM role_permissions 
            WHERE role_id = admin_role_id AND permission_id = ANY(perm_ids)
        );
        
        RAISE NOTICE '已为管理员角色分配公告权限，角色ID: %, 权限数量: %', admin_role_id, array_length(perm_ids, 1);
    ELSE
        RAISE NOTICE '未找到管理员角色，请先创建ADMIN角色';
    END IF;
END $$;

-- 3. 为普通用户角色分配查看权限（可选）
-- 普通用户只能查看自己的消息，不能管理公告
DO $$
DECLARE
    user_role_id INTEGER;
    view_perm_id INTEGER;
BEGIN
    -- 获取普通用户角色ID
    SELECT id INTO user_role_id FROM roles WHERE role_key = 'USER';
    
    -- 获取查看权限ID
    SELECT id INTO view_perm_id FROM permissions WHERE permission_key = 'announcement:view';
    
    IF user_role_id IS NOT NULL AND view_perm_id IS NOT NULL THEN
        -- 为普通用户角色分配查看权限
        INSERT INTO role_permissions (role_id, permission_id)
        VALUES (user_role_id, view_perm_id)
        ON CONFLICT (role_id, permission_id) DO NOTHING;
        
        RAISE NOTICE '已为普通用户角色分配公告查看权限';
    END IF;
END $$;

-- 4. 查询结果验证
SELECT 
    r.role_name,
    r.role_key,
    p.permission_name,
    p.permission_key,
    p.type
FROM roles r
INNER JOIN role_permissions rp ON r.id = rp.role_id
INNER JOIN permissions p ON rp.permission_id = p.id
WHERE p.permission_key LIKE 'announcement:%'
ORDER BY r.role_key, p.permission_key;

-- ============================================
-- 使用说明：
-- 1. 执行此脚本后，管理员角色将拥有所有公告管理权限
-- 2. 普通用户角色将拥有查看权限（可选，根据业务需求调整）
-- 3. 可以通过角色管理界面动态调整权限分配
-- 4. 前端使用 v-permission 指令控制按钮显示：
--    - v-permission="'announcement:view'"   - 查看消息中心
--    - v-permission="'announcement:create'" - 发布公告按钮
--    - v-permission="'announcement:edit'"   - 编辑公告按钮
--    - v-permission="'announcement:delete'" - 删除公告按钮
-- ============================================

