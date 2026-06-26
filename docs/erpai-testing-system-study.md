# ERPAI 测试体系深度研究 — YTS 可借鉴模式

> 研究日期：2026-06-26
> 研究对象：`F:/ERPAI/`（931 个测试文件，80+ 检查脚本，成熟测试体系）

---

## 一、ERPAI 测试体系架构全景

```
┌─────────────────────────────────────────────────────────────┐
│  用户接口层                                                  │
│  /smoke       — 烟雾测试三件套 (UI + Hall + DB)              │
│  /test-file   — 跑单个测试文件                                │
│  /test-domain — 按领域模块跑测试                              │
│  /ui-verify   — 前端验证循环 (重启Server→测页面→报告)          │
│  /loop-test   — Track A/B/C 测试子循环                        │
├─────────────────────────────────────────────────────────────┤
│  Agent/Skill 层                                              │
│  erpai-test-engineer — 测试工程师 Agent                       │
│    ├── skills: /smoke-erpai, /dev-flow                       │
│    ├── commands: !test-file, !test-domain                    │
│    └── 专精: 契约测试/E2E/烟雾/覆盖分析                        │
│  reality-checker    — 对抗式验证，检测假通过                    │
│  erpai-ui-verify    — SSR 渲染验证循环                        │
├─────────────────────────────────────────────────────────────┤
│  基础设施层                                                  │
│  scripts/run-tests-isolated.ts — 测试隔离环境                 │
│  scripts/ui-verify.ts          — 全量 UI 验证循环              │
│  scripts/gate.ts               — 构建门禁                     │
│  scripts/qa-backlinks.ts       — 8 层质量门禁                 │
│  scripts/evidence-gate.ts      — 证据门禁                     │
├─────────────────────────────────────────────────────────────┤
│  测试文件层 (control-center/test/)                            │
│  test/erpai/*.test.ts — 业务域测试 (300+)                    │
│  test/*.test.ts       — 通用功能测试 (600+)                  │
│  test/erpai/smoke*.ts — 烟雾测试                             │
│  test/erpai/flow*.ts  — 业务流 E2E 测试                      │
│  test/erpai/api/*.ts  — API 专项测试                         │
│  scripts/_check_*.ts  — 80+ 专项检查脚本                      │
└─────────────────────────────────────────────────────────────┘
```

---

## 二、关键模式详解

### 模式 1：测试隔离 — `run-tests-isolated.ts`

**为什么重要：** 不 mock 数据库，用真实实例但保证隔离。

```typescript
// 关键流程
1. mkdtemp() — 创建临时目录
2. process.env.OPENCLAW_HOME = runtimeDir — 隔离 agent 配置
3. randomUUID() 生成测试 tenant — 不污染已有数据
4. 运行测试 → 自动销毁临时 namespace
```

**YTS 可以借鉴：** VS Code SPA 不需要数据库隔离，但需要：
- 临时目录隔离测试产物
- 随机化 store 初始状态
- 测试间不共享状态

### 模式 2：UI 验证循环 — `ui-verify.ts`

**ERPAI 做法（SSR）：**
1. check server health (port 4314)
2. 未运行则自动 spawn 子进程启动 server
3. fetch 每个页面 → 验证 HTTP 200 + HTML 结构
4. 验证 SOP/Skill 执行端点
5. 验证 JS 语法 + SSR 模板转义
6. 汇总报告

**YTS 适配方案（Vue SPA）：**
```typescript
// YTS 版 ui-verify.ts 思路
1. check dev server (port 5173)
2. 用 browse skill 导航关键页面
3. 验证 DOM 渲染（非空、标题正确、数据区存在）
4. 验证 API 代理可达（/api/health）
5. 截图对比（可选）
```

### 模式 3：8 层 QA 门禁 — `qa-backlinks.ts`

ERPAI 的质量门禁分为 8 层，由浅到深：

| 层级 | 检查内容 | 实现成本 |
|------|---------|---------|
| L1 | HTTP 状态码 200 | 低 |
| L2 | `<title>` 存在 | 低 |
| L3 | 页面内容非空/无错误信息 | 低 |
| L4 | backLink 指向正确模块 | 中 |
| L5 | 实体前缀 vs 模块一致性 | 中 |
| L6 | 导航菜单项指向真实页面 | 中 |
| L7 | YAML 实体→页面覆盖率 | 高 |
| L8 | 内部链接不返回 404 | 高 |

**YTS 可借鉴的 L1-L4：**
```typescript
// 1. 所有 View 组件能 mount 不抛异常 → 烟雾测试
// 2. 所有 API 模块函数可调用不抛异常 → 契约测试
// 3. 所有路由组件文件存在 → smoke-routes.ts 已有
// 4. 导航链接有对应路由 → smoke-routes.ts 已有
```

### 模式 4：构建门禁 — `gate.ts`

**核心逻辑：**
- 读取 `.claude/loop-state/latest-findings.json`
- 检查每个 finding 是否已修复
- 未修复 → exit 1 阻挡构建
- `SKIP_GATE=true` 可逃逸

**YTS 版 gate 可以：**
```json
// .claude/state/test-snapshot.json
{
  "lastRun": "2026-06-26T17:00:00Z",
  "testFiles": 7,
  "passed": 167,
  "failed": 9,
  "coverage": { "statements": 15, "branches": 8, "functions": 12 },
  "falseGreen": ["hotStamping.spec.ts — 9 个测试失败"]
}
```

