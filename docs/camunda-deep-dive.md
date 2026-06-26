# Camunda 在 YTS 中的落地可行性报告

## Camunda 核心能力

Camunda 是企业级工作流与决策自动化平台，覆盖 BPMN 2.0 流程编排、DMN 决策管理、CMMN 案例管理三大标准。其核心差异化能力在于 **DMN 决策表**：业务规则以表格形式独立管理，热部署生效，无需改代码、无需重启引擎。与之配合的 BPMN 引擎支持 Service Task（External Task 模式）、User Task（人工审批）、Gateway（并行/排他/包容分支）、Event（定时/消息/错误/补偿），完整建模生产流程。

当前版本线存在代际断裂：**C7（嵌入式引擎，同 JVM 共享事务）将于 2025 年 10 月 EOL**，C8 改为 gRPC 分布式架构（Zeebe + Elasticsearch），必须上 K8s。C7 到 C8 不是升级而是替换——JavaDelegate 要重写为 Job Worker，JUEL 全部转 FEEL，插件体系废弃。DMN 文件（.dmn）是两版之间唯一可无缝复用的资产。

---

## YTS 流程映射

| YTS 业务流程 | Camunda 能力映射 | 难度 | 价值 | 说明 |
|---|---|---|---|---|
| **工序匹配引导**（6步：选产品类型→图案→类型→纸张→前处理→后加工） | BPMN User Task 序列 + DMN 决策表路由 + External Task 物料搜索 | 中 | 高 | 线性流程 BPMN 可清晰建模，16 组异构表由 External Task 封装。当前前端 GUIDE_PARAM_MAP 硬编码的选项过滤逻辑可迁入 DMN |
| **兼容性规则管理**（material_process_compatibility + hot_stamping_compatibility，10,071 行规则） | **DMN Decision Table + DRD** | 高 | **高** | **最大收益点**。当前规则混在 16 张表 + Java if-else 中，改规则必须改代码部署。DMN 后工艺人员可在 Web Modeler 直接维护。关键难点：现有 condition_desc 的 Java 字符串拼接逻辑需改为 DMN 输出列带出 |
| **管理员变更审批**（preview → execute → rollback + 操作日志） | BPMN + CMMN（非结构化变更场景）+ DMN 风险定级 | 低 | 中 | 最标准的 BPMN 场景，已有完整的 AdminChangeService 三阶段模型。但需评估替换 ROI——当前方案本身够用 |
| **Copilot AI 对话**（意图识别 → 参数提取 → 匹配查询） | DMN 意图路由（仅关键词→意图映射部分），BPMN **不适用** | 低 | 低 | AI 对话是单次 LLM 调用，BPMN 异步编排徒增延迟。意图路由 DMN 化价值也有限（当前 Pattern 匹配不复杂）。**不要强行塞入 BPMN** |
| **RBAC 权限体系**（角色→权限→用户 + 字段级权限） | Camunda Identity（Keycloak/OIDC），C8.9 Task 级权限 | 中 | 低 | YTS 已有成熟的字段级 RBAC（@FieldPermission 注解），Camunda Identity 不覆盖此粒度。**保留现有体系**，只在审批任务边界映射角色 |
| **操作日志与审计**（AdminOperationLogAspect + admin_operation_log） | BPMN History / Zeebe Audit Log + Optimize 仪表盘 | 中 | 中 | Camunda 自动记录流程实例全生命周期。但当前 AOP 方案简单可靠已有三年数据。建议新建流程用 Camunda 审计，旧数据不动 |
| **物料 ETL**（database_scripts/ 下零散 SQL/Python 脚本） | C8 Job Worker + Timer Start Event | 中 | 中 | 定时启动 BPMN 编排 ETL 各阶段（extract→transform→load→validate）。但批处理场景 Airflow/DolphinScheduler 更优 |

**不适用场景**：AI Copilot 多轮对话核心流程、实时物料搜索（CompatibilityQueryService.searchUnified）、现有字段级 RBAC 权限解析、Excel 导入导出、基本 CRUD。

---

## 第一步 POC 方案：烫金兼容性规则 DMN 化

### 目标与范围

