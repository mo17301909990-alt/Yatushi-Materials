# 企业 DNA 装配引擎

## 来源
- 项目: ERPAI
- 关键文件: `F:/ERPAI/docs/enterprise-dna-assembly-engine.md`

## 问题
企业资源计划（ERP）软件面临根本矛盾：每个行业的生产方式、供应链模式、合规要求、审批流程完全不同。传统 ERP 用"大而全 + 配置开关 + if/else"应对所有行业，结果是代码膨胀、行业适配成本高、维护难度大。一个制造企业的配置涉及 BOM 结构、采购流程、质检深度、财务合并方式等十几个维度的排列组合，if/else 堆砌的方式不可持续。

## 方案
将企业建模为 **8 维向量**：`production_mode / bom_type / supply_chain / traceability / compliance / finance_mode / decision_centralization / projectization`。维度值决定路由、审批、状态机、字段、报表的自动装配，而不是写死的 if/else。企业上线时回答 3-5 个引导式问题，系统自动推导所有维度取值，然后装配出该企业专属的 ERP 实例。

装配引擎分四层：
- **Registry 层**：维度定义 + 变体注册表
- **Resolver 层**：维度值 → 变体选择
- **Generator 层**：选定的变体 → 生成 YAML variant
- **Validator 层**：验证装配结果一致性

### 维度 → 流程变体映射示例
```
production_mode=mts → 标准 MRP 流程 (MPS→MRP→PR→PO)
supply_chain=customer_supplied → PO 状态机多出"向客户申请→客户批准→超耗核销"
compliance=pharma_gmp → 质检放行审批+偏差审批+变更控制审批（3级）
```

## 关键代码/配置片段
```yaml
# enterprise-dna.yaml
version: 1.0
dna:
  production_mode: mts
  bom_type: tree
  supply_chain: own
  traceability: lot
  compliance: iso
  finance_mode: single_entity
  decision_centralization: unified
  projectization: none

overrides:
  procurement:
    approval_threshold: 50000
  quality:
    sampling_rate: 0.65
```

```text
# 同一"采购订单"不同行业不同状态集
own + mts → PO: 草稿 → 审批 → 已发出 → 供应商确认 → 部分收货 → 完成 → 关闭
customer_supplied → PO: 草稿 → 审批 → 向客户申请 → 客户批准 → 供应商下单 → 部分收货 → 超耗核销 → 完成
group_internal → PO: 草稿 → 集团审批 → 内部确认 → 发货 → 内部收货 → 内部结算 → 完成
```

## 在我们项目(yatushi)中的应用

**直接可用的洞察：** yatushi 每种工艺（烫金/裱纸/丝印/过胶/LED UV）的兼容性规则、BOM 结构、状态流转完全不同。目前 `material_process_compatibility` 表 10,071 行数据硬撑着，代码层面仍是 if/else 堆砌。

1. **维度简化**：不需要 8 维，只需要 3-4 维就够了——`process_type`（决定兼容性规则引擎）、`bom_structure`（single/tree，决定 BOM 展开方式）、`matching_mode`（exact/range/compound，决定物料匹配算法）、`traceability`（none/lot，决定是否需要批次追踪）。
2. **规则引擎改造**：`material_process_compatibility` 从一张大表拆成"基础规则（所有工艺共享）+ 工艺特定变体"。新工艺接入时只写一个变体文件，不动主流程。
3. **状态机提取**：把 `HotStampingController` 等中的状态流转逻辑提取成状态机变体，按 `process_type` 维度切换。
4. **边界**：不做运行时热切换，只在系统初始化或新工艺上线时装配。

---

# The Hall 多 Agent 协作系统

## 来源
- 项目: OpenClaw Control Center
- 关键文件:
  - `C:/Users/Admin/.openclaw/control-center/src/agent-collaboration/collaboration/` — 编排层
  - `C:/Users/Admin/.openclaw/control-center/src/agent-collaboration/services/` — 策略层
  - `C:/Users/Admin/.openclaw/control-center/src/agent-collaboration/repositories/` — 持久化

