# /deploy — 部署流程

## 后端部署

```bash
cd yts_project
mvn clean package -DskipTests
# 产物: target/yts_project-0.0.1-SNAPSHOT.jar
```

## 前端部署

```bash
cd yts_project_vueai
npm run build
# 产物: dist/
```

## 部署配置

### 生产环境
- DB: 101.126.27.148:5432 (远程，需 VPN)
- API: localhost:8092
- 部署脚本: `deployment/deploy.sh`

### 本地开发
- DB: localhost:5432 (已恢复数据)
- API: localhost:8092
- 前端代理: localhost:5173 → localhost:8093
