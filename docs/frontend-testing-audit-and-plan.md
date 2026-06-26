# YTS 前端测试体系 — 审计报告与改进方案

> 审计日期：2026-06-26
> 审计范围：YTS 前端测试体系 vs ERPAI / OpenClaw 成熟测试体系

---

## 一、现状盘点

### 1.1 当前测试资产

```
测试文件:         8 个（不含 node_modules）
测试运行器:       Vitest
UI 环境:          jsdom
设置文件:         src/test-setup.ts
烟雾脚本:         scripts/smoke-routes.ts
测试命令:         npm run test:run / test:coverage
```

### 1.2 各层级覆盖

| 层级 | 文件数 | 覆盖内容 | 行数 | 质量 |
|------|--------|---------|------|------|
| **API 模块测试** | 1 | copilot API | 44 行 | 极简，只测请求构造 |
| **组件测试** | 1 | CompatibilityResultCard | 79 行 | 质量好，覆盖 4 状态 |
| **Store 测试** | 4 | auth/hotStamping/matchingStore/copilot | 483-650 行 | 较完整 |
| **路由测试** | 1 | 路由存在性 | 203 行 | 文件存在性检查 |
| **视图测试** | 0 | — | — | **完全缺失** |
| **E2E 测试** | 0 | — | — | **完全缺失** |
| **集成测试** | 0 | — | — | **完全缺失** |
| **可访问性** | 0 | — | — | **完全缺失** |

### 1.3 现有 Agent / Skill

| 组件 | 状态 | 问题 |
|------|------|------|
| `frontend-test-engineer` agent | 已创建 | Haiku 模型，12 回合上限，能力有限 |
| `frontend-testing-loop.md` skill | 已创建 | 设计分层但未真正实现 Tier 2/3 |
| `frontend-testing-infra.js` workflow | 已创建 | 只建了烟雾脚本+Agent骨架 |
| `smoke-routes.ts` 脚本 | 存在 | 只检查文件存在性+导航链接，不测渲染 |

---

## 二、标杆对比：ERPAI 测试体系

### 2.1 ERPAI 关键数据

```
测试文件总数:     931 个
测试运行器:       Node.js native test runner
隔离脚本:         scripts/run-tests-isolated.ts
UI 验证脚本:      scripts/ui-verify.ts
烟雾技能:         smoke-erpai（5 步流水线）
UI 验证技能:      erpai-ui-verify（SSR 渲染验证）
Agent:            erpai-test-engineer（含 !test-file / !test-domain 命令）
```

### 2.2 ERPAI 分层测试架构

```
┌─────────────────────────────────────────────────────────┐
│  Tier 3: E2E / 业务流                                   │
│  test/erpai/flow-*.test.ts                              │
│  真实 DB + 完整链路                                     │
├─────────────────────────────────────────────────────────┤
│  Tier 2: 集成 / 契约                                    │
│  test/erpai/*.test.ts                                   │
│  API 路由 → Repository → SurrealDB                       │
│  Schema-First 同步验证                                  │
├─────────────────────────────────────────────────────────┤
│  Tier 1: 单元 + 烟雾                                     │
│  test/erpai/smoke*.test.ts + ui/                        │
│  UI 可达性 / DB 连接 / Schema 完整 / SSR 渲染             │
├─────────────────────────────────────────────────────────┤
│  测试基础设施                                            │
│  run-tests-isolated.ts ← 隔离环境                        │
│  ui-verify.ts ← UI 验证循环                              │
│  reality-checker ← 假绿检测                              │
└─────────────────────────────────────────────────────────┘
```

### 2.3 ERPAI 假绿检测机制

每次运行测试后执行：
1. **断言合理性检查** — 标记只测 `not.toBeUndefined()` 不等具体值的测试
2. **实际值 vs 期望值失配** — 测试通过但组件已改
3. **无 assert/expect 的测试** — 跑了等于没跑
4. **覆盖 ≠ 测试** — 覆盖率不为 0 但无实质性断言

---

## 三、差距分析

### 3.1 覆盖缺口（P0）

| 缺口 | 风险 | 建议优先级 |
|------|------|-----------|
| 80+ 视图页面 0 测试 | 页面渲染崩了没人知道 | **P0** |
| 70+ API 模块只有 1 个有测试 | API 调用链断裂无感知 | **P0** |
| 30+ 组件只有 1 个有测试 | UI 交互异常无门禁 | **P0** |
| 后端 Spring Boot 0 集成测试 | Java API 改了黑盒不知 | **P1** |

### 3.2 基础设施缺口（P1）

| 缺口 | 影响 |
|------|------|
| 无测试隔离脚本 | 测试互相污染 |
| 无 UI 验证循环 | 不能自动验证页面渲染 |
| 无假绿检测 | 绿通过但实际已经错了 |
| 无覆盖率门禁 | 覆盖度可降到 0 无人管 |