## 问题
多个 AI Agent 围绕一个复杂任务需要协同工作——不只是简单的"谁先谁后"，而是一个完整的讨论、分配、执行、审查、交接闭环。核心问题：谁发言、谁决策、怎么交接、执行中出错了谁兜底、如何在多个 Agent 间传递上下文。

## 方案
**协作大厅（Collab Hall）**架构，所有 Agent（参与者）在同一个空间中。每个任务经历 `创建 → 讨论 → 执行 → 审查 → 完成` 五个阶段，可选经过 PIV 人肉审批关卡。

### 核心流转
1. **任务创建**（`task-lifecycle.ts`）：操作员创建任务卡，系统解析 @ 提及确定目标参与者，创建聊天房间，调度第一轮讨论。
2. **讨论轮次**（`discussion.ts` -> `determineDiscussionTurnParticipants`）：根据意图类型（决策/继续/多方意见）和已发言参与者数量，动态决定本轮谁发言。优先级：显式 @提及 > 语义角色互补 > manager 收口。
3. **执行分配**（`execution.ts` -> `assignHallTaskExecution`）：获取执行锁避免并发冲突，检查 PIV 关卡，运行执行运行时链。
4. **结构化交接**（`hall-handoff.ts`）：交接包包含 goal / currentResult / doneWhen / blockers / nextOwner / requiresInputFrom / artifactRefs。交接顺序与执行队列一致时自动触发下一棒，不一致时发告警但允许继续。
5. **运行时链**（`runtime-chain.ts` -> `runHallRuntimeExecutionChain`）：自动循环执行 Agent 工具调用，每轮后检查指令 `continue/handoff/review/blocked/done`。内置重试机制（`hiddenRetryBudget`），LLM 返回空交付物时无声重试。
6. **Guard Chain**（`hall-guard/`）：BudgetGuard / CostGuard / TimeoutGuard / ContentSafetyGuard / ExecutionLockGuard / LoopGuard / SkillContractGuard，任一返回 block 则阻断执行。
7. **PIV 审批关卡**（`piv-workflow.ts`）：可选 HITL 阶段 `clarify → plan → implement → final_approve`。

### 状态转换图
```
创建 → 讨论 ──→ 执行 ──→ 审查 ──→ 完成
              ↑        ↓
              └── 阻塞 ←┘
```

## 关键代码/配置片段
```typescript
// orchestrator/discussion.ts — 讨论轮流决策
function determineDiscussionTurnParticipants(input: {
  hall: CollaborationHall;
  taskCard: HallTaskCard;
  task?: ProjectTask;
  triggerMessage?: HallMessage;
  recentThreadMessages: HallMessage[];
}): string[] {
  const followupIntent = classifyHallDiscussionFollowupIntent(triggerText);
  const wantsContinuation = requestsDiscussionContinuation(triggerText);
  const wantsDecision = followupIntent === "decision_request";
  const wantsManyVoices = requestsMultiPerspectiveDiscussion(triggerText);
  // 多级 if-else 决定发言人队列
}
```

```typescript
// services/hall-handoff.ts — 结构化交接包
interface CreateStructuredHandoffInput {
  goal: string;           // "做什么"
  currentResult: string;  // "做到哪了"
  doneWhen: string;       // "做到什么程度算完"
  blockers?: string[];    // "卡点"
  nextOwner: string;      // "下一棒谁"
  requiresInputFrom?: string[];  // "还需要谁配合"
  artifactRefs?: TaskArtifact[]; // "产出物"
}
```

## 在我们项目(yatushi)中的应用

1. **轻量级交接协议**（最易落地）：yatushi 目前 AI 场景是"人问 AI 答"的单轮会话。但如果未来把烫金匹配 AI、裱纸匹配 AI、物料兼容性 AI 串联起来，可以用类似于 `StructuredHandoff` 的方式在工序间传递上下文——当前产品规格、已确定的物料、待确认参数，避免中间结果丢失。
2. **讨论→执行→审查的流转模型**：yatushi 的 `AiController` + `DashScopeChatService` 层面，一个 AI 查询需要调用多个工序知识时，可以用类似 `HallTaskCard` 的结构记录当前 AI 任务的阶段。
3. **BudgetGuard 思想**：在 guard chain 中做 AI token 消耗控制。
4. **简化说明**：yatushi 目前没有"多 Agent 实时共处一室"的需求，全盘照搬是过度设计。交接协议是最容易落地的点——把一个工序 AI 的产出物格式化，传递给下一个工序 AI。

