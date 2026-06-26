# 安全围栏模式

## 来源
- 项目: OpenClaw Control Center
- 关键文件:
  - `services/approval-workflow-service.js` — 审批工作流
  - `services/approval-action-service.js` — 审批动作
  - `services/risk-assessment-service.js` — 风险评估
  - `services/guard-service.js` — 操作门控
  - `settings.json` — READONLY_MODE 配置

## 问题
Agent 有执行能力但缺乏判断能力 — 一个 delete 操作不应该由 AI 自己决定。需要多层安全机制防止误操作和数据丢失。

## 方案
四层安全围栏，由浅到深：

### 1. READONLY_MODE（配置层）
```json
{
  "readOnly": true,
  "allowedMutations": ["/api/read/**"]
}
```
默认：所有写操作关闭。改配置才能打开。

### 2. 操作门控（中间件层）
- 每个写操作（create/update/delete）必须通过 Guard Service 校验
- Guard 检查：操作者身份、资源类型、操作时间、频率限制
- 高风险操作（delete、批量 update）需要额外审批

### 3. 审批工作流（人工层）
```
提交变更 → 风险分级 → 通知审批人 → 审批/驳回 → 执行/取消
```
- 低风险：自动通过（但记录日志）
- 中风险：一个人审批
- 高风险：两个人审批 + 24 小时冷静期

### 4. 审计追踪（追溯层）
- 所有操作记录到 `admin_operation_log`
- 风险摘要以自然语言呈现给操作者
- 支持操作回滚（snapshot-based）

## 关键实现
```typescript
interface RiskAssessment {
  level: 'low' | 'medium' | 'high' | 'critical';
  score: number;
  reasons: string[];
  requiredApprovals: number;
  suggestedActions: string[];
}
```

## 在我们项目(yatushi)中的应用

**YTS 已有的基础**：已经有 `AdminOperationLogAspect` 做操作日志、`admin_change_records/snapshots` 做变更记录。权限系统也有 RBAC。

**可以加强的**：
1. **高风险操作确认**：删除兼容性规则、批量修改价格等操作，前端弹确认对话框 + 后端二次确认
2. **风险分级**：给操作类型打标签（低/中/高），高风险操作自动发飞书通知
3. **变更预览**：改兼容性规则前，先展示"修改前 vs 修改后"的对比，人工确认再执行
4. **快照回滚**：利用已有的 `admin_change_snapshots` 表，做个"一键回滚"功能

**不需要的**：READONLY_MODE 对 YTS 太重了，审批工作流等用户量大再考虑。

---

