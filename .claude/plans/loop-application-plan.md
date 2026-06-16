# 印刷厂项目 Loop 体系应用方案

> 借鉴 OpenClaw Control Center 的回路体系（LOOP_MASTER.md），为印刷厂管理系统（YTS+JYY）设计适用的质量保证和数据回馈闭环。

---

## 一、印刷厂项目现状（Loop 成熟度评估）

| 维度 | 当前状态 | 等效成熟度 |
|------|---------|-----------|
| **构建检查** | 每10分钟 mvn compile + npm run build 自动跑 | L2（可观测） |
| **测试** | mvn test 有但无人常态化监控 | L1（手动） |
| **数据质量** | 无自动化数据校验 | L0（无回路） |
| **BOM兼容性** | AI匹配无反馈回路 | L1（单次执行） |
| **权限安全** | 一次性配置，无定期审计 | L0（无回路） |
| **部署** | 无CI/CD | L0（无回路） |

### 差距分析

```
CC 底座（对比参考）        印刷厂项目
─────────────────────────────
6条回路（L3-L4）       →  0条完整回路
Loop Registry          →  无
飞轮健康检查           →  只有基础cron编译检查
Stop Hook自动匹配      →  无
error-patterns学习     →  无
```

---

## 二、建议引入的回路（5条）

### Loop 1: 开发质量回路（Dev Quality Loop）

**目标**：零编译错误 + 零测试失败

```
触发: cron 每30分钟 + event: git push
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
① Observe   → 监听编译/测试结果
② Collect   → 记录 error-patterns（可复用CC的error-patterns.ts脚本）
③ Analyze   → 归类失败类型：编译/测试/运行时
④ Decide    → 连续3次失败 → 发出告警
⑤ Execute   → 自动重试/通知
⑥ Feedback  → auto-check.log 追加
⑦ Correct   → 失败模式入库，下次预判
```

**状态**：✅ 已部分实现（cron健康检查），需增强 error-patterns 记录

### Loop 2: 数据质量回路（Data Quality Loop）

**目标**：物料/BOM数据零冲突零冗余

```
触发: cron 每天凌晨 + manual
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
① Observe   → 扫描 material_process_compatibility (10,071行)
               hot_stamping_compatibility (1,421行) 等核心表
② Collect   → 收集重复记录、冲突规则、空值字段
③ Analyze   → 按模块分组：烫金/裱纸/硅胶/UV油
④ Decide    → 严重度分级：P0(数据冲突) / P1(冗余) / P2(格式)
⑤ Execute   → 自动清洗 / 生成审计报告
⑥ Feedback  → 写入 data-quality.log
⑦ Correct   → 规则库更新，下次跳过已确认项
```

**业务价值**：`material_process_compatibility` 表有 10,071 行数据，重复/冲突规则会直接导致 AI 匹配结果错误

### Loop 3: AI 匹配反馈回路（AI Match Feedback Loop）

**目标**：AI自然语言→BOM匹配准确率 > 85%

```
触发: event: 每次AI匹配请求
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
① Observe   → 捕获 AiController.parseAndMatch() 输入输出
② Collect   → 用户是否接受了匹配结果？修改了什么？
③ Analyze   → 计算准确率趋势（按物料类型分类）
④ Decide    → 某类准确率 < 70% → 标记为"需训练"
⑤ Execute   → 收集失败案例，用于 prompt 优化
⑥ Feedback  → DashScope AI 的 knowledge base 增量更新
⑦ Correct   → PARSE_SYSTEM_PROMPT 自动添加新规则
```

**业务价值**：现有 AiChatOrchestratorService 的 BASE_RULES（50行）靠手动维护，应有反馈回路让它自我改进

### Loop 4: 兼容性规则审计回路（Compatibility Rule Audit Loop）

**目标**：兼容性规则配置一致性 100%

