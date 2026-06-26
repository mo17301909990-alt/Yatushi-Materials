# YAML 规则引擎落地报告

---

## 一、现状：当前兼容性规则怎么跑的

### 整体架构：四大实现层次

```
┌──────────────────────────────────────────────────────────────┐
│  A. 数据库 CRUD 层                                            │
│     18 张兼容性表, 20+ 组 Controller→Service→Mapper→XML 堆栈  │
├──────────────────────────────────────────────────────────────┤
│  B. SQL 多维度过滤层                                          │
│     HotStampingCompatibilityMapper XML 动态 WHERE 1=1         │
├──────────────────────────────────────────────────────────────┤
│  C. JdbcTemplate ILIKE 统一搜索层                              │
│     CompatibilityQueryService 遍历 16 组表对                   │
├──────────────────────────────────────────────────────────────┤
│  D. 自然语言知识层                                            │
│     markdown 文件 + AI agent RAG                              │
└──────────────────────────────────────────────────────────────┘
```

**关键结论：当前没有任何 YAML/YML 配置文件，没有 rules/ 目录，没有 .drl 规则引擎文件。所有"规则"都是数据库表中的行。**

### 核心兼容性表一览

| 表名 | 行数 | 维度数 | 状态取值 | 复杂度 |
|------|------|--------|---------|--------|
| material_process_compatibility | 10,071 | 2 (material_id, operation_name) | V/X | 中 |
| hot_stamping_compatibility | 1,964 | 6 | V/X | 高 |
| lamination_compatibility | 875 | 5 (含 NULL 通配) | V/X/▷ | 高 |
| water_varnish_matte_compatibility | 2,271 | 2 | V/X | 低 |
| uv_oil_matte_compatibility | 923 | 2 | V/X | 低 |
| cloth_paper_compatibility | 941 | 3 | V/X | 低 |
| silicone_*_compatibility (12 张) | ~72/张 | 2 | V/X | **极低（同构模板）** |
| lamination_feasibility_rules | 51 | 4 (含数值范围) | V | 中 |

### 规则逻辑分布

- **SQL 中的规则**：HotStampingCompatibilityMapper.xml 的多维动态 `WHERE 1=1` + LaminationCompatibilityMapper.xml 的 NULL 通配语义。其余 15+ 组 Mapper XML 是纯 CRUD，无业务逻辑。
- **Java 中的规则**：`CompatibilityQueryService.searchUnified()` 是唯一有拼装逻辑的类 — 16 组 ModuleTable 遍历 + ILIKE 模糊搜索。
- **配置文件中的规则**：零。唯一的配置文件是 `ai-knowledge/post-processing-rules.zh.md`（供 AI agent 读的自然语言文档）。
- **Service 层的真相**：`SmartCompatibilityServiceImpl` 是纯管理编排（CRUD + 校验 + 分页 + 批量），没有业务决策逻辑。

### 该不该上规则引擎？

改规则的频率决定了投入产出比。按"改动频率 × 业务复杂度"排序：

1. **烫金兼容性矩阵** — 高频率 ★★★★★，当前 SQL WHERE 1=1，16 列多维度交叉，改一个维度组合就要增删行
2. **过胶兼容性** — 高频率 ★★★★☆，5 键唯一约束含通配语义
3. **物料-工序兼容性** — 中频率 ★★★☆☆，10,071 行大型主表
4. **UV油/水性油/压光UV** (3组) — 中频率 ★★☆☆☆，标准 CRUD
5. **硅胶系列 (12组)** — 低频率 ★★☆☆☆，12 组完全相同 CRUD 代码的模板复制粘贴
6. **lamination_feasibility_rules** — 低频率 ★★★★☆，纯 SQL 查询，数值范围最适合 YAML 但只有 51 行

---

## 二、YAML Schema 设计

### 顶层结构 (RuleDefinition)

```yaml
version:   语义版本号，用于缓存校验和增量热加载
metadata:  元信息（作者、更新时间、备注）
includes:  引用其他 YAML 文件路径（支持分拆）
groups:    规则组列表，每组定义一组业务语义相近的规则
```

