# Worktree 隔离执行模式

## 来源
- 项目: YTS Materials + Claude Code 通用工作流
- 关键文件:
  - `CLAUDE.md` — "铁律：隔离执行" 全局规则
  - `.claude/commands/dispatch.md` — 代码改动执行门
  - `EnterWorktree` / `ExitWorktree` — 内置工具
  - `Agent isolation: worktree` — Agent 级隔离

## 问题
AI 辅助开发中的代码改动容易产生副作用：
- 多个并行任务互相覆盖改动（一个修 Bug 一个加功能）
- 改到一半想切换分支，本地未提交的改动卡住
- Agent 改完代码直接跑测试，测试环境被本次改动污染
- 用户说"试试这个方案"→ 改了回滚麻烦

## 方案

### 三级隔离层级

```
L0: 文件级编辑          L1: 分支隔离            L2: Worktree 隔离
┌──────────┐           ┌──────────┐           ┌──────────────┐
│ 改一个文件 │           │ feature/  │           │ .claude/     │
│ 不需要隔离 │           │ 分支上改   │           │ worktrees/   │
│（typo等） │           │ 多个文件  │           │ 完全独立副本  │
└──────────┘           └──────────┘           └──────────────┘
                         隔离级别 ↑
                         安全性 ↑
                         开销 ↑
```

### Worktree 隔离的工作原理

```
主仓库                              Worktree
┌─────────────┐                 ┌─────────────────────┐
│  main        │                 │  feat/xyz-worktree   │
│  未提交改动   │    git worktree │  基于 main/HEAD 创建  │
│  正常工作区   │  ──────────→   │  完全独立的文件系统    │
│              │                 │  mvn test 不会影响    │
│              │                 │  主仓库的 target/     │
└─────────────┘                 └─────────────────────┘

Agent 在 worktree 内改代码 → 改坏了 → 删 worktree → 零影响
Agent 改好了         → 保留 worktree → 后续提 PR
```

### 强制规则

```yaml
# CLAUDE.md 铁律
代码改动必须走 EnterWorktree 或 Agent isolation:worktree。
不进隔离不动手。
```

### Agent 级隔离（轻量方案）

通过 `isolation: "worktree"` 参数启动隔离 Agent：

```
Agent({
  isolation: "worktree",
  prompt: "改 UserController 的校验逻辑..."
})
// Agent 在临时 worktree 中工作，主仓库不受影响
```

### 生命周期管理

```
创建:  EnterWorktree / Agent({isolation: "worktree"})
工作:  在隔离环境里改→测→循环
退出:
  ├─ keep   → 保留 worktree 和分支（后续回来继续）
  └─ remove → 删 worktree + 删分支（零残留）

注意：ExitWorktree remove 时如果有未提交改动会被拒绝
      → 需加 discard_changes: true 强制清理
```

### 适用场景

| 任务类型 | 隔离级别 | 理由 |
|---------|---------|------|
| 改一行注释/typo | 不隔离 | 零风险 |
| 单个 Java 方法修改 | 分支隔离 | 可回滚 |
| 新功能/新增文件 | Worktree | 防止污染主工作区 |
| 并行多个独立修改 | Worktree × N | 各不干扰 |
| 架构重构/大量改文件 | Worktree | 改坏不伤主库 |
| 实验性探索 | Worktree | 试完就删 |

## 关键设计决策

| 选项 | 选择 | 理由 |
|------|------|------|
| 默认隔离策略 | Worktree 隔离 | 铁律级别，不讨论不例外 |
| 隔离目录 | `.claude/worktrees/` | 统一管理，gitignored |
| 残留清理 | 手动不自动 | 用户决定是否保留，防止误删 |
| 并行上限 | 约 10 个 worktree | git worktree 默认限制 |

## 潜在演进

- **软隔离** — 对低风险改动（如配置文件）放宽为分支级隔离，worktree 保留给高风险操作
- **自动过期** — 超过 N 天未使用的 worktree 提示清理（目前干 run 确认）
- **worktree 内测试** — worktree 建好自动跑一次 baseline test，确认基线通过再改
