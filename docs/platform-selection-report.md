# 企业级底座平台选型报告

## 背景 — 为什么要替换 OpenClaw 底座

YTS 印刷厂管理系统当前已集成 DashScope（通义千问）作为 AI 后端，通过 `AiController` -> `DashScopeChatService` 提供 AI 对话能力。但现行方案存在三个结构性问题：

1. **供应商锁定风险**：当前代码与 DashScope SDK 直接耦合，切换到 OpenAI、Claude 或本地模型需要重写调用层，无法在模型故障/政策变动时快速逃生。
2. **框架能力缺失**：DashScope 仅提供基础 Chat API，RAG（检索增强生成）需自行实现 VectorStore，Agent 编排需从零搭建，结构化输出不稳定。随着 YTS 的物料兼容性查询、多工序推理等场景增多，这些缺失将变成瓶颈。
3. **扩展性天花板**：没有模型无关抽象层，每次新增模型供应商都需要在业务代码中加分支判断，长期维护成本高。

因此需要一个企业级 AI 底座平台来统一管理模型接入、RAG、Agent 编排和结构化输出。

## 候选平台一览 — 所有调研平台一句话总结

| 平台 | 一句话总结 |
|------|-----------|
| **Spring AI** | Spring 生态原生的 AI 框架，与 YTS 现有 Spring Boot 3.3 零摩擦集成，模型无关抽象层覆盖 10+ Provider。 |
| **LangChain4j** | Java 生态最成熟的 LLM 框架，Agent 能力（AiService + Tool 注解）领先，但非 Spring 原生需额外适配。 |
| **DashScope（通义千问）** | YTS 已集成的 AI 后端，国内合规好、延迟低，但框架层面仅提供基础 Chat API，缺乏 RAG/Agent 内置支持。 |
| **OpenAI Direct API** | API 成熟度最高，Structured Outputs 和 Function Calling 体验最佳，但国内访问不稳定、单一供应商锁定。 |
| **Claude API（Anthropic）** | 长上下文（200K）和 Tool Use 表现优异，适合复杂文档分析，但同样单一供应商且国内访问受限。 |
| **Ollama（本地部署）** | 数据不出厂、隐私合规最佳，多模型本地切换，但对 GPU 资源要求高，框架层面能力薄弱。 |
| **Semantic Kernel (Java)** | 微软的 AI 编排框架，架构设计优秀，但 Java 版 SDK 严重落后 C#，社区活跃度低，功能不全。 |

## 能力矩阵对比（8 个维度 x 7 个平台）

| 能力维度 | Spring AI | LangChain4j | DashScope | OpenAI Direct | Claude API | Ollama | Semantic Kernel (Java) |
|---------|-----------|-------------|-----------|---------------|------------|--------|------------------------|
| **Chat/Q&A** | 原生 | 原生 | 原生 | 原生 | 原生 | 原生 | 原生 |
| **Function Calling** | 原生 | 原生 | 原生 | 原生 | 原生 | 需扩展（模型依赖） | 原生 |
| **结构化输出** | 原生（BeanOutputConverter） | 原生（JsonSchemaGenerator） | 需扩展（不稳定） | 原生（Structured Outputs） | 原生（JSON Mode） | 需扩展 | 原生 |
| **RAG** | 原生（VectorStore + DocumentReader） | 原生（ContentRetriever + EmbeddingStore） | 需扩展（无内置 VectorStore） | 需扩展（Assistants 简陋 RAG） | 需扩展 | 需扩展 | 原生（Memory/VectorStore） |
| **Agent** | 需扩展（无内置 Agent 框架） | 原生（AiService + Tool 注解） | 需扩展（需自行编排） | 需扩展（Assistants 实验性） | 需扩展（尚无 Agent API） | 需扩展 | 原生（OpenAI Assistant Agent） |
| **多模态** | 需扩展（仅文本） | 需扩展（仅文本） | 原生（千问 VL） | 原生（GPT-4o Vision） | 原生（Claude 3 Vision） | 需扩展（llava 等） | 需扩展 |
| **流式输出** | 原生（SSE WebFlux） | 原生（TokenStream） | 原生（SSE） | 原生 | 原生 | 原生 | 原生 |
| **模型无关性** | 原生（10+ Provider 适配器） | 原生（15+ 模型 Provider） | 单一供应商 | 单一供应商 | 单一供应商 | 原生（多模型本地切换） | 需扩展（C# 版更好） |