### 规则组 (RuleGroup)

```yaml
group:          组标识（如 "silicone", "uv_oil", "lamination"）
description:    中文描述
type:           规则类型（SIMPLE | DIMENSIONAL | RANGE）
target:         此规则适用的实体类型（material_catalog / product / paper_performance）
dimensions:     维度定义（仅 DIMENSIONAL 类型需要）
defaultStatus:  默认值，规则未匹配时返回（通常为 "X"）
rules:          规则条目列表
```

### 规则条目 (RuleEntry)

```yaml
- 基础字段: status (V/X/▷), priority (数字，高优先覆盖低)
- SIMPLE 类型:
    materialId / productId + step / postProcessingStep
- DIMENSIONAL 类型:
    conditions: {dimension: value, ...} 的多维条件键值对
- RANGE 类型:
    numericRange: {min, max} + category 映射
- 增强字段: conditionDesc (条件说明), displayOrder, noticeIds
```

### 关键设计决策

1. **三态独立表达** — V(兼容)、X(不兼容)、▷(带条件) 统一用 status 字段，不依赖隐式默认值
2. **默认状态降级** — 每个 group 可配置 defaultStatus，未命中时返回该值；不配置则 fallback
3. **优先级覆盖** — priority 值高的规则优先；同一维度的多规则根据 priority 合并
4. **简单/复杂共存** — SIMPLE 类型用于 product_id×step 同构表；DIMENSIONAL 用于多条件交叉匹配
5. **include 机制** — 允许按模块拆分文件，rules/base/silicone.yaml include rules/silicone/*.yaml
6. **code_mapping 兼容** — materialId 字段同时支持 "p0_code" 或 "material_catalog_id" 两种体系

### 完整示例

```yaml
# ============================================================
# YTS 兼容性规则引擎 — 规则定义文件
# Schema 版本: 1.0.0
# ============================================================
version: "1.0.0"
metadata:
  author: "YTS 规则引擎"
  updated: "2026-06-21"
  description: "兼容性规则集中管理入口文件"

includes:
  - rules/silicone/all.yaml
  - rules/uv_oil/all.yaml
  - rules/hot_stamping/basic.yaml

groups:
  # ----- SIMPLE 类型：硅胶粗UV (product × step) -----
  - group: "silicone_coarse_uv"
    description: "硅胶粗UV 兼容性规则"
    type: SIMPLE
    target: "product"
    dimensions:
      - name: "productId"
        type: INTEGER
        required: true
      - name: "postProcessingStep"
        type: STRING
        required: true
    defaultStatus: "X"
    rules:
      - status: "V"
        conditions:
          productId: 1
          postProcessingStep: "UV涂布"
        conditionDesc: "普通粗UV适用"
        displayOrder: 1
      - status: "X"
        conditions:
          productId: 2
          postProcessingStep: "UV涂布"
        conditionDesc: "表面张力不足，无法附着"

  # ----- DIMENSIONAL 类型：烫金兼容性矩阵 (6 维) -----
  - group: "hot_stamping"
    description: "烫金兼容性矩阵规则"
    type: DIMENSIONAL
    target: "paper_performance"
    dimensions:
      - name: "productType"
        type: STRING
        required: false
      - name: "patternCharacteristic"
        type: STRING
        required: false
      - name: "hotStampingType"
        type: STRING
        required: false
      - name: "preProcessingStep"
        type: STRING
        required: false
      - name: "postProcessingStep"
        type: STRING
        required: false
    defaultStatus: "X"
    rules:
      - status: "V"
        conditions:
          productType: "书刊"
          patternCharacteristic: "实地"
          hotStampingType: "普通烫"
        conditionDesc: "书刊实地适用普通烫金"
      - status: "X"
        conditions:
          productType: "书刊"
          patternCharacteristic: "细字"
          hotStampingType: "普通烫"
        conditionDesc: "细字线条无法清晰呈现"

  # ----- DIMENSIONAL 带 NULL 通配：过胶兼容性 -----
  - group: "lamination"
    description: "过胶物料兼容性规则"
    type: DIMENSIONAL
    target: "lamination_material"
    dimensions:
      - name: "foilSeries"
        type: STRING
        required: true
      - name: "laminationMaterialId"
        type: INTEGER
        required: true
      - name: "modelNumber"
        type: STRING
        required: false
        nullable: true      # 支持 NULL 通配符
    defaultStatus: "X"
    rules:
      - status: "V"
        conditions:
          foilSeries: "GT系列"
          laminationMaterialId: 3
          modelNumber: "A-100"
      - status: "V"
        conditions:
          foilSeries: "GT系列"
          laminationMaterialId: 3
        # modelNumber 未指定 → 视为通配
        conditionDesc: "GT系列通用方案"
```

---

## 三、加载器架构

### 核心类关系

```
YamlRuleProperties (Spring @ConfigurationProperties)
    ↓ 读取 rule.engine.* 配置
YamlRuleLoader (读取、解析、校验 YAML)
    ↓ 返回 RuleDefinition 结构
CompatibilityRuleManager (缓存管理 + 路由 + 降级)
    ↓ 提供查询 API
CompatibilityRuleEngine (规则匹配算法)
    ├── SimpleMatcher   (精确匹配 productId + step)
    ├── DimensionalMatcher (多维条件匹配，含 NULL 通配)
    └── RangeMatcher    (数值范围匹配)
```

### 缓存策略：Caffeine Cache

| 参数 | 值 | 说明 |
|------|----|------|
| initialCapacity | 1000 | 初始容量 |
| maximumSize | 10,000 | 最大条目 |
| expireAfterWrite | 60 min | 写入后过期 (TTL) |
| recordStats | true | 开启命中率统计 |

**选型理由**：Spring Boot 3.3.x 通过 `spring-boot-starter-cache` 自动集成 Caffeine，零额外依赖。规则数据量小（~10K 条），本地缓存足够，无需 Redis。

**缓存结构**：
```
一级缓存（本地 Caffeine）:
  group -> Cache<CompositeKey, RuleEntry>

二级缓存（统计/元数据）:
  version -> String
  groupMeta -> Map<String, GroupMeta>
  fallbackCount -> AtomicInteger
```

### 热刷新机制

**默认方式：定时轮询**（@Scheduled，默认 30 秒间隔）
- 检查 YAML 文件 `lastModified`
- 有变更 → 重新加载 + 校验 → Caffeine 原子替换
- 校验失败 → 保留旧缓存，日志告警

**备选方式**：
- 手动触发：`POST /api/rules/reload`
- WatchService（生产可选）：Java NIO 监听目录变更，实时性更好但复杂度高

### 三级降级链

```
Request → CompatibilityRuleEngine
  ├─ YAML 匹配命中 → 直接返回
  ├─ YAML 未命中 (no rule found) → fallback 到 SQL 查询
  └─ YAML 加载/解析失败 → 自动切换到全 DB 模式
```

**自动降级**：连续失败计数 >= 阈值（默认 3）→ 切到 database 模式 → 每 5 分钟尝试恢复。

**健康检查**：通过 Spring Boot Actuator `RuleEngineHealthIndicator` 暴露 source/version/groups/cacheHitRate/fallbackCount。

### rules/ 目录结构

```
src/main/resources/rules/
├── compatibility-rules.yaml          # 入口文件
├── silicone/                         # 12 组同构规则
│   ├── all.yaml
│   ├── coarse-uv.yaml
│   └── ... (其他 11 组)
├── uv_oil/                           # 3 组 UV油/水性油
│   ├── uv-oil-matte.yaml
│   ├── water-varnish-matte.yaml
│   └── yaguang-uv-oil.yaml
├── hot_stamping/
│   ├── basic.yaml
│   └── skip-config.yaml
├── lamination/
│   ├── compatibility.yaml
│   └── feasibility.yaml
├── cloth_paper/
│   └── compatibility.yaml
└── material_process/
    └── base-rules.yaml
```

### Java 包结构

```
src/main/java/com/it/yts_project/rule/
├── YamlRuleProperties.java
├── YamlRuleLoader.java
├── CompatibilityRuleManager.java
├── CompatibilityRuleEngine.java
├── RuleHotReloader.java
├── RuleConsistencyChecker.java
├── RuleManagementController.java
├── RuleEngineHealthIndicator.java
├── model/
│   ├── RuleDefinition.java
│   ├── RuleGroup.java
│   ├── RuleEntry.java
│   ├── RuleMatchResult.java
│   └── RuleSource.java
├── matcher/
│   ├── RuleMatcher.java
│   ├── SimpleMatcher.java
│   ├── DimensionalMatcher.java
│   └── RangeMatcher.java
└── support/
    ├── RuleNotFoundException.java
    ├── YamlParseException.java
    └── CompositeKey.java
```

---

## 四、实施路线（分 5 阶段）

### Phase 1 — 引擎核心 + 硅胶粗UV试点（1.5 天）

**目标**：YAML 规则引擎基础设施就绪 — 加载器、缓存、精确匹配器全部可用。选定 `silicone_coarse_uv` 作为试点完成 YAML 编写，新 API 端点可查询 YAML 规则返回结果并与数据库对比验证。

**产出**：
- 5 个 model 类 + YamlRuleLoader + SimpleMatcher
- CompatibilityRuleManager + CompatibilityRuleEngine
- RuleManagementController（基础 status + query 端点）
- `rules/compatibility-rules.yaml` 入口文件 + `rules/silicone/coarse-uv.yaml` 试点规则

**验收标准**：
1. `mvn compile` 通过
2. 启动后 `/api/rules/status` 返回 `source=yaml, loadedGroups=[silicone_coarse_uv]`
3. 查询 YAML 结果与对应 DB 表查询一致
4. 单元测试覆盖加载、匹配、未命中 3 种场景

**回滚方案**：删除 `rule/` 包下所有新文件 + 回滚 pom.xml 和 application.properties，旧代码不受影响。

---

### Phase 2 — 全量同构规则迁移 + 一致性自动校验（1.5 天）

**目标**：12 组硅胶 + 3 组 UV油规则全部从数据库导出为 YAML 文件。DimensionalMatcher 支持 NULL 通配语义。RuleConsistencyChecker 定时对比 YAML 与 DB 结果并生成差异报告。

**产出**：
- DimensionalMatcher（支持 NULL 通配）
- RuleConsistencyChecker（定时差异检测）
- 剩余 11 组硅胶 YAML + 3 组 UV油 YAML
- `database_scripts/export-rules-to-yaml.py`（DB to YAML 导出脚本）

**验收标准**：
1. 15 组规则 YAML 全量就绪，总行数与 DB 一致
2. RuleConsistencyChecker 执行后差异记录为 0
3. 随机采样 500 组参数，YAML 与 DB 结果完全一致
4. DimensionalMatcher 正确处理 NULL 通配

---

### Phase 3 — 热刷新 + 管理端点 + 健康检查（1 天）

**目标**：YAML 文件热加载无需重启。管理 API 手动刷新/查看状态。Actuator 健康检查集成。三级降级链就绪。

**产出**：
- RuleHotReloader（@Scheduled 定时轮询 + 文件变更检测）
- RuleEngineHealthIndicator（Actuator HealthIndicator）
- RuleManagementController 增强（POST /reload, GET /status, POST /switch-source）

**验收标准**：
1. 修改 YAML 文件后等待 30 秒（默认轮询间隔），新规则生效无需重启
2. POST `/api/rules/reload` 立即刷新
3. GET `/api/rules/status` 返回 version/groups/cacheHitRate/source
4. 人工破坏 YAML 格式后引擎自动降级到 database 模式，5 分钟后自动恢复

---

### Phase 4 — 服务层替换 + 旧代码清理（1.5 天）

**目标**：12 组 Silicone*Service + 3 组 UvOil*Service 的查询方法全部替换为 RuleBackedQueryService 调用。删除冗余的 Mapper XML 和 ServiceImpl。

**产出**：
- 修改 15 组 Service 注入 CompatibilityRuleManager 替代 Mapper
- 修改 CompatibilityQueryService（16 组 ModuleTable 硬编码改为从 YAML group 元数据动态发现）
- 删除 12 组 Silicone*Mapper.xml + ServiceImpl
- 删除 3 组 UvOil*/WaterVarnish* Mapper.xml + ServiceImpl

