# API 约定与测试分层契约

## 来源
- 项目: ERPAI
- 关键文件:
  - `.claude/rules/api-conventions.md` — API 层规范
  - `.claude/rules/testing.md` — 测试分层契约

## 问题
API 层不一致是后端项目最常见的代码异味：
- 有的 handler 用 Zod 校验，有的用 `as Record` 类型强制绕过 — 安全隐患
- 有的 handler 直接写查询语句，有的通过 Repository — 没法统一管控
- 没有 JSDoc / 文档注释 — 调用方要读源码才知道怎么用
- Schema 改动后测试不同步 — 测试绿了但逻辑已经变了

## 方案
ERPAI 定义了两套相互配合的规范：

### API Conventions（接口规范）
三条红线 + 一条绿线：

**红线 1：禁止 `as Record` 类型绕过校验**
```typescript
// 禁止
const data = c.req.json() as Record<string, any>

// 必须
const schema = z.object({ name: z.string(), age: z.number() })
const data = schema.parse(await c.req.json())
```

**红线 2：禁止在 route handler 直接写数据库查询**
```typescript
// 禁止
const result = await db.query('SELECT * FROM users WHERE id = $id', { id })

// 必须
const user = await userRepo.findById(id)
```

**红线 3：每个 route handler 必须包含 JSDoc + Example Response**
```typescript
/**
 * 获取用户信息
 * @param id - 用户 ID
 * @returns { name: string, email: string }
 * @example GET /api/users/123 → { name: "张三", email: "zhangsan@example.com" }
 */
```

### Testing Contract（测试分层契约）
四个层次的测试要求：

1. **API Contract Test** — 每个 route 必须有，验证请求/响应符合规格
2. **Repository Test** — 覆盖正常路径 + 异常路径（找不到、重复、并发）
3. **新逻辑文件必须配套测试** — 放在独立的 `test/` 目录，文件名对应
4. **Schema 变更后必须同步更新测试** — 不是"尽量"，是"必须"

**免测白名单：** 数据库 migrations、类型定义、纯 re-export 文件

## 在我们项目(yatushi)中的应用
YTS 后端是 Spring Boot + MyBatis，已经有 `R<T>` 统一返回格式，但在约定层面可以进一步加强：

### API 规范（Java 版本）
1. **禁止 Controller 直接调用 Mapper** — 必须通过 Service 层，与当前架构一致，但可以加 ArchUnit 测试强制执行
2. **禁止 Service 返回 Entity** — 必须转为 DTO/Resp 再返回
3. **每个 Request DTO 必须包含校验注解** — `@NotBlank`、`@NotNull`、`@Size` 等，不信任前端传参
4. **每个 API 接口必须有对应的 REST 文档** — 可以用 SpringDoc OpenAPI 注解，不用手写 Markdown

### 测试分层
YTS 的测试当前集中在单元测试，可以扩展：
1. **Controller 层测试** — 用 `@WebMvcTest` + MockMvc 验证每个接口的请求校验和响应格式
2. **Service 层测试** — 用 `@SpringBootTest` + `@Transactional` 验证业务逻辑，覆盖正常和异常路径
3. **Mapper 层测试** — 用 `@MybatisTest` + 内嵌数据库验证 SQL 正确性
4. **新增文件必须配套测试** — 在 CLAUDE.md 加入这个约束

### 具体落地
对于当前的 Compatibility Query 模块，可以立标杆：
1. `CompatibilityQueryController` → `@WebMvcTest` 验证请求校验 + 正确响应
2. `CompatibilityQueryService` → `@SpringBootTest` 验证匹配逻辑（正常组合、不兼容组合、空结果）
3. `CodeMappingMapper` → `@MybatisTest` 验证 SQL 正确性

---
