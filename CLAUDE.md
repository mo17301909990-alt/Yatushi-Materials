# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概览

印刷厂管理系统 — 烫金/裱纸/丝印/过胶/LED UV 等工序的 BOM 构建、物料兼容性匹配、权限管理。

- **后端** `yts_project/`：Java 17 + Spring Boot 3.3.10 + MyBatis + PostgreSQL
- **前端** `yts_project_vueai/`：Vue 3 + TypeScript + Vite + Element Plus + Pinia + Tailwind CSS

## 构建 & 运行

### 后端
```bash
cd yts_project
./mvnw spring-boot:run                  # 本地启动 (port 8093)
mvn clean package -DskipTests           # 打包 JAR
mvn test                                # 跑全部测试
mvn test -Dtest=TestClass#method        # 跑单个测试
```

### 前端
```bash
cd yts_project_vueai
npm install                             # 装依赖
npm run dev                             # 开发服务器 (含代理)
npm run build                           # 生产构建
npm run preview                         # 预览构建产物
```

## 本地数据库（已恢复）

数据来自备份文件 `gold_foil_db_2026-05-15_20-56-28_pgsql_data.sql`（3.8MB，103 条 COPY 语句）。
恢复时间：2026-06-07，89 张表共 31,380 行数据。

```
数据库: gold_foil_db
主机:   localhost:5432
用户:   postgres
密码:   HryENprJrxThYSDz
驱动:   PostgreSQL 16
```

连接示例：
```bash
PGPASSWORD=HryENprJrxThYSDz psql -h localhost -U postgres -d gold_foil_db
```

**前 10 大表（按行数）：**
| 表 | 行数 |
|----|------|
| material_process_compatibility | 10,071 |
| admin_operation_log | 1,480 |
| hot_stamping_compatibility | 1,421 |
| specifications | 1,147 |
| products | 1,147 |
| pricing | 1,108 |
| leo_gp_numbers | 1,098 |
| products_snapshot | 1,074 |
| pricing_snapshot | 1,060 |
| gold_foil_type / old_* 系列 | ~1,017 每表 |

## 数据库配置（后端）

| 环境 | 地址 | 说明 |
|------|------|------|
| `application.properties` | 101.126.27.148:5432 | 生产（当前不可达） |
| `application-dev.properties` | 10.2.2.232:5432 | 开发（当前不可达） |
| 本地（已恢复） | localhost:5432 | 当前可用 |

### 前端
- `.env.development` / `.env.production` — API 端点
- `vite.config.ts` — 开发代理指向 localhost:8093

## 架构

### 后端分层
```
controller/ → service/ → mapper/ → MyBatis XML → PostgreSQL
                                        ↑
                                  config/ (安全/权限/CORS)
                                        ↑
                                  aspect/ (AOP 操作日志)
```
- **controller/**: 30+ REST 控制器，请求校验、响应格式化
- **service/**: 业务逻辑层，事务管理；含 `DashScopeChatService`（阿里通义千问 AI 对接）
- **mapper/**: MyBatis 接口 + `resources/mapper/` XML 映射
- **config/**: Spring Security + Kerberos 认证、CORS、RBAC 引导、字段级权限解析
- **aspect/**: `AdminOperationLogAspect` — 通过注解拦截操作记录日志

### 核心业务模块（按 Controller 划分）
| 模块 | 控制器 |
|------|--------|
| 烫金 | `HotStamping*Controller` (compatibility, pattern, type) |
| 裱纸/过胶 | `LaminatingController`, `ClothPaper*Controller` |
| 后道工艺 | `PostProcessingPrintController`, `PostProcessingPrintUvController`, `PostProcessingLeduvglitterController` |
| 产品管理 | `ProductsController`, `ProductController`, `ProductImportController` |
| 权限 | `PermissionController` (RBAC CRUD) |
| AI | `AiController` → DashScopeChatService |
| 物料 | `ResourceGroupSelectController`, `MaterialRuleStatisticsController` |
| 公告/字典 | `NoticeDictionaryController`, `BatchNoticeUpdateController` |

### 前端结构
```
api/modules/ → views/ → components/
stores/ (Pinia) → router/ → App.vue
```
- **api/modules/**: Axios 封装的后端 API 调用
- **views/**: 页面级组件（admin/, auth/, matchPreference/, guide/, message/）
- **stores/**: Pinia 状态管理
- **components/**: 通用 UI 组件
- **directives/**: 自定义指令

## Git 分支
- `saojinzhi`（当前） — 烧金纸相关开发
- `zj105` — 主分支/基线
- `liandonggengxing` — 联动更新功能
- `zhuyishixiang` — 注意事项/修复
- `雅图仕权限版` — 客户定制权限版本

远程仓库：`https://gitee.com/peng-yuyan-3/yst.git`

## 文档（中文）
根目录下大量中文 Markdown 文档：
- 系统架构图.md / 系统设计思路文档.md / 项目交接文档-完整版.md
- 操作手册.md / 操作手册_用户版.md
- PERMISSION_SYSTEM_GUIDE.md / DEPLOYMENT_STEPS.md

## Done Criteria

- [ ] 后端改动 → `mvn compile` 通过
- [ ] 逻辑变更 → 对应测试通过（`mvn test -Dtest=TestClass#method`）
- [ ] 前端改动 → `npm run build` 通过（TypeScript 无错误）
- [ ] API route 变更 → 对应 Zod/参数校验 + 统一返回格式 `R<T>`
- [ ] 数据库变更 → 对应迁移 SQL + 回滚 SQL
- [ ] 敏感信息（密码/key）不硬编码 → 用环境变量或配置文件

## 项目路由

| 任务 | 调用 |
|------|------|
| 查信息/跑命令 | 我直接干 |
| 代码改动 | `build` / `dispatch` |
| 审计/代码审查 | `grill` / `global-code-reviewer` |
| Bug 排查 | `investigate` |
| 端到端验证 | `qa` |
| 构建 | `build` |
| 提交推送 | `commit-push-pr` |
| 错误匹配 | `error-pattern` |

## 注意事项
- **POI/EasyExcel 版本敏感**：pom.xml 中 POI 锁定 5.2.3 + xmlbeans 5.1.1，exclude poi-ooxml-lite 和 poi-ooxml-schemas，依赖冲突会抛 NoSuchFieldError: Factory
- **数据库脚本**：根目录 `*fix*.sql` / `*optimization*.sql` / `database_scripts/` 下大量历史迁移和修复脚本
- 根目录有 `hs_err_pid*.log` 和 `replay_pid*.log`（JVM crash 日志），可清理
- 数据备份文件：`gold_foil_db_2026-05-15_20-56-28_pgsql_data.sql/` 目录下
