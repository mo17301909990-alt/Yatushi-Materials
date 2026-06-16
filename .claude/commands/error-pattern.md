# /error-pattern — 错误模式匹配

遇到构建/运行时错误时，优先自查已知修复方案：

```bash
npx tsx scripts/error-patterns.ts match "<错误信息>"
```

修复后如果是不重复的新模式，记录供后续自动匹配：

```bash
npx tsx scripts/error-patterns.ts record "<症状>" "<分类>" "<修复>" [文件路径]
```

## 已有的错误模式

- `poi-ooxml-schemas` / `NoSuchFieldError: Factory` — POI 版本冲突，检查 pom.xml 排除依赖
- Postgres 连接超时 — 检查网络/VPN 是否可达
- `reasoning_effort` API 错误 — deepseek 代理不兼容，检查 `settings.json` 配置
