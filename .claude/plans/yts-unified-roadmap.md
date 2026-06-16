# 统一路线图：印刷物料工艺兼容性智能系统

> 由审计报告（G:\雅图仕\雅图仕数据审计报告.md）和 YTS AI Copilot 设计方案（.claude/plans/yts-copilot-design.md）交叉对比生成

---

## ✅ 已确认的决策（2026-06-07）

### 1. 技术底座：PostgreSQL 路线，不动 SurrealDB

- 当前 PostgreSQL 不挡路，pgvector + AGE 扩展覆盖 80% 的知识图谱/向量需求
- SurrealDB 和多模态没有强绑定关系。多模态的核心是视觉模型（Qwen-VL）+ 向量搜索（pgvector），PostgreSQL 完全能承载
- 未来如果真的遇到 JOIN 瓶颈或图遍历性能问题，再考虑迁移或旁挂 SurrealDB 做知识图谱专用

### 2. 不寄生 ERPAI

- 独立 Spring Boot 应用，不引入 ERPAI 框架
- 借鉴理念（Event 驱动/策略模式/AOP）但不抄代码
- 未来扩展到其他部门的需求，用 Spring 原生能力满足

### 4. 不改原有系统，AI 版本平行存在

- 现有烫金系统（所有页面、功能、路由）**保持原样不动**
- AI Copilot 作为一个**新版本/新入口**存在（已有的 `/smart` 路由 + `SmartVersion.vue` 占位页）
- 两个版本共享同一个后端和数据库，前端通过路由隔离
- 用户可以选择用旧版流程操作，也可以切换到 AI 版对话操作
- 等 AI 版成熟稳定后，再考虑是否合并

### 3. 核心知识源：统一纳入 PostgreSQL

- 权威数据源：`G:\雅图仕\download`（4.5 年的 21 个 Excel 文件）
- 入库到 PostgreSQL 形成标准表单数据库
- 现有系统（89 表/31,380 行）的生产记录作为辅助参考（只读）
- 两者通过物料编码/工艺编码关联，不双写不冲突

---

## 路线图总览

| Phase | 主题 | 时长 | 核心交付 | 技术栈 |
|-------|------|------|---------|--------|
| **0** | 技术债清理 + Excel 知识库入库 | 4-6 周 | G:\雅图仕\download 数据 → PostgreSQL 表 | Spring Boot + EasyExcel + PostgreSQL |
| **1** | 关键词兼容性查询 MVP | 3-4 周 | Copilot 侧边栏 + 知识库关键词查询 | Vue 3 + Spring Boot + PostgreSQL |
| **2** | LLM + 流式 + RAG | 4-5 周 | 自然语言问答 + 流式渲染 + 多轮对话 | 同上 + DashScope + pgvector |
| **3** | 流程引擎 + 模块感知 | 5-7 周 | 新物料评估线上流程 + 页面上下文感知 | Spring Event/状态模式 + Vue 3 |
| **4** | 数据飞轮 + 质量保障 | 5-6 周 | 一致性校验 + 使用分析 + KPI + 测试体系 | Spring AOP + JUnit + Playwright |

**总预估：21-28 周（约 5-7 个月），含验证缓冲。**

---

## Phase 0 详细设计：基础设施清理 + Excel 知识库入库

### 0.1 技术债清理（1 周）

- 清理根目录 `hs_err_pid*.log`、`replay_pid*.log`、重复的 `.sql` 和 `.gz` 备份文件
- 确认本地 PostgreSQL 中 89 张表的索引覆盖情况，缺索引的补上
- 确认 `application.properties` 中的生产数据库配置已不再硬编码可达地址
- 建立 `dev.properties` 和 `prod.properties` 分离策略，不把敏感地址提交到 git

### 0.2 G:\雅图仕\download 数据摸底 + 入库（2-3 周）

**覆盖的文件（21个）：**

