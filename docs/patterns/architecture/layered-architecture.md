# 三层分层架构与依赖方向强制

## 来源
- 项目: OpenClaw + ERPAI
- 关键文件:
  - OpenClaw: CLAUDE.md 架构层次图 + 禁止反向 import 规则
  - ERPAI: `.claude/rules/agents.md` + `repositories/` 目录结构

## 问题
多 Agent / 多模块项目中，模块间依赖关系不清晰，导致：
- 底层模块被上层直接 import，修改底层影响所有调用方
- 平台工程师无意间写了业务逻辑，业务工程师又碰了基础设施
- 代码评审没有统一的依赖违例检查标准，全靠自觉

## 方案
OpenClaw 采用**三层严格分层**，依赖方向只许向下，禁止反向：
```
react-c/ (UI 渲染层)              ─ 禁止 import src/
    ↓
src/marketing-os/ (行业应用层)     ─ 禁止 import platform/
src/agent/
    ↓
src/platform/ (平台核心层)         ─ 被上层依赖，不依赖上层
src/data/
```

ERPAI 采用类似的五层结构：
```
ui/ (SSR 前端)
    ↓
agents/ (26 个数字员工)
    ↓
memory/ (记忆层)
    ↓
capabilities/ (Skill 执行层)
    ↓
repositories/ + metadata/ (数据层)
```

两套方案的共同要点：
1. **枚举每层的职责边界** — 不模糊、不重叠
2. **用目录结构物理表达** — 不依赖文档约定，目录本身就是架构图
3. **CI 或 Hook 检查 import 方向** — 违反即报错
4. **层间通信只通过接口** — 不直接 import 内部实现

## 在我们项目(yatushi)中的应用
YTS 当前后端是 `controller/ → service/ → mapper/ → MyBatis XML → PostgreSQL`，分层已经清晰但缺乏**依赖方向的形式化检查**。可以引入：
1. 在 CLAUDE.md 中显式画出架构图并标注依赖方向红线
2. 用 ArchUnit（Java）写单元测试，在 `mvn test` 阶段检查 package 依赖方向 — 例如禁止 controller 直接 import mapper，禁止 service 访问 HTTP 请求对象
3. 前端 Vue 同理：禁止 `views/` 直接 import `api/modules/` 的 axios 调用，必须走 store 或 service 封装

前端 Vite 项目也可以通过 `import-inspector` 或自定义 ESLint rule 限制 import 路径范围。

---

