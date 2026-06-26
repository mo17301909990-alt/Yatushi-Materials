# YTS 测试地基方案

---

## 现状

### 当前测试状态

| 层 | 测试文件数 | 配置状态 | 实际可运行 |
|---|---|---|---|
| 后端 | 2 个 (YtsProjectApplicationTests / SecondMatchMapperTestCase) | `maven-surefire-plugin` 配置了 `<skipTests>true</skipTests>` | **不能** — 两个测试都是 `@SpringBootTest`，依赖本地 PostgreSQL 数据库连接，没有 Docker 或 TestContainers 支撑 |
| 前端 | 0 | 无测试依赖、无测试配置、无测试文件 | — |

### 痛点

1. **测试被硬跳过** — `pom.xml` 里 surefire 配置了 `skipTests=true`，导致 `mvn package` / `mvn test` 直接跳过所有测试，CI 和本地构建从不验证
2. **现有测试不可靠** — `YtsProjectApplicationTests` 和 `SecondMatchMapperTestCase` 都是 `@SpringBootTest`，依赖外部 PostgreSQL 实例（生产/开发数据库都用 IP 直连，当前不可达）。跑测试等于碰运气
3. **前端测试为零** — 29 个组件、66 个 API 模块、9 个 Pinia Store，完全没有单元测试或组件测试。每次改动靠手动在浏览器里验证
4. **无门禁** — 没有 pre-commit 或 CI 层面的测试门禁，代码质量依赖个人自觉
5. **回归成本高** — 153 张表的业务逻辑，改动一个兼容性匹配规则，无法自动化验证对其他物料的影响
6. **测试资源目录缺失** — `src/test/resources/` 目录不存在，连测试用的配置文件和 schema DDL 都没有

---

## 方案总览

```
┌─────────────────────────────────────────────────────┐
│                   YTS 测试地基                        │
│                                                     │
│  后端                       前端                     │
│  TestContainers             Vitest                  │
│  PostgreSQL 16              jsdom + Vue TU          │
│                                                     │
│  ┌─────────────────────┐  ┌──────────────────────┐  │
│  │ AbstractIntegration │  │ test-setup.ts        │  │
│  │ Test 基类            │  │ Vitest globals       │  │
│  │ @DynamicPropertySrc │  │ localStorage mock    │  │
│  │ schema.sql (DDL)    │  │ axios mock 基底      │  │
│  │ reference-data.sql  │  │                      │  │
│  └─────────────────────┘  └──────────────────────┘  │
│                                                     │
│  ┌──────────────────────────────────────────────┐   │
│  │           门禁 (hooks + pre-commit)           │   │
│  │  mvn test (跳过不影响构建)                     │   │
│  │  npm run test:run (不阻塞提交)                 │   │
│  └──────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────┘
```

**设计原则**：
- 不要求 100% 覆盖率起步 — 先让测试能跑起来（地基），再逐步加用例
- 降级优先 — Docker 不可用时自动 fallback 到本地数据库，不阻塞开发
- 门禁不挡路 — 测试失败时警告但不硬拦，给团队适应期
- 渐进增量 — 按优先级分批覆盖，不是一口气写完

---

## 后端 — TestContainers PostgreSQL

### 依赖

**pom.xml 改动分两步**：

#### 第一步：引入 BOM（dependencyManagement 段）

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>testcontainers-bom</artifactId>
            <version>1.20.4</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

#### 第二步：添加三个 TC 依赖（dependencies 段，test scope）

```xml
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>testcontainers</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>postgresql</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>
```

#### 第三步：放开测试门禁

将 `maven-surefire-plugin` 的：
```xml
<configuration>
    <skipTests>true</skipTests>
</configuration>
```
改为：
```xml
<configuration>
    <!-- 默认跳过集成测试；如需运行，用 -DskipITs=false 或 -Pwith-tests -->
    <skipTests>${skipTests}</skipTests>
</configuration>
```
并在 `<properties>` 中设置默认值：
```xml
<skipTests>false</skipTests>
```
或抽取为一个 profile：