---

# 服务分解模式

## 来源
- 项目: OpenClaw Control Center
- 关键文件:
  - `src/services/ai-copilot-service.ts` — 主服务（660行，边界上限）
  - `src/services/ai-copilot/` — 子模块分解目录
  - `src/services/marketing-routing-service.ts` — 声明式路由

## 问题
单体服务膨胀。传统做法是一个 Service 类包所有业务逻辑，几百上千行，职责混在一起——路由、数据组装、LLM 调用、配置注入全揉一团。要拆的时候边界模糊，不敢动。

## 方案
OpenClaw `src/services/` 目录下只有 8 个文件/子目录，总行 1566，遵循极其严格的粒度控制：

```
src/services/
├── ai-copilot-service.ts              # 660行 — 意图路由 + 多模型调度
├── ai-copilot/                        # 子模块分解
│   ├── data-bridge.ts                 #  41行 — 数据注入桥接
│   ├── intent-handler.ts              # 100行 — 意图处理分派
│   ├── section-agent-registry.ts      # 157行 — 分区 Agent 注册表
│   ├── skill-bridge.ts                # 182行 — Skill 桥接层
│   └── static-actions.ts              #  20行 — 静态动作枚举
├── marketing-hall-bridge.ts           # 242行 — Hall<=>Marketing 桥
└── marketing-routing-service.ts       # 164行 — 声明式路由匹配引擎
```

### 核心分解原则
1. **声明式路由从控制器剥离** — `marketing-routing-service.ts` 将 14 条路由定义为纯数据（`MARKETING_ROUTES = MARKETING_SKILLS.map(...)`），不含执行逻辑。路由匹配返回 `{ route, confidence, matchedKeywords }`，消费方自己决定怎么用。
2. **"可提取的子职责"独立成文件** — `intent-handler.ts` / `data-bridge.ts` 是被主 service import 的独立模块，不是内部方法。每个文件不超过 200 行，20-180 行是常态。
3. **Bridge 模式对接外部系统** — `marketing-hall-bridge.ts` 是 fire-and-forget 桥接器，从不抛异常。定义了 `MarketingHallLink` / `HallTaskSuggestion` / `PipelineRunSummary` 作为契约边界。
4. **配置即代码** — 常量直接写在文件里，不在小团队下创建额外的 config 目录。

### 对比
| 维度 | 大 Service（传统） | 单职责文件（OpenClaw） |
|------|-------------------|----------------------|
| 边界认知 | 一个文件几百行，方法混在一起 | 文件名即职责声明 |
| 测试 | mock 一整个类 | 引入小模块，极轻量 |
| 重构 | 动一发牵全身 | 替换一个文件不影响其他 |
| 跨模块复用 | 靠抽出 Util 类，语义丢失 | 天然可 import |

## 关键代码/配置片段
```typescript
// marketing-routing-service.ts — 声明式路由
const MARKETING_ROUTES: MarketingRoute[] = MARKETING_SKILLS.map((s) => ({
  id: s.id, name: s.name, description: s.description,
  exampleIntents: s.exampleIntents ?? [],
  dataInjections: (s.dataInjections ?? []) as DataInjection[],
  runtimeSkillId: s.runtimeSkillId ?? "",
  capabilities: s.capabilities ?? [],
}));
```

```typescript
// ai-copilot-service.ts — 消费方只做编排不做路由
const SERVICE_DISPATCH = {
  marketingBriefService: { generateMarketingBrief: () => ... },
  contentPipelineService: { listPipelines: () => ... },
  // 7 个 services, 每个一行
};
```

## 在我们项目(yatushi)中的应用

