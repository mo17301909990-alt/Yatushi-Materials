# 知识飞轮与错误沉淀管道

## 来源
- 项目: OpenClaw + ERPAI（合并对比）
- 关键文件:
  - OpenClaw: CLAUDE.md 错误记录区块 + `flywheel-manifest.json` + `scripts/error-patterns.ts`
  - ERPAI: memory/ 目录 + MEMORY.md + E:\shared\signals\ + E:\shared\patterns\

## 问题
在 AI 辅助开发中，知识沉淀面临两个核心矛盾：
- 已修复的错误反复出现，知识留在个人脑中不沉淀。"这个 bug 我修过两次"是常态
- Session 结束时的知识丢失 — 关掉终端，关键决策也就忘了
- 沉淀机制如果太繁琐（要求写文档），没人愿意执行

## 方案
两套项目各有侧重，互为补充：

### OpenClaw 飞轮（自动捕获 + 自动晋升）
```
Stop Hook 捕获错误
    → 飞轮 pipeline 匹配已知模式
    → 新模式自动记录到 flywheel-manifest.json
    → 晋升到 agent Common Pitfalls 文档
```
- **触发条件**：Stop Hook 检测到编译/类型错误
- **匹配工具**：`scripts/error-patterns.ts` match/record 两条命令
- **晋升规则**：相同模式出现 3 次 → 自动升级到 Gotchas
- **每条记录**：现象、根因、修复、出现次数、相关 Agent

### ERPAI 知识飞轮（手动记录 + 分层升级）
```
memory/YYYY-MM-DD.md → 同类 3 次 → MEMORY.md → 关键发现 → CLAUDE.md
                                                                ↓
                                           E:\shared\signals\ + E:\shared\patterns\
```
- **触发条件**：排查修复完成 → memory + observation
- **晋升规则**：同类问题 3 次 → 升级到 Gotchas；重大架构决策 → docs/adr/
- **额外机制**：observation 带 importance 评分（1-10），>=7 自动进入 CC Pipeline

### 对比总结
| 维度 | OpenClaw 飞轮 | ERPAI 飞轮 |
|------|---------------|------------|
| 触发方式 | 自动 Hook 捕获 | 手动记录 + 强制沉淀 |
| 优势 | 不依赖自觉性，覆盖面广 | 深度好，包含架构决策 |
| 劣势 | 依赖工具链，仅覆盖技术错误 | 依赖人的纪律性 |
| 共同点 | 三层晋升、事件/重要性评分、跨 session 共享 |

## 在我们项目(yatushi)中的应用
YTS 当前没有错误沉淀机制。可以直接建立轻量版：

**第一阶段（本周可做）：**
1. 在项目根目录创建 `flywheel/` 目录，包含：
   - `known-errors.yaml` — 已知错误模式（错误信息、根因、修复方案、出现次数）
   - `architecture-decisions/` — 重大架构决策记录（轻量版 ADR）
2. 在 CLAUDE.md 中加入沉淀规则："排查修复完成必须记录到 flywheel/known-errors.yaml"

**第二阶段（自动化）：**
1. 写一个简单的 Stop Hook 脚本，当 Maven 编译或 Vue 构建报错时，自动 match 已知模式
2. 匹配到已知模式 → 直接显示修复建议
3. 匹配不到 → 提示"这是一个新的错误模式，是否记录？"

**第三阶段（跨 session）：**
1. 错误模式积累到一定数量后，自动汇总到 `docs/gotchas.md`
2. 每次 SessionStart 时自动展示上次 session 记录的新模式

对于 YTS 最实用的场景：**兼容性匹配规则的常见错误模式**（数据缺失、类型不匹配、规则冲突）优先沉淀。

---

