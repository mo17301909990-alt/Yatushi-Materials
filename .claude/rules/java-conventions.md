# Java 后端开发规范

## 架构分层

```
controller/ → service/ → mapper/ → MyBatis XML → PostgreSQL
```

- **Controller**: 只做请求校验 + 响应格式化，不写业务逻辑
- **Service**: 业务逻辑 + 事务管理（`@Transactional`）
- **Mapper**: MyBatis 接口，对应 resources/mapper/ XML
- **DTO**: 出入参分离，禁止 Entity 直接暴露给前端

## 命名规范

- Controller: `XxxController.java`
- Service: `XxxService.java` + `Impl/XxxServiceImpl.java`
- Mapper: `XxxMapper.java`
- Entity: `Xxx.java`（或对应表名）
- DTO: `XxxReq.java` / `XxxResp.java` / `XxxQuery.java`

## 红线

- 禁止 `@Autowired` 字段注入 → 使用构造器注入或 `@RequiredArgsConstructor`
- 禁止 Controller 里直接写 `try-catch` → 全局异常处理器处理
- 事务注解 `@Transactional` 不要写在 Controller 层
- MyBatis Mapper XML 的 SQL 必须经过 `EXPLAIN ANALYZE` 验证
- 数组/列表参数用 `@NotEmpty` / `@NotNull` 校验
- 敏感信息（密码、密钥）禁止硬编码 → 用环境变量或配置中心
- API 接口统一返回 `R<T>` 格式

## POI/EasyExcel 注意

- POI 锁定 5.2.3 + xmlbeans 5.1.1
- 必须 exclude poi-ooxml-lite 和 poi-ooxml-schemas
- 依赖冲突表现为 `NoSuchFieldError: Factory`