**验收标准**：
1. 15 组旧 Service 查询替换后功能回归测试通过
2. CompatibilityQueryService.searchUnified() 返回结果与替换前一致
3. 移除的 Mapper XML 无其他引用
4. `mvn test` 全部通过

---

### Phase 5 — 业务编辑流程 + DMN 桥接评估（1 天）

**目标**：Element Plus 管理界面可视化编辑 YAML 规则。DB to YAML 反向同步工具。Feishu 通知集成。DMN 桥接试点方案评估文档。

**产出**：
- 前端 `src/views/rule/RuleManagement.vue` + `RuleEditor.vue`
- 前端 `src/api/modules/rules.ts`
- 后端 RuleManagementController 增强（POST/PUT 规则 CRUD）
- `database_scripts/sync-db-to-yaml.py`（DB 变更同步回 YAML）
- `docs/dmn-bridge-evaluation.md`

**验收标准**：
1. 管理员可在 Web 页面查看/编辑 YAML 规则，保存后触发热加载
2. 新增规则从编辑器写入 YAML 文件，无需直接操作服务器
3. DB to YAML 同步脚本可在 5 分钟内完成全量导出
4. DMN 桥接文档输出试点范围和建议

---

**总工作量：6.5 天**（单人独立开发，不含代码评审和回归测试等待时间）