```xml
<profiles>
    <profile>
        <id>skip-tests</id>
        <activation>
            <activeByDefault>true</activeByDefault>
        </activation>
        <properties>
            <skipTests>true</skipTests>
        </properties>
    </profile>
    <profile>
        <id>with-tests</id>
        <properties>
            <skipTests>false</skipTests>
        </properties>
    </profile>
</profiles>
```

这样开发时 `mvn package` 仍然跳过测试（默认 profile），CI 中 `mvn package -Pwith-tests` 才跑测试。

### 配置改动

#### 1. `pom.xml` — 如上所示

#### 2. 新建 `src/test/resources/application-test.properties`

用途：测试 profile 配置，测试时通过 `--spring.profiles.active=test` 激活。

```properties
spring.datasource.url=${TEST_DB_URL:jdbc:postgresql://localhost:5432/gold_foil_db}
spring.datasource.username=${TEST_DB_USER:postgres}
spring.datasource.password=${TEST_DB_PASS:HryENprJrxThYSDz}
# 注意：实际 TC 容器 URL 由 @DynamicPropertySource 动态注入，这里只是 fallback
# 禁用 Spring SQL init，由 TC initScript 接管
spring.sql.init.mode=never
# MyBatis 配置与主配置一致
mybatis.mapper-locations=classpath:mapper/*.xml
mybatis.type-aliases-package=com.it.yts_project.model
mybatis.configuration.map-underscore-to-camel-case=true
logging.level.com.it.yts_project.mapper=DEBUG
```

### 新建文件

#### `AbstractIntegrationTest.java`

路径：`src/test/java/com/it/yts_project/config/AbstractIntegrationTest.java`

用途：集成测试基类。用 `@Testcontainers` 管理 PostgreSQL 16 容器生命周期，`@DynamicPropertySource` 注入动态数据源。包含 schema 加载（initScript）、container reuse 开关、等待策略。

```java
package com.it.yts_project.config;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public abstract class AbstractIntegrationTest {

    private static final DockerImageName PG_IMAGE = DockerImageName.parse("postgres:16");

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(PG_IMAGE)
            .withDatabaseName("gold_foil_db")
            .withUsername("postgres")
            .withPassword("test")
            .withInitScript("schema.sql")
            .withReuse(true);  // 多次运行不销毁，加速本地开发

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
    }
}
```

#### `schema.sql`

路径：`src/test/resources/schema.sql`

用途：数据库完整 DDL — 从 `pg_dump --schema-only` 导出，剥离 owner 和 ACL。按 TYPE → FUNCTION → TABLE → INDEX 顺序组织。当前数据库含 89 张表（生产 153 张）、Custom ENUM Type 和 PL/pgSQL 函数。

**导出命令**：
```bash
PGPASSWORD=HryENprJrxThYSDz pg_dump -h localhost -U postgres -d gold_foil_db --schema-only --no-owner --no-acl > schema.sql
```

**关键注意**：导出的 DDL 必须手动调整加载顺序，确保：
1. 先创建 ENUM TYPE（如果有 `CREATE TYPE`）
2. 再创建 PL/pgSQL FUNCTION（如果有函数依赖 TYPE）
3. 再创建所有表（含外键）
4. 最后创建 INDEX 和 CONSTRAINT

由于 `withInitScript` 是逐条执行，如果原始 `schema.sql` 混合了 TYPE/FUNCTION/TABLE，需要拆分。建议按 `schema-types.sql` → `schema-functions.sql` → `schema-tables.sql` 拆分，然后在基类中按顺序执行。

#### `reference-data.sql`

路径：`src/test/resources/test-data/reference-data.sql`

用途：参考数据种子 — 从本地数据库导出"选项类"表（material_options、type_options 等）的核心行。让 TC 容器中的 MyBatis 查询能走到数据，避免空结果集。

**导出注意事项**：
- 按外键依赖拓扑排序插入（先导父表，再导子表）
- 只导出少量代表性数据行（每表 1-3 行），不导全量
- 不导业务操作日志等运行时数据

