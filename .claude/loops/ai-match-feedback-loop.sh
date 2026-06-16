#!/bin/bash
# AI匹配反馈回路 — 采样AI推荐记录，统计置信度分布
# 回路: ai-match-feedback (L0->L1)
# 触发: manual
# 目标: AI匹配准确率>85%

BASE_DIR="$(cd "$(dirname "$0")/../.." && pwd)"
LOG="$BASE_DIR/.claude/loops/ai-match.log"
TS=$(date '+%Y-%m-%d %H:%M:%S')
DB="${DB_NAME:-gold_foil_db}"

export PGPASSWORD=HryENprJrxThYSDz
PSQL="psql -h localhost -U postgres -d $DB -t -A"

echo "====== ai-audit $TS ======" >> "$LOG"

# 1. 总推荐次数
$PSQL -c "SELECT 'total', count(*) FROM ai_recommendation_history" 2>>"$LOG" >>"$LOG"

# 2. 置信度分布 (<50, 50-80, >80)
$PSQL -c "
SELECT 'conf_bins',
  COALESCE(SUM(CASE WHEN confidence<50 THEN 1 END),0) AS low,
  COALESCE(SUM(CASE WHEN confidence BETWEEN 50 AND 80 THEN 1 END),0) AS medium,
  COALESCE(SUM(CASE WHEN confidence>80 THEN 1 END),0) AS high
FROM ai_recommendation_history" 2>>"$LOG" >>"$LOG"

# 3. 最近7天活跃度
$PSQL -c "
SELECT 'weekly_active', count(DISTINCT user_id)
FROM ai_recommendation_history
WHERE created_at >= NOW() - INTERVAL '7 days'" 2>>"$LOG" >>"$LOG"

# 4. 无推荐记录的条数
$PSQL -c "
SELECT 'empty_recommendations', count(*)
FROM ai_recommendation_history
WHERE recommendations IS NULL OR recommendations = '[]'::jsonb" 2>>"$LOG" >>"$LOG"

# 5. Top5 高频请求用户
$PSQL -c "
SELECT 'top_users', user_id, count(*)
FROM ai_recommendation_history
GROUP BY user_id ORDER BY count(*) DESC LIMIT 5" 2>>"$LOG" >>"$LOG"

echo "====== done ======" >> "$LOG"
echo "ai-audit done: $TS"
