# YTS 端到端测试指南

> 本文档记录 E2E 测试策略、工具链与关键用户流程，供后续浏览器自动化测试参考。

---

## 目录

- [分层测试策略](#分层测试策略)
- [Skill: browse — 真实浏览器测试](#skill-browse--真实浏览器测试)
- [关键用户流程](#关键用户流程)
- [截图对比方案](#截图对比方案)
- [附录：现有测试资产](#附录现有测试资产)

---

## 分层测试策略

```
CU (烟雾层)            scripts/e2e-smoke.sh
 │                      curl 测 dev server 存活 + /api/health 代理
 │                      CI 门禁、部署后快速验证
 ▼
BDD / 浏览器层          人工 / browse skill
 │                      关键用户流程的冒烟回归
 │                      截图对比防止视觉退化
 ▼
单元 / 集成测试          npm run test:run / mvn test
                        Vitest (前端) + JUnit (后端)
                        覆盖函数级逻辑
```

**当前状态**: 烟雾层已实现（`scripts/e2e-smoke.sh`），浏览器层开发中。

---

## Skill: browse — 真实浏览器测试

项目配置了 `browse` skill，基于 [GStack Browser](https://gstack.com) 的无头浏览器，支持：

| 能力 | 用法 |
|------|------|
| 导航 | 打开任意 URL，等待页面加载 |
| 元素交互 | 点击、输入、选择、上传文件 |
| 截图 | 全页或指定元素截图，带标注 |
| 状态断言 | 检查元素存在/文本/属性/CSS 类 |
| 对话框处理 | alert/confirm/prompt 确认或取消 |
| 响应式布局 | 设置 viewport 尺寸测试移动端 |

### 常见调用方式

```bash
# 在对话中触发
browse: 打开 http://localhost:5173/login，截取登录页截图

# 更精确的指令
browse: 导航到登录页，输入用户名 admin，密码 *****，点击登录，等待跳转后截图
```

### 注意事项

- browse skill 需要项目已配置 GStack，默认在 `port 5033` 上通信
- 首次调用会启动浏览器窗口，后续操作复用同一会话
- 截图保存在 GStack 工作目录，可指定路径前缀

---

## 关键用户流程

以下流程是每次发布前应回归的核心路径，按优先级排列。

### P0: 登录 + 权限

```
1. 导航到 /login
2. 输入用户名/密码 → 点击登录
3. 验证：跳转到 /process-config，页面包含用户信息
4. 导航到 /admin → 验证管理员页面可访问
5. 导航到 /login（已登录状态）→ 应自动跳转到 /process-config
```

**视觉检查点**：
- 登录页：logo、表单布局、按钮状态
- 首页：侧边栏、面包屑、欢迎信息

### P0: 烫金匹配

```
1. 登录后进入烫金匹配页 (/tag-matching)
2. 选择一个布料类型 → 系统列出可选烫金纸
3. 选择一个烫金纸 → 系统显示兼容性详情
4. 验证匹配结果表格正确渲染
```

**视觉检查点**：
- 匹配页面：筛选器布局、结果表格、分页
- 结果详情：兼容性标识（兼容/不兼容）、备注

### P1: 丝印匹配

```
1. 登录后进入丝印匹配页 /matching/silicone
2. 选择硅胶类型 / 基材
3. 点击匹配 → 查看结果列表
```

### P1: 各 UV 匹配页

```
- /matching/uv-oil-matte        — UV 油哑光匹配
- /matching/water-varnish-matte — 水性哑光油匹配
- /matching/silicone-glow-ink   — 硅胶发光油墨匹配
- /matching/silicone-white-uv   — 硅胶白 UV 匹配
- /matching/silicone-coarse-uv  — 硅胶粗 UV 匹配
- /matching/silicone-orange-peel-uv — 硅胶橘皮 UV 匹配
- /matching/silicone-sandblast-uv   — 硅胶喷砂 UV 匹配
- /matching/silicone-wrinkle-uv     — 硅胶皱纹 UV 匹配
- /matching/silicone-watercolor-ink — 硅胶水彩墨匹配
- /matching/silicone-mica-pearl     — 硅胶珠光匹配
```

每个匹配页的共同验证点：
- 页面能正常加载（无白屏、无 JS 报错）
- 筛选条件控件可用（下拉框、输入框、按钮）
- 点击匹配后结果区域出现

### P1: 裱纸/过胶匹配

```
- /matching/lamination-material — 裱纸材料匹配
```

### P1: LEO 纸品匹配

```
- /matching/leo-book-board      — LEO 纸板匹配
- /matching/leo-flat            — LEO 平面匹配
- /matching/leo-non-flat        — LEO 非平面匹配
- /matching/leo                 — LEO 综合匹配
```

### P2: 管理员页面

```
- /admin                            — 仪表盘
- /admin/material-rule-management   — 物料规则管理
- /admin/material/hot-stamping-material — 烫金材料管理
- /admin/user-management            — 用户管理
- /admin/role-management            — 角色管理
- /admin/permission-management      — 权限管理
```

**常见坑**：
- 管理员页面需要 `ADMIN` 角色，无权限时显示空白或 403 组件
- 表格操作（新增/编辑/删除）需验证弹窗正常交互

### P2: Agent / AI 流程

```
- /agent/chat       — AI Copilot 聊天
- /agent/process    — Agent 流程
- /smart-version    — 智能版本
```

**注意**：AI 流程需要 DashScope API Key 配置，测试需 mock 后端。

---

## 截图对比方案

### 工具链

推荐两阶段方案：

| 阶段 | 工具 | 用途 |
|------|------|------|
| 当前 | __人工 + browse skill__ | 手动验证 + 截图留存 |
| 未来 | Playwright Visual Regression | 自动化截图 diff |

### Golden 截图管理

```
tests/e2e/
├── screenshots/
│   ├── golden/          # 基线截图（人工审核通过后存入）
│   │   ├── login-page.png
│   │   ├── tag-matching.png
│   │   └── ...
│   └── diff/            # 差异输出
│       └── login-page.diff.png
├── specs/               # Playwright 测试用例
│   ├── login.spec.ts
│   └── matching.spec.ts
└── config.ts
```

### 截图命名规范

```
{页面/功能名}-{场景}.png
例：
  login-page.png          — 登录页初始状态
  login-error.png         — 登录失败提示状态
  tag-matching-result.png — 烫金匹配有结果
  tag-matching-empty.png  — 烫金匹配无结果
```

### 对比策略

1. **全页截图对比**：适用于布局稳定、不依赖数据的页面（登录页、404）
2. **元素级截图对比**：对动态数据区域，只截取骨架/控件部分，忽略数据内容
3. **阈值容忍**：设 pixelmatch 阈值 0.1%，避免字体渲染差异导致 false positive

---

## 附录：现有测试资产

| 资产 | 路径 | 说明 |
|------|------|------|
| 烟雾测试脚本 | `scripts/smoke.sh` | 构建+单元测试+覆盖检查 |
| E2E 烟雾脚本 | `scripts/e2e-smoke.sh` | 服务器存活+API 代理验证 |
| API 契约测试 | `scripts/api-contract-test.sh` | 关键后端端点可达性 |
| 路由完整性检查 | `yts_project_vueai/scripts/smoke-routes.ts` | 前端路由组件存在性 |
| 前端单元测试 | `yts_project_vueai/src/**/*.spec.ts` | Vitest 单元/组件测试 |
| 后端测试 | `yts_project/src/test/` | JUnit + Mockito |
| 飞轮审计 | `scripts/flywheel-keeper.ts` | 回路一致性审计 |
| False-green 检测 | `scripts/check-false-green.ts` | 测试虚假通过检测 |

---

> **下一步建议**：搭建 Playwright E2E 框架，从登录+烫金匹配两个 P0 流程开始，配合 browse skill 做人工验证兜底。