### 关键发现

- **流式输出**是所有平台都原生支持的，不是决策分歧点。
- **模型无关性**是区分"框架"和"SDK 封装"的核心指标。Spring AI 和 LangChain4j 是第一梯队，其余都是单一供应商或生态不完整。
- **Agent 能力**是 LangChain4j 的最强差异点——只有它提供了开箱即用的声明式 Agent 编排。Spring AI 的 Agent 支持尚在早期，需要自行组装 LLM + Tool + Memory。
- **RAG** 方面，Spring AI 和 LangChain4j 都内置了完整的 RAG pipeline（文档读取 -> 切片 -> Embedding -> 向量检索），其他平台要么没有（DashScope），要么很简陋（OpenAI Assistants）。

## Top 3 推荐方案 — 针对 YTS 的详细建议

### 1. Spring AI — 长期演进的最佳基座

**推荐理由**：YTS 已经是 Spring Boot 3.3 + Java 17 架构，引入同一生态的 AI 框架代价最低：
- Spring AI 的 `AiClient` 抽象层可以直接对接 DashScope（当前在用）、OpenAI、Claude 甚至 Ollama，切换模型只需改配置，不动业务代码。
- 原生支持 WebFlux SSE 流式输出，与 YTS 现有响应式能力无缝衔接。
- VectorStore 抽象层支持 PGVector，可以部署在现有的 PostgreSQL 集群上，不需额外引入 Elasticsearch 或 Milvus，降低运维复杂度。
- Spring 社区的迭代节奏、安全响应、文档质量在 Java 生态无可替代。

**风险与缓解**：
- 缺乏内置 Agent 框架 -> 通过 LangChain4j 战术补充（见方案组合）
- 相对较新（2023 年底才 GA）-> Spring 官方项目，迭代快、社区活跃，风险可控

### 2. LangChain4j — Agent 场景的战术补充

**推荐理由**：Java 生态中唯一提供开箱即用 Agent 编排能力的框架：
- `@AiService` 注解 + `@Tool` 注解的声明式 Agent 模式，适合 YTS 中物料兼容性查询、多工序推理等需要"规划 -> 调用 -> 观察"的多步推理场景。
- 15+ 模型 Provider 适配器覆盖主流模型，和 Spring AI 一样不锁定供应商。
- RAG 能力完善：ContentRetriever + EmbeddingStore 抽象层成熟。

**风险与缓解**：
- 非 Spring 原生，需额外做 Spring Boot 集成 -> 可以封装为独立的 Service 模块，通过 Spring AI 的 Bridge 或内部 RPC 调用
- 社区规模小于 Spring AI -> 核心功能稳定，作为战术补充层风险可控

### 3. DashScope（通义千问）— 保留为国内主力模型 Provider

**推荐理由**：YTS 当前已深度集成，有现成的业务对接代码：
- 国内合规和数据不出境要求对印刷企业管理场景（含客户工艺数据、成本数据）尤为重要。
- 国内网络延迟最低，无需翻墙。
- 千问 VL 多模态能力在候选平台中最成熟，适合未来印刷样品图片识别场景。

**风险与缓解**：
- 框架能力弱，不适合作为底座平台 -> 降级为"模型供应商"而非"框架"，通过 Spring AI 的模型抽象层对接
- 结构化输出不稳定 -> 改用 Spring AI 的 BeanOutputConverter 在应用层做结构化约束

## 最终推荐 — 我选哪个，为什么

### 结论：Spring AI 为首选基座 + LangChain4j Agent 战术补充 + DashScope 国内模型主力

### 架构示意

