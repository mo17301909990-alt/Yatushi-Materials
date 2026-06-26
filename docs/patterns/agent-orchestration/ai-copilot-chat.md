# AI Copilot 多层级对话架构

## 来源
- 项目: YTS Materials（印刷厂物料匹配系统）
- 关键文件:
  - `service/ai/AiChatOrchestratorService.java` — 3 层渐进上下文编排器
  - `controller/AiController.java` — 对话 / Copilot / parse-and-match 三个入口
  - `service/DashScopeChatService.java` — OpenAI 兼容 HTTP 调用
  - `agent/IntentDetector.java` — 意图检测接口
  - `agent/KeywordIntentDetector.java` — 关键词意图实现
  - `agent/SimpleSkillRouter.java` — 技能路由分发
  - `service/ai/AiKnowledgeRetriever.java` — 知识库 RAG 检索
  - `service/ai/EmbeddingService.java` — 向量嵌入（可选依赖）
  - `service/CompatibilityQueryService.java` — AI-less 兼容性查询降级

## 问题
印刷厂 AI 助手需要处理多种场景：简单的产品咨询、自然语言匹配查询、工艺建议、生产数据统计。单层 prompt 在以下场景失效：
- 问候语也加载全套数据库 → 浪费 token（10k+ token/次）
- 复杂查询没有足够上下文 → 模型编造数据
- 无法区分"用户闲聊"vs"正经查物料"→ 答非所问

## 方案

### 三层渐进上下文注入

```
Layer 1 ─── Layer 2 ─── Layer 3 ─── Extra
基础规则    实时摘要    完整数据    角色指令
（始终）    （始终）    （按需）    （按需）
```

**Layer 1 — 基础规则**（~2.5k tokens）
- 系统身份定义 + 回答行为约束
- 工厂指引书核心知识（金纸分类/价格/品质标准/过胶规则）
- 卡片输出格式模板（product_card/compatibility_table）

**Layer 2 — 系统数据摘要**（~2k tokens）
- `AiToolDataService.buildSummaryContext()` — 实时聚合关键指标
- 产品总数、类型数、工序选项数量等轻量统计

**Layer 3 — 完整上下文**（~15k tokens，仅需要时注入）
- 全量工具数据（`buildToolContext()`）
- 数据库表结构（`DatabaseSchemaCatalogService` 读 information_schema）
- 知识库 RAG 片段（`AiKnowledgeRetriever.retrieveTop()` — 语义检索 top-4）

**Extra — 角色指令**（按需）
- 由前端传入 `systemPrompt`，覆盖身份定义
- 例如"你是生产助手"→ 额外注入资源组/产能数据

### 三层入口策略

```
POST /api/ai/chat ─── 简单对话——基础问答
POST /api/ai/copilot/chat ─── 智能 Copilot——带建议+卡片+参数
POST /api/ai/parse-and-match ─── 自然语言→结构化查询
POST /api/ai/compatibility/query ─── 关键词兼容性搜索（AI-less 兜底）
```

### 意图检测 + 降级路由

```
用户输入
  ↓
IntentDetector (关键词匹配)
  ├─ COMPATIBILITY/MATCH → CompatibilityQueryService（AI-less 直查数据库）
  └─ 其他 → AiChatOrchestrator（走 LLM）
```

关键设计：兼容性查询仅在意图明确时走 AI-less 路径，既省 token 又精准。

### 结构化参数提取（parse-and-match）

大模型将自然语言转为 JSON → 解析为 `GoldFoilQueryParams` → 走标准查询 → 返回标准分页结果。模型不参与匹配逻辑，只做翻译。

```
用户："我要找适合精平装的金色烫金纸"
  → LLM 返回 {"productTypeId": 1, "paizi": "金色"}
  → Java 代码解析 → GoldFoilProductService 查询
  → 返回 PagedResult<GoldFoilProductDTO>
```

### 卡片化回复

模型在回复中附带结构化数据块，前端渲染为卡片：
```
===CARD_START===
{"type":"product_card","data":{...}}
===CARD_END===
```
支持三种卡片类型：product_card / compatibility_table / action_buttons。

### 异常安全

每层都有 fallback：
- Embedding 不可用 → 降为纯关键词检索
- LLM 调用失败 → 返回友好错误 + 建议操作
- 兼容性查询无结果 → 提示换个关键词搜索

## 关键设计决策

| 选项 | 选择 | 理由 |
|------|------|------|
| LLM 调用方式 | OpenAI 兼容 HTTP（无 SDK） | 可切换任意 OpenAI 兼容服务（DashScope/DeepSeek/Ollama） |
| 上下文裁剪策略 | 渐进注入 | 简单对话省 80% token，复杂查询不缺数据 |
| 匹配逻辑归属 | Java 代码（不靠 LLM） | 匹配是精确业务逻辑，不是生成任务 |
| 卡片格式 | Markdown 块 + 前端解析 | 模型原生支持，无需 function calling |
| 降级策略 | 关键词意图检测 | 不计费、低延迟、精确可控 |

## 潜在演进

- **多轮对话上下文管理** — 当前是无状态设计，每次独立注入
- **Function Calling** — 替代人工 JSON 解析（目前用 simple KeywordIntentDetector）
- **Agent 自修正** — LLM 确认查询结果后决定"再加个条件"还是"换个品类"
- **Token 用量监控** — 按 Layer 级别统计，调优裁剪策略
