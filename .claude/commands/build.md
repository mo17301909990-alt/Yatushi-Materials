# /build — 构建命令参考

## 后端 (Maven)

```bash
cd yts_project

./mvnw spring-boot:run              # 本地启动 (port 8093)
mvn clean package -DskipTests       # 打包 JAR
mvn compile                         # 仅编译
mvn test                            # 跑全部测试
mvn test -Dtest=TestClass#method    # 跑单个测试
mvn mybatis-generator:generate      # MyBatis 代码生成
```

## 前端 (npm)

```bash
cd yts_project_vueai

npm run dev                         # 开发服务器 (含代理 → localhost:8093)
npm run build                       # 生产构建
npm run preview                     # 预览构建产物
npm install                         # 装依赖
```

## 数据库

```bash
# 本地 PostgreSQL
PGPASSWORD=HryENprJrxThYSDz psql -h localhost -U postgres -d gold_foil_db

# 运行 SQL 迁移脚本
PGPASSWORD=HryENprJrxThYSDz psql -h localhost -U postgres -d gold_foil_db -f <script.sql>
```
