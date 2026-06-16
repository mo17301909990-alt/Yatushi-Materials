# Vue 前端开发规范

## 架构

```
api/modules/ → views/ → components/
stores/ (Pinia) → router/ → App.vue
```

## 命名

- 视图组件: `PascalCase.vue` 放在 `views/` 对应模块目录
- 通用组件: `PascalCase.vue` 放在 `components/`
- API 模块: `camelCase.ts` 放在 `api/modules/`
- Store: `camelCase.ts` 放在 `stores/`

## State 管理

- 全局状态 → Pinia Store（auth, permission）
- 页面局部状态 → `ref()` / `reactive()`
- 跨组件状态 → provide/inject 或 Pinia

## 路由

- 路由配置在 `router/index.ts`
- 懒加载: `() => import('@/views/xxx.vue')`
- 权限路由通过 `v-permission` / `v-role` 指令控制

## 样式

- 使用 Tailwind CSS 工具类优先
- Element Plus 组件样式覆盖在 `style.css` 统一管理
- 避免内联样式

## API 调用

- 通过 `api/modules/` 下封装好的函数调用
- 统一错误处理由 axios 拦截器管理
- 请求头自动附加 X-User-Id / X-User-Name（操作日志用）

## 红线

- 禁止直接修改 Pinia store 外的全局状态
- API 请求路径不硬编码，使用 `api/modules/` 封装
- 组件销毁时清理定时器和事件监听
