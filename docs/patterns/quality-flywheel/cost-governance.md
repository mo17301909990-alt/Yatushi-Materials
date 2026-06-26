# 成本治理体系

## 来源
- 项目: OpenClaw Control Center
- 关键文件:
  - `services/cost-alert-service.js`
  - `services/cost-circuit-breaker-service.js`
  - `services/cost-forecast-service.js`
  - `services/budget-governance.js`
  - `services/budget-policy.js`
  - `services/context-pressure-monitor.js`

## 问题
AI Agent 调用 LLM API 是按 token 计费的，不加控制的话一个失控的循环就能烧掉几百美元。传统监控只关注「是否挂了」，不关注「花了多少钱」。

## 方案
三层递进式成本治理：

### 1. 成本告警（Cost Alert）
- 按服务/Agent 维度统计 token 消耗
- 超过阈值发通知（飞书/邮件）
- 日/周/月趋势分析

### 2. 预算策略（Budget Policy）
- 每个 Session/Agent 有预算上限
- 接近上限时降级服务（不用最强模型，用便宜模型）
- 超过上限时熔断（Circuit Breaker）

### 3. 上下文压力监控（Context Pressure）
- 监控每个 Session 的 token 使用量
- 接近上下文窗口极限时提前告警
- 自动触发上下文压缩（Summarization）

## 关键代码片段
```json
{
  "budget": {
    "defaultMaxTokens": 100000,
    "hardLimit": 500000,
    "alertAtPercent": 80,
    "circuitBreakAtPercent": 95,
    "downgradeModelAtPercent": 90
  }
}
```

## 在我们项目(yatushi)中的应用

**当前**：YTS 只用 DashScope 做 AI 匹配，调用量不大。但 Copilot 功能上线后、AgentProcess 和 AgentChat 页面投入使用后，API 成本会成为问题。

**落地步骤**：
1. 在 `AiController` 加一个请求计数器，统计每次调用消耗的 token（DashScope 响应里有 `usage` 字段）
2. 把计数存到 `ai_recommendation_history` 表
3. 每天跑一个定时任务统计 token 消耗，超过配置阈值发飞书通知
4. 后续：在管理后台加"AI 成本看板"页面

**不用照搬全套**，前三步半天就能落地，足以覆盖 80% 的成本风险。

---

