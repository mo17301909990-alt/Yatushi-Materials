#!/bin/bash
# CC Board — 多 Claude Code 实例协作白板
# 用法:
#   cc-board.sh signin  <project> [task]    # 签到
#   cc-board.sh update <task>               # 更新任务
#   cc-board.sh show                        # 查看所有在线
#   cc-board.sh signout                     # 签退
#   cc-board.sh clean                       # 清理超时（>30min无更新）

BOARD_DIR="$HOME/.claude/cc-board"
mkdir -p "$BOARD_DIR"

# 用主机名+PID 唯一标识当前实例
INSTANCE_ID="$(hostname)-$$"
INSTANCE_FILE="$BOARD_DIR/$INSTANCE_ID.json"

case "${1:-show}" in
  signin)
    PROJECT="${2:-unknown}"
    TASK="${3:-无}"
    cat > "$INSTANCE_FILE" <<EOF
{
  "instanceId": "$INSTANCE_ID",
  "hostname": "$(hostname)",
  "pid": $$,
  "project": "$PROJECT",
  "task": "$TASK",
  "shell": "$(basename "$SHELL")",
  "lastSeen": "$(date '+%Y-%m-%d %H:%M:%S')",
  "timestamp": $(date +%s)
}
EOF
    echo "✅ 已签到 | $PROJECT | $TASK"
    ;;
  update)
    TASK="${2:-无}"
    if [ -f "$INSTANCE_FILE" ]; then
      # 更新 task 和 lastSeen
      tmpf="$INSTANCE_FILE.tmp"
      jq --arg t "$TASK" --arg ts "$(date '+%Y-%m-%d %H:%M:%S')" --arg tn $(date +%s) \
        '.task = $t | .lastSeen = $ts | .timestamp = ($tn | tonumber)' \
        "$INSTANCE_FILE" > "$tmpf" && mv "$tmpf" "$INSTANCE_FILE"
      echo "🔄 任务更新: $TASK"
    else
      echo "⚠️  还没签到，先跑 cc-board.sh signin <project> [task]"
    fi
    ;;
  show)
    echo "═══════════════════════════════════════════════"
    echo "  CC Board — 在线 Claude Code 实例"
    echo "  $(date '+%Y-%m-%d %H:%M:%S')"
    echo "═══════════════════════════════════════════════"
    count=0
    now=$(date +%s)
    for f in "$BOARD_DIR"/*.json; do
      [ -f "$f" ] || continue
      instance=$(basename "$f" .json)
      project=$(jq -r '.project // "?"' "$f")
      task=$(jq -r '.task // "?"' "$f")
      lastSeen=$(jq -r '.lastSeen // "?"' "$f")
      ts=$(jq -r '.timestamp // 0' "$f")
      age=$(( now - ts ))
      age=$(( age / 60 ))

      # 超时标记
      if [ $age -gt 30 ]; then
        echo "  ⚫  $instance  (离线 ${age}min)"
        echo "      项目: $project | 最后任务: $task"
      else
        echo "  🟢  $instance  (在线 ${age}min)"
        echo "      项目: $project | 当前: $task"
        echo "      最后更新: $lastSeen"
      fi
      echo ""
      count=$((count + 1))
    done
    if [ $count -eq 0 ]; then
      echo "  (空 — 还没有实例签到)"
    fi
    echo "═══════════════════════════════════════════════"
    echo "  共 $count 个实例"
    ;;
  signout)
    rm -f "$INSTANCE_FILE"
    echo "👋 已签退"
    ;;
  clean)
    now=$(date +%s)
    cleaned=0
    for f in "$BOARD_DIR"/*.json; do
      [ -f "$f" ] || continue
      ts=$(jq -r '.timestamp // 0' "$f")
      age=$(( (now - ts) / 60 ))
      if [ "$age" -gt 60 ]; then
        rm -f "$f"
        cleaned=$((cleaned + 1))
      fi
    done
    echo "🧹 清理了 $cleaned 个超时实例（>60min）"
    ;;
  *)
    echo "用法: cc-board.sh {signin|update|show|signout|clean} [参数...]"
    ;;
esac