#### 适配现有测试

**`SecondMatchMapperTestCase.java`** — 改为继承 `AbstractIntegrationTest` 并激活 test profile：

```diff
- @SpringBootTest
+ @ActiveProfiles("test")
-public class SecondMatchMapperTestCase {
+public class SecondMatchMapperTestCase extends AbstractIntegrationTest {
```

**`YtsProjectApplicationTests.java`** — 同样处理，确认 `@SpringBootTest` 能正常拉起 ApplicationContext。

### 测试分层

```
┌──────────────────────────────────────────────────────────────────┐
│  E2E (人工 + Playwright)                                         │
│  前端 + 后端联调，浏览器自动化                                    │
├──────────────────────────────────────────────────────────────────┤
│  Integration (TestContainers, @SpringBootTest)                   │
│  Mapper 查询、Service 事务、Controller 请求链路                    │
│  继承 AbstractIntegrationTest，每次跑在隔离的 PostgreSQL 16 容器  │
├──────────────────────────────────────────────────────────────────┤
│  Unit (JUnit 5, Mockito)                                        │
│  Service 层纯逻辑（无 DB）、工具函数、DTO 转换                     │
│  不启动 Spring Context，毫秒级                                    │
└──────────────────────────────────────────────────────────────────┘
```

- **Unit 测试**：纯 JUnit 5 + Mockito，不启动 Spring Context，覆盖 Service 层的规则引擎、DTO 转换逻辑
- **Integration 测试**：继承 `AbstractIntegrationTest`，启动 TC PostgreSQL 容器 + Spring Context，覆盖 Mapper SQL 执行和 Service + DB 的交互
- **E2E 测试**：不在本次地基范围内（后续可引入 Playwright）

### 降级策略

Docker Desktop 不可用时（离线/CI 节点无 Docker/Windows Docker 未启动）：

| 方案 | 做法 | 适用场景 |
|------|------|---------|
| **Profile 切换** | 使用 `@ActiveProfiles("test-localdb")` 激活本地 PostgreSQL 配置 | 开发者本地有数据库服务 |
| **条件跳过** | `@EnabledIfSystemProperty(named = "integration.test.enabled", matches = "true")` — 默认不跑 | CI 中显式启用 |
| **H2 内存库** | 对纯 schema 验证的场景，用 H2 替代 PostgreSQL（但要承担 SQL 方言差异风险） | 仅验证 DDL 合法 |

推荐做法：**Maven Profile 控制**。CI 中 `-Pwith-tests` 触发 TC 集成测试，本地开发默认 `-Pskip-tests`（无 Docker 也能正常构建），需要时 `-Pwith-tests -Ddocker.available=true` 主动触发。

---

## 前端 — Vitest

### 依赖

在 `yts_project_vueai/` 下安装：

```bash
npm install -D vitest @vue/test-utils jsdom @vitest/coverage-v8
```

| 包 | 版本（latest） | 用途 |
|---|---|---|
| `vitest` | ^3.x | 测试运行器（兼容 Vite 5） |
| `@vue/test-utils` | ^2.x | Vue 组件渲染和断言 |
| `jsdom` | ^25.x | DOM 环境模拟 |
| `@vitest/coverage-v8` | ^3.x | 覆盖率报告 |

### 配置改动

#### `vite.config.ts`

在 `defineConfig` 中添加 `test` 块：

```typescript
/// <reference types="vitest" />
import { defineConfig } from 'vite'
// 注意：vitest 3.x 可以用 vitest/config 的 defineConfig，也可直接复用 vite 的
// 如果 build 时报 test 配置不识别，改为：
// import { defineConfig } from 'vitest/config'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src')
    }
  },
  // ... 原有 build / server 配置不变 ...
  test: {
    globals: true,               // 自动注入 describe/it/expect
    environment: 'jsdom',        // DOM 环境
    setupFiles: './src/test-setup.ts',  // 全局 setup
    coverage: {
      provider: 'v8',
      reporter: ['text', 'json', 'html'],
      include: ['src/**/*.{ts,vue}'],
      exclude: ['src/**/*.spec.ts', 'src/**/__tests__/**']
    }
  }
})
```