**POC 标题**：烫金兼容性规则 DMN 化 — 快速验证试点
**工期**：2-3 天
**目标**：用 Camunda DMN 替代 hot_stamping_compatibility 表的 Java 过滤逻辑（1,421 行规则），验证 DMN 输出与现有 SmartCompatibilityService / CompatibilityQueryService 结果一致性。

### 详细步骤

**第 1 天上午 — 基础设施与 DMN 文件创建**
- 在 `yts_project/pom.xml` 添加依赖，锁定 C7 嵌入式引擎（与现有 Spring Boot 3.3.x 同 JVM 运行，零额外基础设施）：

```xml
<dependency>
    <groupId>org.camunda.bpm.springboot</groupId>
    <artifactId>camunda-bpm-spring-boot-starter</artifactId>
    <version>7.22.0</version>
    <exclusions>
        <exclusion>
            <groupId>org.camunda.bpm.springboot</groupId>
            <artifactId>camunda-bpm-spring-boot-starter-rest</artifactId>
        </exclusion>
        <exclusion>
            <groupId>org.camunda.bpm</groupId>
            <artifactId>camunda-engine-plugin-connect</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```
- 配置 `application.properties`：`camunda.bpm.enabled=true`、`dmn.enabled=true`、`camunda.bpm.history-level=none`、`camunda.bpm.authorization.enabled=false`（禁用引擎内置认证，避免与现有 ApiAuthorizationFilter 冲突）
- 用 Python 脚本从 `hot_stamping_compatibility` 表导出 100 条代表性数据为 DMN XML，人工校验 50 条边界规则的 condition_desc 完整性

**第 1 天下午 — 服务层与测试**
- 创建 `DmnCompatibilityTestService`（接口 + 实现），封装 `DmnEngine.evaluateDecisionTable()` 调用
- 在 `SmartCompatibilityServiceImpl.getPaperPerformanceByMultipleIds()` 末尾插入 DMN 兜底分支：优先走 DMN，无匹配时 fallback 到 Mapper XML SQL。通过 `dmn.enabled=true/false` 一键切换
- 写 JUnit 5 测试对比 DMN 与 DB 查询结果的准确率（目标 >= 95%）

**第 2 天 — 引导流程 BPMN 模型 + 路由 DMN**
- 创建 `hot-stamping-guide.bpmn` 模型文件（不部署引擎），建模 6 步 User Task + Exclusive Gateway 分支
- 创建 `guide-flow-routing.dmn`：输入 `currentStep + selectedOptionId`，输出 `nextStep + filterCriteria`，替代前端 GUIDE_PARAM_MAP
- 前端不动，只验证后端 BPMN 变量推导逻辑与当前 step 状态管理结果一致

**第 3 天 — 端到端验证与文档**
- curl 模拟完整参数传递链，断言 DMN 返回的 paperPerformance 列表与 DB 一致
- 随机采样 500 组参数做比对测试，输出准确率报告
- 整理踩坑记录到 `.claude/gotchas/`

### 依赖清单

| 依赖 | 版本 | 说明 |
|---|---|---|
| camunda-bpm-spring-boot-starter | 7.22.0 | C7 嵌入式引擎，与 Spring Boot 3.3.x 兼容 |
| camunda-bpm-dmn | 7.22.0 | DMN 引擎，starter 已传递依赖 |
| PostgreSQL + mybatis-spring-boot-starter | 现有 | 不变，DB 中的 1,421 行原始数据作为对照基线 |

**不需要**：Elasticsearch（C8 才需要）、Operate/Tasklist/Identity（POC 阶段全部禁用）、Kubernetes（C7 嵌入式运行在现有 JVM 中）。

### 代码骨架

**CamundaConfig.java** — 引擎配置，禁用 Web 接口和认证：

```java
@Configuration
@ConditionalOnProperty(name = "dmn.enabled", havingValue = "true")
public class CamundaConfig {

    @Bean
    public ProcessEngineConfiguration processEngineConfiguration(DataSource dataSource) {
        SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();
        config.setDataSource(dataSource);
        config.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        config.setHistory(ProcessEngineConfiguration.HISTORY_NONE);
        config.setJobExecutorActivate(false);
        config.setAuthorizationEnabled(false);
        return config;
    }

    @Bean
    public DmnEngineConfiguration dmnEngineConfiguration() {
        DmnEngineConfiguration config = DmnEngineConfiguration.createDefaultDmnEngineConfiguration();
        // 仅加载 resources/dmn/ 目录下的 .dmn 文件
        config.setResourcePattern("classpath*:dmn/**/*.dmn");
        return config;
    }

    @Bean
    public DmnEngine dmnEngine(DmnEngineConfiguration config) {
        return config.buildDmnEngine();
    }
}
```

