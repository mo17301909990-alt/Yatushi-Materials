---
name: smoke
description: 三条烟雾测试 — 路由完整性 → 测试套件 → 前端构建
---

# /smoke

三步烟雾测试，10 秒验证系统核心通路。

## 用法

- `/smoke` — 运行全部烟雾测试
- `/smoke routes` — 仅路由完整性检查
- `/smoke test` — 仅测试套件
- `/smoke build` — 仅前端构建

## 执行

```bash
bash scripts/smoke.sh
```

## 检查项

| Step | 命令 | 检查什么 |
|------|------|---------|
| 1 | `node --import tsx scripts/smoke-routes.ts` | 所有路由组件文件存在、导航链接都有对应路由 |
| 2 | `npm run test:run` | 252 个测试通过（70 API 模块 + 7 组件/store） |
| 3 | `npm run build` | Vite 生产构建无错误 |

## 可选: API 契约烟雾测试（需后端运行）

当后端正在运行时（`cd yts_project && mvn spring-boot:run`），额外跑 API 端点可达性检查:

```bash
bash scripts/api-contract-test.sh
```

默认连接 `http://localhost:8092`，自定义端口: `bash scripts/api-contract-test.sh http://localhost:8092`

检查项:
- `GET /api/health` — 健康端点
- `GET /api/gold-foil/products` — 烫金产品
- `GET /api/laminating/options/materials` — 裱纸/覆膜
- `POST /api/compatibility/hot-stamping-types` — 烫金兼容性
- `GET /api/cloth-paper-types` — 布纸类型对照表

## 当烟雾测试失败

| 失败 | 原因 | 修法 |
|------|------|------|
| 路由缺失 | 新页面加了路由但 .vue 文件没创建 | 建对应文件或修正路由 |
| 测试失败 | 代码改了测试没同步 | 看 vitest 报告修 |
| 构建报错 | 类型错误/语法问题 | 看 Vite 输出定位 |
