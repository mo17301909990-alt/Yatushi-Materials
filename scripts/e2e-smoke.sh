#!/usr/bin/env bash
# scripts/e2e-smoke.sh — YTS 端到端烟雾测试
#
# 作用: 验证前端 Vite 开发服务器运行正常 + 后端 API 代理可达
# 适用: CI 门禁、开发机自检、部署后冒烟
#
# 行为:
#   1. 检测 dev server (port 5173) 是否在运行
#   2. 未运行则自动启动并等待就绪（最多 30 秒）
#   3. 通过 curl 测试关键页面（Vite SPA 返回 index.html 表明 ok）
#   4. 验证 /api/health 后端代理是否工作
#   5. 若由本脚本启动了 dev server，退出时自动清理
#
# 用法:
#   bash scripts/e2e-smoke.sh            # 默认端口 5173
#   bash scripts/e2e-smoke.sh 3000       # 自定义端口
#
# 退出码:
#   0 — 全部通过
#   1 — 有失败项

set -euo pipefail

PORT="${1:-5173}"
BASE="http://localhost:${PORT}"
FAIL=0
STARTED_BY_US=false
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
VUEAI_DIR="$SCRIPT_DIR/../yts_project_vueai"
TIMEOUT=30

# ── 辅助函数 ──────────────────────────────────────────────────────────────────

info()  { echo "  ℹ️  $*"; }
pass()  { echo "  ✅ PASS: $*"; }
fail()  { echo "  ❌ FAIL: $*"; FAIL=1; }

# 等待服务器响应（curl 重试，最长 $TIMEOUT 秒）
wait_for_server() {
  local url="$1" label="$2" waited=0
  while [ $waited -lt $TIMEOUT ]; do
    if curl -s -o /dev/null -w "%{http_code}" "$url" 2>/dev/null | grep -q .; then
      return 0
    fi
    sleep 1
    waited=$((waited + 1))
  done
  return 1
}

# curl 单次检查：期望指定 HTTP 状态码
check_http() {
  local name="$1" url="$2" expect="${3:-200}"
  local status
  status=$(curl -s -o /dev/null -w "%{http_code}" "$url" 2>/dev/null || echo "000")
  if [ "$status" = "$expect" ]; then
    pass "$name (${status})"
  else
    fail "$name — 期望 ${expect}，收到 ${status}"
  fi
}

# 检查响应体是否包含预期文本
check_body() {
  local name="$1" url="$2" pattern="$3"
  if curl -s "$url" 2>/dev/null | grep -q "$pattern"; then
    pass "$name (响应包含 \"${pattern}\")"
  else
    fail "$name — 响应中未找到 \"${pattern}\""
  fi
}

# ── 入口 ──────────────────────────────────────────────────────────────────────

echo ""
echo "═══════════════════════════════════════════════"
echo "  YTS E2E 烟雾测试 — dev server :${PORT}"
echo "═══════════════════════════════════════════════"
echo ""

# ── [Step 1] 检查 / 启动 dev server ──────────────────────────────────────────
echo "━━━ [1/3] 开发服务器状态 ━━━"

if curl -s -o /dev/null -w "" "$BASE" 2>/dev/null; then
  pass "dev server 已在 :${PORT} 上运行"
else
  info "dev server 未运行，正在启动..."
  STARTED_BY_US=true

  if [ ! -d "$VUEAI_DIR" ]; then
    fail "找不到前端目录: $VUEAI_DIR"
    echo ""
    echo "═══════════════ 失败 ═══════════════"
    exit 1
  fi

  cd "$VUEAI_DIR"
  npm run dev &
  DEV_PID=$!
  echo "  PID: $DEV_PID"

  if wait_for_server "$BASE"; then
    pass "dev server 启动成功（${TIMEOUT}s 内就绪）"
  else
    fail "dev server 启动超时（${TIMEOUT}s）"
    echo ""
    echo "═══════════════ 失败 ═══════════════"
    exit 1
  fi
fi

echo ""

# ── [Step 2] 页面可达性测试 ──────────────────────────────────────────────────
echo "━━━ [2/3] 页面可达性 ━━━"

# Vite SPA: 所有前端路由都返回 index.html (200)，所以主要测 200 可达
# 但首页(/)会 307 redirect 到 /login，所以接受 200/307
check_http "首页 (/)"        "$BASE/"        "200"
check_http "登录页 (/login)"  "$BASE/login"   "200"

# 选取 3 个代表性的 SPA 路由（无需鉴权，仅测 Vite 服务正常）
check_http "调试页 (/debug-permissions)"  "$BASE/debug-permissions"  "200"
check_http "API 测试页 (/test-api)"       "$BASE/test-api"          "200"
check_http "简单测试页 (/simple-test)"     "$BASE/simple-test"       "200"

echo ""

# ── [Step 3] 后端 API 代理测试（通过 dev server proxy）───────────────────────
echo "━━━ [3/3] 后端 API 代理 ━━━"

# /api/health 是后端自带端点，通过 Vite proxy 转发到 localhost:8092
check_http    "后端健康检查 (/api/health)"      "$BASE/api/health"  "200"
check_body    "后端健康检查响应内容"             "$BASE/api/health"  "ok"

# 验证响应中有 "service":"yts-project" 标识
check_body    "后端服务标识"                     "$BASE/api/health"  "yts-project"

echo ""

# ── 汇总 ──────────────────────────────────────────────────────────────────────
if [ $FAIL -eq 0 ]; then
  echo "═══════════════ ✅ 全部 E2E 烟雾测试通过 ═══════════════"
else
  echo "═══════════════ ❌ E2E 烟雾测试有失败 ═══════════════"
fi
echo ""

# ── 清理：由我们启动的 server → 杀掉 ─────────────────────────────────────────
if [ "$STARTED_BY_US" = true ]; then
  if [ -n "${DEV_PID:-}" ]; then
    info "清理：停止 dev server (PID $DEV_PID)"
    kill "$DEV_PID" 2>/dev/null || true
    # 确保端口释放
    sleep 1
  fi
fi

exit $FAIL