### 风险清单

| 风险 | 缓解措施 |
|------|---------|
| YAML 文件体积超 120KB 需拆分 | include 机制支持细粒度拆分，超出前自动规划拆分策略 |
| 热刷新期间读旧数据 | 最终一致性可接受，管理端提供刷新状态指示 |
| NULL 通配语义需逐条核对 | RuleConsistencyChecker 自动对比 + 人工抽样验证 |
| 业务人员不直接编辑 YAML | Phase 5 交付 Web UI，纯 YAML 仅作为存储格式 |
| 隐式 LEFT JOIN 过滤无法从 YAML 还原 | 在 conditionDesc 中记录隐式逻辑，一致性校验白名单 |
| Controller 层 @Autowired 散布 | 先做 Service 层替换，Controller 改造留到 Phase 4 统一处理 |

---

## 五、YAML vs DMN 对比

| 维度 | YAML 方案 | DMN 方案 (Camunda) |
|------|-----------|-------------------|
| **运行时依赖** | SnakeYAML 由 Spring Boot 自动引入，零额外依赖 | Camunda C7 已 EOL (2025.10)；C8 需 K8s + Elasticsearch 全套 |
| **文件体积** | 1,000 条规则约 30KB | 同等规则 200KB+ DMN XML |
| **学习成本** | 业务人员能看懂 key:value | 需要学 FEEL 表达式 + DMN 规范 |
| **Git diff 可读性** | 精确到行，diff 可读 | 大段机器生成 XML，diff 不可读 |
| **热加载** | File.lastModified + Caffeine，~20 行代码 | 依赖 Camunda 的规则部署机制 |
| **基础设施** | 零，Spring Boot 原生支撑 | C8 需要 K8s + Elasticsearch + Operate/Tasklist |
| **可视化编辑** | Phase 5 自行开发 Element Plus 页面 | Camunda Modeler 开箱即用 |
| **复杂计算** | 需 Java 扩展 | FEEL 表达式支持日期运算、聚合等 |
| **决策追踪** | 需自行开发 | 引擎内置 audit log |
| **行业标准** | 无，团队约定 | OMG 标准，有社区生态 |

