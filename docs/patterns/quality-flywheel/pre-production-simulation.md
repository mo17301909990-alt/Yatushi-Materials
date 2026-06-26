# 三轨上线前模拟

## 来源
- 项目: OpenClaw
- 关键文件:
  - CLAUDE.md 第 33-43 行
  - `src/marketing-os/user-simulator/`
  - `src/marketing-os/self-play/`

## 问题
传统"上线 → 用户反馈 → 修复"循环在 AI Agent 场景下风险极大：
- 每次上线消耗信任和用户耐心
- 真实用户遇到 bug 的反馈周期长（小时到天）
- 边缘场景在上线前几乎没有覆盖

## 方案
在上线前跑三轨模拟，枚举所有场景：

**Track A — User Simulator（用户模拟器）：**
- 定义 4 个用户画像（如：操作员、管理员、质检员、外部客户）
- 每个画像有典型的业务行为模式
- 模拟器写 15 张测试数据表，覆盖正常流程 + 异常流程
- 验证 CRUD 和数据一致性

**Track B — API Journey（API 旅程验证）：**
- 按业务路径串 API 调用（如：创建订单 → 分配工序 → 质检 → 完成）
- 验证每一步的输入输出符合契约
- 覆盖边界条件（空数据、超大数量、非法参数）

**Track C — 自博弈（Self-Play）：**
- 使用真实 LLM 调用（不是沙盘推演），模拟真实 AI 行为
- SkillRuntime 调 LLM 执行任务，验证智能体决策质量
- 发现的问题自动进入飞轮管道

**三轨递进关系：** Track A 做数据 → Track B 做验证 → Track C 做真实执行。前一个通过才能进入后一个。

## 在我们项目(yatushi)中的应用
YTS 是做印刷厂管理系统的，三轨模拟的思想可以对应到业务测试：

**Track A — 操作场景模拟：**
- 定义 3-4 个典型用户画像：生产主管（排产）、操作工（领料/报工）、质检员（检验）、财务（对账）
- 每个画像有脚本化的操作序列（如：操作工：登录 → 查看今日工单 → 领料 → 开工 → 完工报工）
- 脚本调用后端 API，验证每一步的数据库状态正确

**Track B — 业务流程集成测试：**
- 用 Spring Boot 的 `@SpringBootTest` + `TestRestTemplate` 串完整的业务流程
- 覆盖兼容性匹配这个核心流程：选择材料 → 系统匹配兼容工序 → 生成 BOM → 计算价格
- 覆盖边界条件：空 BOM、不兼容材料组合、超大订单

**Track C — AI 兼容性推荐测试：**
- YTS 已接入阿里通义千问（`DashScopeChatService`），可以模拟 AI 推荐场景
- 用真实 LLM 调用验证 AI 推荐的兼容性组合是否合理
- AI 推荐结果和规则引擎匹配结果做交叉验证

**具体落地步骤：**
1. 在 `yts_project/src/test/java/` 下建立 `simulation/` 测试包
2. Track A：用 `@Sql` 注解准备测试数据集，`TestRestTemplate` 执行操作序列
3. Track B：用 `@Nested` 组织业务流程测试类，每个流程一个内部类
4. Track C：Mock 外部 LLM 调用（用 WireMock）或在开发环境配真实的 DashScope key

这个模式对 YTS 最直接的价值是：**印刷厂的物料兼容性匹配是核心高价值功能，上线前必须保证所有组合不遗漏、不冲突**。