**DmnCompatibilityTestServiceImpl.java** — DMN 决策表调用核心：

```java
@Service
@ConditionalOnProperty(name = "dmn.enabled", havingValue = "true")
public class DmnCompatibilityTestServiceImpl implements DmnCompatibilityTestService {

    private final DmnEngine dmnEngine;
    private final DmnDecision decision;

    public DmnCompatibilityTestServiceImpl(DmnEngine dmnEngine) {
        this.dmnEngine = dmnEngine;
        // 加载 DMN 决策模型（类加载时解析一次，后续重复使用）
        DecisionRequirementsGraph drg = dmnEngine
            .parseDecisionRequirementsGraph(
                getClass().getResourceAsStream("/dmn/hot-stamping-compatibility.dmn"));
        this.decision = drg.getDecisions().iterator().next();
    }

    @Override
    public List<DmnCompatibilityResult> evaluate(Map<String, Object> variables) {
        // 执行 DMN 决策表
        DmnDecisionResult result = dmnEngine.evaluateDecision(decision, variables);

        return result.getResultList().stream()
            .map(r -> {
                DmnCompatibilityResult res = new DmnCompatibilityResult();
                res.setCompatibilityStatus((String) r.getEntry("compatibilityStatus"));
                res.setSuggestedPaperPerformance((String) r.getEntry("suggestedPaperPerformance"));
                res.setConditionDesc((String) r.getEntry("conditionDesc"));
                return res;
            })
            .collect(Collectors.toList());
    }

    @Override
    public boolean hasMatch(Map<String, Object> variables) {
        return !evaluate(variables).isEmpty();
    }

    @Override
    public DmnCompatibilityResult fallback(Map<String, Object> variables) {
        // 无匹配时返回空结果（由调用方触发 fallback JDBC 查询）
        return null;
    }
}
```

**SmartCompatibilityServiceImpl 改动片段** — 插入 DMN 兜底分支：

```java
// 在 getPaperPerformanceByMultipleIds() 中，SQL 查询之前插入：
if (dmnEnabled && dmnTestService != null) {
    Map<String, Object> dmnVars = Map.of(
        "productTypeId", productTypeId,
        "patternCharacteristicId", patternCharacteristicId,
        // ...
    );
    List<DmnCompatibilityResult> dmnResults = dmnTestService.evaluate(dmnVars);
    if (!dmnResults.isEmpty()) {
        log.info("DMN 匹配命中 {} 条规则，跳过 DB 查询", dmnResults.size());
        return dmnResults.stream()
            .filter(r -> "V".equals(r.getCompatibilityStatus()) || "▷".equals(r.getCompatibilityStatus()))
            .map(DmnCompatibilityResult::getSuggestedPaperPerformance)
            .distinct()
            .collect(Collectors.toList());
    }
    log.info("DMN 无匹配，fallback 到 Mapper XML SQL");
}
// 后续原有的 Mapper XML 查询逻辑不变
```

**DmnCompatibilityTest.java** — JUnit 5 验证测试骨架：