### YAML 的优势

- **零额外运行时依赖** — SnakeYAML 由 Spring Boot 自动引入，一行 pom 都不用加
- **文件体积小** — 1000 条规则约 30KB YAML vs 200KB+ DMN XML
- **学习成本极低** — 业务人员能看懂 key:value，无需学 FEEL 表达式
- **Git diff 可读** — YAML 的 diff 精确到行，DMN XML 是大段机器生成 XML
- **热加载实现简单** — File.lastModified + Caffeine 原子替换，20 行代码
- **基础设施零依赖** — Camunda C7 已 EOL，C8 需要 K8s + Elasticsearch + Operate/Tasklist，YTS 当前架构无法承载
- **与现有 code_mapping 自然集成** — YAML 的 materialId 可直接映射到数据库 ID 体系
- **SnakeYAML 解析性能优异** — 5000 行 YAML 解析 < 50ms

### DMN 的优势

- **OMG 行业标准**，有 Camunda Modeler 可视化表格编辑
- **决策表展示直观** — 行列表格格式对业务人员最友好
- **自带命中策略语义** — UNIQUE/COLLECT/PRIORITY/OUTPUT ORDER 等
- **FEEL 表达式支持复杂计算** — 日期运算、聚合、字符串处理
- **有标准决策追踪和审计** — DMN 引擎自动记录每次决策的输入输出
- **.dmn 文件可在 C7 和 C8 之间无缝复用**