1. **做一次 Service 行数扫描**：对 `service/` 目录下所有 `ServiceImpl` 做 `wc -l`，标出 >300 行的文件。
2. **提取子职责**：不是拆 Service 类（这会破坏 Spring DI 结构），而是把核心算法/路由/数据处理提取成 `util/` 或 `strategy/` 下的 stateless 静态方法或 Spring `@Component`。
3. **前端复用**：yatushi 的 `stores/copilot.ts` 和 `components/copilot/` 下的组件——确保每个 store 只管一个领域（不要一个 copilot store 管对话+兼容性查询+历史记录三个事情）。OpenClaw 的 `ai-copilot/` 子目录分解模式可以直接照搬到前端 `components/copilot/`。
4. **红线**：超过 500 行的 Service 文件就应考虑子目录拆分。

---

# Skill 进化引擎

## 来源
- 项目: OpenClaw Control Center
- 关键文件:
  - `src/agent/services/skill-evolution-types.ts` — 类型定义
  - `src/agent/services/skill-evolution-service.ts` — 进化引擎核心（254行）
  - `src/agent/services/skill-evolution-log-repository.ts` — 持久化
  - `src/agent/services/skill-suggestion-engine.ts` — 数据驱动的 Skill 草稿生成（160行）

## 问题
技能（Skill）用久了以后，质量评估停留在"人评"——没有自动反馈回路。一个 Skill 做得好不好、要不要继续探索、应该降权还是升权，全靠人的主观判断。Agent 越多，人工评估成本越高。

## 方案
闭环自动进化，核心流程：

### EvaluateSkillEvolution 算法
1. 拉取 RewardSignal → 聚合 mean / variance / positiveCount
2. EMA 更新 confidence：`newConfidence = α * normalizedMean + (1-α) * oldConfidence`（α=0.3，7天窗口，minSamples=5）
3. 方差驱动的 exploration_bonus：
   - `variance > 0.25` → bonus +0.1（不确定性高→多探索）
   - `variance < 0.05 + mean > 0.5` → bonus -0.05（成熟技能→减少探索）
   - 每个周期乘 0.95 衰减
4. 趋势检测：improving / declining / stable
5. 持续衰退触发 Data Flywheel（铜→银模式提取）

### Skill 建议引擎闭合成环
当某个 Skill 的模式被飞轮提取出来，`skill-suggestion-engine.ts` 从 `agent_observations` 取出相关内容，做词频分析，生成 `SkillSuggestionDraft`。状态机：`pending → approved / rejected`，仅当 `pattern.confidence >= 0.7` 才产生建议。

**设计决策**：不放在生产路径上做实时推理——`runSkillEvolutionCycle` 通过 `monitoring-cron` 批量运行，不在热路径上增加查询负担。

## 关键代码/配置片段
```typescript
// skill-evolution-service.ts — EMA 平滑 + 方差探索
const newConfidence = clamp(
  config.emaAlpha * normalizedMean + (1 - config.emaAlpha) * oldConfidence,
  config.confidenceFloor, config.confidenceCeiling,
);

if (variance > 0.25) newExplorationBonus += 0.1;
else if (variance < 0.05 && meanReward > 0.5) newExplorationBonus -= 0.05;
```

```typescript
// skill-suggestion-engine.ts — 数据驱动的 Skill 草稿生成
const draft: SkillInput = {
  name: `Suggested: ${trigger.skillId} ${pattern.kind} pattern`,
  knowledge: {
    validElements,
    invalidElements,
    workflow: `Auto-extracted from data flywheel ${pattern.kind} pattern. ${pattern.description}`,
  },
  status: "testing",
  confidence: pattern.confidence,
  explorationBonus: 0.3,  // 新 Skill 给高探索值
};
```

## 在我们项目(yatushi)中的应用

1. **短期（收益最大）**：给 `DashScopeChatService` 加一个简单的 reward feedback loop。用户的回答满意/不满意（点赞/踩）作为 reward signal，统计每类问题的 confidence。如果某个问答类型的 confidence 持续下降，给管理员提示"这个业务域的 AI 回答质量在下降"。
2. **中期**：将兼容性查询（`CompatibilityQueryService`）中用户反复查询但 AI 答错的 case 提取为 `invalidElements`，人工确认后更新 prompt 模板。
3. **不推荐做**：全套 skill 进化引擎（SurrealDB + RewardSignal + 飞轮）。yatushi 的 AI 场景是"人问 AI 答"的单轮会话，不是多 Agent 协作，用 EMA 置信度跟踪就够了，方差驱动的探索机制没有意义。

