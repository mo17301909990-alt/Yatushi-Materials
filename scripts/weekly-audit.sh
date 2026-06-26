#!/usr/bin/env bash
# scripts/weekly-audit.sh — YTS 每周综合审计脚本
#
# 作用:
#   每周一 9AM 自动运行，执行全套质量门禁并产出结构化审计报告。
#   数据写入 .claude/state/test-snapshot.json，供 /dispatch review 引用。
#
# 步骤:
#   1. Quality Gate（编译 + 前端构建 + 测试 + 冒烟 + 假绿检查）
#   2. E2E Smoke（页面可达性 + 后端 API 代理）
#   3. Coverage（前端测试覆盖率）
#   4. 写入带时间戳的 JSON 报告
#
# 用法:
#   bash scripts/weekly-audit.sh
#
# 退出码:
#   0 — 全部通过
#   1 — 有失败项

set -u

PROJECT_ROOT="/f/yatushi-6-6"
FRONTEND_DIR="$PROJECT_ROOT/yts_project_vueai"
STATE_DIR="$PROJECT_ROOT/.claude/state"
SNAPSHOT_FILE="$STATE_DIR/test-snapshot.json"
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

TIMESTAMP=$(date -u +"%Y-%m-%dT%H:%M:%SZ")

QUALITY_GATE_RESULT="fail"
E2E_SMOKE_RESULT="fail"
TESTS_TOTAL=0
TESTS_PASSED=0
TESTS_FAILED=0
COV_STATEMENTS=0
COV_BRANCHES=0
COV_FUNCTIONS=0
COV_LINES=0
FALSE_GREENS=0

FAIL_COUNT=0

# ── 辅助函数 ──────────────────────────────────────────────────────────────────
warn() { echo "  [WARN] $*"; }
pass() { echo "  [PASS] $*"; }
fail() { echo "  [FAIL] $*"; FAIL_COUNT=$((FAIL_COUNT + 1)); }
fail_stage() { echo "  [FAIL] $*"; }

section() {
  echo ""
  echo "═══════════════════════════════════════════════════════"
  echo "  $*"
  echo "═══════════════════════════════════════════════════════"
}

# ── 确保 state 目录存在 ───────────────────────────────────────────────────────
mkdir -p "$STATE_DIR"

# ═══════════════════════════════════════════════════════════════════════════════
#   STEP 1: Quality Gate
# ═══════════════════════════════════════════════════════════════════════════════
section "STEP 1/4: Quality Gate"

if bash "$PROJECT_ROOT/scripts/quality-gate.sh"; then
  QUALITY_GATE_RESULT="pass"
  pass "Quality Gate"
else
  QUALITY_GATE_RESULT="fail"
  fail_stage "Quality Gate"
fi

# ═══════════════════════════════════════════════════════════════════════════════
#   STEP 2: E2E Smoke
# ═══════════════════════════════════════════════════════════════════════════════
section "STEP 2/4: E2E Smoke"

if bash "$PROJECT_ROOT/scripts/e2e-smoke.sh"; then
  E2E_SMOKE_RESULT="pass"
  pass "E2E Smoke"
else
  E2E_SMOKE_RESULT="fail"
  fail_stage "E2E Smoke"
fi

# ═══════════════════════════════════════════════════════════════════════════════
#   STEP 3: Test Results + Coverage
# ═══════════════════════════════════════════════════════════════════════════════
section "STEP 3/4: Tests & Coverage"

COVERAGE_OUTPUT=""
if cd "$FRONTEND_DIR" 2>/dev/null; then
  COVERAGE_OUTPUT=$(npm run test:coverage 2>&1)
  COV_EXIT_CODE=$?
else
  COVERAGE_OUTPUT="ERROR: cannot cd to $FRONTEND_DIR"
  COV_EXIT_CODE=1
fi

# ── 解析 Test Results ─────────────────────────────────────────────────────────
# 输出格式: "Tests  1 failed | 251 passed (252)"
TESTS_LINE=$(echo "$COVERAGE_OUTPUT" | grep -E '^\s+Tests\s+' | tail -1)
if [ -n "$TESTS_LINE" ]; then
  TESTS_FAILED=$(echo "$TESTS_LINE" | sed -n 's/.* \([0-9]\+\) failed.*/\1/p')
  TESTS_PASSED=$(echo "$TESTS_LINE" | sed -n 's/.* \([0-9]\+\) passed.*/\1/p')
  TESTS_TOTAL=$(echo "$TESTS_LINE" | sed -n 's/.*(\([0-9]\+\))/\1/p')

  # fallback: if sed didn't capture, try grep
  [ -z "$TESTS_FAILED" ] && TESTS_FAILED=0
  [ -z "$TESTS_PASSED" ] && TESTS_PASSED=$(echo "$TESTS_LINE" | grep -oP '\d+(?= passed)' || echo 0)
  [ -z "$TESTS_TOTAL" ]  && TESTS_TOTAL=$((TESTS_PASSED + TESTS_FAILED))

  TESTS_FAILED=${TESTS_FAILED:-0}
  TESTS_PASSED=${TESTS_PASSED:-0}
  TESTS_TOTAL=${TESTS_TOTAL:-0}

  echo "  Tests: $TESTS_TOTAL total, $TESTS_PASSED passed, $TESTS_FAILED failed"
fi

