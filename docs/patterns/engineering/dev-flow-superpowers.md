# 强制开发流程（Dev Flow / Superpowers）

## 来源
- 项目: OpenClaw
- 关键文件:
  - `C:/Users/Admin/.openclaw/skills/dev-flow/SKILL.md` — 流程定义
  - 7 个子 skill 目录对应 6 个阶段

## 问题
开发随意性大是 AI Agent 和人类开发者共有的问题：
- 跳过设计直接写代码 → 写到一半发现方向错了
- 跳过测试 → 提 PR 后被 reviewer 打回
- 没有结构化的调试流程 → 同一个 bug 修好几次
- "先上线再说"心态 → 技术债务积累

核心矛盾：**流程是好的，但没人愿意主动执行流程**。

## 方案
把开发流程编码为不可跳过的 6 阶段强制流水线，每个阶段是独立子 skill，有明确的输入输出：

```
brainstorming
    ↓ (产出: design.md)
writing-plans
    ↓ (产出: plan.md)
executing-plans
    ↓ (产出: code)
TDD
    ↓ (产出: tests + green bar)
debugging
    ↓ (产出: debug-log.md)
finishing
    ↓ (产出: finish-checklist.md)
```

**每个阶段的设计：**
1. **Brainstorming** — 产出 `design.md`，包含问题定义、方案对比、选型理由
2. **Writing Plans** — 产出 `plan.md`，包含实现步骤、文件清单、影响范围
3. **Executing Plans** — 按 plan 写代码，不允许自己发明步骤
4. **TDD** — 先写测试再写实现，`plan.md` 中必须包含测试用例设计
5. **Debugging** — 结构化调试：复现 → 隔离 → 根因分析 → 修复 → 验证，产出 `debug-log.md`
6. **Finishing** — 对照 checklist 验收：构建通过、测试通过、lint 通过、文档更新

**关键机制：** Agent 无法省略任何阶段。每个阶段不产出规定 artifact 就不能进入下一阶段。

## 在我们项目(yatushi)中的应用
YTS 当前的开发流程在 CLAUDE.md 中有路由（`build` / `dispatch` / `grill` / `investigate` / `qa`），但没有强制顺序。

**可以借鉴的轻量版本：**
1. 在 `.claude/commands/` 下创建一个 `dev-flow` 命令入口，封装 4 个阶段（简化版）：
   - `design` → 写设计文档（`docs/design/YYYY-MM-DD-feature-name.md`）
   - `plan` → 写实施计划（包含文件清单、测试策略）
   - `implement` → 编码 + 测试
   - `verify` → 跑构建 + lint + 测试 + 写 changelog

2. 每个阶段生成对应的 artifact 文件，存放在 `docs/design/` 和 `docs/plans/` 目录

3. 在 CLAUDE.md 加入规则：
   ```
   涉及 yts_project/ 和 yts_project_vueai/ 的改动必须：
   1. 先写 design.md （除非是 bug fix）
   2. 再写 plan.md （列出所有要改的文件）
   3. 实现 + 测试
   4. 最后跑 mvn compile + npm run build 验证
   ```

4. 对于当前正在开发的 **Compatibility Query** 模块，可以先用完整的 4 阶段流程验证效果

**注意：** 这个模式对 YTS 的价值在于减少"写到一半发现方向错了"的返工成本。但不需要照搬 6 阶段，4 阶段（设计 → 计划 → 实现 → 验证）已经覆盖核心质量控制点。

---