---

# 成本治理体系

## 来源
- 项目: OpenClaw Control Center
- 关键文件:
  - `src/agent/services/budget-policy.ts` — 预算策略定义
  - `src/agent/services/budget-governance.ts` — 预算评估引擎
  - `src/agent/services/cost-alert-service.ts` — 三种告警类型
  - `src/agent/services/cost-circuit-breaker-service.ts` — 熔断器
  - `src/agent/services/cost-forecast-service.ts` — 线性回归预测
  - `src/agent/services/usage-cost.ts` — 全面用量快照
  - `src/agent-collaboration/services/hall-guard/budget-guard.ts` — Guard链集成
  - `src/agent-collaboration/services/hall-guard/cost-guard.ts` — 模型分档

## 问题
AI Agent 调用 LLM 会产生 token 成本，多个 Agent、多个任务、多个项目同时运行时成本可能失控。需要一套分层治理体系：预算设置、实时告警、熔断机制、成本预测。

## 方案
**分层预算模型**：Agent 级 -> Task 级 -> Project 级，每层可设置独立阈值，子层未设置时继承上层默认值。

### 7 个核心组件
1. **BudgetPolicy** — 从 JSON 加载三层 override：`defaults → scope (agent/project/task) → 具体条目`，每个维度的 warnRatio 默认 80%。
2. **BudgetGovernance** — `computeBudgetSummary` 计算每个维度三个指标的 tokensIn / tokensOut / totalTokens / cost 的状态：ok / warn / over。
3. **CostAlertService** — 三种告警：`token_threshold`（累计 token 超阈）、`latency_threshold`（平均延迟超阈）、`cost_spike`（环比增长率超阈）。30 分钟节流防重复。
4. **CircuitBreaker** — 四条件触发熔断（2+预算超限 / 存在成本激增告警 / 7日预测达 90% 红线 / 多个 warn），熔断后自动暂停超预算 Agent，手动恢复（先 half_open）。
5. **ForecastService** — OLS 线性回归预测未来 7 天成本，confidence 依赖历史数据量（14天=high / 7天=medium / 更少=low）。
6. **BudgetGuard** — 每次 AI 调用前预留预算（`reserveBudget`），余额不足直接 block。
7. **CostGuard** — 按字符长度分类模型 tier：`<=800ch → fast / <=4000 → balanced / >4000 → powerful`。

另外用量看板 `usage-cost.ts` 支持按 Agent/Project/Task/Model/Provider 等多维度 30 天和今日分片统计。

## 关键代码/配置片段
```json
// runtime/budgets.json
{
  "defaults": { "warnRatio": 0.8 },
  "agent": {
    "coding-agent": { "totalTokens": 500000, "cost": 10 },
    "planner-agent": { "totalTokens": 200000 }
  },
  "project": {
    "marketing-os": { "cost": 100 }
  },
  "task": {}
}
```

```typescript
// cost-circuit-breaker-service.ts — 四条件熔断
if (budgetSummary.over >= 2) shouldOpen = true;
if (hasCostSpike) shouldOpen = true;
if (forecast.predictedCost >= tokenThreshold * 0.9) shouldOpen = true;
if (hasTokenThreshold && budgetSummary.warn >= 2) shouldOpen = true;
```

```typescript
// hall-guard/cost-guard.ts — 内容长度分档
const tier = classifyModelTier(ctx.messageContent);
// <=800ch → fast, <=4000 → balanced, else powerful
return { action: "allow", detail: `routed to tier "${tier}"` };
```

## 在我们项目(yatushi)中的应用

1. **最简单的起点**：yatushi 的 AI 功能（`DashScopeChatService`）在调用通义千问前插入预算预留逻辑，每个用户/会话每日设置 token 限额，超过就拒绝调用而不是报错。
2. **成本熔断**：当 AI 调用出现异常激增（如轮询 bug 导致大量重复调用）时，自动停掉非核心 AI 功能，管理员手动恢复。
3. **简化方案**：不需要持久化数据库，在 `AiController` 或 `DashScopeChatService` 中维护一个 `Map<userId, DailyUsage>` 的轻量成本追踪。逻辑完全一致：阈值 -> 告警 -> 阻断。
4. **最适配场景**：后道工序匹配 AI 如果按 API 调用计费，可以给每个工序（烫金/裱纸/丝印）分配独立预算，防止某个工序的异常调用消耗完所有预算。

