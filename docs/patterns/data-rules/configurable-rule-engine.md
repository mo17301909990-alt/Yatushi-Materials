# YAML 驱动可配置规则引擎

## 来源
- 项目: ERPAI
- 关键文件:
  - `control-center/src/erpai/services/configurable-rule-engine/types.ts` — 全类型定义
  - `engine.ts` — 核心引擎（事件匹配 + 动作执行）
  - `loader.ts` — YAML 规则加载器
  - `actions/` — 4 种动作实现
  - `template-engine.ts` — 模板表达式引擎

## 问题
业务规则硬编码在代码中，每次规则变更都要：
- 改 Java/Vue 代码 → 提 PR → 走发布流程 → 重新部署
- 业务人员（非技术人员）无法自己配置规则
- 规则逻辑散落在多个 Service 中，难以审计和追溯
- 没有规则版本管理，改错了只能回滚代码

## 方案
将业务规则以 YAML 声明，运行时加载匹配，完全与代码解耦。

**规则结构：**
```yaml
trigger:
  event: SalesOrder.Created          # 触发事件
  filter:                            # 过滤条件
    and:
      - field: totalAmount
        operator: ">"
        value: "10000"
      - field: customerTier
        operator: in
        value: ["gold", "platinum"]
  schedule:                          # 调度约束
    delay: "5m"
    maxRetries: 3
action:
  type: create_todo | notify | publish_event | update_field
  config:
    assignee: "{order.salesRep}"
    priority: high
```

**核心引擎设计：**
- 过滤器支持嵌套 AND/OR/NOT，11 种运算符
- 内建熔断器：连续 3 次失败自动断开，60 秒后恢复
- 内建限流：按窗口/次数，支持全局或 per-entity 维度
- 错误处理：支持重试 + 降级动作
- 模板表达式引擎：`{order.salesRep}` 格式运行时求值
- 所有类型经过 Zod 校验，保证运行时类型安全
- 每条规则执行结果记录 observation，支持可观测性

## 在我们项目(yatushi)中的应用
YTS 有很多"规则类"业务逻辑，目前散落在 Service 层：
- **物料兼容性匹配规则** — 什么材料可以和什么材料搭配
- **工序价格计算规则** — 不同规格、数量的价格公式
- **权限校验规则** — 什么角色可以访问什么数据

这些规则可以逐步提取为 YAML 配置：
1. 从**兼容性匹配**开始试点 — 当前 `CompatibilityQueryService` 中的匹配逻辑，可以提取为 YAML 规则，运行时加载
2. 用数据库表存储规则（而非文件），方便通过管理界面修改
3. 规则变更记录 `admin_operation_log` 自动记录，满足审计需求
4. 在 Java 端实现一个轻量级规则引擎（可以用 `@Conditional` 注解 + `application-rule.yml`，不需要完全照搬 ERPAI 的实现）

这个模式对 YTS 最直接的价值是：**非技术业务人员可以在管理后台配置兼容性规则，不需要开发改代码**。

---

