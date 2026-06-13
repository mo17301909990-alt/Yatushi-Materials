# 资源组规则类型说明

## 规则分类

### 1. **硬性规则** (is_required = TRUE)
- **定义**：必须满足的条件，不满足就FAIL
- **示例**：
  - 尺寸限制：400*350mm <= 生产尺寸 <= 1060*750mm
  - 厚度限制：纸张厚度 <= 1.5mm
  - 印张石数：>= 2000石
- **判断逻辑**：不满足就FAIL，满足就继续检查其他规则

### 2. **可选规则** (is_required = FALSE)
- **定义**：满足其中一个就可以上机，不满足但不违反阻塞规则时，不影响判断（不显示或标记为UNKNOWN）
- **示例**：
  - WC08-DCT033：板紙 [適用界面] - 满足就PASS
  - WC08-DCT033：非印刷的裱紙類（流程无印刷任务 AND 適用界面=裱紙）- 满足就PASS
- **判断逻辑**：
  - 满足其中一个可选规则组 → PASS
  - 都不满足但不违反阻塞规则 → UNKNOWN（不影响判断）
  - 都不满足且违反阻塞规则 → FAIL

### 3. **阻塞规则** (is_blocking = TRUE)
- **定义**：不可上机的原因，违反就FAIL
- **示例**：
  - 膠片 [適用界面]
  - 壓紋紙 [適用界面]
  - 产品类型不为拼图
- **判断逻辑**：违反就FAIL，优先级最高

## 规则判断优先级

1. **阻塞规则**（is_blocking = TRUE）→ 违反就FAIL，直接返回
2. **硬性规则**（is_required = TRUE）→ 不满足就FAIL
3. **可选规则**（is_required = FALSE）→ 满足其中一个组就PASS，都不满足但不违反阻塞规则时标记为UNKNOWN

## 条件组 (condition_group)

- **同组内**：AND关系（必须全部满足）
- **不同组间**：OR关系（满足其中一个组即可）
- **命名规范**：
  - 硬性规则：condition_group = NULL 或 'MUST'
  - 可选规则：condition_group = 'OPTIONAL_1', 'OPTIONAL_2' 等

## 示例：WC08-DCT033

### 规则配置：
1. **硬性规则**（如果有尺寸限制等）
2. **可选规则组1** (OPTIONAL_1)：
   - surface_type IN ('板紙')
3. **可选规则组2** (OPTIONAL_2)：
   - workflow_contains_task NOT_IN ('PRINTING') AND surface_type IN ('裱紙')
4. **阻塞规则**：
   - surface_type NOT_IN ('膠片,皮類,布類,貼紙,牛油紙,光柵片,磁膠,海棉膠,坑紙')

### 判断逻辑：
- 如果适用界面 = "板紙" → PASS（满足可选规则组1）
- 如果适用界面 = "裱紙" 且流程无印刷任务 → PASS（满足可选规则组2）
- 如果适用界面 = "普通纸" 且不违反阻塞规则 → UNKNOWN（不影响判断）
- 如果适用界面 = "膠片" → FAIL（违反阻塞规则）

## 数据库更新步骤

1. 执行 `alter_resource_rule_add_required_flag.sql` 添加 is_required 字段
2. 执行 `insert_resource_group_dept_rules.sql` 添加/更新规则数据
3. 确保所有硬性规则 is_required = TRUE（默认值）
4. 确保所有可选规则 is_required = FALSE 且 condition_group 以 'OPTIONAL_' 开头

