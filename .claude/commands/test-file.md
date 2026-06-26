---
name: test-file
description: "运行单个测试文件"
---

# /test-file \<filename\>

```bash
cd yts_project_vueai && npx vitest run src/<filename>.spec.ts
```

## 示例

- `/test-file stores/auth` — 运行 `src/stores/auth.spec.ts`
- `/test-file api/modules/user` — 运行 `src/api/modules/user.spec.ts`
- `/test-file components/Button` — 运行 `src/components/Button.spec.ts`