---

# 安全围栏模式

## 来源
- 项目: OpenClaw Control Center
- 关键文件:
  - `src/agent-collaboration/services/hall-guard/types.ts` — Guard 接口定义
  - `src/agent-collaboration/services/hall-guard/budget-guard.ts` — 预算围栏
  - `src/agent-collaboration/services/hall-guard/cost-guard.ts` — 模型分级
  - `src/agent-collaboration/services/hall-guard/timeout-guard.ts` — 超时控制
  - `src/agent-collaboration/services/hall-guard/content-safety-guard.ts` — 内容安全
  - `src/agent-collaboration/services/hall-guard/loop-guard.ts` — 循环检测
  - `src/agent-collaboration/services/hall-guard/execution-lock-guard.ts` — 执行锁
  - `src/agent-collaboration/services/hall-guard/skill-contract-guard.ts` — 技能合约
  - `src/agent/services/approval-workflow-service.ts` — 审批链
  - `src/agent/services/feature-gate.ts` — 功能门控

## 问题
在多人多 Agent 协作系统中，操作需要有安全边界——谁可以做什么、花多少钱、跑多久、操作什么内容。如果不设围栏，一个误操作可能导致预算超支、死循环、内容违规或数据泄露。

## 方案
分三圈安全体系：

### 第一圈：HallGuard 链（运行时围栏）

核心接口：
```typescript
interface HallGuard {
  readonly name: string;
  readonly enabled: boolean;
  check(ctx: GuardContext): Promise<GuardResult>;
}
interface GuardResult {
  readonly ok: boolean;
  readonly action: "allow" | "warn" | "block";
  readonly detail: string;
}
```

7 个 Guard 实现：
| Guard | 职责 | 触发条件 |
|-------|------|---------|
| TimeoutGuard | 任务执行超时控制 | warn@10min, block@30min |
| BudgetGuard | 预算饥饿控制 | 每日限额预扣制，默认 5 美分/次 |
| ContentSafetyGuard | 内容安全过滤 | block/warn 双重策略的正则匹配 |
| CostGuard | 模型分级降本 | 按字符长度路由 fast/balanced/powerful |
| LoopGuard | 死循环检测 | Jaccard 相似度对比历史记录，阈值 85% |
| HallExecutionLockGuard | 执行锁/参与者验证 | 并发执行锁定 |
| SkillContractGuard | 输入契约校验 | Skill 入参格式验证 |

任一 guard 返回 `block` 则阻断执行，`block` 可被 `applySelfLimiting` 降级为 `warn`。

### 第二圈：Approval Workflow（审批流程）
多级审批链，预置规则：
```
budget ($0):      Manager → Director → VP
budget ($100k+):  Director → VP → CEO
policy_rollback:  Director → VP
skill_promote:    Director
tenant_delete:    CEO
general:          Manager
```
支持角色校验 + 自动升级（可跳过 optional step）。

### 第三圈：配置级围栏（settings.json）
细到命令参数的 allowlist 模式，70+ 条精确匹配，READONLY 模式通过 `openclaw-readonly.ts` 封装只读 API。Feature gating（`feature-gate.ts`）按订阅等级检查功能权限，超出返回 403。

## 关键代码/配置片段
```typescript
// BudgetGuard — 每日预算饥饿控制
const balance = await getBudgetBalance(ctx.tenantId, dimension, dimensionId);
const result = await reserveBudget({
  amountCents: this.defaultCostPerTurnCents,
  dailyLimitCents: balance.daily_limit_cents,
});
```

```typescript
// LoopGuard — Jaccard 相似度死循环检测
// 每次新对话和历史记录算 Jaccard 相似度，>85% 时 log warning
```

```typescript
// approval-workflow-service.ts — 多级审批链构建
export function buildApprovalChain(category: string, amount: number): ApprovalChain {
  const matched = rules.filter(r => r.category === category)
    .filter(r => amount >= r.minAmount)
    .sort((a, b) => b.minAmount - a.minAmount)[0];
  return { steps: matched.steps.map((s, i) => ({ stepIndex: i, ...s })), currentStep: 0 };
}
```

