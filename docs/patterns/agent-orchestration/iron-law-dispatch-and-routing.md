# 铁律调度与 Agent 路由

## 来源
- 项目: OpenClaw
- 关键文件:
  - `C:/Users/Admin/.openclaw/control-center/.claude/commands/dispatch.md`
  - CLAUDE.md 路由表 + 调度规则表

## 问题
在大项目中，AI Agent 或开发者面对多种任务类型，最容易犯的错误是：
- 擅自写代码 — "方案通过了，我直接改吧"（最危险的违规行为）
- 工程师在自己的舒适区解决所有问题 — 前端工程师去写数据库迁移，平台工程师去改业务逻辑
- 没有统一的入口，每个人按自己的习惯工作，流程不可审计

## 方案
OpenClaw 建立了一套**两阶段门控**机制：

**第一道门：铁律调度（Iron Law Dispatch）**
所有代码改动必须走 `/dispatch <agent> <task>` 命令。用户说"同意"只代表方案通过，不触发执行。调度入口是唯一允许触达代码的通道。
- 关键设计：方案评审和执行分离，两个阶段不能合并
- 命令入口文件定义可调度的 Agent 白名单，不在列表中的 Agent 无权执行

**第二道门：Path-Scoped Agent Routing（路径范围路由）**
按文件路径精确分配任务给对应的专用 Agent：
```
src/platform/**          → cc-platform-engineer
test/**/*.test.ts        → cc-test-engineer
*.mcp.ts                 → 主 context
src/marketing-os/**      → cc-marketing-agent
```
路由表在 CLAUDE.md 维护快捷参考，`registry.json` 是唯一真相源。

## 在我们项目(yatushi)中的应用
YTS 当前 CLI AUDEmd 已有项目路由表（`build` / `dispatch` / `grill` / `investigate` / `qa` 等），但缺乏两层设计：
1. **强化 dispatch 命令** — 当前 `/dispatch` 只是一个 skill 入口，可以明确定义：方案评审通过的修改才能 dispatch；dispatch 本身不做方案设计
2. **路径范围扩展** — 当前路由表按任务类型分，可以增加按目录范围的精确路由：
   ```
   yts_project/src/main/java/**/controller/  → java-controller-agent
   yts_project/src/main/java/**/service/     → java-service-agent
   yts_project_vueai/src/views/              → vue-view-agent
   database_scripts/                         → db-migration-agent
   ```
3. **gate 模式落地** — 在 CLAUDE.md 铁律部分增加一条：所有涉及 `yts_project/` 和 `yts_project_vueai/src/` 的修改必须先有 execution plan，plan 需要 CC 确认后再 dispatch 执行

---