```
┌──────────────────────────────────────────────────────────────┐
│                    YTS Application Layer                       │
├──────────────────────────────────────────────────────────────┤
│              Spring AI (Orchestration Layer)                   │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────────┐ │
│  │ChatClient │  │ Model    │  │ Vector   │  │ Structured   │ │
│  │(Streaming)│  │Adapter   │  │Store(RAG)│  │ Output       │ │
│  └──────────┘  └────┬─────┘  └────┬─────┘  └──────────────┘ │
├──────────────────────┼─────────────┼─────────────────────────┤
│              ┌───────┴───────┐     │  LangChain4j (Agent)    │
│              │   Model       │     │  ┌────────────────────┐ │
│              │   Adapters    │     │  │ AiService + @Tool  │ │
│              └───┬───┬───┬──┘     │  │ Agent Orchestration│ │
│         ┌────────┤   │   ├────┐   │  └────────────────────┘ │
│    ┌────┴────┐┌──┴──┐│┌──┴──┐│   └──────────┬───────────────┤
│    │DashScope││OpenAI││Claude││Ollama         PGVector       │
│    │(主力)   ││(补充)││(补充)││(隐私)    (PostgreSQL RAG)   │
│    └─────────┘└─────┘│└─────┘│                              │
└──────────────────────┴───────┴──────────────────────────────┘
```

### 核心理由

1. **生态匹配成本最低**：YTS 项目已经是 Spring Boot 3.3 + Java 17，引入 Spring AI 不需要改架构、不需要学新框架规范、不需要新增基础设施。迁移路径是：`DashScopeChatService` -> `Spring AI ChatClient`，改一行配置即可切换模型。

2. **模型无关性保留战略灵活性**：印刷行业的 AI 需求还在快速演进。今天用 DashScope 做文本对话，明天可能需要 OpenAI 做多模态识别，后天可能需要本地 Ollama 处理敏感工艺数据。Spring AI 的模型抽象层让这一切变成配置变更而非代码重写。

3. **企业就绪度最高**：Spring 社区的迭代节奏（月度小版本、季度大版本）、安全响应（CVE 通常在 24 小时内有修复方案）、文档质量（官方参考文档 + 全套 Getting Started 指南）在 Java 生态中无可替代。这对生产系统至关重要。

4. **RAG 架构简洁**：Spring AI 的 VectorStore 抽象 + PGVector 实现 = 不需要引入 Elasticsearch、Milvus、Pinecone 等额外中间件。PostgreSQL 已经有了，加一个向量扩展就够了，运维团队不需要学新技能。

5. **Agent 缺口有战术补充**：Spring AI 目前没有内置 Agent 编排框架，但这不是否决理由。对需要复杂 Agent 推理的场景（如多工序兼容性自动推导），可以封装 LangChain4j 的 AiService + Tool 为独立的 Agent 服务模块，通过 Spring AI 的 Bridge 调用。大多数场景（简单问答、RAG 查询、结构化输出）直接用 Spring AI 就够了。

### 淘汰理由

- **LangChain4j 为什么不首选？**—— 非 Spring 原生，作为主框架需要处理 Spring Boot 集成、事务管理、配置体系的对齐，引入额外适配成本。更适合作为 Agent 战术补充而非主框架。
- **DashScope 为什么降级？**—— 框架能力不足，结构化输出不稳定、无内置 RAG 和 Agent，作为底座平台扩展性受限。但其模型能力（特别是国内合规和低延迟）优秀，作为主力模型供应商是正确位置。
- **Semantic Kernel 为什么不选？**—— Java 版 SDK 严重落后 C#，核心功能（Memory、Plugin 系统）不完整，社区活跃度低，不适合生产系统。
- **OpenAI/Claude Direct API 为什么不选？**—— 国内访问不稳定、单一供应商锁定、缺乏框架层能力（RAG/结构化输出需自行实现）。保留为"可通过 Spring AI 切换的模型供应商"即可。
- **Ollama 为什么不选？**—— 框架能力薄弱，GPU 资源要求高。本地部署方案建议在 Spring AI 的模型适配层支持，仅用于数据敏感场景，不作为主框架。
