# /dispatch — 代码改动执行门

**铁律入口。** 涉及代码改动必须通过此流程。用户说"同意"只代表方案通过。

## 执行路径

```
接到任务
  → 匹配已有 Skill → 调用 Skill
  → 无 Skill 但有 Agent → spawn Agent
  → 都没有 → 创建新 Skill 或 Agent
  → 我负责写 prompt、合结果、验收
```

## 前置检查

```
□ Step 1: 查现有 Skill/Agent（.claude/skills/ 和 registry.json）
□ Step 2: 审方案（仅新功能/架构变更 → spawn software-architect）
□ Step 3: 匹配 Agent（按以下路由表）
□ Step 4: verify 步骤（确认 Done Criteria 强检查命令通过）
```

## Agent 路由

| 任务 | Agent | 验证命令 |
|------|-------|---------|
| Java 后端逻辑/API | `java-backend` | `mvn compile -q` |
| Vue 前端/UI | `vue-frontend` | `npm run build` |
| 数据库/SQL/MyBatis | `db-specialist` | `mvn compile -q` + SQL EXPLAIN |
| 兼容性规则引擎（YAML） | `rule-engine` | YAML lint + 规则版本校验 |
| Service 拆分/Excel 抽离 | `service-decomposer` | ≤300 行检查 + grep Excel |
| 质量门禁/Hook 配置 | `quality-gate` | 路径校验 + hook 触发测试 |
| 安全审计/密码扫描 | `guard` | Controller 权限注解扫描 |
| Bug 排查 | `investigate` | 定位→复现→修复→记录 gotcha |
| 代码审查 | `global-code-reviewer` | 编译通过 + 逻辑审查 |
| 任何代码改动 | `quality-gate` | `bash scripts/quality-gate.sh` |
| 端到端验证 | `qa` | curl API + 前端构建 |
| 架构/方案设计 | `software-architect` | 方案文档产出 |

## Fallback

路由表无匹配 → 自动进入 `review` 流程。
不可自行决定派发给未注册的 Agent。

## 记录

每次 dispatch 完成后，判断是否触发知识飞轮升级条件：
```
□ 本次修复是否与已有 gotcha 同类？→ 计数满 3 次考虑升级白银
□ 本次发现是否值得升级 ai-knowledge？→ 写领域知识文档
```
