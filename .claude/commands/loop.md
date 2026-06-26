---
name: loop
description: "YTS 前端检测 Loop — 波次检测 + 失败累计 + 趋势追踪"
---

# /loop

YTS 前端检测 Loop。基于 ERPAI / 营销 OS 的 DAG 波次模式。

## 用法

- `/loop` — 全量检测（4 波次，~2min）
- `/loop quick` — 快速模式（仅编译+静态分析+测试）
- `/loop step:<name>` — 仅运行指定步骤

## 波次

```
Wave 0: 编译
  ├── 0a. Backend compile   (mvn compile -q)
  └── 0b. Frontend build    (npm run build)

Wave 1: 静态分析
  ├── 1a. Route integrity   (smoke-routes.ts)
  └── 1b. Type check        (vue-tsc --noEmit)

Wave 2: 测试
  ├── 2a. Test suite        (vitest run)
  ├── 2b. False-green check (check-false-green.ts)
  └── 2c. Coverage          (vitest --coverage)

Wave 3: 质量门禁
  └── 3a. Quality gate      (quality-gate.sh)
```

## 执行

```bash
bash scripts/detection-loop.sh         # 全量
bash scripts/detection-loop.sh --quick  # 快速
bash scripts/detection-loop.sh --step=tests  # 单步
```

## 检测项明细

| 步骤 | 检查内容 | 失败影响 |
|------|---------|---------|
| 0a | Maven 编译 | 后端代码有语法错误 |
| 0b | Vite 构建 | 前端代码或类型错误 |
| 1a | 68 路由文件存在性 | 页面文件缺失或导航死链 |
| 1b | vue-tsc 类型 | 预存 5 个警告 |
| 2a | 252 测试全绿 | 功能回归 |
| 2b | 0 假绿 | 弱断言新增 |
| 2c | 覆盖率阈值 | statements≥20% / functions≥15% |
| 3a | quality-gate 5 步 | 综合门禁 |

## 调度

系统已有 cron 任务（`scheduled_tasks.json`）：
- 每 6 小时: `bash scripts/quality-gate.sh`
- 每周一 9am: `bash scripts/weekly-audit.sh`

## 失败处理

- 非阻断步骤仅告警
- 同一步骤连续失败 3 次 → 记录 error-patterns
- 全量检测完成后写报告到 `.claude/state/detection-loop-last.json`
