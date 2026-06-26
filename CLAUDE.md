# 印刷厂物料匹配系统 — Yatushi Materials

> 叫我 **CC**。印刷厂管理系统 AI 搭档。
> 当前成熟度：**L1.5**（烫金匹配线上运行 → 全品类 ETL + AI Agent）

---

## 一、项目身份

印刷厂管理系统，覆盖烫金/裱纸/丝印/过胶/LED UV 等工序的 BOM 构建、物料兼容性匹配、权限管理。

- **后端** `yts_project/`：Java 17 + Spring Boot 3.3.10 + MyBatis + PostgreSQL
- **前端** `yts_project_vueai/`：Vue 3 + TypeScript + Vite + Element Plus + Pinia + Tailwind CSS
- **AI**：阿里 DashScope（AiChatOrchestrator 3 层渐进上下文 + IntentDetector + SkillRouter）
- **远程**：`git@github.com:mo17301909990-alt/Yatushi-Materials.git`

---

## 二、架构原则

1. **DB 是唯一真相** — 兼容性规则、价格、配置都从数据库读，不硬编码业务规则到 Java 代码
2. **AI 是翻译层，不是决策层** — 大模型只做意图识别+参数解析，匹配逻辑走规则表，不靠 AI 编造
3. **ETL 先于匹配** — 原始资料必须先通过 ETL Pipeline 入库，才能被匹配系统使用
4. **写操作必须有人审** — Agent 提变更预览 → 人工确认 → 执行 → 审计，不跳步
5. **Schema-First 数据定义** — 新增数据表先写 `create_*.sql` → 再写 Mapper XML → 再写 Service
6. **声明式规则优先** — 兼容性匹配规则逐步从 SQL JOIN + Java 条件分支迁移到 YAML 声明式配置（configurable-rule-engine），让业务人员可配置，不改代码
7. **Service 拆分原则** — Service 超 300 行必须拆。Excel 处理逻辑不混入业务 Service，统一归入 `util/excel` 包

---

## 三、红线（不可触碰）

| # | 红线 | 后果 |
|---|------|------|
| 1 | 改生产数据库（101.126.27.148） | 数据灾难 |
| 2 | 改 DB 不写迁移 SQL | 团队不同步 |
| 3 | force push 到 main | 历史丢失 |
| 4 | 密码/Key 硬编码到代码里（含 `:-fallback` 默认值） | 安全泄露 |
| 5 | 用 `@Autowired` 字段注入 | 违反项目规范 |
| 6 | Controller 层写 try-catch 业务逻辑 | 全局异常处理器接管 |
| 7 | 用户说"同意"就直接写代码 | 必须走 `dispatch` |
| 8 | 跳过 Done Criteria 强检查就提交 | 质量门禁失效 |

---

## 四、构建 & 运行

```bash
# 后端
cd yts_project && ./mvnw spring-boot:run          # 本地启动 (port 8092)
mvn clean package -DskipTests                      # 打包 JAR
mvn test                                           # 跑全部测试

# 前端
cd yts_project_vueai && npm install                # 装依赖
npm run dev                                        # 开发服务器 (含代理)
npm run build                                      # 生产构建

# ETL 数据管道
cd database_scripts && python p0_etl.py             # 全量导入 G 盘资料
python p0_etl.py --validate                        # 校验表行数
```

### 本地数据库
```
gold_foil_db | localhost:5432 | postgres | HryENprJrxThYSDz | PostgreSQL 16
```
生产/开发环境当前不可达，仅本地可用。

---

## 五、完成标准（Done Criteria + 强检查）

| 改了啥 | 必须过的检查 |
|--------|------------|
| 后端 Java | `mvn compile` 静默通过 |
| 逻辑变更 | 对应测试通过（`mvn test -Dtest=TestClass#method`） |
| 前端代码 | `npm run build` 通过（TypeScript 无错误） |
| API 路由 | 统一返回 `R<T>` 格式 |
| 数据库表 | 对应建表 SQL + 回滚 SQL |
| 敏感信息 | 环境变量配置，不用 `:-fallback` 明文 |
| XML Mapper | `mvn compile` 通过（无 `BuilderException`） |
| Vite 代理 | 改 `vite.config.ts` 后同步检查所有 Controller `/api` 前缀 |

**强检查规则**：每次 dispatch 完成后，必须运行对应改动的验证命令并看到通过输出。不允许"确认已跑过"口头承诺。命令写在 dispatch 的 verify 步骤中。

---

## 六、改前查 / 改后验