# ── 解析 Coverage ─────────────────────────────────────────────────────────────
# vitest v8 coverage 报告格式:
#   "All files"        | 57.14 |  43.75 |  47.82 |  57.14 |
#   "src/api/modules/" | 80.00 |  72.72 |  71.42 |  80.00 |
# 或 JSONL 格式:
#   {...,"statements":{"pct":57.14},"branches":{"pct":43.75},...}
#
# 优先从 JSON 格式的 summary 提取（更精确）
# 如果 vitest 输出了 JSON 报告，用 jq 提取
if command -v jq &>/dev/null; then
  # 找最近的 coverage/coverage-summary.json
  COV_SUMMARY="$FRONTEND_DIR/coverage/coverage-summary.json"
  if [ -f "$COV_SUMMARY" ]; then
    COV_STATEMENTS=$(jq -r '.total.statements.pct // 0' "$COV_SUMMARY" 2>/dev/null || echo 0)
    COV_BRANCHES=$(jq -r '.total.branches.pct // 0' "$COV_SUMMARY" 2>/dev/null || echo 0)
    COV_FUNCTIONS=$(jq -r '.total.functions.pct // 0' "$COV_SUMMARY" 2>/dev/null || echo 0)
    COV_LINES=$(jq -r '.total.lines.pct // 0' "$COV_SUMMARY" 2>/dev/null || echo 0)
  fi
fi

# fallback: 从 vitest 文本输出解析（表格格式）
if [ "$COV_STATEMENTS" = "0" ] || [ -z "$COV_STATEMENTS" ]; then
  COV_LINE=$(echo "$COVERAGE_OUTPUT" | grep -E '^\|\s+All files\s+' | head -1)
  if [ -z "$COV_LINE" ]; then
    # 有些版本用 "All files" 不带 \s+
    COV_LINE=$(echo "$COVERAGE_OUTPUT" | grep -E 'All files' | head -1)
  fi
  if [ -n "$COV_LINE" ]; then
    # 格式: | All files | 57.14 | 43.75 | 47.82 | 57.14 |
    COV_VALUES=$(echo "$COV_LINE" | grep -oP '\d+\.?\d*' | head -4)
    COV_ARR=($COV_VALUES)
    COV_STATEMENTS=${COV_ARR[0]:-0}
    COV_BRANCHES=${COV_ARR[1]:-0}
    COV_FUNCTIONS=${COV_ARR[2]:-0}
    COV_LINES=${COV_ARR[3]:-0}
  fi
fi

echo "  Coverage: statements=${COV_STATEMENTS}%, branches=${COV_BRANCHES}%, functions=${COV_FUNCTIONS}%, lines=${COV_LINES}%"

# ═══════════════════════════════════════════════════════════════════════════════
#   STEP 4: False Green Check
# ═══════════════════════════════════════════════════════════════════════════════
section "STEP 4/4: False Green Detection"

FG_OUTPUT=""
if [ -f "$PROJECT_ROOT/scripts/check-false-green.ts" ]; then
  FG_OUTPUT=$(cd "$PROJECT_ROOT" && npx tsx scripts/check-false-green.ts 2>&1)
  FG_EXIT=$?
  FALSE_GREENS=$(echo "$FG_OUTPUT" | grep -oP '(?<=可疑测试|FALSE_GREEN|假绿|false.green)\s*:\s*\K\d+' | head -1)
  if [ -z "$FALSE_GREENS" ]; then
    FALSE_GREENS=$(echo "$FG_OUTPUT" | grep -c -i 'false.green\|假绿\|suspicious' 2>/dev/null || echo 0)
  fi
  if [ "$FG_EXIT" -eq 0 ]; then
    pass "False-green check"
  else
    fail_stage "False-green check detected issues"
  fi
else
  warn "check-false-green.ts not found, skipping"
fi

FALSE_GREENS=${FALSE_GREENS:-0}

# ═══════════════════════════════════════════════════════════════════════════════
#   WRITE REPORT
# ═══════════════════════════════════════════════════════════════════════════════
section "Writing Report"

# 清理旧的覆盖报告（避免下次误读）
rm -rf "$FRONTEND_DIR/coverage" 2>/dev/null

cat > "$SNAPSHOT_FILE" <<EOF
{
  "timestamp": "$TIMESTAMP",
  "quality_gate": "$QUALITY_GATE_RESULT",
  "e2e_smoke": "$E2E_SMOKE_RESULT",
  "tests": {
    "total": $TESTS_TOTAL,
    "passed": $TESTS_PASSED,
    "failed": $TESTS_FAILED
  },
  "coverage": {
    "statements": $COV_STATEMENTS,
    "branches": $COV_BRANCHES,
    "functions": $COV_FUNCTIONS,
    "lines": $COV_LINES
  },
  "false_greens": $FALSE_GREENS
}
EOF

echo "  Report written to: $SNAPSHOT_FILE"

# ═══════════════════════════════════════════════════════════════════════════════
#   SUMMARY
# ═══════════════════════════════════════════════════════════════════════════════
section "Summary"

echo "  Quality Gate:   $QUALITY_GATE_RESULT"
echo "  E2E Smoke:      $E2E_SMOKE_RESULT"
echo "  Tests:          $TESTS_TOTAL total, $TESTS_PASSED passed, $TESTS_FAILED failed"
echo "  Coverage:       $COV_STATEMENTS% / $COV_BRANCHES% / $COV_FUNCTIONS% / $COV_LINES%"
echo "  False Greens:   $FALSE_GREENS"
echo ""

if [ "$FAIL_COUNT" -gt 0 ]; then
  echo "  Audit completed with $FAIL_COUNT failure(s)."
  exit 1
else
  echo "  Weekly audit completed successfully."
  exit 0
fi