**注意 TypeScript 兼容**：在 `tsconfig.json` 的 `compilerOptions.types` 中添加 `"vitest/globals"`，或在每个测试文件头部加 `/// <reference types="vitest/globals" />`。

#### `package.json`

在 `scripts` 中添加：

```json
{
  "scripts": {
    "dev": "vite",
    "build": "vite build",
    "preview": "vite preview",
    "test": "vitest",
    "test:run": "vitest run",
    "test:coverage": "vitest run --coverage"
  }
}
```

- `npm run test` — watch 模式，开发时持续运行
- `npm run test:run` — 单次运行，CI 中使用
- `npm run test:coverage` — 带覆盖率报告

### 烟雾测试清单

按优先级排序，从零 mocks 的纯函数到高 mocks 的组件测试，渐进验证 Vitest 链路。

| 优先级 | 测试文件 | 被测对象 | 依赖 | 验证点 |
|--------|---------|---------|------|--------|
| P0 | `src/api/__tests__/copilot.spec.ts` | `copilot.ts` 的 `extractSources` 纯函数 | 无（纯函数） | Vitest + jsdom + TS 配置是否通 |
| P0 | `src/api/__tests__/auth.spec.ts` | `auth.ts` 的 API 封装 | 需 mock axios | axios mock 链路是否正常 |
| P1 | `src/stores/__tests__/copilot.spec.ts` | `copilot.ts` 的 `advanceGuideStage` / `parseParamsFromReply` / `recordGuideSelection` | Pinia | Store 纯逻辑测试能力 |
| P1 | `src/stores/__tests__/auth.spec.ts` | `auth.ts` 的 `login/logout/initUserState` | localStorage mock | 验证 setup 中 localStorage 环境 |
| P2 | `src/components/copilot/__tests__/CompatibilityResultCard.spec.ts` | `CompatibilityResultCard.vue` | props 驱动 | 组件挂载、props 渲染、slot |
| P2 | `src/components/copilot/__tests__/CopilotChat.spec.ts` | `CopilotChat.vue`（核心 AI 聊天组件） | mock Pinia + axios | 完整组件测试链路（渲染+交互） |
| P3 | `src/utils/__tests__/*.spec.ts` | 工具函数 | 无/轻量 | 覆盖 `src/utils/` 下的纯函数 |
| P3 | 其他 API 模块 | 按业务模块分组覆盖 | mock axios | API 模块的幂等性验证 |

### 新建文件

#### `src/test-setup.ts`

用途：全局 setup — `@vue/test-utils` 的 `vi.stubGlobal` 配置、`localStorage` mock、`request`/`axios` mock 基底。

```typescript
import { vi } from 'vitest'
import { config } from '@vue/test-utils'

// localStorage mock
const localStorageMock = (() => {
  let store: Record<string, string> = {}
  return {
    getItem: (key: string) => store[key] ?? null,
    setItem: (key: string, value: string) => { store[key] = value },
    removeItem: (key: string) => { delete store[key] },
    clear: () => { store = {} }
  }
})()
Object.defineProperty(window, 'localStorage', { value: localStorageMock })

// 全局组件 stub（如果有 Element Plus 组件，可以在这里统一 stub）
// config.global.stubs = { 'el-button': true, 'el-dialog': true }
```

#### 烟雾测试示例：`src/api/__tests__/copilot.spec.ts`

```typescript
import { describe, it, expect } from 'vitest'

describe('extractSources', () => {
  it('应该从 AI 回复中正确提取引用来源', () => {
    const reply = '根据数据[1]，材料A兼容性良好。\n\n[1]: 材料兼容性表 #123'
    const result = extractSources(reply)
    expect(result).toHaveLength(1)
    expect(result[0]).toContain('#123')
  })
})
```

---

## 门禁体系

### settings.json hooks

