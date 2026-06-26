# SSR 组件库（零框架前端模式）

## 来源
- 项目: ERPAI
- 关键文件: `control-center/src/erpai/ui/shared/README.md`

## 问题
传统 SPA（React/Vue）项目需要打包、路由、状态管理，对服务端渲染的 Java 项目来说太重了。一个小功能动辄引入整个框架。

## 方案
**Server-Side Rendering + 薄前端组件库**，不依赖任何前端框架：

### 核心组件
```
ui/shared/
├── pageShell.ts        # 页面骨架布局（header/sidebar/content）
├── showToast.ts        # 轻提示
├── showConfirmDialog.ts # 确认弹窗
├── renderEmptyState.ts # 空状态占位
├── error-pages.ts      # 404/403/500 统一错误页
├── notification.ts     # SSE 实时推送 + 通知铃铛
├── design-tokens.css   # CSS 变量（色板/间距/阴影）
└── responsive.css      # 四档响应式断点
```

### 特点
- **零运行时依赖**：没有 React/Vue/Svelte，纯 TypeScript + CSS
- **SSR 优先**：HTML 在服务端渲染，前端只做渐进增强
- **函数式组件**：`pageShell({title, content, breadcrumb})` 返回 HTML 字符串
- **CSS 变量体系**：所有设计令牌通过 CSS 变量控制，主题切换只需改变量
- **四档断点**：`mobile(320) | tablet(768) | desktop(1024) | wide(1440)`

### 与框架方案的权衡
| 维度 | SSR 组件库 | Vue/React SPA |
|------|-----------|---------------|
| 首屏加载 | 快（无 JS 阻塞） | 慢（需要加载框架） |
| 交互复杂度 | 受限于 DOM API | 任意复杂 |
| 团队门槛 | 低（懂 HTML/CSS/JS 即可） | 中（需要框架知识） |
| SEO | 天然友好 | 需要额外处理 |

## 在我们项目(yatushi)中的应用

**YTS 已经是 Vue 3 + Element Plus，不太可能推翻重来。**

可以借鉴的设计理念：
1. **CSS 变量体系**：把当前 Tailwind + Element Plus 混搭的颜色/间距/阴影统一为 `--yts-*` CSS 变量，替代分散的内联样式
2. **通用组件函数化**：`showConfirmDialog()` / `showToast()` 这类纯逻辑组件可以作为工具函数放在 `src/utils/ui.ts`，不用每次都 find 对应的 Element Plus 组件
3. **空状态/错误页标准化**：统一 404/403/500 页面和空状态占位，不用每个页面各自写

**不做**：把 Vue 项目改成 SSR，工程量大且没有实际收益。

---