## 在我们项目(yatushi)中的应用

1. **高价值 — LoopGuard 死循环检测**：yatushi 的 `AiController` 如果出现重复提问模式（用户不断问同一类兼容性问题），可以用 Jaccard 相似度检测并提示管理员。简单实现：在 `DashScopeChatService` 里维护最近 10 次对话的 HashMap，每次新对话和前几条算 Jaccard，>85% 时 log warning。
2. **高价值 — ContentSafetyGuard 内容安全过滤**：如果 AI 回答中检测到敏感关键词（价格泄露、跨客户数据），直接 block。对印刷厂这种 B2B 系统，重点是防止"把 A 客户的报价泄露给 B 客户"。
3. **中价值 — ApprovalWorkflow 审批链模式**：如果 yatushi 的"审核导出"功能需要多人审批，可直接用 `buildApprovalChain` 的思路——按操作类型+金额分层审批。
4. **不适用**：BudgetGuard（yatushi 没有按调用计费的场景）、Feature gating（无订阅制）。
5. **可以直接交的作业**：参考 OpenClaw 的 `settings.json` allowlist 模式，团队成员用 Claude Code 开发时一条一条配精确的 `Bash(npx mvn *)` / `Bash(git commit *)`，减少权限确认弹窗。

---

# SSR 组件库（无框架 UI）

## 来源
- 项目: ERPAI
- 关键文件:
  - `F:/ERPAI/control-center/src/erpai/ui/shared/layout.ts` — pageShell 核心布局
  - `F:/ERPAI/control-center/src/erpai/ui/shared/toast.ts` — Toast 通知
  - `F:/ERPAI/control-center/src/erpai/ui/shared/confirm-dialog.ts` — 确认对话框
  - `F:/ERPAI/control-center/src/erpai/ui/shared/empty-state.ts` — 空状态
  - `F:/ERPAI/control-center/src/erpai/ui/shared/error-pages.ts` — 错误页面
  - `F:/ERPAI/control-center/src/erpai/ui/shared/notification-bell.ts` — SSE 实时通知
  - `F:/ERPAI/control-center/src/erpai/ui/shared/css/tokens.ts` — CSS 设计令牌
  - `F:/ERPAI/control-center/src/erpai/ui/shared/css/` — 全套 CSS 组件样式

## 问题
前端框架（React/Vue/Angular）带来构建工具链复杂、包体积大、SSR 需要依赖重型框架、页面上线需编译。对于内部管理系统/ERP 类应用，大部分页面是列表/表单/详情页，数据在服务端已准备好，浏览器只需简单的展示和事件交互。引入虚拟 DOM + 框架运行时属于过度抽象。

## 方案
TypeScript 函数生成 HTML 字符串，所有组件在服务端拼接成完整 HTML 一次性吐出，浏览器端只有极简的 JS（Toast/Confirm/SSE 通知）。没有虚拟 DOM、没有框架运行时、没有打包构建（仅 tsc 编译 TS）。

### 目录结构
```
shared/
├── layout.ts              # pageShell 核心 — 统一页面骨架生成
├── toast.ts               # Toast 通知组件
├── confirm-dialog.ts      # 确认对话框
├── empty-state.ts         # 空状态
├── error-pages.ts         # 404/403/500/503 错误页面
├── notification-bell.ts   # SSE 实时通知铃铛
├── nav-tree.ts            # 左侧导航树
├── board-css.ts           # 看板布局 CSS
├── action-bar.ts          # 操作栏
├── editable-table.ts      # 可编辑表格
├── erp-list-page.ts       # 列表页模板
├── document-list-page.ts  # 单据列表页模板
├── markdown-renderer.ts   # Markdown 渲染
├── panel-renderer.ts      # 面板渲染
├── perf.ts                # Brotli/gzip 压缩
├── workfkow-ui.ts         # 工作流 UI
├── hint-data.ts / hint-engine.ts  # 进化日志提示条
├── ai-copilot-panel.ts    # AI Copilot 侧栏
├── calc-engine.ts         # 计算引擎
├── event-handler.ts       # 事件处理
└── css/
    ├── tokens.ts          # 设计令牌（CSS 变量）
    ├── reset.ts           # CSS Reset
    ├── index.ts           # CSS 聚合入口
    ├── button.ts / badge.ts / card.ts / form.ts  # 组件样式
    ├── table.ts / tab.ts / modal.ts              # 布局样式
    └── nav.ts / responsive.ts / dark-theme.ts    # 主题样式
```

