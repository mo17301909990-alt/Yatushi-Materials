# ERPAI 前端检测体系深度研究 — 面向 YTS 的适配方案

> 研究日期：2026-06-26
> 研究对象：营销 OS (C:\Users\Admin\.openclaw) + ERPAI (F:\ERPAI)
> 研究方式：3 Agent 并行 Workflow + 人工合成

---

## 一、营销 OS 前端检测飞轮架构

### 1.1 三层体系

```
┌─────────────────────────────────────────────────────────────┐
│  调度层  │  schtasks / Claude cron / scheduled_tasks.json   │
├─────────────────────────────────────────────────────────────┤
│  引擎层  │  run-loop-dag.ts (DAG 波次执行 / Kahn 算法)      │
│          │  loop-nightly.sh (bash 线性执行)                 │
├─────────────────────────────────────────────────────────────┤
│  命令层  │  /loop, /loop-check, /loop-full, /loop-health,   │
│          │  /loop-nightly, /loop-self-play, /loop-commissar │
├─────────────────────────────────────────────────────────────┤
│  执行层  │  .claude/loop.md 定义的 bash 步骤 + 独立脚本     │
└─────────────────────────────────────────────────────────────┘
```

### 1.2 DAG 波次执行（核心创新）

`run-loop-dag.ts` 基于 Kahn 算法建立依赖拓扑：

```
Wave 0: api-health (5566)              ← 无依赖
Wave 1: lint, tsc (root), ui-smoke    ← 无依赖
Wave 2: tsc (react-c)                 ← 依赖 tsc
Wave 3: build (root)                  ← 依赖 tsc
Wave 4: build (react-c)               ← 依赖 tsc-c
Wave 5: test                          ← 依赖 build
Wave 6: evaluator, self-play          ← 无进一步依赖
```

**关键特性：**
- 同波次内 **并行**（所有 `run-loop-step.ts` 并行 spawn）
- 波次间 **串行**（上一波全部完成后才启动下一波）
- 依赖失败 → 下游自动 SKIP
- 每个 step 有独立 timeout

### 1.3 完整检测项清单（22+ 项）

| 检测项 | 类型 | 检查内容 | 阻断 |
|--------|------|---------|------|
| **tsc** | 编译 | TypeScript 类型检查 | ❌ |
| **build** | 编译 | npm run build | ❌ |
| **test** | 测试 | npm test | ❌ |
| **ui-smoke** | 前端 | curl 检查所有页面 200 | ❌ |
| **ui-render** | 前端 | JS 语法 + SSR 转义 | ❌ |
| **api-health** | 后端 | 端口连通性 + `/health` | ❌ |
| **health-skills** | 运行时 | Skill 注册计数 | ❌ |
| **health-hall** | 运行时 | Hall 文件完整性 | ❌ |
| **health-db** | 数据 | SurrealDB 进程 + SQL 查询 | ❌ |
| **flywheel** | 质量 | 12 个飞轮环路健康 | ❌ |
| **self-play** | 智能体 | 自博弈心跳 < 24h | ❌ |
| **evaluator** | 对抗 | 独立 session 盲审 | ✅ stop |
| **promote** | 知识 | Gold pattern 晋升 | ❌ |
| **schema-drift** | 数据 | Schema 漂移检测 | ❌ |
| **quality-trend** | 质量 | 历史趋势快照 | ❌ |
| **quality-decay** | 质量 | beta-confidence 衰减 0.995/天 | ❌ |
| **goal-check** | 管理 | 目标 stall 检测 | ❌ |

### 1.4 失败处理链

```
步骤失败
  ├→ 非阻断步骤 → 仅告警，不阻断
  ├→ 阻断步骤 (evaluator) → exit 1，管线停止
  └→ 重复失败检测
       ├→ < 3 次 → 累计 +1
       └→ >= 3 次 → 记录到 error-patterns + 日志告警 + "需人工介入"
```

**附加机制：**
- **EWMA 跟踪**：对每个步骤建立指数加权移动平均，趋势恶化时告警
- **SHA-256 hash 压制**：同一 finding 连续出现 5 次后从报告中隐藏
- **Keeper 自治修复**：对已知模式自动 apply 修复

---

## 二、ERPAI UI 验证体系

### 2.1 两级烟雾测试

**Level A: HTTP 可达性（smoke-full-modules.test.ts）**

