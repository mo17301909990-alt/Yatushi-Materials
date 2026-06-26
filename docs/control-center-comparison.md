# Control Center 对比报告

## 关系判断

两个项目确实来自同一个代码库，`package.json` 的 `name` 均为 `openclaw-control-center`，但已经分化成两个定位截然不同的进化分支。

**OpenClaw Control Center** 定位为 AI 驱动的**营销自动化运营平台** + 可观测性/任务编排控制面。核心资产是 Marketing OS（20 个子模块的完整营销自动化能力），依赖 NATS（事件驱动架构）、OpenTelemetry（链路追踪）、Prometheus（指标监控）、Playwright（浏览器自动化）等基础设施型库。

**ERPAI Control Center** 定位为 AI-native 的**完整 ERP 系统**。在 OpenClaw 基础上大幅叠加了 30+ 领域 Agent、590+ ERP 实体定义、6 条 ETL 管道、35 个业务服务、飞书集成等工业级 ERP 能力。依赖 PostgreSQL/pg、node-cron、nodemailer、pdfkit、exceljs 等企业级库。

架构层次关系：ERPAI 在上层通过 `src/adapters/`（三个适配器：openclaw-readonly、openclaw-runtime-plugin、openclaw-live-client）将 OpenClaw 作为底层基础设施消费，形成"ERPAI 在上、OpenClaw 在下"的分层结构。

---

## 结构差异

### OpenClaw 独有

| 模块 | 说明 |
|------|------|
| `src/marketing-os/` (20 子模块) | 完整营销自动化平台，含品牌自动驾驶、竞品情报、关键词研究、趋势分析、内容策略、用户模拟、自我对战评估、多语言地理评估 |
| NATS 消息总线 | 事件驱动架构核心基础设施 |
| Playwright | E2E 测试 + 路由扫描 |
| OpenTelemetry | 链路追踪 |
| prom-client | Prometheus 指标暴露 |
| @copilotkit/runtime | AI Copilot 集成 |
| react-c/ | React 前端 |
| `src/brand-positioning/` | 品牌定位模块 |
| `src/proactive/` | 主动分析引擎 |
| `src/skills/` | Skill 执行引擎 |
| `src/memory/` | 记忆子系统 |
| `src/agent-collaboration/` | Agent 协作层 |
| `loop-template/` | Loop 模板库 |
| `scripts/` (250+) | 运营/工具脚本（飞轮系统、黄金模式发现、技能推广等） |
| `src/marketing-os/self-play/` | 自我对战评估系统（46KB run + 39KB judge + 15 个数据适配器） |
| `src/marketing-os/geo-evaluation/` | 多语言地理评估（en/ja/ko/zh 四个语言包） |

### ERPAI 独有

| 模块 | 说明 |
|------|------|
| `src/erpai/` (30+ 子目录) | ERP AI 引擎核心，覆盖生产、销售、采购、财务、HR、库存、品质、研发、模具、设备、物流、售后、MRP、车间、OA、知识、记忆、演化、验证、数据采集等全部领域 |
| `src/erpai/metadata/` | 596 个实体 YAML 定义文件 |
| `src/erpai/models/generated/` | 592 个自动生成的 TypeScript 模型 |
| `src/erpai/api/routes/generated/` | 598 个自动生成的 CRUD 路由 |
| `src/etl/` | 6 条 ETL 管道（生产排程、物料、客户、供应商、仓库、库存） |
| PostgreSQL 双库架构 | SurrealDB + pg 双数据库 |
| `src/adapters/` | 将 OpenClaw 作为外部系统消费的三个适配器 |
| services/ (35 个) | 含 lead-scoring、cost-alert、canary-release、virtual-combat-engine、wiki、feishu-bot、reward-decay、policy-health/rollback 等 |
| 飞书集成三件套 | feishu-bot-service、feishu-command-service、feishu-daily-brief-service |
| 安全架构 | 默认只读模式、token 认证、审批 dry-run、导入 dry-run |
| 代码生成管线 | YAML 定义 → 模型 → 路由的全自动管线 |

### 共有

| 项目 | 说明 |
|------|------|
| 项目外壳 | 同一个 `openclaw-control-center` 项目骨架 |
| package.json 基础结构 | 共享项目名称和部分基础设施配置 |
| 核心路由/工具 | 可能共享部分通用工具函数 |

---

## 营销 OS 嵌入方式

Marketing OS 在 OpenClaw 中的位置是 `src/marketing-os/`，作为顶层源码目录下的**独立模块**嵌入，不是 `services/` 的一部分。它包含约 20 个子模块：

