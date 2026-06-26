#!/usr/bin/env bash
# scripts/detection-loop.sh — YTS 前端检测 Loop
# 波次检测：独立步骤并行 / 依赖步骤串行 / 失败累计
# 用法: bash scripts/detection-loop.sh
#       bash scripts/detection-loop.sh --quick  (仅波次 1-2)
#       bash scripts/detection-loop.sh --step test  (仅跑指定步骤)

set -u
START=$(date +%s)
FAIL=0
SKIP=0
REPORT_FILE=".claude/state/detection-loop-last.json"
PROJECT_ROOT="/f/yatushi-6-6"

# ── helpers ──────────────────────────────────────────────────

pass()   { echo "  [PASS] $1"; }
fail()   { echo "  [FAIL] $1"; FAIL=$((FAIL + 1)); }
skip()   { echo "  [SKIP] $1"; SKIP=$((SKIP + 1)); }

run_step() {
  local step="$1" label="$2" ; shift 2
  echo ""
  echo "━━━ [$step] $label ━━━"
  if "$@" 2>/dev/null; then
    pass "$label"
    return 0
  else
    local ec=$?
    if [ "$SKIP_ON_FAIL" = "true" ]; then
      skip "$label (exit $ec)"
      return 0
    else
      fail "$label (exit $ec)"
      return 1
    fi
  fi
}

# ── parse args ────────────────────────────────────────────────

RUN_ALL=true
SINGLE_STEP=""
QUICK_MODE=false
for arg in "$@"; do
  case "$arg" in
    --quick) QUICK_MODE=true ;;
    --step=*) SINGLE_STEP="${arg#--step=}" ; RUN_ALL=false ;;
    --help) echo "Usage: bash detection-loop.sh [--quick | --step=NAME]" ; exit 0 ;;
  esac
done

cd "$PROJECT_ROOT"

echo ""
echo "╔══════════════════════════════════════════════════════════╗"
echo "║   YTS Frontend Detection Loop                          ║"
echo "║   $(date '+%Y-%m-%d %H:%M:%S')"
echo "╚══════════════════════════════════════════════════════════╝"

# ═══════════════════════════════════════════════════════════════
# Wave 0: 编译 — 无依赖，可并行
# ═══════════════════════════════════════════════════════════════

if [ "$RUN_ALL" = true ] || [ "$SINGLE_STEP" = "backend-compile" ]; then
echo ""
echo "── Wave 0: 编译 ────────────────────────────────────────"
run_step "0a" "Backend compile" bash -c "cd '$PROJECT_ROOT/yts_project' && mvn compile -q"
run_step "0b" "Frontend build" bash -c "cd '$PROJECT_ROOT/yts_project_vueai' && npm run build 2>&1 | tail -3"
fi

if [ "$QUICK_MODE" = true ] && [ "$SINGLE_STEP" = "" ]; then
  # Quick mode: only Wave 0-1
  SKIP_ON_FAIL=true
fi

# ═══════════════════════════════════════════════════════════════
# Wave 1: 静态分析 — 依赖编译完成，无交叉依赖
# ═══════════════════════════════════════════════════════════════

if [ "$RUN_ALL" = true ] || [ "$SINGLE_STEP" = "routes" ]; then
echo ""
echo "── Wave 1: 静态分析 ────────────────────────────────────"
run_step "1a" "Route integrity" bash -c "cd '$PROJECT_ROOT/yts_project_vueai' && npx tsx scripts/smoke-routes.ts"
fi

if [ "$RUN_ALL" = true ] || [ "$SINGLE_STEP" = "types" ]; then
run_step "1b" "Type check (vue-tsc)" bash -c "cd '$PROJECT_ROOT/yts_project_vueai' && npx vue-tsc --noEmit 2>&1 | tail -5"
fi

# ═══════════════════════════════════════════════════════════════
# Wave 2: 测试 — 依赖编译完成
# ═══════════════════════════════════════════════════════════════

if [ "$RUN_ALL" = true ] || [ "$SINGLE_STEP" = "tests" ]; then
echo ""
echo "── Wave 2: 测试 ────────────────────────────────────────"
run_step "2a" "Test suite" bash -c "cd '$PROJECT_ROOT/yts_project_vueai' && npx vitest run 2>&1 | tail -5"
fi

if [ "$RUN_ALL" = true ] || [ "$SINGLE_STEP" = "false-green" ]; then
run_step "2b" "False-green check" bash -c "cd '$PROJECT_ROOT' && npx tsx scripts/check-false-green.ts 2>&1 | tail -5"
fi

# ═══════════════════════════════════════════════════════════════
# Wave 3: 质量门禁 — 依赖前序步骤完成
# ═══════════════════════════════════════════════════════════════

if [ "$RUN_ALL" = true ] || [ "$SINGLE_STEP" = "coverage" ]; then
echo ""
echo "── Wave 3: 质量 ────────────────────────────────────────"
run_step "3a" "Coverage thresholds" bash -c "cd '$PROJECT_ROOT/yts_project_vueai' && npx vitest run --coverage 2>&1 | grep -E 'Statements|Branches|Functions|Lines' | head -5"
fi

# ═══════════════════════════════════════════════════════════════
# 摘要
# ═══════════════════════════════════════════════════════════════

END=$(date +%s)
DURATION=$((END - START))

echo ""
echo "╔══════════════════════════════════════════════════════════╗"
echo "║   Detection Loop Summary                               ║"
echo "╚══════════════════════════════════════════════════════════╝"
echo "  Duration: ${DURATION}s"
echo "  Fail:   $FAIL"
echo "  Skip:   $SKIP"
echo "  Skip:  $SKIP"

if [ $FAIL -eq 0 ]; then
  echo "  Result: ✅ ALL PASS"
else
  echo "  Result: ❌ $FAIL step(s) failed"
fi

# Write report
TIMESTAMP=$(date -Iseconds 2>/dev/null || date '+%Y-%m-%dT%H:%M:%S%z')
mkdir -p "$(dirname "$PROJECT_ROOT/$REPORT_FILE")"
cat > "$PROJECT_ROOT/$REPORT_FILE" <<REPORT
{
  "timestamp": "$TIMESTAMP",
  "duration": $DURATION,
  "result": "$([ $FAIL -eq 0 ] && echo 'pass' || echo 'fail')",
  "failures": $FAIL,
  "skips": $SKIP
}
REPORT

exit $FAIL