在 `.claude/settings.json` 中配置 PostToolUse Hook 自动检查：

```json
{
  "hooks": {
    "PostToolUse": {
      "command": "",
      "script": ""
    }
  }
}
```

具体门禁规则由 pre-commit hooks 承载，而非 Claude hooks（Claude hooks 更适合 IDE 内部触发）。

### Pre-commit 检查链

使用 `husky` + `lint-staged`（或者直接写 `.git/hooks/pre-commit` 脚本）：

```
pre-commit 钩子执行顺序：
  1. git diff --cached --name-only            → 确定哪些文件被改动
  2. 后端文件 (*.java / pom.xml)
     └─ mvn compile -q                        → 编译通过
  3. 前端文件 (*.ts / *.vue / package.json)
     └─ npx vue-tsc --noEmit                  → TypeScript 检查
  4. 如果包含 test 目录文件
     └─ npm run test:run --changed            → 运行受影响测试
```

**第一阶段（适应期）**：只 `mvn compile` + `vue-tsc`，不强制跑测试
**第二阶段（成熟期）**：添加 `npm run test:run --changed`

### 失败时怎么办

| 阶段 | 失败行为 | 说明 |
|------|---------|------|
| 适应期（前 2 周） | **警告但不阻塞** | pre-commit hook 输出测试失败信息，但允许提交。给团队时间适配测试流程 |
| 过渡期（2-4 周） | **阻塞提交，可 --no-verify 绕过** | 默认拦截，赶时间时可以绕过 |
| 稳定期（4 周后） | **硬阻塞，CI 门禁** | CI pipeline 中集成测试失败直接驳回 PR，不允许合并 |

---

## 实施路线

### Day 1: 后端地基 + 验收

| 步骤 | 内容 | 验收标准 |
|------|------|---------|
| 1.1 | `pom.xml` 添加 testcontainers-bom + 三个 TC 依赖 | `mvn compile` 通过，无依赖冲突 |
| 1.2 | 修改 surefire 配置，`skipTests` 抽取为属性 | `mvn test -DskipTests=false` 不跳过测试 |
| 1.3 | 从本地 DB 导出 DDL → 创建 `src/test/resources/schema.sql`，清洗 TYPE/FUNCTION/TABLE 加载顺序 | TC 容器能用 `initScript` 完整建表 |
| 1.4 | 编写 `AbstractIntegrationTest.java` 基类 | 基类能启动 PostgreSQL 16 容器并动态注入数据源 |
| 1.5 | 从本地 DB 导出少量参考数据 → 创建 `reference-data.sql` | TC 容器内有种子数据，MyBatis 查询能返回行 |
| 1.6 | 适配 `SecondMatchMapperTestCase` 继承基类 | `mvn test -Dtest=SecondMatchMapperTestCase` 通过 |
| 1.7 | 适配 `YtsProjectApplicationTests` 继承基类 | `mvn test -Dtest=YtsProjectApplicationTests` 通过 |
| 1.8 | 验证降级策略：无 Docker 时 `mvn package` 正常通过 | `mvn package`（默认 profile）不报错 |

**验收**：执行 `mvn test -Pwith-tests` 且 Docker 运行中，所有后端测试通过。

### Day 2: 前端地基 + 验收

| 步骤 | 内容 | 验收标准 |
|------|------|---------|
| 2.1 | `npm install -D vitest @vue/test-utils jsdom @vitest/coverage-v8` | 安装成功，无 peer dependency 冲突 |
| 2.2 | 修改 `vite.config.ts` 添加 `test` 块 | `npm run test:run` 能正常执行 |
| 2.3 | `package.json` 添加三个 test scripts | `npm run test` / `test:run` / `test:coverage` 都能执行 |
| 2.4 | `tsconfig.json` 添加 `vitest/globals` types | IDE 不提示 `describe`/`it`/`expect` 未定义 |
| 2.5 | 创建 `src/test-setup.ts` (localStorage mock + stubs) | test-setup 能被 Vitest 自动加载 |
| 2.6 | 创建 P0 烟雾测试：`copilot.spec.ts` (`extractSources` 纯函数) | smoke test 通过 |
| 2.7 | 创建 P1 烟雾测试：`copilot.spec.ts` (store 纯逻辑) | smoke test 通过 |
| 2.8 | 创建 P2 烟雾测试：`CompatibilityResultCard.spec.ts` (组件渲染) | smoke test 通过 |