- **agents/**：采集编排器、Copilot 集成、模拟引擎、洞察编排器、分析器
- **brand-autopilot/**：品牌自动驾驶调度器、周报、地理预测、竞品跟踪、ROI 计算
- **brand-profile/**：品牌画像、上线、演化、向导
- **competitor-intel/**：竞品情报构建器、调度器、广告/产品/社交数据适配器
- **content/**：批量变体、分发编排、草稿工作流、平台适配器
- **content-strategy/**：主题推荐、规划、黄金推广、覆盖评分、数字人视频
- **geo-evaluation/**：地理评估器（29KB）+ 多语言支持（en/ja/ko/zh）
- **keyword-research/**：关键词聚合器（25KB）+ 百度/微博/小红书/抖音/DataForSEO 适配器
- **trending/**：事件回归、预测、情感分析、突发检测
- **self-play/**：自我对战（46KB）+ 裁判（39KB）+ 15 数据适配器
- **user-simulator/**：用户模拟运行器 + 角色 + 写作器

这个模块的体量（20 子模块、多语言、多平台适配、自我对战系统）说明它是一个完整的营销自动化平台，作为独立产品模块嵌入 OpenClaw Control Center，不是简单的功能插件。

---

## ERPAI 的扩展方式

ERPAI 在 OpenClaw 基础上的扩展方式是**以 `src/erpai/` 为核心的大规模叠加式扩展**，不是微调或配置修改：

1. **新增 `src/erpai/` ERP AI 引擎根目录**，内含 agents/、ai/、api/、domain/、evolution/、finance/、inventory/、production/、procurement/、quality/、sales/、metadata/、models/、repositories/、services/、rules/、seed-engine/、knowledge/、reconciliation/、loop-engine/、evolution-engine/ 等 30+ 子目录

2. **代码生成管线**：从 596 个 YAML 定义自动生成 592 个 TypeScript 模型 + 598 个 CRUD 路由，实现了实体定义→持久层→API 层的全自动生产

3. **新增 `src/etl/`**：6 条 ETL 管道覆盖生产排程、物料、客户、供应商、仓库、库存等核心业务数据流

4. **服务层大扩展**：services/ 从轻量级扩展为 35 个服务的完整服务层，含飞书集成、奖励系统、政策生命周期、金丝雀发布等工业级功能

5. **适配器层**：通过 `src/adapters/` 的三个适配器将 OpenClaw 作为底层基础设施消费（只读适配器、运行时插件适配器、实时客户端），形成"ERPAI 在上、OpenClaw 在下"的架构层次

---

## 对我们（YTS）的参考价值

### 架构启发

- **代码生成管线**：ERPAI 的 YAML 定义 → 模型 → 路由全自动管线，对 YTS 的物料/工序/兼容性等实体管理有直接参考价值。YTS 目前的手工 CRUD 可以用类似方式自动化，减少重复代码

- **叠加式扩展模式**：ERPAI 在 OpenClaw 基础上不做破坏性修改，而是通过 `src/erpai/` 和 `adapters/` 分层叠加。YTS 在做模块扩展（如从烫金扩展到 LED UV、丝印等）时也可以借鉴这种"不改原系统、迭加新模块"的策略

- **适配器模式**：ERPAI 通过适配器将 OpenClaw 的能力"消费"而非"修改"。YTS 的 AI Copilot 集成（`DashScopeChatService` + `AiController`）同样可以走适配器路线，将 AI 能力作为外部服务消费而非深度耦合

### 业务参考

- **营销 OS 的自我对战评估系统**（self-play）：YTS 的物料兼容性校验（`material_process_compatibility` 表 10071 行规则）可以引入类似的自动化验证评估机制，而非纯手工维护规则

- **ETL 管线**：YTS 目前没有显式 ETL 层，数据导入依赖手动 SQL 脚本。可以参考 ERPAI 的 6 条 ETL 管线建立结构化的数据导入/同步体系，特别是客户端数据导入场景

- **知识/记忆子系统**：ERPAI 有 knowledge/ + memory/ + evolution/ 三个持续学习模块，YTS 的 AI Copilot 目前是 stateless 问答，可以考虑引入上下文记忆来提升用户体验

### 直接可复用思路

- `database_scripts/generate_code_mapping.py` + `create_code_mapping.sql` 已经在走代码生成的路，ERPAI 的模式可以启发下一步的自动管线设计

- YTS 后端已有 30+ Controller，若后续继续增加实体，ERPAI 的 "metadata YAML → 代码生成" 模式是值得引入的自动化手段

### 差异化方向

- YTS 是印刷制造行业的垂直系统，ERPAI 是通用 ERP。YTS 的优势在于**工序级兼容性规则引擎**（热烫、裱纸、过胶、UV 等），这是通用 ERP 不具备的领域深度，应继续深耕

- 营销 OS 的自动化思路（竞品跟踪、趋势分析）对 YTS 的客户价值有限，但**质量追溯**和**工艺参数优化**方面的自我评估系统值得借鉴
