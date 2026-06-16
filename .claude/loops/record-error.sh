#!/bin/bash
# 错误模式记录脚本
# 用法: record-error.sh <类别> <症状> <修复建议>
# 类别: compile / build / test / data-quality / db

LOG_DIR="$(cd "$(dirname "$0")" && pwd)"
LOG="$LOG_DIR/dev-quality.log"
CATEGORY="$1"
SYMPTOM="$2"
FIX="$3"
TIMESTAMP=$(date '+%Y-%m-%d %H:%M:%S')

echo "{\"ts\":\"$TIMESTAMP\",\"cat\":\"$CATEGORY\",\"sym\":\"$SYMPTOM\",\"fix\":\"$FIX\"}" >> "$LOG"
echo "[$TIMESTAMP] [$CATEGORY] $SYMPTOM -> $FIX" >> "$LOG.human"

COUNT=$(grep -c "$(date '+%Y-%m-%d')" "$LOG.human" 2>/dev/null || echo 0)
if [ "$COUNT" -ge 3 ] 2>/dev/null; then
  echo "[$TIMESTAMP] !! 今日已累计 $COUNT 次失败" >> "$LOG.human"
fi
