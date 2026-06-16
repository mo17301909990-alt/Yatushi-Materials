#!/bin/bash
# 兼容性规则审计回路 — 检查兼容性数据一致性
# 回路: compatibility-audit (L0->L1)
# 触发: cron weekly
# 目标: 兼容性规则一致性100%

BASE_DIR="$(cd "$(dirname "$0")/../.." && pwd)"
LOG="$BASE_DIR/.claude/loops/compatibility-audit.log"
TS=$(date '+%Y-%m-%d %H:%M:%S')
DB="${DB_NAME:-gold_foil_db}"

export PGPASSWORD=HryENprJrxThYSDz
PSQL="psql -h localhost -U postgres -d $DB -t -A"

echo "====== compat-audit $TS ======" >> "$LOG"

# 1. material_process_compatibility 重复记录检查
$PSQL -c "
SELECT 'dup_material', COALESCE(SUM(cnt),0)
FROM (
  SELECT count(*) AS cnt
  FROM material_process_compatibility
  GROUP BY product_id, process_id, material_id, compatibility_status
  HAVING count(*) > 1
) t" 2>>"$LOG" >>"$LOG"

# 2. material_process_compatibility 孤立记录（product不存在）
$PSQL -c "
SELECT 'orphan_products', count(*)
FROM material_process_compatibility m
LEFT JOIN products p ON m.product_id = p.id
WHERE p.id IS NULL" 2>>"$LOG" >>"$LOG"

# 3. hot_stamping_compatibility 重复检查
$PSQL -c "
SELECT 'dup_hotstamp', COALESCE(SUM(cnt),0)
FROM (
  SELECT count(*) AS cnt
  FROM hot_stamping_compatibility
  GROUP BY product_id, type_id, gold_foil_type_id, compatibility
  HAVING count(*) > 1
) t" 2>>"$LOG" >>"$LOG"

# 4. 兼容性状态分布统计
$PSQL -c "
SELECT 'st_material_statuses', compatibility_status, count(*)
FROM material_process_compatibility
GROUP BY compatibility_status ORDER BY compatibility_status" 2>>"$LOG" >>"$LOG"

$PSQL -c "
SELECT 'st_hotstamp_statuses', compatibility, count(*)
FROM hot_stamping_compatibility
GROUP BY compatibility ORDER BY compatibility" 2>>"$LOG" >>"$LOG"

# 5. 各模块总记录数
$PSQL -c "SELECT 'cnt_material', count(*) FROM material_process_compatibility" 2>>"$LOG" >>"$LOG"
$PSQL -c "SELECT 'cnt_hotstamp', count(*) FROM hot_stamping_compatibility" 2>>"$LOG" >>"$LOG"
$PSQL -c "SELECT 'cnt_products', count(*) FROM products" 2>>"$LOG" >>"$LOG"

echo "====== done ======" >> "$LOG"
echo "compat-audit done: $TS"
