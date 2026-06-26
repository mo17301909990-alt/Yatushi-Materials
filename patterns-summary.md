# 模式匹配总结 + 落地路线图

> 基于项目基因（DNA）扫描与全局模式库匹配，为 YTS 定制的演进路线。

---

## 匹配方法论

从全局模式库中筛选出 12 个候选卡片，经过以下评审维度过滤：
- **领域命中**：模式是否直击 YTS 的核心业务（兼容性匹配、BOM 构建、ETL）
- **架构债务**：模式是否解决当前代码库的已知痛点（测试缺失、Service 臃肿、hook 路径错误）
- **ROI 评估**：实施成本 vs 长期收益
- **成熟度适配**：模式是否匹配 YTS 当前 L1.5 成熟度

结果：P0 采纳 4 个，P1 采纳 2 个，P2 采纳 1 个，跳过 11 个。

---

## P0 — 立即启动（当前 Sprint）

### 1. configurable-rule-engine（YAML 声明式规则引擎）

| 维度 | 内容 |
|------|------|
| 问题 | `material_process_compatibility`（10071行）和 `hot_stamping_compatibility`（1421行）的匹配逻辑埋在 SQL JOIN 和 Java 条件分支中，改规则需要改代码重新部署 |
| 方案 | 用 YAML 文件定义工序间兼容性规则（如"烫金+过胶=不兼容"、"UV油+丝印=需要预处理"），Java 服务加载到 Guava Cache，规则变更走数据库 versioning，不重启服务 |
| 文件落地 | `yts_project/src/main/resources/rules/` + 新 Service `CompatibilityRuleService` |
| 验证 | YAML lint + 规则版本递增 + 兼容性测试套 |

### 2. service-decomposition（Service 拆分）

| 维度 | 内容 |
|------|------|
| 问题 | Large Service Impl 混入 Excel 处理逻辑（导入导出、格式转换），违反 SRP，严重影响可测试性和可维护性 |
| 方案 | 两步：1) Excel 逻辑统一归入 `util/excel/` 包或独立 `excel/` 模块；2) 复杂业务匹配逻辑拆到独立 Dedicated Service 或 Strategy 类。目标：每个 Service ≤ 300 行 |
| 文件落地 | `yts_project/src/main/java/com/it/yts_project/util/excel/` |
| 验证 | grep Excel 依赖不在 service 包 + 行数检查 |

### 3. api-conventions-testing（测试覆盖）

| 维度 | 内容 |
|------|------|
| 问题 | 仅 1 个 mapper 测试 + 1 个 Application 测试，pom.xml 设了 `skipTests=true`。31K 行数据、89 张表、53 个 Controller 的生产系统零测试覆盖 |
| 方案 | 三步：1) 删除 `skipTests=true`；2) 给核心领域层写 Service 单元测试（@JdbcTest/H2）；3) 给主要 Controller 写 @WebMvcTest |
| 优先级 | 兼容性匹配、价格计算、规则引擎相关 Service 优先 |
| 验证 | `mvn test` 通过 |

### 4. quality-gate-hooks（质量门禁修复）

| 维度 | 内容 |
|------|------|
| 问题 | Stop Hook 路径指向已废弃的 `/f/YTS+JYY`，pom.xml `skipTests=true` 导致构建完全不跑测试 |
| 方案 | 1) 修复 settings.json Stop Hook 路径到 `F:/yatushi-6-6`；2) Pre-commit 自动跑 `mvn compile` + `npm run build`；3) Done Criteria 加入强制检查项 |
| 状态 | **已完成** — settings.json 已修复，CLAUDE.md 已更新 Done Criteria |
| 验证 | 改 Java 文件后检查 Stop Hook 是否触发 compile |

---

## P1 — 短期（下一轮 Sprint）

### 5. flywheel-bug-pipeline（知识飞轮升级通道）

| 维度 | 内容 |
|------|------|
| 问题 | gotchas 只有记录层，没有升级机制，同类 bug 反复出现 |
| 方案 | 青铜（gotcha 单文件）→ 白银（ai-knowledge 领域文档）→ 黄金（固化到规则引擎/Service 逻辑）的三级晋升通道 |
| 状态 | CLAUDE.md 已更新知识飞轮描述，dispatch.md 已加入升级判断步骤 |

### 6. dev-flow-superpowers（CLAUDE.md 活架构增强）

| 维度 | 内容 |
|------|------|
| 问题 | Done Criteria 是静态 checklist，没有强制力，容易流于形式 |
| 方案 | 1) Done Criteria 强检查——每个改动必须关联通过的命令，不允许口头确认；2) 路由表 fallback——不匹配时自动进入 review；3) internal-flag 机制 |
| 状态 | CLAUDE.md 已加入强检查规则和路由表 fallback |

---

## P2 — 远期（技术债清理）

### 7. security-guardrails（安全护栏）

| 维度 | 内容 |
|------|------|
| 问题 | 环境变量回退明文（`${APP_PASSWORD:-admin888}`），部分 Controller 可能缺权限注解 |
| 方案 | 1) 扫描所有 Controller @PreAuthorize 覆盖情况；2) 移除 application.properties 明文密码 fallback，改为启动时环境变量不存在就报错退出 |

---

## 实施依赖关系

```
P0: quality-gate-hooks（基础，无依赖）
     ↓
P0: api-conventions-testing（依赖 quality-gate-hooks 打通测试通道）
     ↓
P0: service-decomposition（依赖测试覆盖，保证拆分不破坏功能）
     ↓
P0: configurable-rule-engine（依赖 service-decomposition 腾出的整洁 Service 层）
     ↓
P1: flywheel-bug-pipeline（独立，可并行）
P1: dev-flow-superpowers（独立，可并行）
     ↓
P2: security-guardrails（独立，低优先级）
```

---

## 跳过的模式及原因

| 模式 | 跳过原因 |
|------|---------|
| layered-architecture | YTS 已深度采用，不需要再推荐 |
| schema-first-metadata-driven | 成熟项目代码生成 ROI 极低 |
| enterprise-dna-engine | 8 维引擎过于抽象，不匹配工序间 2-3 维规则 |
| iron-law-dispatch-and-routing | YTS 已有完整的 dispatch 路由表 |
| digital-employee-registry | 当前只有单一 AI Orchestrator，不需要注册管理器 |
| the-hall-collaboration | 单一业务流程不需要多 Agent 协调 |
| repository-adapter-pattern | MyBatis Mapper 已足够，再加 Repository 层是多余抽象 |
| skill-evolution-engine | 数据驱动的技能进化需要大量历史数据积累 |
| pre-production-simulation | 内部业务系统不需要三轨部署 |
| cost-governance | DashScope 调用场景明确，成本可控 |
| session-lifecycle-dashboard | 小团队单项目不需要多实例感知 |
| ssr-component-library | YTS 是 SPA，技术栈完全不匹配 |
