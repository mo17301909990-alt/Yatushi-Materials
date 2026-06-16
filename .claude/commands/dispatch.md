# /dispatch — 代码改动执行门

**铁律入口。** 涉及代码改动必须通过此流程。用户说"同意"只代表方案通过。

---

## 执行路径

```
接到任务
  → 匹配已有 Skill → 调用 Skill
  → 无 Skill 但有 Agent → spawn Agent
  → 都没有 → 创建新 Skill 或 Agent
  → 我负责写 prompt、合结果、验收
```

## Agent 路由

| 任务 | Agent |
|------|-------|
| Java 后端逻辑/API | spawn Java developer agent |
| Vue 前端/UI | spawn Vue developer agent |
| 数据库/SQL/MyBatis | spawn DB specialist agent |
| 审计/代码审查 | spawn global-code-reviewer |
| Bug 排查 | spawn investigate |
| 端到端验证 | spawn qa |
| 架构/方案设计 | spawn software-architect |

## 前置检查

每次 spawn Agent 前，按顺序执行：

```
□ Step 1: 查现有 Skill/Agent
   · 扫 .claude/skills/ 和 registry.json
   · 已有则直接用，不新建

□ Step 2: 审方案（仅新功能/架构变更）
   · 先 spawn software-architect 审查方案
   · 审查通过才能继续

□ Step 3: 匹配 Agent
   · 按上表路由匹配
   · 确认: "Agent 路由匹配"
```