| 目录 | 文件 | 说明 |
|------|------|------|
| 印刷工艺数据 | 丝印扫金粉/UV洒闪粉/云母珠光粉/沟闪粉/局部UV/橘皮UV/岩石UV/磨砂UV/遇水变透明/皱纹UV/水彩墨/夜光油墨 | 13 个 UV/特殊油墨工艺指引书 |
| 烫金工艺数据 | LEO烫金工艺.docx、纸张类-平面/非平面、书版 | 4 个 |
| 成本数据 | JP 750S成本分析.docx、成本核算(34个版本).xlsx | 2 个 |
| UV油墨类数据 | 印刷UV油类、印刷过水油类 | 2 个 |

**核心表结构设计（最小模型）：**

```sql
-- 工艺主数据
processes (id, name, category, description, created_at, updated_at)

-- 基材分类（Sheet 2 参考表）
base_materials (id, name, category, parent_id)

-- 工艺参数（Sheet 1 的参数规格）
process_parameters (id, process_id, param_name, param_value, unit)

-- 兼容性矩阵（物料类型 × 基材 × 工艺 → 兼容性评级）
compatibility_matrix (
  id, process_id, base_material_id, 
  compatibility_rating,  -- 优/良/可/否
  notes, source_file, version, created_at
)

-- 后加工适配性
post_process_compatibility (
  id, process_id, post_process_id,
  compatible, notes
)

-- 编码映射表（连接 Excel 编码 ↔ 现有系统编码）
code_mapping (id, excel_code, system_code, mapping_type, notes)
```

**实现方案：**
- Spring Boot + EasyExcel（POI 5.2.3 已验证）解析 Excel
- 数据导入 Service（`KnowledgeImportService`），支持增量更新和冲突检测
- 不做 YAML Schema 抽象层，直接 Java Entity + MyBatis

### 0.3 物料编码对齐（1 周）

- Excel 中的物料编码 / 工艺编码 与 现有 PostgreSQL 产品表（1,147 行）做交叉映射
- 建立 `code_mapping` 关联表，连接新旧系统
- 产出：完整的"Excel 编码 ↔ 现有系统编码"映射表

### 前置条件

- 拿到实际 Excel 文件（确认 G:\雅图仕\download 可读，权限正常）
- 确认 pgvector 扩展在本地 PostgreSQL 可用（`CREATE EXTENSION vector;` 测试）

---

## Phase 1 详细设计：关键词匹配兼容性查询 MVP（3-4 周）

### 1.1 后端：关键词意图解析 + 兼容性查询 API（1.5 周）

- `KeywordIntentParser`：从用户输入中提取物料、工艺关键词
- `CompatibilitySkill`：查询 Phase 0 的 `compatibility_matrix` 表
- `DelegatingIntentParser`：先关键词，LLM 不可用时不降级（Phase 2 才接 LLM）
- API 统一 `R<CopilotResponse>` 格式

### 1.2 前端：Copilot Sidebar 基础框架（1.5 周）

- 侧边栏组件：CopilotSidebar + CopilotChat + 建议卡片
- 交互：输入关键词 → 发送请求 → 显示结果卡片
- Pinia store 单一数据源，不做双向 watch 同步（吸取 G 盘教训）
- 不需要 SessionContext（Phase 2 加），不需要 Section Registry（Phase 3 加）

### 1.3 连接 Phase 0 知识库 + 生产辅助数据（0.5 周）

- 兼容性判断以 Phase 0 知识库为主
- 显示结果时标注"生产验证 N 次，成功率 M%"，数据从现有 `material_process_compatibility` 表捞

---

## Phase 2 详细设计：LLM 接入 + 智能意图解析 + 流式回复（4-5 周）

### 2.1 DashScope LLM 集成

- 复用现有 `DashScopeChatService`（已对接阿里通义千问）
- `LlmIntentParser`：从自然语言解析 {物料,工艺,意图类型,约束条件}
- 三级降级：LLM → 关键词 → null（10s 超时）
- **多模态预留**：通义千问 Qwen-VL 支持图片理解，Phase 2 不做但留接口

### 2.2 SSE 流式渲染