### 核心工作流
```typescript
// 1. 页面函数调用 pageShell() 生成完整 HTML
const html = pageShell("采购订单列表", renderTable(rows), {
  breadcrumbs: ["采购管理", "采购订单"],
  activeModule: "procurement",
  username: "张三",
});
// 2. 所有 CSS 内联到 <style> 标签（无外部 CSS 文件依赖）
// 3. 所有 JS 函数注入到 <script>（全局可用，无需 import）
// 4. 响应通过 Brotli/gzip 压缩发送
// 5. 浏览器端按需调用 showToast / showConfirmDialog
```

### 组件示例对比
```typescript
// React 组件
const EmptyState = (props) => <div><Icon/><h3>{props.title}</h3><p>{props.desc}</p></div>;

// ERPAI 纯函数（无框架）
export function renderEmptyState(options): string {
  return `<div style="padding:48px 20px;text-align:center">
    <div style="font-size:40px;line-height:1;margin-bottom:12px">${e(icon)}</div>
    <h3 style="margin:0 0 6px;font-size:15px;font-weight:600;color:#1f2937">${e(title)}</h3>
    <p style="margin:0 0 16px;font-size:13px;color:#6b7280">${e(description)}</p>
    ${action ? renderAction(action) : ""}
  </div>`;
}
```

### 设计令牌体系
```css
:root {
  --erp-primary: #1a8cff;
  --erp-success: #52c41a;
  --erp-error: #ff4d4f;
  --erp-text: #1d2129;
  --erp-bg-main: #eef2f6;
  --erp-sidebar-bg: #1a1d2e;
  --erp-header-bg: linear-gradient(135deg, #1a3a6b 0%, #2d5a8e 100%);
  --erp-shadow-card: 0 1px 3px rgba(0,0,0,.06), 0 1px 2px rgba(0,0,0,.04);
}
[data-theme="dark"] {
  --erp-text: #e0e0e6;
  --erp-bg-main: #141416;
  --erp-sidebar-bg: #0f0f1a;
}
```

## 在我们项目(yatushi)中的应用

**核心启示：yatushi 的前端太重了。** 当前 Vue 3 + TypeScript + Vite + Element Plus + Pinia + Tailwind 对于内部管理系统来说，每次改动都要走完整构建链，页面加载需要下载完整的 Vue runtime + Element Plus (~300KB gzip)。

1. **尝试特定模块 SSR 化**：选择交互最简单的页面（如公告/字典管理 `NoticeDictionaryController`、物料规则统计 `MaterialRuleStatisticsController`），实验性地用 Java 后端直接生成 HTML（Thymeleaf 或字符串拼接），参照 ERPAI 的 `pageShell` 模式。
2. **组件体系借鉴**：
   - `pageShell` → 公共布局（header/sidebar/footer）+ 面包屑
   - `toast.ts` → 手动拼接 Toast HTML + 简单 JS
   - `empty-state.ts` → 一个公共 fragment
   - `error-pages.ts` → 统一错误页面模板
3. **设计令牌迁移**：CSS 变量体系直接复用到现有的 Tailwind + Element Plus 混搭风格中，把主色/语义色/间距/阴影统一成 `--yts-*` 变量，替代目前分散的内联样式。
4. **不做**：复杂交互页面（物料兼容性多维筛选、BOM 树编辑、拖拽看板）保留 Vue 组件，不重写。只在"信息展示 > 交互复杂"的页面尝试 SSR 模式。
5. **长远方向**：如果后期考虑新项目，可以探索"Java 后端 SSR + 前端原生 JS"的混合模式，去掉 Vue 依赖，把前端变成极薄的 HTML 模板层。