```java
@SpringBootTest
@EnableConfigurationProperties
class DmnCompatibilityTest {

    @Autowired(required = false)
    @ConditionalOnProperty(name = "dmn.enabled", havingValue = "true")
    private DmnCompatibilityTestService dmnService;

    @Autowired
    private HotStampingCompatibilityMapper mapper;

    @Test
    void testKnownCompatibilityRules() {
        // 从数据库抽取 100 条已知 V/X/▷ 记录
        List<Integer> knownIds = List.of(1, 2, 3, /* ... 100 条 ID */);
        for (Integer id : knownIds) {
            var dbResult = mapper.selectByPrimaryKey(id);
            Map<String, Object> vars = Map.of(
                "productTypeId", dbResult.getProductTypeId(),
                "patternCharacteristicId", dbResult.getPatternCharacteristicId(),
                "hotStampingTypeId", dbResult.getHotStampingTypeId()
            );
            var dmnResult = dmnService.evaluate(vars);
            // 断言兼容性状态一致
            assertEquals(
                dbResult.getCompatibilityStatus(),
                dmnResult.get(0).getCompatibilityStatus(),
                "ID=" + id + " 兼容性状态不匹配"
            );
        }
    }

    @Test
    void testEdgeCases() {
        // 不存在的参数组合 → DMN 无匹配 → fallback
        Map<String, Object> invalidVars = Map.of(
            "productTypeId", 999,
            "patternCharacteristicId", 999
        );
        assertTrue(dmnService.evaluate(invalidVars).isEmpty());
    }

    @Test
    void testBulkRandomSample() {
        // 随机 500 组参数组合，输出准确率报告
        int total = 0, match = 0;
        // ... 从数据库随机抽取 500 条，逐一对比
        double accuracy = (double) match / total;
        assertTrue(accuracy >= 0.95, "准确率 " + (accuracy * 100) + "% 低于 95% 阈值");
    }
}
```

---

## DMN 决策表示例：烫金兼容性规则

### hot-stamping-compatibility.dmn

| 属性 | 值 |
|---|---|
| 决策 ID | `decision_hot_stamping_compatibility` |
| 命中策略 | `UNIQUE`（唯一匹配，每组合参数只有一条确定结果） |
| 决策表名称 | 烫金兼容性判定 |

**输入列（Input Expressions）：**

| 输入 ID | 名称 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| Input-1 | productTypeId | number | 是 | 产品类型 ID：1=精平装, 2=贺卡, 3=包装 |
| Input-2 | patternCharacteristicId | number | 是 | 图案特征 ID：1=净线条≤0.5mm, 2=净实地10-20mm |
| Input-3 | hotStampingTypeId | number | 是 | 烫金类型 ID：1=平面烫金-中间, 2=立体烫金 |
| Input-4 | preProcessingStepId | number | 否 | 前工序 ID：5=UV 底油, null=无 |
| Input-5 | postProcessingStepId | number | 否 | 后工序 ID：8=过胶, null=无 |

**输出列（Output Expressions）：**

| 输出 ID | 名称 | 类型 | 说明 |
|---|---|---|---|
| Output-1 | compatibilityStatus | string | V=兼容, X=不兼容, ▷=条件兼容 |
| Output-2 | suggestedPaperPerformance | string | 如"高耐磨"、"普通金纸"、"普通耐磨" |
| Output-3 | conditionDesc | string | 条件说明文本（如"需配合 UV 底油使用"），替代 Java 动态拼接 |

**示例规则行：**

| productTypeId | patternCharacteristicId | hotStampingTypeId | preProcessingStepId | postProcessingStepId | compatibilityStatus | suggestedPaperPerformance | conditionDesc |
|---|---|---|---|---|---|---|---|
| 1 | 1 | 1 | - | - | V | 高耐磨 | 标准精平装烫金 |
| 1 | 1 | 1 | 5 | - | V | 普通耐磨 | 配合 UV 底油时降级为普通耐磨 |
| 1 | 1 | 1 | 5 | 8 | X | - | UV 底油+过胶组合不兼容 |
| 1 | 2 | 2 | - | - | V | 普通金纸 | 立体烫金标准配置 |
| 2 | 1 | 1 | - | - | ▷ | 普通耐磨 | 贺卡材质需打样确认 |
| 2 | 1 | 1 | - | 8 | V | 普通耐磨 | 过胶后贺卡兼容性确认通过 |
| - | - | - | - | - | X | - | 默认不兼容 |

**FEEL 表达式关键适配说明：**

1. **空值处理**：参数为 null 时，FEEL 用 `null` 字面量。DMN 规则行用 `-`（连字符）表示该输入列不参与匹配（任意值匹配）
2. **条件描述**：当前 Java 中的 `conditionDesc` 动态拼接逻辑（`需配合 X 工序且 Y 条件`）改为 DMN 输出列直接带出，每行规则预写完整描述文本。存量数据中缺失 desc 的行批量生成默认文本
3. **多候选场景**：同一参数组合有多个 `suggestedPaperPerformance` 候选时，命中策略改为 `COLLECT` 返回所有匹配行，调用方聚合去重
4. **当前 Java 逻辑对比**：现有 `HotStampingCompatibilityMapper.getCompatibilityRulesByMultipleIds()` 的 WHERE 子句动态条件片段映射为 DMN InputEntry FEEL 表达式；ORDER BY 和兼容状态过滤（`IN ('V', '▷')`）由调用方处理，DMN 规则表本身只包含有效的 V/▷ 规则行