```typescript
// 纯 fetch + redirect:manual，无浏览器依赖
async function pageOk(path: string) {
  const res = await fetch(`http://localhost:4314${path}`, { redirect: 'manual' })
  assert.ok(res.status < 400, `${path} -> ${res.status}`)
}
```

覆盖 ~45 个页面，按模块分组：系统页、采购、销售、库存、生产、质量、财务等。

**Level B: 全量 UI 验证（ui-verify.ts — 10 层）**

| Step | 检查项 | 验证内容 |
|------|--------|---------|
| 1 | Server 可达 | Health 端点或自动启动 + 30s 等待 |
| 2 | 数字员工页面 (×3) | HTTP 200 + HTML 含 page-shell |
| 3 | Agent 状态页 | HTTP 200 |
| 4 | SOP 执行验证 | POST /skills/execute 返回结果 |
| 5 | Skill 注册验证 | GET /skills/SKILL_ID 返回有效 |
| 6 | Copilot 端点 (×4) | agents/status, query(two modes), sidebar |
| 7 | JS 语法检查 | `node --check` 验证 SSR 输出 |
| 8 | selectedAgent 路由 | sales-agent, procurement-agent |
| 9 | SSR 模板转义 | 无泄漏的 `${...}` 模板字面量 |
| 10 | 报告汇总 | PASS/FAIL 计数 |

### 2.2 UI 覆盖度审计（ui-gap-analysis.md）

系统化的 UI 缺口分析方法论：

```
┌──────────────────────────────────────────────┐
│  P0 — 用户完成不了核心操作（阻断式）            │
│  P1 — 数据假 / 页面缺失（功能不完整）           │
│  P2 — 可延后                                  │
└──────────────────────────────────────────────┘
```

每个缺口记录：位置、原因、涉及文件、人天估算。

---

## 三、YTS 可复用的检测模式

### 模式 A：UI 烟雾 / 页面渲染检查

| 维度 | ERPAI 做法 | YTS 适配 |
|------|-----------|---------|
| 技术 | SSR: fetch 每个页面返回 HTML | **SPA: browse skill 驱动真实浏览器** |
| 检查 | HTTP < 400 + HTML 含 page-shell | DOM 渲染验证 + 截图对比 |
| 覆盖 | ~45 页面 | 68 路由（已有 smoke-routes 做文件存在性） |
| 实现 | `npx tsx test/erpai/smoke-full-modules.test.ts` | `browse` skill 导航 + screenshot |
| **工作量** | — | **2 人天** |

### 模式 B：导航覆盖检查

| 维度 | ERPAI / 营销 OS | YTS 当前 |
|------|----------------|----------|
| 当前 | 侧边栏菜单→真实页面（L6 QA gate） | `smoke-routes.ts` 检查文件存在性 |
| 缺口 | 无 | 只检查文件存在，不检查渲染 |
| 增强 | 无 | 加 `browse` 验证每个页面渲染正常 |
| **工作量** | — | **1 人天** |

### 模式 C：API 健康检查 Loop

| 维度 | 营销 OS | YTS 适配 |
|------|---------|---------|
| 当前 | 4 个 api-health 端点 | 已有 `api-contract-test.sh` |
| 增强 | 自动启动/停止 server | 集成到 `/smoke` 命令 |
| 调度 | cron 每 30min | 目前手动，可加 cron |
| **工作量** | — | **0.5 人天** |

### 模式 D：UI 覆盖度审计

| 维度 | ERPAI | YTS 适配 |
|------|-------|---------|
| 方法 | 人工分析已有 UI → 对照功能清单 → 缺口分类 P0/P1/P2 | 对照 68 路由 + 70 API 模块 → 分析缺什么 |
| 产出 | ui-gap-analysis.md | 类似缺口表 |
| 周期 | 单次 | 季度 |
| **工作量** | — | **1 人天** |

### 模式 E：DAG 波次检测引擎

| 维度 | 营销 OS | YTS 适配 |
|------|---------|---------|
| 复杂度 | `run-loop-dag.ts` + Kahn 算法 | 不需要完整 DAG，三步串行够了 |
| 适配 | 完整任务编排引擎 | 直接强化 `/smoke` 命令即可 |
| **工作量** | — | **0.5 人天** |

### 模式 F：检测 Loop 飞轮

| 维度 | 营销 OS | YTS 适配 |
|------|---------|---------|
| 层级 | 4 层调度 + 20+ 检测项 | 已有 `/smoke` + `quality-gate.sh` |
| 缺口 | 无 | 增加定时调度 + 历史趋势 + 失败累计 |
| 增强 | 失败 3 次 → 记录 error-patterns | 集成 error-patterns.ts |
| **工作量** | — | **2 人天** |

---

## 四、YTS 最小可行检测体系（推荐路线）

基于 ERPAI / 营销 OS 的研究，为 YTS 设计渐进式检测体系：

### 第一步：定时调度检测 Loop（0.5 天）

在 `scheduled_tasks.json` 增加 cron 任务：
```
"0 */6 * * *" → bash scripts/quality-gate.sh    # 每 6 小时
"0 9 * * 1"   → bash scripts/weekly-audit.sh     # 每周一（已有）
```

### 第二步：UI 渲染验证（1 天）

用 `browse` skill 写一个导航烟雾脚本：
```bash
# scripts/ui-browse-smoke.sh (概念)
browse http://localhost:5173/login     → 截图验证登录框
browse http://localhost:5173/          → 截图验证首页
browse http://localhost:5173/gold-foil → 截图验证烫金页
```

### 第三步：失败累计 + 趋势跟踪（1 天）

参照营销 OS 的 EWMA tracker + SHA-256 压制：
- 失败连续 3 次 → 记录到 error-patterns
- 同一 finding 连续 5 次 → 从报告隐藏（噪声压制）

### 第四步：全量 UI 覆盖度审计（1 天）

参照 ERPAI `ui-gap-analysis.md` 方法：
- 对照 68 路由 + 所有 Controller API → 标记缺页面/缺测试的区域
- 按 P0/P1/P2 分级

---

## 参考源

| 组件 | 路径 |
|------|------|
| 营销 OS Loop 定义 | `/c/Users/Admin/.openclaw/control-center/.claude/loop.md` |
| ERPAI ui-verify.ts | `/f/ERPAI/control-center/scripts/ui-verify.ts` |
| ERPAI smoke-full-modules | `/f/ERPAI/control-center/test/erpai/smoke-full-modules.test.ts` |
| ERPAI UI gap analysis | `/f/ERPAI/docs/research/ui-gap-analysis.md` |
| 营销 OS vs ERPAI 对比 | `/f/ERPAI/docs/presentation/营销OS-vs-ERPAI-审计对比.md` |
| YTS quality-gate.sh | `scripts/quality-gate.sh` |
| YTS /smoke 命令 | `.claude/commands/smoke.md` |
| YTS weekly-audit.sh | `scripts/weekly-audit.sh` |