### 改前
```
□ 查 gotchas/ 有没有类似踩坑记录（grep -r "关键字" .claude/gotchas/）
□ 查 CLAUDE.md 近期 Gotchas 区
□ 读涉及文件的完整内容（禁止半猜半写）
□ 确认当前分支正确（不在 main 上直接改）
```

### 改后
```
后端:  □ mvn compile 通过  □ 有逻辑变更 → 对应测试  □ 改了 API → curl 测通
前端:  □ npm run build 通过  □ 组件/页面改动 → 浏览器看效果
DB:    □ 迁移 SQL + 回滚 SQL 都有  □ 已有数据不受影响
规则:  □ YAML 规则改完 → 用兼容性测试套验证  □ 规则版本号递增
拆分:  □ 新 Service 不超过 300 行  □ Excel 逻辑不进业务 Service
```

---

## 七、调度规则（Agent 路由）

| 任务 | 路由 | 说明 |
|------|------|------|
| 查信息/跑命令/读代码 | **我直接干** | 一行命令解决的不废话 |
| 烫金匹配规则 | `烫金匹配专员` | 兼容性规则 CRUD |
| 丝印/UV/后加工数据 | `丝印UV专员` | 硅胶/UV/过胶数据 ETL 与管理 |
| ETL Pipeline | `etl-data-pipeline` | 建表→导入→校验→质量报告 |
| 权限/用户 | `权限管理` | 开户/授权/审计/角色 |
| Java 后端代码 | `java-backend` | Controller/Service/Mapper/XML |
| Vue 前端页面 | `vue-frontend` | 页面/组件/Store/路由 |
| 兼容性规则引擎 | `rule-engine` | YAML 规则配置/版本管理/Guava Cache |
| Service 拆分重构 | `service-decomposer` | 超大 Service 拆分/Excel 逻辑抽离 |
| 质量门禁配置 | `quality-gate` | Hook 配置/Done Criteria 强检查/测试覆盖 |
| 代码审查 | `code-review` | 编译→类型检查→逻辑审查 |
| Bug 排查 | `investigate` | 定位→复现→修复→记录 gotcha |
| 安全审计 | `guard` | Controller 权限扫描/密码硬编码检查 |
| 提交推送 | `commit-push-pr` | 分支→commit→push→PR |

**Fallback**：路由表无匹配 → 自动进入 `review` 流程，审核后再定方案。

---

## 八、知识飞轮（青铜→白银→黄金）

```
踩坑/修 Bug
  ↓ 记录到 .claude/gotchas/YYYY-MM-DD-简述.md      ← 青铜级（单次记录）
  ↓ 同类问题出现 3 次
  ↓ 提炼为 .claude/ai-knowledge/ 领域知识文档          ← 白银级（可复用知识）
  ↓ 经过 3 次验证确认有效
  ↓ 固化到规则引擎配置或 Service 逻辑中                 ← 黄金级（不需人记）
```

每次 dispatch 完成后，由 CC 判断当前修复是否触发升级条件。

---

## 九、项目管线

### ETL 管线：`phase0(建表) → phase1(导入) → data-quality(校验) → export(报表)`
### 飞轮回路：`ai-match-feedback` / `compatibility-audit` / `data-quality` / `permission-security`
### 健康检查：`dev-quality-check` 每 6h（`mvn compile + npm run build`），失败跑 `record-error.sh`

---

## 十、近期 Gotchas

| 日期 | 问题 | 根因 | 预防 |
|------|------|------|------|
| 2026-06-18 | Vite 代理 rewrite 被误删 + Controller 路径不统一 | `vite.config.ts` rewrite 被删 + `UserController` 缺 `/api` 前缀 | 改 proxy 必须同步检查全量 Controller 路径 |
| 2026-06-18 | AdminOperationLogMapper 残留导致启动失败 | `target/` 残留旧 Mapper XML | 启动前跑 `mvn clean` |
| 2026-06-18 | `<batchInsert>` 非法 XML 元素 | MyBatis XML 误用非标准标签 | 新 Mapper XML 确认只用标准元素 |

---

## 十一、注意事项

- **POI/EasyExcel 版本敏感**：POI 5.2.3 + xmlbeans 5.1.1，依赖冲突抛 `NoSuchFieldError: Factory`
- **启动必须设 DASHSCOPE_API_KEY**：`DASHSCOPE_API_KEY=xxx mvn spring-boot:run`
- **Stop Hook 路径**：已指向本项目根目录（`settings.json`），勿改
- **密码无 fallback**：`application.properties` 不应含 `:-` 默认值，环境变量缺失直接报错退出
- **根目录大量中文文档**（架构图、操作手册、交接文档），开发前先看