---

## 风险与建议

### 风险清单

| 风险 | 概率 | 影响 | 降级方案 |
|---|---|---|---|
| DMN 数据迁移语义丢失：1,421 行规则导出为 DMN XML 时，condition_desc 等动态生成文本可能丢失 | 中 | 高 | POC 用 100 行采样验证，准确率 < 95% 则全量迁移前引入半自动标注流程（Python 脚本从 DB 导出 CSV 转 DMN XML + 人工校验边界规则） |
| FEEL 表达式能力边界：ID→中文名称转换、condition_desc 动态拼接涉及 DB 查询，FEEL 不能调 Java 方法 | 高 | 中 | DMN 输出列保留 ID，文本渲染留在 Java/DTO 层。condition_desc 由 Service 层后处理组装，DMN 只负责规则匹配 |
| C7 嵌入式引擎与现有 Spring Security 冲突：camunda-bpm-spring-boot-starter 自带身份认证可能冲撞 ApiAuthorizationFilter | 中 | 高 | CamundaConfig 中禁用全部内置认证/授权（authorizationEnabled=false），禁用 REST 和 Web 接口（排除 camunda-bpm-spring-boot-starter-rest），仅启用 DMN 引擎子模块 |
| DMN 文件加载性能：1,421 行导出为约 200-300KB XML，首次解析可能超 1s | 低 | 低 | POC 只导出 100 行；全量时按 productTypeId 分区为多张子决策表并用 DRD 组合 |
| 团队零 Camunda 经验：上手 DMN XML 和 FEEL 语法需额外 0.5 天 | 中 | 低 | POC 聚焦 DMN 决策表（上手最快），使用 Camunda Modeler GUI 编辑，不手写 XML |

### 综合建议

**值得做，但必须分阶段、限定范围。**

"值得"的理由：DMN 化直击 YTS 最大痛点——兼容性规则改代码才能变，业务人员完全被开发团队瓶颈卡住。10,071 行规则、16 张异构表、散布的 Java if-else 分支，是 Camunda DMN 最理想的落地场景。POC 成本仅 2-3 天，切换风险可控（`dmn.enabled=false` 一键回退）。

"分阶段"的含义：
- **Phase 1：仅 DMN（POC）**。先吃最大收益，不碰 BPMN 引擎，不碰前端。选 hot_stamping_compatibility（1,421 行，规则最成熟）做试点，验证 DMN 与 DB 结果一致后扩展到剩余 15 张表
- **Phase 2：审批流程 BPMN 化**。基于 AdminChangeService 现有三阶段模型，构建 BPMN 审批流程，高风险变更走 Camunda Tasklist 多级审批
- **Phase 3：工序匹配引导 BPMN 编排**。在 DMN 兼容性规则全量就绪后，将 6 步引导流程建模为 BPMN User Task 序列，前端 guideStepIndex 状态管理替换为 Tasklist Task 表单

"限定范围"的含义：
- **AI Copilot 不碰**。实时对话与 BPMN 的异步编排模型天然不匹配，意图路由 DMN 化价值有限
- **RBAC 字段级权限不动**。保留现有 @FieldPermission 注解体系，只在审批任务边界做 Camunda Identity 角色映射
- **基本 CRUD / Excel 导入导出不工作流化**。不要过度工程化

**技术路线选择**：推荐 **C7 嵌入式引擎**用于 POC 和 Phase 1（DMN 化）。理由：YTS 当前没有 K8s 基础设施，C8 的 Zeebe + Elasticsearch + Operate + Identity 全套引入架构复杂度陡增。C7 嵌入式引擎一行依赖即可运行，DMN 文件可与 C8 无缝复用。在 C7 2025 年 10 月 EOL 节点前，完成 DMN 核心资产的剥离即可——届时 .dmn 文件可直接部署到 C8 引擎，迁移成本最低。

最终结论：**先 DMN 化，再做 BPMN，AI 和权限不动。分三步走，每步可独立交付、独立回滚。**