### 3.3 流程缺口（P2）

| 缺口 | 影响 |
|------|------|
| 测试不在 CI 门禁内 | 可以带坏测试合并 |
| Agent 能力不足 | Haiku 写测试质量不够 |
| 无定期测试审计 | 退化无人发现 |
| 测试文档缺失 | 新人不知道怎么加测试 |

---

## 四、改进方案

### Phase 1：补 Tier 1 烟雾测试（1-2 天）

**目标：** 所有页面不崩 + 所有 API 模块可调用

#### 1.1 API 烟雾测试

为每个 `src/api/modules/*.ts` 创建烟雾测试，验证：
- 函数存在且可调用
- 请求 URL / method 格式正确
- 错误响应有兜底处理

```typescript
// src/api/__tests__/smoke-api-modules.test.ts — 自动发现所有 API 模块
import * as fs from 'fs'
import * as path from 'path'

const modulesDir = path.resolve(__dirname, '../../api/modules')
const files = fs.readdirSync(modulesDir).filter(f => f.endsWith('.ts'))

describe.each(files)('API 模块烟雾: %s', (file) => {
  it('模块可导入，导出的函数存在', async () => {
    const mod = await import(`../../api/modules/${file}`)
    expect(Object.keys(mod).length).toBeGreaterThan(0)
  })
})
```

#### 1.2 View 烟雾测试

为每个 `src/views/` 下的页面创建基本渲染测试：
- 导入不抛异常
- 组件定义存在
- 必要的数据区段（template 中有 root 元素）

#### 1.3 Component 烟雾测试

按优先级补组件测试：
- 第一批（共享组件）：ProcessNavigation, FoilConfig, GoldFoilSupplyList
- 第二批（业务组件）：SmartCompatibility 相关
- 第三批（其余组件）

### Phase 2：建测试基础设施（2-3 天）

**目标：** 跑得快、环境干净、能看覆盖

#### 2.1 创建测试隔离脚本

借鉴 ERPAI 的 `run-tests-isolated.ts`：
- 每次测试前创建临时目录/数据库
- 测试后自动清理
- 防止文件系统状态污染

#### 2.2 完善 UI 验证循环

Vue SPA 场景下 (对比 ERPAI SSR)：
- 启动 dev server → browse 关键页面 → 截图对比
- 用 `browse` skill 检测真实 DOM 渲染
- 验证路由跳转正确

#### 2.3 配置覆盖率门禁

```bash
# package.json 已有 test:coverage
# 需要补充覆盖率阈值:
# branches > 20%, functions > 15%, lines > 25%, statements > 20%
```

#### 2.4 接入假绿检测

```typescript
// scripts/check-false-green.ts
// 在 test:run 之后执行:
// 1. 扫描每个测试文件的 expect 断言数量
// 2. 标记只有 toBeTruthy()/not.toBeNull() 的测试
// 3. 标记无 expect 的 it() 块
// 4. 生成假绿风险报告
```

### Phase 3：升级测试 Agent（1 天）

**目标：** 能写、能跑、能验

#### 3.1 Agent 升级

| 项目 | 当前 | 目标 |
|------|------|------|
| 模型 | Haiku | Sonnet（写测试需要更好理解力） |
| maxTurns | 12 | 30 |
| 工具 | Read/Grep/Glob/Bash | + Edit/Write（能直接写测试） |
| 命令 | 无 | `!test-file` / `!test-domain` / `!smoke` |

#### 3.2 Agent 技能绑定

```
frontend-test-engineer
  ├── /smoke-routes        ← 已有
  ├── /frontend-testing-loop ← 已有但需增强
  └── 新增: 测试编写 SOP 指令
```

#### 3.3 测试编写 SOP

绑定到 Agent 的 system prompt：

```
当你接到"给 X 写测试"任务:
1. 读 X 的源码，理解输入/输出/依赖
2. 判断测试类型:
   - API 模块 → 测请求格式 + 错误处理
   - Vue 组件 → 测渲染状态 + 交互 + 边界
   - Store → 测初始状态 + Action + Getter
   - View → 测渲染不抛异常 + 数据区存在
3. 按 Arrange-Act-Assert 写测试
4. 运行 npm run test:run 验证
5. 运行 npm run test:coverage 确认覆盖新增
```

### Phase 4：补齐 Tier 2 集成测试（3-5 天）

**目标：** 前端+后端联调验证

#### 4.1 后端集成测试

`yts_project/pom.xml` 已有 testcontainers + H2 依赖，但：
- 只有 `AbstractIntegrationTest.java` 基类
- 没有实际的集成测试用例
- 需要后端 `test-engineer` agent 来写

#### 4.2 前端-后端集成测试

需要运行中的后端 + 前端 dev server：
```bash
# 验证关键 API 端点的可达性和响应结构
curl http://localhost:8092/api/health
curl http://localhost:8092/api/gold-foil/products
```