- 后端：SseEmitter 实现流式返回
- 前端：EventSource 接收 + 逐字渲染
- 关键词模式也走流式（一次性推送，不分片）

### 2.3 SessionContext 会话管理

- 后端：Redis 或内存 Map 存储会话上下文
- 前端：Pinia store 管理会话 ID + 历史消息
- 支持追问自动带上文

### 2.4 RAG 基础

- 向量化：操作手册（根目录的 .md）分块 → Embedding → pgvector
- 查询：用户问题向量化 → 相似度搜索 → 拼入 LLM Prompt
- 先不做全量知识图谱 RAG，验证链路后再扩展

---

## Phase 3 详细设计：流程引擎 + 模块感知（5-7 周）

### 3.1 新物料评估线上流程

- "申请→分派→测试→审核→发布"全流程替代 Excel + 邮件
- 推荐实现：`@EventListener` + 状态模式（`current_status` 字段追踪）
- 不引入 Camunda/Activiti（太重），不引入 Spring Statemachine（备选）

### 3.2 Section Registry 模块感知

- 前端：Pinia 管理模块注册表，页面导航时通知 Copilot 当前模块
- Copilot 根据模块自动切换快捷操作和建议

### 3.3 后台管理

- 流程实例列表、审批历史、改派
- 兼容性矩阵运营编辑入口

---

## Phase 4 详细设计：数据飞轮 + 质量保障（5-6 周）

### 4.1 一致性校验（类 Commissar）

- `@ConsistencyCheck` 注解（Spring AOP），在写入知识库时自动校验
- 规则：同一物料-工艺组合不能有两个不同评级、例外必须附带审批人

### 4.2 使用分析 + 免疫回路

- 埋点：每次 Copilot 查询（物料、意图、是否命中、是否点击）
- 知识盲区自动生成"补全"工单
- 高频组合标记为"热门"
- 连续未命中自动通知管理员补充

### 4.3 测试体系

- LLM 输出测试：20-30 条预设用例 + LLM-as-Judge
- 关键词匹配单元测试
- 降级链路测试
- E2E 测试（Playwright）

### 4.4 多工厂/多客户预留

- `compatibility_matrix` 增加 `tenant_id`
- 编码映射支持多工厂前缀
- API 增加 `X-Tenant-Id` 头部

---

## 与原始方案的关系

| 原始方案主张 | 路线图处理 | 理由 |
|------------|-----------|------|
| 审计报告：先 YAML Schema 再入库 | 不采纳，直接 Java Entity | 先跑通数据比跑通抽象重要 |
| 审计报告：用 SurrealDB | 不采纳，PostgreSQL + 扩展 | 当前不需要，未来旁挂评估 |
| 审计报告：寄生 ERPAI | 不采纳，独立 Spring Boot | 当前是独立应用 |
| 审计报告：RAG 全量知识图谱 | 部分采纳，Phase 2 从小开始 | 先验证链路可信度和成本 |
| 审计报告：L4 自主进化飞轮 | 拆入 Phase 4，Spring Event 实现 | 理念保留，技术栈替换 |
| Copilot 设计：查现有生产表 | 改为查 Phase 0 知识库 | 现有表是记录不是权威 |
| Copilot 设计：所有前端设计 | 全部保留，逐步实现 | 交互层是亮点 |
| 两端都没覆盖的盲区 | 逐个安排进各 Phase | 不做就是空中楼阁 |

---

## 建议开工顺序

1. **立刻启动 Phase 0.2 数据摸底** — 打开 G:\雅图仕\download 的 Excel，逐列标注字段含义（1 周）
2. **并行 Phase 0.1 技术债清理**（1 周）
3. **Phase 0.3 编码对齐**（完成摸底后 1 周）
4. **Phase 1 开工** — 数据入库后立即建 API 和侧边栏
5. **Phase 2 → Phase 3/4**（按资源情况决定并行或串行）

**一句话总结：让 4.5 年的 Excel 知识库在 PostgreSQL 安家 → 配对话界面 → 接流程和组织能力 → 上飞行记录仪和自动驾驶。**