门禁检查：`failed > 0 → WARN`，覆盖率下降 `> 5% → BLOCK`

### 模式 5：烟雾测试三件套

| 命令 | YTS 对应方案 |
|------|-------------|
| `npm run smoke:ui` | `npm run test:run` + browse skill |
| `npm run smoke:hall` | 不适用（无 Hall） |
| `/smoke` | `npm run test:run` + smoke-routes |

**YTS 烟雾测试实现：**
```bash
#!/usr/bin/env bash
# scripts/smoke.sh — YTS 烟雾测试
echo "=== L1: 路由组件完整性 ==="
node --import tsx scripts/smoke-routes.ts

echo "=== L2: 测试套件 ==="
npm run test:run

echo "=== L3: API 可达性 ==="
curl -s -o /dev/null -w "health: %{http_code}\n" http://localhost:8092/api/health

echo "=== L4: 前端构建 ==="
npm run build
```

### 模式 6：测试工程师 Agent 知识体系

ERPAI test-engineer 的领域知识（直接可复用）：

```yaml
专精领域:
  - 契约测试: API 模块每个函数测请求格式 + 错误处理
  - 组件测试: Vue 组件测渲染状态 + 交互 + 边界
  - Store 测试: 初始状态 + Action + Getter + 异步三态
  - View 烟雾: 渲染不抛异常 + 数据区存在
  - 假绿检测: 断言合理性 + 无断言行 + 实际值失配

测试原则:
  - 先写测试，再修代码
  - 测试失败 = blocker
  - 不修改测试来让失败的逻辑"通过"

编写流程:
  - 读源码理解输入/输出/依赖
  - 按 Arrange-Act-Assert 组织
  - 跑 npm run test:run 验证
  - 跑 npm run test:coverage 确认覆盖
```

---

## 三、YTS vs ERPAI 对比总结

| 维度 | ERPAI | YTS 当前 | 差距 |
|------|-------|---------|------|
| 测试文件数 | 931 | 7 | **133x** |
| 测试隔离脚本 | `run-tests-isolated.ts` | 无 | ❌ |
| UI 验证循环 | `ui-verify.ts` | 无 | ❌ |
| 构建门禁 | `gate.ts` | 无 | ❌ |
| QA 门禁 | `qa-backlinks.ts` 8 层 | 无 | ❌ |
| 烟雾命令 | `/smoke` + 3 种 | 无 | ❌ |
| 测试领域命令 | `!test-file`, `!test-domain` | 无 | ❌ |
| 测试 Agent | `erpai-test-engineer` | `frontend-test-engineer` | 弱 |
| 假绿检测 | `reality-checker` | 无 | ❌ |
| E2E 测试 | 有 (flow-*.test.ts) | 无 | ❌ |
| 覆盖率门禁 | 有 | 无 | ❌ |
| 检查脚本 | 80+ 个专用脚本 | 1 个 smoke-routes.ts | **80x** |

---

## 四、YTS 最小可行测试体系（MVP）

从 ERPAI 模式中提取 YTS 最缺的 3 个组件：

### 4.1 烟雾测试命令 `/smoke`

```bash
# 三步烟雾，10 秒跑完
npm run smoke:routes    # 已有 smoke-routes.ts
npm run smoke:test      # npm run test:run
npm run smoke:build     # npm run build
```

### 4.2 测试 Agent 升级

```
当前: Haiku + 12 turns + 无命令
目标: Sonnet + 30 turns + !test-file + !test-domain
```

### 4.3 API 模块烟雾测试（最快见效）

每个 `api/modules/*.ts` 加一个 10 行烟雾测试，验证：
- 文件可导入
- 导出函数签名正确
- URL 和 method 符合命名规范

```typescript
// 自动发现所有 API 模块并验证导入
import * as fs from 'fs'
const modules = fs.readdirSync('src/api/modules').filter(f => f.endsWith('.ts'))
describe('API Modules Smoke', () => {
  it.each(modules)('%s 可导入且导出的函数存在', async (file) => {
    const mod = await import(`../api/modules/${file}`)
    expect(Object.keys(mod).length).toBeGreaterThan(0)
  })
})
```

这一条就能把 API 覆盖从 1/70 提到 70/70，不到 30 行代码。

---

## 参考源

| 组件 | 路径 | 行数 |
|------|------|------|
| smoke-erpai skill | `F:/ERPAI/.claude/skills/smoke-erpai/skill.md` | ~80 |
| erpai-ui-verify skill | `F:/ERPAI/.claude/skills/erpai-ui-verify/skill.md` | ~50 |
| test-engineer-skill | `F:/ERPAI/.claude/skills/test-engineer-skill/skill.md` | ~60 |
| erpai-test-engineer agent | `F:/ERPAI/.claude/agents/erpai-test-engineer.md` | ~110 |
| ui-verify.ts 脚本 | `F:/ERPAI/control-center/scripts/ui-verify.ts` | ~200 |
| run-tests-isolated.ts | `F:/ERPAI/control-center/scripts/run-tests-isolated.ts` | ~120 |
| qa-backlinks.ts | `F:/ERPAI/control-center/scripts/qa-backlinks.ts` | ~400 |
| gate.ts | `F:/ERPAI/control-center/scripts/gate.ts` | ~80 |
