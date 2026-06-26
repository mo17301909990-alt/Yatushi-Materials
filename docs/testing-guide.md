# YTS 测试运行指南

## 前置条件

- **后端测试**: 需要 Docker Desktop 运行中
- **前端测试**: 无需额外依赖

## 后端测试

```bash
cd yts_project

# 跑集成测试（需要 Docker）
mvn test -Pwith-tests

# 跑单个测试
mvn test -Pwith-tests -Dtest=SecondMatchMapperTestCase

# 默认构建（跳过测试）
mvn package
```

## 前端测试

```bash
cd yts_project_vueai

# 单次运行
npm run test:run

# Watch 模式（开发时持续跑）
npm run test

# 带覆盖率报告
npm run test:coverage

# 覆盖率报告在 coverage/ 目录
```

## 门禁说明

pre-commit hook 会检查：
1. 后端 Java 文件 → mvn compile -q
2. 前端文件 → vue-tsc --noEmit + npm run test:run
3. 适应期仅警告，不阻塞提交

紧急跳过门禁：git commit --no-verify