**验收**：`npm run test:run` 输出至少有 3 个测试通过（P0 + P1 + P2），覆盖率报告可生成。

### Day 3: 门禁 + 集成验证

| 步骤 | 内容 | 验收标准 |
|------|------|---------|
| 3.1 | 创建 `.husky/pre-commit` 或直接写 `.git/hooks/pre-commit` | `git commit` 时自动触发 `mvn compile -q` 和 `npx vue-tsc --noEmit` |
| 3.2 | 验证 pre-commit 在代码有语法错误时正确拦截 | 故意引入一个语法错误 → commit 被阻断 |
| 3.3 | 确认 CI pipeline 中后端测试可用（Docker 已安装） | CI 中 `mvn test -Pwith-tests` 通过 |
| 3.4 | 确认 CI pipeline 中前端测试可用 | CI 中 `npm run test:run` 通过 |
| 3.5 | 验证降级场景：无 Docker 时构建不阻塞 | `mvn package`（默认 profile）通过，测试被跳过 |
| 3.6 | 输出一份"测试运行指南"文档（放项目 README 或 docs/ 下） | 团队新人能按文档跑通全部测试 |

**验收**：全链路验证通过 — 开发者提交代码 → pre-commit 检查 → CI 测试通过 → PR 可合并。

---

## 风险与降级

| 风险 | 概率 | 影响 | 应对措施 |
|------|------|------|---------|
| Docker 不可用 | 中 | 集成测试全部无法运行 | Maven Profile 切换降级到本地 DB；`@EnabledIf` 条件跳过 |
| schema.sql 过期（生产表结构变更） | 高 | TC 中建表失败或 SQL 执行错误 | 纳入 pre-commit hook：`git diff --name-only | grep schema.sql` 触发重新导出提醒；CI 中定期校验 DDL 与实际数据库一致性 |
| schema.sql 加载顺序错乱（ENUM TYPE / FUNCTION / TABLE） | 中 | `initScript` 报错，容器启动失败 | DDL 拆分为 schema-types.sql → schema-functions.sql → schema-tables.sql 三步加载，不要合在一个文件里 |
| reference-data.sql 外键约束顺序 | 中 | 数据插入失败 | 按 `pg_dump --data-only` 的默认顺序（已按外键拓扑排序），清洗后插入前用 `SET CONSTRAINTS ALL DEFERRED` |
| TC 容器首次启动需拉取镜像（~400MB） | 低（首次后缓存） | 首次运行耗时 1-2 分钟 | 设置 `withReuse(true)` 避免反复拉取；离线环境手动预拉 `docker pull postgres:16` |
| Windows 文件挂载路径问题 | 低 | TC initScript 找不到文件 | TC 的 `withInitScript("classpath:schema.sql")` 使用 classpath 路径，规避文件系统斜杠差异 |
| Vitest + Vite 配置冲突 | 低 | `test` 配置块不能被 vite 的 defineConfig 识别 | 改用 `vitest/config` 的 `defineConfig` 或独立的 `vitest.config.ts` |
| jsdom 与完整浏览器行为差异 | 低 | 组件测试通过但浏览器行为不同 | 明确 jsdom 的边界（不测布局、不测动画）；交互测试留到 E2E 阶段 |
| 团队无测试习惯，抵触门禁 | 中 | pre-commit 被频繁 `--no-verify` 绕过 | 适应期只警告不拦截，用数据说话（2 周后展示"测试防止了多少回归"），逐步收紧 |
| CI 环境无 Docker | 低（CI 节点通常有） | 集成测试无法在 CI 中运行 | CI pipeline 分两步：第一步 mvn compile（无 Docker 需求），第二步在带 Docker 的 runner 上跑集成测试 |

