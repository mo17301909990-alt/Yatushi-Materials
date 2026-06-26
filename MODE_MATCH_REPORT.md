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
| 问题 | material_process_compatibility（10071行）+ hot_stamping_compatibility（1421行）匹配逻辑埋在 SQL JOIN 和 Java 条件分支中 |
| 方案 | YAML 声明工序兼容性规则，Java 加载到 Guava Cache，数据库 versioning，不重启 |
| 落地 | yts_project/src/main/resources/rules/ + CompatibilityRuleService |
| 验证 | YAML lint + 规则版本递增 + 兼容性测试套 |

### 2. service-decomposition（Service 拆分）

| 维度 | 内容 |
|------|------|
| 问题 | Large Service Impl 混入 Excel 处理逻辑，违反 SRP |
| 方案 | Excel 逻辑归 util/excel/，复杂匹配拆独立 Service，目标 ≤300 行 |
| 落地 | yts_project/src/main/java/com/it/yts_project/util/excel/ |
| 验证 | grep Excel 依赖不在 service 包 + 行数检查 |

### 3. api-conventions-testing（测试覆盖）

| 维度 | 内容 |
|------|------|
| 问题 | 仅 2 个测试，skipTests=true，生产系统零覆盖 |
| 方案 | 三步：删 skipTests → Service 层 @JdbcTest → Controller @WebMvcTest |
| 优先 | 兼容性匹配、价格计算、规则引擎 Service |
| 验证 | mvn test 通过 |

### 4. quality-gate-hooks（质量门禁修复）

| 维度 | 内容 |
|------|------|
| 问题 | Stop Hook 指向废弃路径 /f/YTS+JYY，skipTests=true |
| 方案 | 修复路径到 F:/yatushi-6-6，pre-commit 自动 mvn compile + npm run build |
| 状态 | **已完成** |
| 验证 | 改 Java 文件后 Stop Hook 触发 compile |

---

## P1 — 短期（下一轮 Sprint）

### 5. flywheel-bug-pipeline（知识飞轮升级通道）

| 维度 | 内容 |
|------|------|
| 问题 | gotchas 只有记录层，同类 bug 反复出现 |
| 方案 | 青铜（gotcha）→ 白银（ai-knowledge）→ 黄金（规则/Service 固化）三级晋升 |
| 状态 | CLAUDE.md + dispatch.md 已更新飞轮逻辑 |

### 6. dev-flow-superpowers（CLAUDE.md 活架构增强）

| 维度 | 内容 |
|------|------|
| 问题 | Done Criteria 静态 checklist，无强制力 |
| 方案 | 强检查 + 路由 Fallback + internal-flag |
| 状态 | CLAUDE.md 已更新 |

---

## P2 — 远期

### 7. security-guardrails（安全护栏）

| 维度 | 内容 |
|------|------|
| 问题 | 环境变量回退明文（:-admin888），Controller 可能缺权限注解 |
| 方案 | 扫描 @PreAuthorize 覆盖，移除明文 fallback，缺变量报错退出 |

---

## 实施依赖关系

```
P0: quality-gate-hooks（基础）
     ↓
P0: api-conventions-testing（依赖 quality-gate-hooks）
     ↓
P0: service-decomposition（依赖测试覆盖）
     ↓
P0: configurable-rule-engine（依赖 service-decomposition）
     ↓
P1: flywheel-bug-pipeline（独立，可并行）
P1: dev-flow-superpowers（独立，可并行）
     ↓
P2: security-guardrails（独立，低优先级）
```

---

## 跳过的模式及原因

| 模式 | 原因 |
|------|------|
| layered-architecture | YTS 已深度采用 |
| schema-first-metadata-driven | 成熟项目代码生成 ROI 低 |
| enterprise-dna-engine | 8 维引擎过于抽象 |
| iron-law-dispatch-and-routing | YTS 已有完整路由 |
| digital-employee-registry | 单一 AI 不需要注册管理器 |
| the-hall-collaboration | 单一流程不需要多 Agent |
| repository-adapter-pattern | MyBatis 已足够 |
| skill-evolution-engine | 缺少历史数据积累 |
| pre-production-simulation | 内部系统不需要 |
| cost-governance | 成本可控可预测 |
| session-lifecycle-dashboard | 小团队不需要 |
| ssr-component-library | 技术栈不匹配 |
