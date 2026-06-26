# Schema-First 元数据驱动

## 来源
- 项目: ERPAI
- 关键文件:
  - `control-center/src/erpai/metadata/definitions/` (594 个 YAML 定义文件)
  - `control-center/src/erpai/metadata/generator/build.ts` (代码生成器)
  - `.claude/rules/metadata-definitions.md` (规范约束)

## 问题
传统开发中，实体定义散布在三个地方：数据库 Schema、代码类型定义、API 接口文档。三处不同步是经典痛点：
- 改了数据库字段忘了更新 TypeScript 类型
- API 返回的字段名和代码里的不一致
- 新开发者不知道"真相源"在哪，只能到处翻

## 方案
以 YAML 文件作为唯一真相源（Single Source of Truth），用代码生成器自动产出三样东西：
1. **TypeScript 类型定义** — `entity.ts` 自动生成，禁止手写
2. **数据库 Schema** — SurrealQL 自动生成，保证类型一致
3. **Repository 适配器** — CRUD 操作自动生成，免去重复的模板代码

修改流程是单向的：**改 YAML → 跑 build.ts → 所有产出自动同步**。任何人不得直接修改生成的文件。

YAML 定义示例结构：
```yaml
entity: SalesOrder
fields:
  orderNo: { type: string, required: true, unique: true }
  customerId: { type: ref, target: Customer, required: true }
  items: { type: array, of: OrderItem }
  status: { type: enum, values: [draft, confirmed, shipped] }
```

## 在我们项目(yatushi)中的应用
YTS 后端是 Spring Boot + MyBatis，当前模式是：手写 Java Entity 类 → 手写 Mapper XML → 手写 DTO。三套手写容易不一致。

可以考虑引入**轻量级 Schema-First**：
1. 在 `database_scripts/` 下维护一张实体定义清单（YAML 或 JSON），描述每个业务实体的字段名、类型、约束、DTO 映射关系
2. 用 FreeMarker 或 Mustache 模板 + 一个 Java  generator main 方法，自动生成：
   - Java Entity 类（带 JPA/MyBatis 注解）
   - Mapper XML 基础 CRUD 片段
   - DTO（`XxxReq.java` / `XxxResp.java`）
   - PostgreSQL DDL 迁移脚本
3. 在 CLAUDE.md 写入约束："改实体必须改 YAML 定义，然后跑 generator，不得手写 Entity/XML"

考虑到 YTS 已在生产运行，可以渐进式引入：新模块（如当前的 compatibility query）使用 Schema-First，旧模块逐步迁移。

---

