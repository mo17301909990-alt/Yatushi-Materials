# Repository 三层适配模式

## 来源
- 项目: ERPAI
- 关键文件:
  - `control-center/src/erpai/repositories/` — 所有 Repository 实现
  - `.claude/rules/repositories.md` — 规范约束

## 问题
同一个数据访问接口，面对不同复杂度的实体时，实现策略截然不同：
- **高频核心实体**（PO/SO/WO）：需要最高性能的 CRUD，适配器要轻量
- **低频辅助实体**：手动写全套 CRUD 太浪费，代码高度重复
- **复杂查询**：跨表 join、聚合报表，通用的 CRUD 不够用

传统做法要么全部手写（大量重复），要么一个 ORM 方案打天下（复杂查询性能差或难以表达）。

## 方案
同一套 Repository 接口，按实体复杂度分三级实现，调用方无感知：
```
高频实体（PO/SO/WO）   → createTypedRepoAdapter(Entity) → 元数据引擎 CRUD
低频辅助实体           → createGeneratedRepoAdapter(metadata) → 元数据引擎 CRUD
复杂查询/报表          → 手写 SurrealDB repo + registry.ts 注册
```

**设计要点：**
1. 对外接口统一 — 所有 Repository 实现同一个接口签名，调用方不需要知道它用的是哪级适配器
2. 代码生成兜底 — 低频实体不需要手写任何 Repository 代码，声明 metadata 即可获得完整 CRUD
3. 手写是例外不是默认 — 只有复杂查询才值得手写，且必须在 `registry.ts` 注册（审计追踪）
4. 禁止返回原始响应 — 所有 Repository 必须映射为业务类型再返回，违反即 code review 不通过
5. 禁止在 route handler 中直接写查询语句 — 必须封装到 Repository 接口后面

## 在我们项目(yatushi)中的应用
YTS 后端使用 MyBatis + XML Mapper，当前的模式是每个实体对应一个 Mapper XML 文件，手写所有 SQL。

**三层适配可以这样落地：**
1. **高频实体**（products, pricing, specifications）：保留手写 Mapper XML，进行 SQL 优化和 EXPLAIN ANALYZE 验证
2. **低频/辅助实体**（如新的 `CodeMapping`）：使用 MyBatis Generator 或通用 Mapper（如 tk.mybatis）自动生成基础 CRUD，只在需要复杂查询时手写 XML
3. **报表/复杂查询**（如兼容性查询结果聚合）：手写专门的 Mapper XML，通过 Service 层封装

**具体落地步骤：**
1. 在 pom.xml 中引入 MyBatis Generator 插件或 `mybatis-plus`（如果兼容性允许）
2. 定义自动生成的基础 Mapper 模板，只覆盖 `insert/update/delete/selectByPrimaryKey`
3. 复杂查询走手写 Mapper，继承基础 Mapper 接口
4. 在 CLAUDE.md 加上约束：新建实体优先用自动生成，需要手写时必须在方法名加 `Custom` 后缀以示区分

考虑到 YTS 使用 MyBatis 而非 JPA，不建议全套搬 mybatis-plus（有兼容风险），可以先从 generator 插件开始。