#### 4.3 API 契约测试

为 70+ API 模块每个写契约测试：
```
api/modules/copilot.ts         → copilot 契约测试 ✅ 已有
api/modules/goldFoil.ts        → goldFoil 契约测试 ❌
api/modules/product.ts         → product 契约测试 ❌
...（其余 67 个）
```

### Phase 5：E2E + 门禁（3-5 天）

**目标：** 关键业务流能跑通，坏代码进不了 main

#### 5.1 关键业务流 E2E

| 业务流 | 前端页面 | 后端 API |
|--------|---------|---------|
| 烫金匹配 | HotStamping | `/api/compatibility/recommend` |
| 丝印匹配 | Silicone*Matching | `/api/silicone*/match` |
| 过胶匹配 | Laminating | `/api/laminating/options/materials` |
| 用户登录 | Login | `/api/user/login` |

用 `browse` skill 驱动真实浏览器操作。

#### 5.2 门禁配置

```yaml
# .claude/settings.json 中 gate 规则
pre-merge:
  - npm run test:run 必须通过
  - 覆盖率不降（与 main 分支对比）
  - smoke-routes 无 missing component
```

#### 5.3 定期审计 Loop

```
cron "0 9 * * 1"  # 每周一早上
  → 跑全量测试
  → 跑覆盖率分析
  → 假绿检测
  → 报告写入 .claude/state/test-snapshot.json
  → 如果覆盖率下降超过 5% 或假绿数增加 → 告警
```

---

## 五、实施路线图

```
Week 1              Week 2              Week 3              Week 4
─────────────────   ─────────────────   ─────────────────   ─────────────────
Phase 1             Phase 2             Phase 3 + 4         Phase 5
├─ API 烟雾         ├─ 隔离脚本          ├─ Agent 升级        ├─ E2E 业务流
├─ View 烟雾        ├─ UI 验证循环       ├─ Agent 知识绑定    ├─ 门禁配置
├─ 组件烟雾补第一波  ├─ 覆盖率门禁        ├─ 后端集成测试      ├─ 审计 Loop
                    ├─ 假绿检测          ├─ API 契约测试      └─ 飞轮落地
                                        └─ 前端-后端联调
```

### 工作量估算

| Phase | 人天 | 依赖 | 前置条件 |
|-------|------|------|---------|
| Phase 1 | 1-2 天 | 无 | 可直接开工 |
| Phase 2 | 2-3 天 | Phase 1 完成 | 确认构建通过 |
| Phase 3 | 1 天 | Phase 1 完成 | 确认 Agent 配置路径 |
| Phase 4 | 3-5 天 | 后端测试模式确认 | 后端测试依赖就绪 |
| Phase 5 | 3-5 天 | Phase 1-4 完成 | 所有测试通过 |

---

## 六、验收标准

### Phase 1 完成标志
- [ ] 所有 API 模块有烟雾测试（文件存在 + 函数可调用）
- [ ] 所有 View 页面有烟雾测试（渲染不抛异常）
- [ ] 共享组件有基本测试
- [ ] `npm run test:run` 测试文件数 > 30

### Phase 2 完成标志
- [ ] 测试隔离脚本可运行
- [ ] 覆盖率报告可生成，阈值生效
- [ ] 假绿检测可运行并输出报告
- [ ] UI 验证循环可验证关键页面

### Phase 3 完成标志
- [ ] Agent 可自动写测试（调用 Agent + prompt 看生成质量）
- [ ] Agent 可分析测试失败原因
- [ ] Agent 有 `!test-file` 命令

### Phase 4 完成标志
- [ ] 后端有 5+ 集成测试通过
- [ ] 前端-后端联调可验证 3+ 关键 API
- [ ] API 契约测试覆盖 > 50%

### Phase 5 完成标志
- [ ] 3 条关键业务流 E2E 通过
- [ ] PR 门禁禁止覆盖率下降
- [ ] 每周审计报告自动生成

---

## 附录：参考项目

| 参考源 | 路径 | 核心可借鉴 |
|--------|------|-----------|
| ERPAI smoke-erpai | `F:/ERPAI/.claude/skills/smoke-erpai/` | 5 步烟雾流水线 |
| ERPAI ui-verify | `F:/ERPAI/.claude/skills/erpai-ui-verify/` | SSR 渲染验证循环 |
| ERPAI test-engineer | `F:/ERPAI/.claude/skills/test-engineer-skill/` | 测试编写 SOP |
| ERPAI agent | `F:/ERPAI/.claude/agents/erpai-test-engineer.md` | Agent 定义模板 |
| ERPAI test files | `F:/ERPAI/control-center/test/` | 931 个参考用例 |
| OpenClaw smoke | `/c/Users/Admin/.openclaw/check_*.js` | HTTP 烟雾模式 |