```
触发: cron 每周一
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
① Observe   → 扫描所有 *_compatibility 表
② Collect   → 规则分布（V/X/▷比例）、孤立规则、未使用规则
③ Analyze   → 每类工艺（烫金/裱纸/硅胶/UV油）单独统计
④ Decide    → 孤立规则>5% → 通知管理员
⑤ Execute   → 生成审计报告（Excel导出）
⑥ Feedback  → 写入 compatibility-audit.log
⑦ Correct   → 标记过期规则待人工确认
```

### Loop 5: 权限安全审计回路（Permission Security Loop）

**目标**：零未授权访问 + 零幽灵账号

```
触发: cron 每天 + event: 角色变更
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
① Observe   → 扫描 admin_operation_log (1,480行)
② Collect   → 异常登录尝试、越权API调用
③ Analyze   → 按用户/角色/时间分类
④ Decide    → 3次失败 → 标记可疑
⑤ Execute   → 通知管理员
⑥ Feedback  → 更新权限黑名单
```

---

## 三、优先级实施路线

| 阶段 | 回路 | 预计工作量 | 业务价值 | 依赖 |
|------|------|-----------|---------|------|
| **P0** | 开发质量回路（增强现有cron） | 2天 | 中 | 已有健康检查 |
| **P0** | 数据质量回路 | 3天 | 高 | 数据库访问 |
| **P1** | AI匹配反馈回路 | 5天 | 高 | AI服务运行中 |
| **P1** | 兼容性规则审计回路 | 2天 | 中 | P0数据质量 |
| **P2** | 权限安全审计回路 | 1天 | 低 | - |

---

## 四、技术实现要点

### 4.1 轻量级脚本框架（不引入新语言）

印刷厂项目是 Java + Vue，不适合跑 CC 的 TypeScript 脚本。
建议用 **Shell 脚本 + Java 内置工具** 实现 loop 逻辑：

```bash
# 示例：data-quality-loop.sh
#!/bin/bash
# 数据质量回路入口

LOG_FILE="/f/YTS+JYY/.claude/loops/data-quality.log"
touch "$LOG_FILE"

# ① Observe & Collect: SQL查询异常数据
psql -h localhost -U postgres -d gold_foil_db <<EOF > /tmp/dq_report.txt
  -- 重复规则检测
  SELECT '重复', count(*) FROM (
    SELECT product_id, count(*) FROM material_process_compatibility 
    GROUP BY product_id HAVING count(*) > 1
  ) dup;
  
  -- 孤立产品检测（有兼容性但无对应product）
  SELECT '孤立', count(*) FROM material_process_compatibility m
  LEFT JOIN products p ON m.product_id = p.id
  WHERE p.id IS NULL;
EOF

# ④ Decide: 分析报告
if grep -q "重复|0" /tmp/dq_report.txt && grep -q "孤立|0"; then
  echo "【数据质量通过】$(date)" >> "$LOG_FILE"
else
  echo "【数据质量问题】$(date)" >> "$LOG_FILE"
  cat /tmp/dq_report.txt >> "$LOG_FILE"
fi
```

### 4.2 Loop Contract 模板

每条回路用一个 JSON 定义（借鉴 LOOP_TEMPLATE.md）：

```json
{
  "id": "dev-quality",
  "name": "开发质量回路",
  "maturity": "L2",
  "target": "零编译错误",
  "trigger": "cron 30min",
  "observe": "mvn compile + npm run build",
  "log": ".claude/loops/dev-quality.log",
  "escalation": "连续3次失败 → 通知"
}
```

### 4.3 目录结构

```
.claude/loops/
├── dev-quality.log          # 开发质量日志
├── data-quality.log         # 数据质量日志  
├── ai-match.log             # AI匹配反馈日志
├── compatibility-audit.log  # 兼容性审计日志
├── permission-security.log  # 权限安全日志
└── loop-registry.json       # 回路注册表（所有回路定义）
```

---

## 五、下一步推荐

1. **先强化已有的开发质量回路** — 把 auto-check.log 改造成 error-patterns 格式，失败自动分类
2. **搭数据质量回路** — 这是业务价值最高的，10,071行兼容性数据需要自动化审计
3. **再看 AI 和权限** — 等前两个跑稳了再扩展

要不要安排推进？
