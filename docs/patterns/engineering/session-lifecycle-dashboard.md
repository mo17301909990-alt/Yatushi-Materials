# 会话生命周期管理与会话启动仪表盘

## 来源
- 项目: OpenClaw
- 关键文件:
  - `C:/Users/Admin/.openclaw/control-center/.claude/settings.json`（SessionStart / PreCompact / SessionEnd hooks）
  - `scripts/precompact-save.sh` — 分支状态和脏文件保存
  - `scripts/session-end-precipitate.sh` — 关键决策沉淀

## 问题
开发工作经常被中断（切换任务、下班、紧急 bug），每次回来都要花时间恢复上下文：
- "我上次干到哪了？" — 需要翻 git log、翻终端历史
- 会话结束时的关键决策和发现没有记录，下次再来时一切归零
- 多 session 并行工作时，分支状态混在一起难以管理

## 方案
在 Claude Code 的 Hook 管线上挂载三个生命周期点，实现"进来有仪表盘，出去有沉淀"。

### SessionStart — 会话启动仪表盘
每次启动时自动运行约 10 个命令，呈现一个信息面板：
```
========================================
 铁律提醒：先设计再执行
 L2 记忆可用性：[OK]
 知识缓冲待学习：3 条
 夜间巡检状态：[PASS]
 Skill 飞轮待裁决：2 条
 错误模式走廊：5 条已知模式
 Git 多 session 提交统计
 协作提醒：[无待处理]
========================================
```
这个仪表盘解决了"我上次干到哪了"的问题，零认知成本。

### PreCompact — 会话压缩前保存
在上下文压缩之前保存当前分支状态和脏文件列表。
- 保存分支名、未提交文件清单、当前任务状态
- 压缩后恢复时，仪表盘可以直接展示这些信息

### SessionEnd — 会话结束沉淀
会话结束时自动执行：
- 沉淀关键决策到记录文件
- 更新状态文件（state.json + active.md）
- 如果做了重大架构变更，触发 ADR 记录

### 额外：CC Board 白板机制（ERPAI 补充）
多 Claude Code 实例通过 `.claude/board/{hostname}-{pid}.json` 签到文件互知状态，30 分钟无更新视作离线。无需外部依赖，纯文件系统实现。

## 在我们项目(yatushi)中的应用
YTS 项目可以从 SessionStart Dashboard 直接获得最大收益：

**立即落地 — SessionStart 仪表盘：**
在 `.claude/settings.json` 中添加：
```json
{
  "hooks": {
    "SessionStart": [
      "echo '=== YTS 项目状态 ==='",
      "cd yts_project && ./mvnw compile -q 2>&1 || echo '后端编译有错误'",
      "cd yts_project_vueai && npx vue-tsc --noEmit 2>&1 || echo '前端类型检查有错误'",
      "echo '待处理事项: ' && cat .claude/active.md 2>/dev/null || echo '(无)'",
      "echo 'Git 状态: ' && git status -s | head -20"
    ]
  }
}
```

**后续扩展：**
1. PreCompact hook 保存当前修改的文件列表到 `.claude/session-state.json`
2. SessionEnd hook 自动记录本次 session 的关键变更到 `docs/changelog/YYYY-MM-DD.md`
3. 多分支并行时，CC Board 机制可以让不同分支的工作互不干扰

这个模式成本极低（一个 hook 数组），但信息密度极高，适合 YTS 这样涉及后端+前端+数据库三端的项目。

---

