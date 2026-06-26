#!/usr/bin/env bash
set -euo pipefail

# =============================================================================
# run-tests-isolated.sh - YTS Vue SPA (vitest) 隔离测试运行器
#
# 用途：
#   在隔离的临时目录中运行 yts_project_vueai/ 的 vitest 测试套件，
#   捕获退出码、测试输出，最后清理临时文件并输出 JSON 摘要。
#
# 用法：
#   bash scripts/run-tests-isolated.sh [--coverage] [--verbose]
#
# 选项：
#   --coverage  同时生成覆盖率报告（vitest run --coverage）
#   --verbose   显示完整测试输出（默认只显示摘要行）
#
# 返回值：
#   0 - 所有测试通过
#   1 - 部分测试失败 / 编译错误
#   2 - 脚本内部错误（目录不存在、命令找不到等）
# =============================================================================

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
VUEAI_DIR="$PROJECT_ROOT/yts_project_vueai"

# ---- 配置 ----
COVERAGE=false
VERBOSE=false
EXIT_CODE=0

# ---- 解析参数 ----
while [[ $# -gt 0 ]]; do
  case "$1" in
    --coverage) COVERAGE=true; shift ;;
    --verbose)  VERBOSE=true;  shift ;;
    *)
      echo "[run-tests-isolated] 未知参数: $1" >&2
      echo "用法: bash $0 [--coverage] [--verbose]" >&2
      exit 2
      ;;
  esac
done

# ---- 前置检查 ----
if ! command -v node &>/dev/null; then
  echo '{"status":"error","reason":"node is not installed","exitCode":2}'
  exit 2
fi

if [[ ! -d "$VUEAI_DIR" ]]; then
  echo "{\"status\":\"error\",\"reason\":\"vueai dir not found: $VUEAI_DIR\",\"exitCode\":2}"
  exit 2
fi

# ---- 创建临时目录 ----
TMPDIR="$(mktemp -d "${TMPDIR:-/tmp}/yts-test-isolated.XXXXXXXXXX")"
trap 'rm -rf "$TMPDIR"' EXIT

# ---- 设置隔离环境变量 ----
export NODE_ENV=test
export VITE_USER_NO_IMPORTS=1
export VITEST_SEGFAULT_RETRY=3
export CI=true
export TMPDIR="$TMPDIR"
# 阻止 Vite 扫描 node_modules/.vite 缓存
export VITE_CACHE_DIR="$TMPDIR/.vite"

echo "[run-tests-isolated] 测试项目: yts_project_vueai"
echo "[run-tests-isolated] 临时目录: $TMPDIR"

# ---- 检查 node_modules ----
if [[ ! -d "$VUEAI_DIR/node_modules" ]]; then
  echo "[run-tests-isolated] node_modules 不存在，运行 npm install..." >&2
  (cd "$VUEAI_DIR" && npm install --silent) || {
    echo '{"status":"error","reason":"npm install failed","exitCode":2}'
    exit 2
  }
fi

# ---- 检查 vitest 可执行 ----
VITEST_MJS="$VUEAI_DIR/node_modules/vitest/vitest.mjs"
if [[ ! -f "$VITEST_MJS" ]]; then
  echo '{"status":"error","reason":"vitest is not installed, run npm install first","exitCode":2}'
  exit 2
fi

# ---- 运行测试 ----
OUTPUT_FILE="$TMPDIR/test-output.txt"
SUMMARY_FILE="$TMPDIR/test-summary.json"

echo "[run-tests-isolated] 执行: vitest run [--coverage=$COVERAGE]"
echo "----------------------------------------"

set +e
# 在 vueai 目录下运行，确保相对路径和 @ 别名解析正确
(cd "$VUEAI_DIR" && \
  NODE_ENV=test \
  CI=true \
  TMPDIR="$TMPDIR" \
  VITE_CACHE_DIR="$TMPDIR/.vite" \
  node --experimental-vm-modules "$VITEST_MJS" run \
    --config "$VUEAI_DIR/vite.config.ts" \
    --reporter verbose \
    $($COVERAGE && echo "--coverage") \
) > "$OUTPUT_FILE" 2>&1
EXIT_CODE=$?
set -e

# ---- 解析结果 ----
if $VERBOSE; then
  echo ""
  echo "========== 测试输出 =========="
  cat "$OUTPUT_FILE"
  echo "============================="
else
  # 提取关键摘要行: 每行测试结果 + 最后的统计摘要
  grep -E "✓|FAIL|Tests|Test Files" "$OUTPUT_FILE" 2>/dev/null | head -50 || true
fi

# ---- 生成 JSON 摘要 ----
if [[ "$EXIT_CODE" -eq 0 ]]; then
  STATUS="pass"
else
  STATUS="fail"
fi

# 从输出中提取统计信息
TESTS_TOTAL="?"
TESTS_PASSED="?"
TESTS_FAILED="?"
if grep -qE "Tests " "$OUTPUT_FILE" 2>/dev/null; then
  TESTS_LINE=$(grep -E "Tests " "$OUTPUT_FILE" | tail -1 | tr -d '\r')
  # vitest 4.x 格式: "     Tests  252 passed (252)"  or "     Tests  3 failed | 2 passed (5)"
  if echo "$TESTS_LINE" | grep -q "|"; then
    TESTS_FAILED=$(echo "$TESTS_LINE" | sed -n 's/.* \([0-9][0-9]*\) failed.*/\1/p')
    TESTS_PASSED=$(echo "$TESTS_LINE" | sed -n 's/.* \([0-9][0-9]*\) passed.*/\1/p')
  else
    TESTS_PASSED=$(echo "$TESTS_LINE" | sed -n 's/[^0-9]*\([0-9][0-9]*\) passed.*/\1/p')
    TESTS_FAILED="0"
  fi
  TESTS_TOTAL=$(echo "$TESTS_LINE" | sed -n 's/.*(\([0-9][0-9]*\))/\1/p')

  # 从 Test Files 行提取文件统计
  if grep -qE "Test Files " "$OUTPUT_FILE" 2>/dev/null; then
    FILES_LINE=$(grep -E "Test Files " "$OUTPUT_FILE" | tail -1 | tr -d '\r')
    FILES_TOTAL=$(echo "$FILES_LINE" | sed -n 's/.*(\([0-9][0-9]*\))/\1/p')
  else
    FILES_TOTAL="?"
  fi
else
  FILES_TOTAL="?"
fi

cat > "$SUMMARY_FILE" <<JSONEOF
{
  "status": "$STATUS",
  "exitCode": $EXIT_CODE,
  "tests": {
    "total": $TESTS_TOTAL,
    "passed": $TESTS_PASSED,
    "failed": $TESTS_FAILED
  },
  "testFiles": $FILES_TOTAL,
  "coverage": $COVERAGE,
  "timestamp": "$(date -u +%Y-%m-%dT%H:%M:%SZ)",
  "project": "yts_project_vueai",
  "runner": "vitest"
}
JSONEOF

echo ""
echo "========== 摘要 (JSON) =========="
cat "$SUMMARY_FILE"
echo ""

# ---- 返回 ----
exit "$EXIT_CODE"
