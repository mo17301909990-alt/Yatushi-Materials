# Skill 进化引擎

## 来源
- 项目: OpenClaw Control Center
- 关键文件: `services/skill-evolution-service.js`, `services/skill-suggestion-engine.js`

## 问题
Skill（技能/工具）是写死的。Agent 用了一段时间后，有些 skill 很少用、有些场景没有对应的 skill、有些 skill 的参数设计不合理。手动维护 skill 清单成本高、反应慢。

## 方案
让 skill 根据使用模式自动进化：

### 使用统计
- 每个 skill 记录：调用次数、成功率、平均耗时、用户评分
- 定期生成 skill 热度排行榜

### 技能建议（Suggestion Engine）
- 分析高频问题模式 → 建议新 skill
- 分析低效 skill → 建议合并/废弃
- 分析参数使用分布 → 优化参数设计

### 自动进化（Evolution Service）
- 新 skill 先进入「沙箱」模式（仅特定 Agent 可用）
- 观察一段时间后，效果好的自动发布
- 效果差的自动回滚

## 关键代码片段
```typescript
interface SkillMetrics {
  skillId: string;
  invokeCount: number;
  successRate: number;
  avgLatencyMs: number;
  userRating?: number;
  lastUsed: Date;
}

interface SkillSuggestion {
  type: 'create' | 'merge' | 'deprecate' | 'optimize';
  skillId: string;
  reason: string;
  confidence: number; // 0-1
}
```

## 在我们项目(yatushi)中的应用

**当前适用场景有限**，因为 YTS 还没有独立的 Skill 系统（目前是 hardcode 的 DashScope 调用）。

**可以借鉴的"进化"思维**：
1. **兼容性规则的"热数据"分析**：哪些兼容性规则被频繁命中、哪些规则从未被使用 → 优化规则权重
2. **Copilot 回复分析**：用户的追问/纠错 → 反映 AI 回答的质量问题 → 优化 prompt
3. **操作日志挖掘**：`admin_operation_log` 表分析用户操作模式 → 找出可以自动化的高频操作

**本质**：不是搬代码，而是搬「数据驱动改进」的理念。先做埋点，再做分析，再做自动化。

---

