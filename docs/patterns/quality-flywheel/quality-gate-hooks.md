# Hook 质量门

## 来源
- 项目: OpenClaw
- 关键文件:
  - `C:/Users/Admin/.openclaw/control-center/.claude/settings.json`（Stop / PostToolUse hooks）
  - `scripts/stop-typecheck.sh`（Stop hook 调用的类型检查脚本）

## 问题
代码质量问题通常在两个阶段暴露：
1. **构建/CI 阶段** — 此时离写完代码已经过去很久，context 已切换，修复成本高
2. **Code Review 阶段** — reviewer 发现格式/类型问题，浪费人的注意力

核心矛盾：开发者自觉性不可靠。"我会记得跑 lint"永远是不可执行的承诺。

## 方案
用 Hook 机制在编辑器/工具层面强制质量门，不依赖人的自觉性。

**两道门：**

**第一道 — PostToolUse 自动格式化：**
工具完成文件写入后，Hook 自动触发 prettier 格式化。开发者不需要手动跑 `npm run format`，也不需要 IDE 插件。
```json
{
  "hooks": {
    "PostToolUse": "npx prettier --write --ignore-unknown {file_path}"
  }
}
```

**第二道 — Stop 类型检查：**
工具完成 Edit/Write/Create 操作后，Stop hook 自动触发 TypeScript 类型检查（轻量模式）。类型错误在进入 context 之前就被捕获。
```json
{
  "hooks": {
    "Stop": [
      { "glob": "**/*.{ts,tsx,js,jsx}", "run": "npx tsc --noEmit --light" }
    ]
  }
}
```

**为什么有效：**
- 系统级强制，不是文化级约定
- 比 CI 阶段拦截早了两个数量级（秒级 vs 分钟级）
- 零信任 — 不假设开发者会自觉跑检查
- 成本极低（配置 20 行 JSON），收益极高（零类型错误进入 context）

## 在我们项目(yatushi)中的应用
YTS 项目当前已有 CLAUDE.md 和后端 Java + 前端 TypeScript，可以立刻落地的 Hook：

**前端（yts_project_vueai）— 直接可用：**
1. PostToolUse 自动格式化：`.claude/settings.json` 中配置 prettier 自动格式化
2. Stop 类型检查：在 Edit/Write Vue/TS 文件后自动跑 `npx vue-tsc --noEmit` 或 `tsc --noEmit`

**后端（yts_project）— Java 版本：**
Claude Code 的 Hook 本身是进程级工具，但可以包装 Maven 命令：
1. PostToolUse 对 `*.java` 文件自动跑 `./mvnw spotless:apply`（如果配置了 Spotless）
2. Stop 对 `*.java` 文件自动跑 `./mvnw compile -q` 快速检查编译错误

**具体 settings.json 配置：**
```json
{
  "hooks": {
    "PostToolUse": [
      { "glob": "yts_project_vueai/**/*.{vue,ts,js}", "run": "cd yts_project_vueai && npx prettier --write {file_path}" },
      { "glob": "yts_project/**/*.java", "run": "cd yts_project && ./mvnw spotless:apply -q" }
    ],
    "Stop": [
      { "glob": "yts_project_vueai/**/*.{ts,vue}", "run": "cd yts_project_vueai && npx vue-tsc --noEmit" },
      { "glob": "yts_project/**/*.java", "run": "cd yts_project && ./mvnw compile -q" }
    ]
  }
}
```

这是最值得优先采纳的模式 — 配置 20 分钟，收益每天可见。

---

