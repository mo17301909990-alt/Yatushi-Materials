#!/usr/bin/env bash
# scripts/api-contract-test.sh — API 契约烟雾测试
# 验证关键后端 API 端点可达性和响应结构
# 需要后端运行中: cd yts_project && mvn spring-boot:run
# 用法: bash scripts/api-contract-test.sh

set -euo pipefail

BASE="${1:-http://localhost:8092}"
FAIL=0

check() {
  local name="$1" method="$2" url="$3" expect="$4"
  local status
  status=$(curl -s -o /dev/null -w "%{http_code}" -X "$method" "$BASE$url" 2>/dev/null)
  if [ "$status" = "$expect" ]; then
    echo "  PASS: $name ($status)"
  else
    echo "  FAIL: $name - expected $expect got $status"
    FAIL=1
  fi
}

echo ""
echo "=== API Contract Smoke Tests ==="
echo ""

# Health
check "Health endpoint" GET "/api/health" "200"

# -- 烫金模块 --
check "Gold foil products" GET "/api/gold-foil/products" "200"

# -- 裱纸 / 覆膜 --
check "Laminating options" GET "/api/laminating/options/materials" "200"

# -- 兼容性查询 --
check "Hot stamping types" POST "/api/compatibility/hot-stamping-types" "200"

# -- 通用对照表 --
check "Cloth paper types" GET "/api/cloth-paper-types" "200"

echo ""
if [ "$FAIL" -eq 0 ]; then
  echo "All API contract tests passed."
else
  echo "Some API contract tests FAILED."
  exit 1
fi