### 关键决策记录

1. **不对 schema.sql 做侵入性修改** — 只用 `pg_dump --schema-only --no-owner --no-acl` 导出原始 DDL，清洗顺序但不改内容。表结构变更时重新导出即可
2. **参考数据不追求全量** — 每张选项表导 1-3 行代表性数据即可，目的是让 SQL 查询能返回行，不做完整的数据覆盖
3. **testcontainers 的 reuse 功能默认开启** — 本地开发时容器不销毁，第二次起秒级启动。CI 中关闭（通过环境变量 `TESTCONTAINERS_REUSE_ENABLE=false`）
4. **前端测试从烟雾测试起步** — 不追求覆盖率百分比，而是验证测试基础设施可工作。后续由开发者逐步补全
5. **门禁分阶段收紧** — 不一步到位硬阻塞，给团队 2-4 周适应期

---

## 附录：文件清单汇总

### 后端新增/修改文件

| 文件路径 | 类型 | 说明 |
|---------|------|------|
| `yts_project/pom.xml` | 修改 | 添加 TC BOM + 依赖 + surefire profile 配置 |
| `yts_project/src/test/resources/application-test.properties` | 新建 | Test profile 配置 |
| `yts_project/src/test/resources/schema.sql` | 新建 | 数据库完整 DDL（从 pg_dump 导出） |
| `yts_project/src/test/resources/schema-types.sql` | 新建 | 拆分后的 ENUM TYPE DDL（可选） |
| `yts_project/src/test/resources/schema-functions.sql` | 新建 | 拆分后的 FUNCTION DDL（可选） |
| `yts_project/src/test/resources/schema-tables.sql` | 新建 | 拆分后的 TABLE DDL（可选） |
| `yts_project/src/test/resources/test-data/reference-data.sql` | 新建 | 参考数据种子 |
| `yts_project/src/test/java/com/it/yts_project/config/AbstractIntegrationTest.java` | 新建 | 集成测试基类 |
| `yts_project/src/test/java/com/it/yts_project/mapper/SecondMatchMapperTestCase.java` | 修改 | 继承 AbstractIntegrationTest + @ActiveProfiles |
| `yts_project/src/test/java/com/it/yts_project/YtsProjectApplicationTests.java` | 修改 | 继承 AbstractIntegrationTest + @ActiveProfiles |

### 前端新增/修改文件

| 文件路径 | 类型 | 说明 |
|---------|------|------|
| `yts_project_vueai/vite.config.ts` | 修改 | 添加 `test` 配置块 |
| `yts_project_vueai/package.json` | 修改 | 添加三个 test scripts |
| `yts_project_vueai/tsconfig.json` | 修改 | 添加 `vitest/globals` types |
| `yts_project_vueai/src/test-setup.ts` | 新建 | 全局 setup |
| `yts_project_vueai/src/api/__tests__/copilot.spec.ts` | 新建 | P0 烟雾测试 |
| `yts_project_vueai/src/api/__tests__/auth.spec.ts` | 新建 | P0 烟雾测试 |
| `yts_project_vueai/src/stores/__tests__/copilot.spec.ts` | 新建 | P1 烟雾测试 |
| `yts_project_vueai/src/stores/__tests__/auth.spec.ts` | 新建 | P1 烟雾测试 |
| `yts_project_vueai/src/components/copilot/__tests__/CompatibilityResultCard.spec.ts` | 新建 | P2 烟雾测试 |
| `yts_project_vueai/src/components/copilot/__tests__/CopilotChat.spec.ts` | 新建 | P2 烟雾测试 |

### 门禁文件

| 文件路径 | 类型 | 说明 |
|---------|------|------|
| `.husky/pre-commit` | 新建 | pre-commit hook 脚本 |

### 文档

| 文件路径 | 说明 |
|---------|------|
| `docs/testing-guide.md` | 测试运行指南（Day 3 产出） |
