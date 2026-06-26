# The Hall 多 Agent 协作系统

## 来源
- 项目: OpenClaw Control Center
- 关键文件: `control-center/src/services/hall*`, `control-center/src/services/agent-collaboration*`

## 问题
单 Agent 有上下文窗口限制、单点知识盲区、缺乏质量制衡 — 一个问题只经过一个 Agent 处理，没有第二双眼睛审查。

## 方案
**The Hall** 是一个多 Agent 协作聊天室，工作流：

```
用户发布任务 → Agent们讨论 → 自愿/指派执行 → 其他Agent审查 → 交接/完成
```

核心机制：
- **共享时间线**：所有消息进入一个共同时间线，每个 Agent 都能看到上下文
- **自愿认领**：不强制指定执行者，Agent 根据自己的能力领域自愿认领
- **交叉审查**：执行结果必须经过至少一个其他 Agent 审查才能标记完成
- **SSE 实时流**：Agent 的思考过程和回复通过 Server-Sent Events 实时推送到前端

状态转换：
```
discussion → assign → execution → review → handoff → done
     ↑           ↑           ↑          ↑          ↑
  讨论中      已指派      执行中      审查中     已完成
```

## 关键实现
- `hall-session-service.ts` — 管理协作会话生命周期
- `hall-message-bus.ts` — Agent 间消息路由
- `agent-volunteer-service.ts` — 任务认领匹配
- `review-assignment-service.ts` — 审查者调度

## 在我们项目(yatushi)中的应用

**短期**：在 Copilot 对话中加一个"多角度分析"模式 — 用户提问后，后端同时调 DashScope 两次（不同 prompt 角度），合并结果返回，模拟"多 Agent 讨论"。

**中期**：对于兼容性匹配这种高价值决策，增加"执行 + 验证"两步：AI 先给匹配方案 → 另一个 Service 做交叉验证 → 不一致时标记人工审核。

**长期**：如果要构建完整的 Agent 系统（像 AgentChat/AgentProcess 页面那样），直接把 The Hall 的状态机搬过来。

---

