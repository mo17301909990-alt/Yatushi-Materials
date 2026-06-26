# 数字员工代理注册表

## 来源
- 项目: ERPAI
- 关键文件:
  - `control-center/src/erpai/agents/registry.ts` — 统一注册表 `createERPaiAgentTeam()`
  - `.claude/rules/agents.md` — Agent 层规范

## 问题
多 Agent 系统最常见的混乱：Agent 职责不明确、发现难、赋权无标准。
- 新 Agent 加到哪？没人知道
- "这个 Agent 能做什么？"要翻代码才知道
- 权限管理随意，有的 Agent 能删库，有的只让读，没有统一的等级

## 方案
将 Agent 映射为企业 HR 员工档案，通过一个统一注册表管理全生命周期。

**注册表结构（惰性单例）：**
```
createERPaiAgentTeam() → 返回所有 Agent 实例
```

**每个 Agent 的注册信息：**
- `employeeNo` — 工号（DA-001 ~ DA-026）
- `positionCode` — 岗位编码（映射到组织架构）
- `displayName` — 显示名称
- `responsibilities` — 职责清单
- `department` — 所属部门
- `manager` — 上级
- `skills` — 绑定的 Skill 列表
- `permissionLevel` — L1 只读 / L2 低风险自动 / L3 审批执行
- `sopList` — SOP 列表（从 `capabilities/sops/{department}/` 自动扫描）

**设计要点：**
1. Agent ID ↔ employeeNo ↔ positionCode 三向映射，不重不漏
2. SOP 自动扫描 — 加新 SOP 只需放到对应目录，注册表自动发现
3. 跨 Agent 查询走 `cross-agent-query.ts`，避免直接依赖其他 Agent 实例
4. LLM 调用必须走 `ollamaChat()` + circuit breaker 保护

## 在我们项目(yatushi)中的应用
YTS 当前没有多 Agent 编排的需求，但这个模式的管理思想可以直接落地到**团队协作和任务分配**：
1. **对应到实际开发团队** — 在 `.claude/` 下维护一个 `team-registry.yaml`，描述每个开发者的擅长领域、负责模块、审批权限等级（CRUD 谁可以改什么）。当 dispatch 任务时自动匹配给对应的人或 Agent
2. **权限等级落地** — 结合 YTS 现有的 RBAC 权限体系，定义三级操作权限：
   - L1：查数据、读代码、做分析
   - L2：改配置文件、加测试、修注释
   - L3：改业务逻辑、改数据库 Schema、改 API
3. **SOP 目录自动扫描** — 在 `docs/sops/` 下维护标准操作流程，每个流程一个 Markdown 文件。新流程只需放进去，路由表自动发现

这对于当前正在开发的 compatibility query 模块特别有用 — 定义清楚这个模块的"数字员工"职责边界，不会越界去改其他模块。

---

