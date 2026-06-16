#!/bin/bash
# 数据质量回路 — 物料/BOM兼容性数据每日审计
# 回路: data-quality (L1->L2)
# 触发: cron daily

BASE_DIR="$(cd "$(dirname "$0")/../.." && pwd)"
LOG="$BASE_DIR/.claude/loops/data-quality.log"
TS=$(date '+%Y-%m-%d %H:%M:%S')
DB="gold_foil_db"

export PGPASSWORD=HryENprJrxThYSDz
PSQL="psql -h localhost -U postgres -d $DB -t -A"

echo "====== dq-audit $TS ======" >> "$LOG"

# row counts
$PSQL -c "SELECT 'cnt_products', count(*) FROM products" 2>>"$LOG" >>"$LOG"
$PSQL -c "SELECT 'cnt_material_process', count(*) FROM material_process_compatibility" 2>>"$LOG" >>"$LOG"
$PSQL -c "SELECT 'cnt_hotstamp', count(*) FROM hot_stamping_compatibility" 2>>"$LOG" >>"$LOG"
$PSQL -c "SELECT 'cnt_specs', count(*) FROM specifications" 2>>"$LOG" >>"$LOG"
$PSQL -c "SELECT 'cnt_pricing', count(*) FROM pricing" 2>>"$LOG" >>"$LOG"

# compatibility status distribution
$PSQL -c "SELECT 'st_material', compatibility_status, count(*) FROM material_process_compatibility GROUP BY compatibility_status ORDER BY compatibility_status" 2>>"$LOG" >>"$LOG"
$PSQL -c "SELECT 'st_hotstamp', compatibility, count(*) FROM hot_stamping_compatibility GROUP BY compatibility ORDER BY compatibility" 2>>"$LOG" >>"$LOG"

echo "====== done ======" >> "$LOG"
echo "dq-audit done: $TS"
