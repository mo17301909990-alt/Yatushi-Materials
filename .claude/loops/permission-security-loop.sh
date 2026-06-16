#!/bin/bash
# 权限安全审计回路 — 检查RBAC配置完整性
# 回路: permission-security (L0->L1)
# 触发: cron daily
# 目标: 零未授权访问

BASE_DIR="$(cd "$(dirname "$0")/../.." && pwd)"
LOG="$BASE_DIR/.claude/loops/permission-security.log"
TS=$(date '+%Y-%m-%d %H:%M:%S')
DB="${DB_NAME:-gold_foil_db}"

export PGPASSWORD=HryENprJrxThYSDz
PSQL="psql -h localhost -U postgres -d $DB -t -A"

echo "====== perm-audit $TS ======" >> "$LOG"

# 1. 无角色用户
$PSQL -c "
SELECT 'users_no_role', count(*)
FROM users u
LEFT JOIN user_roles ur ON u.id = ur.user_id
WHERE ur.role_id IS NULL" 2>>"$LOG" >>"$LOG"

# 2. 无权限角色
$PSQL -c "
SELECT 'roles_no_perm', count(*)
FROM roles r
LEFT JOIN role_permissions rp ON r.id = rp.role_id
WHERE rp.permission_id IS NULL" 2>>"$LOG" >>"$LOG"

# 3. 权限表总览
$PSQL -c "SELECT 'cnt_permissions', count(*) FROM permissions" 2>>"$LOG" >>"$LOG"
$PSQL -c "SELECT 'cnt_roles', count(*) FROM roles" 2>>"$LOG" >>"$LOG"
$PSQL -c "SELECT 'cnt_role_perms', count(*) FROM role_permissions" 2>>"$LOG" >>"$LOG"
$PSQL -c "SELECT 'cnt_users', count(*) FROM users" 2>>"$LOG" >>"$LOG"
$PSQL -c "SELECT 'cnt_user_roles', count(*) FROM user_roles" 2>>"$LOG" >>"$LOG"

# 4. 孤立权限引用
$PSQL -c "
SELECT 'orphan_role_perms', count(*)
FROM role_permissions rp
LEFT JOIN roles r ON rp.role_id = r.id
LEFT JOIN permissions p ON rp.permission_id = p.id
WHERE r.id IS NULL OR p.id IS NULL" 2>>"$LOG" >>"$LOG"

$PSQL -c "
SELECT 'orphan_user_roles', count(*)
FROM user_roles ur
LEFT JOIN users u ON ur.user_id = u.id
LEFT JOIN roles r ON ur.role_id = r.id
WHERE u.id IS NULL OR r.id IS NULL" 2>>"$LOG" >>"$LOG"

echo "====== done ======" >> "$LOG"
echo "perm-audit done: $TS"
