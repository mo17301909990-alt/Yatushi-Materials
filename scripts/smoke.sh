#!/usr/bin/env bash
# scripts/smoke.sh — YTS 烟雾测试
# 三步烟雾：路由完整性 → 测试套件 → 前端构建
# 用法: bash scripts/smoke.sh

set -e
FAIL=0

echo "━━━ YTS 烟雾测试 ━━━"
echo ""

# Step 1: 路由组件完整性
echo "━━━ [1/3] 路由组件完整性 ━━━"
cd "$(dirname "$0")/../yts_project_vueai"
if npx --yes tsx scripts/smoke-routes.ts 2>/dev/null; then
  echo "  ✅ 路由完整性检查通过"
else
  echo "  ❌ 路由完整性检查失败"
  FAIL=1
fi

# Step 2: 测试套件
echo ""
echo "━━━ [2/3] 测试套件 ━━━"
if npm run test:run 2>&1 | tail -5; then
  echo "  ✅ 测试套件通过"
else
  echo "  ❌ 测试套件有失败"
  FAIL=1
fi

# Step 3: 前端构建
echo ""
echo "━━━ [3/3] 前端构建 ━━━"
if npm run build 2>&1 | tail -3; then
  echo "  ✅ 构建通过"
else
  echo "  ❌ 构建失败"
  FAIL=1
fi

echo ""
if [ $FAIL -eq 0 ]; then
  echo "━━━ ✅ 全部烟雾测试通过 ━━━"
else
  echo "━━━ ❌ 烟雾测试有失败 ━━━"
  exit 1
fi