---

## 六、最终建议

### 核心结论：YAML 先行，DMN 备胎

**推荐 YAML 方案作为主路线**，理由有三：

**第一，基础设施不可行。** Camunda C7 已于 2025 年 10 月 EOL（生命周期终结），而 C8 需要 K8s + Elasticsearch + Operate/Tasklist 全套基础设施。YTS 当前部署架构是单机 Spring Boot JAR + PostgreSQL，没有容器编排，没有 ES 集群，引入 C8 意味着先搭一套 K8s。这个前置投入远超规则引擎本身的价值。

**第二，业务复杂度匹配。** YTS 的兼容性规则 80% 是简单的 V/X/▷ 三态判定，YAML 的 `conditions: {k: v}` 表达力完全够用。只有烫金 6 维规则到了需要 DMN 决策表的复杂程度，但那是少数场景。用 C8 跑全校车是大炮打蚊子。

**第三，交付速度差距显著。** YAML 方案 7 天可交付全量规则迁移（含全部 5 个阶段）。DMN POC 光搭 C8 环境就要 2-3 天，且只覆盖烫金一个模块。YAML 方案在同等时间内能把硅胶 + UV 油 + 烫金 + 过胶全部搞定。

### DMN 的定位

保留 DMN 作为 **Phase 5 可选桥接** — 仅对烫金 6 维规则做试点验证，评估 DMN 表格编辑对业务人员的额外价值是否值得引入 C8 基础设施。**先把 YAML 跑起来，等业务方真觉得 YAML 不够用了再说 DMN 的事。**

### 推荐的首次迁移范围

**第一批（Phase 1-2，3 天）**：12 组硅胶 + 3 组 UV油 = 15 组同构表，约 4,058 行数据
- 理由：结构最简（product_id × step），12 组 Service 是模板复制粘贴，合并为 1 份 YAML + 1 个通用 Service 能消除 11 份重复代码
- 可以用 Python 脚本从数据库自动导出 YAML

**第二批（Phase 3-4，2.5 天）**：热刷新 + 降级 + 服务层替换
- 旧代码删除前必须确保一致性校验通过

**第三批（Phase 5，1 天）**：管理界面 + DMN 桥接评估文档

### 不做的事情

- **hot_stamping_compatibility 不首批迁移** — 维度太多、改得频繁、已经有多维过滤 SQL 体系
- **lamination_compatibility 不首批迁移** — 5 维 + 通配语义复杂，YAML 表达能力需通过 DimensionalMatcher 先行验证
- **material_process_compatibility 不首批迁移** — 10,071 行的大型主表，改频率中等但数据量大，YAML 文件的单文件体积会超标
- **lamination_feasibility_rules 延后处理** — 虽然最适合 YAML（数值范围 + 类别映射），但只有 51 行且业务稳定，做不做影响不大

### 一句话总结

YAML 方案 6.5 天交付全量规则迁移，零新增基础设施，不回滚旧代码，业务方有 Web 编辑页面不用碰 YAML 语法。DMN 作为后手，等业务真长出复杂规则需求时再评估。
