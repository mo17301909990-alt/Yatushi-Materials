package com.it.yts_project.config;

/**
 * 测试基类。
 * 当前使用 H2 内存数据库（由 application-test.properties 配置）。
 * 如需切换回 TestContainers PostgreSQL，取消下方注释并删除本行上方内容。
 */
public abstract class AbstractIntegrationTest {
}

// === TestContainers 方案（需要 Docker 环境） ===
// 取消注释以下内容，并删除上面这个类的定义，即可启用：
//
// package com.it.yts_project.config;
//
// import org.springframework.test.context.DynamicPropertyRegistry;
// import org.springframework.test.context.DynamicPropertySource;
// import org.testcontainers.containers.PostgreSQLContainer;
// import org.testcontainers.junit.jupiter.Container;
// import org.testcontainers.junit.jupiter.Testcontainers;
// import org.testcontainers.utility.DockerImageName;
//
// @Testcontainers
// public abstract class AbstractIntegrationTest {
//
//     private static final DockerImageName PG_IMAGE = DockerImageName.parse("postgres:16");
//
//     @Container
//     static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(PG_IMAGE)
//             .withDatabaseName("gold_foil_db")
//             .withUsername("postgres")
//             .withPassword("test")
//             .withInitScript("schema.sql")
//             .withReuse(true);
//
//     @DynamicPropertySource
//     static void configureProperties(DynamicPropertyRegistry registry) {
//         registry.add("spring.datasource.url", postgres::getJdbcUrl);
//         registry.add("spring.datasource.username", postgres::getUsername);
//         registry.add("spring.datasource.password", postgres::getPassword);
//         registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
//     }
// }
