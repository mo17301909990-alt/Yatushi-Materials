---
name: test-domain
description: "按模块/领域运行测试"
---

# /test-domain \<domain\>

```bash
cd yts_project_vueai && npx vitest run src/<domain>/
```

## 示例

- `/test-domain stores` — 运行所有 store 测试
- `/test-domain api` — 运行所有 API 测试
- `/test-domain components` — 运行所有组件测试
- `/test-domain views` — 运行所有视图测试
- `/test-domain utils` — 运行所有工具函数测试
