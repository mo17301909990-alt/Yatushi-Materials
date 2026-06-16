# YTS AI Copilot — 设计方案

## 背景

基于 ERPAI 的 AI Copilot 架构设计理念，重写印刷厂管理系统的 AI 能力。G 盘 `fix-paper-type-intent` 的 Agent 系统是过渡方案，这里有全套教训。

## 参考学习源

- G 盘 worktree：`G:\雅图仕\yst-saojinzhi\.claude\worktrees\fix-paper-type-intent\`（GoldFoilAgent / KeywordIntentDetector / Skill 路由）
- ERPAI 架构：`F:\ERPAI\control-center\src\erpai\ai\llm-intent-parser.ts`（LLM→关键词三级降级）
- ERPAI Copilot Sections：`F:\ERPAI\control-center\src\erpai\capabilities\copilot-sections\`（模块注册中心）

## 技术栈

- **后端** Java 17 + Spring Boot 3.3.10（现有），新增 `ai/` 包
- **前端** Vue 3 + TypeScript + Pinia（现有），新增 Copilot 组件化面板
- **LLM** 阿里通义千问 DashScope API（项目已有 DashScopeChatService）

## 架构

```
前端 (Vue 3)
  CopilotSidebar.vue           ← 右侧面板容器（感知当前模块）
    ├── CopilotChat.vue         ← 对话区域（流式 SSE 渲染）
    ├── CopilotActions.vue      ← 模块感知的快捷操作按钮
    └── CopilotSuggestions.vue  ← 智能建议卡片
  stores/copilot.ts             ← 单一 store（无双向同步）
  types/copilot.ts              ← 共享类型（前后端一致）

后端 (Spring Boot)
  ai/
    ├── IntentParser.java               ← 接口
    ├── LlmIntentParser.java            ← LLM 实现
    ├── KeywordIntentParser.java        ← 关键词降级
    ├── DelegatingIntentParser.java     ← LLM→关键词→null 三级降级编排
    ├── skill/
    │   ├── Skill.java                  ← 接口
    │   ├── MatchSkill.java             ← 产品匹配
    │   ├── CompatibilitySkill.java     ← 工艺兼容性
    │   └── SearchSkill.java            ← 通用搜索
    ├── SkillRegistry.java              ← 技能注册中心
    ├── CopilotExecutor.java            ← 编排：parse→route→execute→reply
    └── context/
        ├── SessionContext.java         ← 当前模块/用户/历史
        └── MaterialContext.java        ← 物料上下文数据
  controller/CopilotController.java
    POST /api/copilot/chat              ← 对话（非流式）
    POST /api/copilot/chat/stream       ← SSE 流式
    GET  /api/copilot/sections/{module} ← 模块建议
  dto/
    CopilotRequest.java / CopilotResponse.java
```

## 核心流程

```
用户输入 → DelegatingIntentParser.parse()
  ├→ LlmIntentParser (DashScope, 10s timeout)
  │   confidence≥0.5 → 用 LLM 结果
  │   confidence<0.5 → 与关键词交叉验证
  │   fail/timeout → 降级
  └→ KeywordIntentParser (零延迟)
      成功 → action + params
      失败 → return null → 前端显示"没理解"
        ↓
CopilotExecutor
  ├→ SkillRegistry.route(action) → MatchSkill
  ├→ MatchSkill.execute(params) → DB 查询
  └→ 构建回复 (含数据 + 建议按钮)
        ↓
SSE stream / 一次性响应 → 前端渲染
```

## 模块感知 Copilot (Section Registry)

每个业务域注册自己的快捷操作和建议卡片，Copilot 根据当前页面自动切换：

```
registries/product.ts:   产品匹配 → "推荐热门" / "检查兼容性"
registries/material.ts:  物料管理 → "找同类" / "批量更新"
registries/process.ts:   工艺配置 → "检查工艺" / "对比方案"
```

## 关键设计决策

| G 盘的坑 | 本方案 |
|----------|--------|
| 双向 watch 循环（22 个 watch） | 单一 store + 用户操作主动同步，不加 watch 双向绑定 |
| Agent results 丢弃（两次 API 调用） | 单数据流：Copilot 查的结果直接渲染 |
| 前后端字段不一致（7 处冲突） | 共享类型文件 + Java Record 校验 |
| 字段注入 @Autowired | 构造器注入 |
| intents 无映射 | 明确的 Skill → action 枚举映射，缺失编译即报错 |
| setTimeout 竞态（1500ms 窗口） | SSE 流式 + AbortController，组件卸载自动 cancel |
| 反馈用 ArrayList 内存存储 | 持久化到数据库（已有 announcement 表） |
| only_filter hotStampingPaperType | Skill 确保参数真实传到 DB 查询 |

## 实施路径

### Phase 1：MVP 可演示
- 后端：KeywordIntentParser + MatchSkill + CopilotExecutor + CopilotController
- 前端：CopilotSidebar + CopilotChat（一次性响应）
- 单一数据源 store（无 watch 循环）

### Phase 2：LLM 加持
- LlmIntentParser（集成 DashScope）
- DelegatingIntentParser 三级降级
- SSE 流式响应

### Phase 3：模块感知
- Section Registry 注册各业务域建议
- CopilotActions + CopilotSuggestions 动态渲染
- 上下文感知（当前页面自动关联对应模块建议）
