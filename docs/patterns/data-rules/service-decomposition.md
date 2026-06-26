# 服务分解模式（微服务即文件）

## 来源
- 项目: OpenClaw Control Center
- 关键文件: `control-center/src/services/`（~70 个文件，每个文件一个服务）

## 问题
传统 Spring Boot 项目的 Service 层往往是「大单体 Service」：一个 `XxxService.java` 几百上千行，各种职责耦合在一起。改一个功能要翻半天的文件，测试也难写。

## 方案
**每个文件一个职责**。不是按「模块」拆分，而是按「职责」拆分：

```
services/
├── cost-alert-service.js        # 只做成本告警
├── cost-circuit-breaker-service.js  # 只做成本熔断
├── cost-forecast-service.js      # 只做成本预测
├── budget-governance.js          # 只做预算管控
├── budget-policy.js              # 只做预算策略
├── hall-session-service.js       # 只做协作会话管理
├── hall-message-bus.js           # 只做消息路由
├── agent-volunteer-service.js    # 只做任务认领匹配
├── review-assignment-service.js  # 只做审查调度
├── correction-layer-service.js   # 只做错误修正
├── correction-layer-audit.js     # 只做修正审计
├── ...
```

每个文件的职责边界：
- **文件名就是职责声明**：看文件名就知道这个文件管什么
- **自包含**：一个文件通常 100-300 行，不需要跨文件跳转就能理解
- **单一依赖方向**：只依赖 repositories/ 和 interfaces/，不循环依赖

## 优点
- 可读性极高：打开一个文件就能理解一个完整功能
- 可测试性极好：每个服务只需要 mock 2-3 个依赖
- 并行开发：多人同时改不同服务，零冲突
- 按需加载：不需要用的服务根本不会加载

## 在我们项目(yatushi)中的应用

**现状**：YTS 的后端 Service 层平均每个 300-600 行，有些超过 800 行。

**渐进改进**：
1. 新功能直接按职责拆小文件，不往老 Service 里堆
2. 比如当前的 `CompatibilityQueryService` — 如果后续加规则校验、缓存、统计等逻辑，拆成 `compatibility-query-service.java`、`compatibility-validator.java`、`compatibility-cache.java`
3. 对于已经很大的 Service，每次改的时候顺手拆一个职责出去
4. **目标**：每个 Service 不超过 200 行

---

