-- ============================================
-- 资源组选择系统 - 事业部/产品类型相关规则数据
-- ============================================
-- 说明：
-- 1. 事业部(department): 贴青事业部、精平装单部、盒子/月卡主 等
-- 2. 产品类型(product_type): 盒类、賀咭、拼圖、信封、日本文具 等
-- 3. 适用界面(surface_type): 光面、哑面、特种纸 等
-- 4. 纹路方向(grain_direction): 长纹、短纹
-- ============================================

-- ============================================
-- 更新 WC08-DCT029 全自动啤机 - 增加事业部规则
-- ============================================
-- 添加事业部相关的结构化规则
-- 规则：贴青事业部-印张>=10,000石
INSERT INTO rg_resource_rule_condition (
    rule_header_id, 
    condition_group,
    param_code, 
    param_name, 
    operator, 
    value_min, 
    value_max, 
    value_text,
    rule_type, 
    sort_order
)
VALUES 
((SELECT id FROM rg_resource_rule_header 
  WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT029') 
  AND rule_type = 'STRUCT_RULES'),
 'DEPT_TQ', 'department', '事业部', 'IN', NULL, NULL, '贴青事业部', 'STRUCT_RULE', 10),
((SELECT id FROM rg_resource_rule_header 
  WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT029') 
  AND rule_type = 'STRUCT_RULES'),
 'DEPT_TQ', 'sheet_count', '印张石数(贴青)', '>=', 10000, NULL, NULL, 'STRUCT_RULE', 11);

-- 规则：精平装单部-印张>=5,000石
INSERT INTO rg_resource_rule_condition (
    rule_header_id, 
    condition_group,
    param_code, 
    param_name, 
    operator, 
    value_min, 
    value_max, 
    value_text,
    rule_type, 
    sort_order
)
VALUES 
((SELECT id FROM rg_resource_rule_header 
  WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT029') 
  AND rule_type = 'STRUCT_RULES'),
 'DEPT_JPZ', 'department', '事业部', 'IN', NULL, NULL, '精平装单部', 'STRUCT_RULE', 12),
((SELECT id FROM rg_resource_rule_header 
  WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT029') 
  AND rule_type = 'STRUCT_RULES'),
 'DEPT_JPZ', 'sheet_count', '印张石数(精平装)', '>=', 5000, NULL, NULL, 'STRUCT_RULE', 13);

-- 规则：盒子/月卡主-印张>=5,000石
INSERT INTO rg_resource_rule_condition (
    rule_header_id, 
    condition_group,
    param_code, 
    param_name, 
    operator, 
    value_min, 
    value_max, 
    value_text,
    rule_type, 
    sort_order
)
VALUES 
((SELECT id FROM rg_resource_rule_header 
  WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT029') 
  AND rule_type = 'STRUCT_RULES'),
 'DEPT_HZ', 'department', '事业部', 'IN', NULL, NULL, '盒子/月卡主', 'STRUCT_RULE', 14),
((SELECT id FROM rg_resource_rule_header 
  WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT029') 
  AND rule_type = 'STRUCT_RULES'),
 'DEPT_HZ', 'sheet_count', '印张石数(盒子)', '>=', 5000, NULL, NULL, 'STRUCT_RULE', 15);


-- ============================================
-- WC08-DCT032 圆压圆模切机 - 产品类型：賀咭
-- ============================================
-- 该机台已在 family 字段标注为賀咭，增加产品类型规则
INSERT INTO rg_resource_rule_condition (
    rule_header_id, 
    param_code, 
    param_name, 
    operator, 
    value_min, 
    value_max, 
    value_text,
    rule_type, 
    sort_order
)
VALUES 
((SELECT id FROM rg_resource_rule_header 
  WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT032') 
  AND rule_type = 'STRUCT_RULES'),
 'product_type', '产品类型', 'IN', NULL, NULL, '賀咭,日本机,满底机', 'STRUCT_RULE', 10);


-- ============================================
-- WC05-HOT025 平面燙金機 - 日本文具产品
-- ============================================
INSERT INTO rg_resource_rule_condition (
    rule_header_id, 
    param_code, 
    param_name, 
    operator, 
    value_min, 
    value_max, 
    value_text,
    rule_type, 
    sort_order
)
VALUES 
((SELECT id FROM rg_resource_rule_header 
  WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT025') 
  AND rule_type = 'STRUCT_RULES'),
 'product_type', '产品类型', 'IN', NULL, NULL, '日本文具,GHD客户', 'STRUCT_RULE', 10);


-- ============================================
-- WC06-SSPO17 手落丝印機 - 日本文具产品
-- ============================================
INSERT INTO rg_resource_rule_condition (
    rule_header_id, 
    param_code, 
    param_name, 
    operator, 
    value_min, 
    value_max, 
    value_text,
    rule_type, 
    sort_order
)
VALUES 
((SELECT id FROM rg_resource_rule_header 
  WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC06-SSPO17') 
  AND rule_type = 'STRUCT_RULES'),
 'product_type', '产品类型', 'IN', NULL, NULL, '日本文具', 'STRUCT_RULE', 10);


-- ============================================
-- WC10-MPPO09 平張裱紙機 - 盒类产品规则
-- ============================================
INSERT INTO rg_resource_rule_condition (
    rule_header_id, 
    param_code, 
    param_name, 
    operator, 
    value_min, 
    value_max, 
    value_text,
    rule_type, 
    sort_order
)
VALUES 
((SELECT id FROM rg_resource_rule_header 
  WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC10-MPPO09') 
  AND rule_type = 'STRUCT_RULES'),
 'product_type', '产品类型', 'IN', NULL, NULL, '盒类', 'STRUCT_RULE', 10);


-- ============================================
-- 适用界面(surface_type)相关规则
-- ============================================
-- WC05-HOT023 平壓平燙金機 - 先光油后燙金只选用100-150g
-- 这意味着如果是光面（过光油后），有克重限制
INSERT INTO rg_resource_rule_condition (
    rule_header_id, 
    condition_group,
    param_code, 
    param_name, 
    operator, 
    value_min, 
    value_max, 
    value_text,
    rule_type, 
    sort_order
)
VALUES 
((SELECT id FROM rg_resource_rule_header 
  WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT023') 
  AND rule_type = 'STRUCT_RULES'),
 'SURFACE_GLOSS', 'surface_type', '适用界面', 'IN', NULL, NULL, '光面', 'STRUCT_RULE', 10),
((SELECT id FROM rg_resource_rule_header 
  WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT023') 
  AND rule_type = 'STRUCT_RULES'),
 'SURFACE_GLOSS', 'gsm', '克重(光面)', 'BETWEEN', 100, 150, NULL, 'STRUCT_RULE', 11);


-- ============================================
-- 选纸张数(selected_sheet_count)相关规则
-- ============================================
-- WC10-MPPO09 平張裱紙機 - 出紙數量<10令
INSERT INTO rg_resource_rule_condition (
    rule_header_id, 
    param_code, 
    param_name, 
    operator, 
    value_min, 
    value_max, 
    value_text,
    rule_type, 
    sort_order
)
VALUES 
((SELECT id FROM rg_resource_rule_header 
  WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC10-MPPO09') 
  AND rule_type = 'STRUCT_RULES'),
 'selected_sheet_count', '选纸张数(令)', '<=', NULL, 10, NULL, 'STRUCT_RULE', 11);

-- WC10-MPPO08 滾筒裱紙機 - 出紙數量>=10令
INSERT INTO rg_resource_rule_condition (
    rule_header_id, 
    param_code, 
    param_name, 
    operator, 
    value_min, 
    value_max, 
    value_text,
    rule_type, 
    sort_order
)
VALUES 
((SELECT id FROM rg_resource_rule_header 
  WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC10-MPPO08') 
  AND rule_type = 'STRUCT_RULES'),
 'selected_sheet_count', '选纸张数(令)', '>=', 10, NULL, NULL, 'STRUCT_RULE', 10);


    -- ============================================
    -- 纹路方向(grain_direction)相关规则
    -- ============================================
    -- WC08-DCT035 自动啤板纸机 - 灰纸<350GSM最好纹路
    -- 这里的"最好纹路"暗示了纹路方向的考量
    INSERT INTO rg_resource_rule_condition (
        rule_header_id, 
        param_code, 
        param_name, 
        operator, 
        value_min, 
        value_max, 
        value_text,
        rule_type, 
        sort_order
    )
    VALUES 
    ((SELECT id FROM rg_resource_rule_header 
    WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT035') 
    AND rule_type = 'STRUCT_RULES'),
    'grain_direction', '纹路方向', 'IN', NULL, NULL, '长纹,短纹', 'STRUCT_RULE', 10);

    -- WC08-DCT035 自动啤板纸机 - 适用界面排除双坑纸/胶片
    INSERT INTO rg_resource_rule_condition (
        rule_header_id, 
        condition_group,
        param_code, 
        param_name, 
        operator, 
        value_min, 
        value_max, 
        value_text,
        rule_type, 
        sort_order,
        is_blocking
    )
    VALUES 
    ((SELECT id FROM rg_resource_rule_header 
    WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT035') 
    AND rule_type = 'STRUCT_RULES'),
    'SURFACE_EXCLUDE', 'surface_type', '适用界面', 'NOT_IN', NULL, NULL, '雙坑紙,胶片', 'STRUCT_RULE', 11, true);

    -- WC08-DCT035 自动啤板纸机 - 满足“流程含印刷任务且适用界面为裱紙”才可上机
    INSERT INTO rg_resource_rule_condition (
        rule_header_id, 
        condition_group,
        param_code, 
        param_name, 
        operator, 
        value_min, 
        value_max, 
        value_text,
        rule_type, 
        sort_order
    )
    VALUES 
    ((SELECT id FROM rg_resource_rule_header 
    WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT035') 
    AND rule_type = 'STRUCT_RULES'),
    'SURFACE_PRINTING', 'workflow_contains_task', '流程包含任务', 'IN', NULL, NULL, 'PRINTING', 'STRUCT_RULE', 12),
    ((SELECT id FROM rg_resource_rule_header 
    WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT035') 
    AND rule_type = 'STRUCT_RULES'),
    'SURFACE_PRINTING', 'surface_type', '适用界面', 'IN', NULL, NULL, '裱紙', 'STRUCT_RULE', 13);


-- ============================================
-- WC08-DCT029 全自动啤机 - 产品类型排除规则
-- ============================================
-- 暂不上机：揭頁咭書、立體咭書 [产品类型]
INSERT INTO rg_resource_rule_condition (
    rule_header_id, 
    param_code, 
    param_name, 
    operator, 
    value_min, 
    value_max, 
    value_text,
    rule_type, 
    sort_order,
    is_blocking
)
VALUES 
((SELECT id FROM rg_resource_rule_header 
  WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT029') 
  AND rule_type = 'STRUCT_RULES'),
 'product_type', '产品类型', 'NOT_IN', NULL, NULL, '揭頁咭書,立體咭書', 'STRUCT_RULE', 20, true);


-- ============================================
-- WC08-DCT028 自动啤/手啤 - 产品类型和适用界面规则
-- ============================================
-- 可上机类型：信封 [产品类型]
INSERT INTO rg_resource_rule_condition (
    rule_header_id, 
    param_code, 
    param_name, 
    operator, 
    value_min, 
    value_max, 
    value_text,
    rule_type, 
    sort_order
)
VALUES 
((SELECT id FROM rg_resource_rule_header 
  WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT028') 
  AND rule_type = 'STRUCT_RULES'),
 'product_type', '产品类型', 'IN', NULL, NULL, '信封', 'STRUCT_RULE', 10);

-- 暂不上机：壓紋紙(公司內部壓紋紙) [適用界面]
INSERT INTO rg_resource_rule_condition (
    rule_header_id, 
    param_code, 
    param_name, 
    operator, 
    value_min, 
    value_max, 
    value_text,
    rule_type, 
    sort_order,
    is_blocking
)
VALUES 
((SELECT id FROM rg_resource_rule_header 
  WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT028') 
  AND rule_type = 'STRUCT_RULES'),
 'surface_type', '适用界面', 'NOT_IN', NULL, NULL, '壓紋紙', 'STRUCT_RULE', 11, true);


-- ============================================
-- WC08-DCT031 拼图机 - 产品类型规则
-- ============================================
-- 先创建资源组（如果不存在）
INSERT INTO rg_resource_group (resource_group_code, name, work_center_code, work_center_name, family)
SELECT 'WC08-DCT031', '拼图机', 'WC08', '啤机', NULL
WHERE NOT EXISTS (SELECT 1 FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT031');

-- 任务映射
INSERT INTO rg_resource_group_task_map (resource_group_id, task_code, support_type)
SELECT (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT031'), 'PM', 'YES'
WHERE NOT EXISTS (
    SELECT 1 FROM rg_resource_group_task_map 
    WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT031')
    AND task_code = 'PM'
);

-- 规则头
INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
SELECT (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT031'), 'STRUCT_RULES', '硬性条件'
WHERE NOT EXISTS (
    SELECT 1 FROM rg_resource_rule_header 
    WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT031')
    AND rule_type = 'STRUCT_RULES'
);

-- 上机规则：生产最大尺寸 1270*914mm
INSERT INTO rg_resource_rule_condition (
    rule_header_id, 
    param_code, 
    param_name, 
    operator, 
    value_min, 
    value_max, 
    value_text,
    rule_type, 
    sort_order
)
VALUES 
((SELECT id FROM rg_resource_rule_header 
  WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT031') 
  AND rule_type = 'STRUCT_RULES'),
 'width', '宽度(mm)', '<=', NULL, 1270, NULL, 'STRUCT_RULE', 1),
((SELECT id FROM rg_resource_rule_header 
  WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT031') 
  AND rule_type = 'STRUCT_RULES'),
 'height', '高度(mm)', '<=', NULL, 914, NULL, 'STRUCT_RULE', 2);

-- 暂不上机：产品类型不为拼图的不可用（即：只有拼图可用）
INSERT INTO rg_resource_rule_condition (
    rule_header_id, 
    param_code, 
    param_name, 
    operator, 
    value_min, 
    value_max, 
    value_text,
    rule_type, 
    sort_order,
    is_blocking
)
VALUES 
((SELECT id FROM rg_resource_rule_header 
  WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT031') 
  AND rule_type = 'STRUCT_RULES'),
 'product_type', '产品类型', 'NOT_IN', NULL, NULL, '拼图', 'STRUCT_RULE', 10, true);


-- ============================================
-- WC08-DCT034 - 适用界面排除规则
-- ============================================
-- 先创建资源组（如果不存在）
INSERT INTO rg_resource_group (resource_group_code, name, work_center_code, work_center_name, family)
SELECT 'WC08-DCT034', '待定机台', 'WC08', '啤机', NULL
WHERE NOT EXISTS (SELECT 1 FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT034');

-- 任务映射
INSERT INTO rg_resource_group_task_map (resource_group_id, task_code, support_type)
SELECT (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT034'), 'PM', 'YES'
WHERE NOT EXISTS (
    SELECT 1 FROM rg_resource_group_task_map 
    WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT034')
    AND task_code = 'PM'
);

-- 规则头
INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
SELECT (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT034'), 'STRUCT_RULES', '硬性条件'
WHERE NOT EXISTS (
    SELECT 1 FROM rg_resource_rule_header 
    WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT034')
    AND rule_type = 'STRUCT_RULES'
);

-- 上机规则：纸张厚度<=1.5mm
INSERT INTO rg_resource_rule_condition (
    rule_header_id, 
    param_code, 
    param_name, 
    operator, 
    value_min, 
    value_max, 
    value_text,
    rule_type, 
    sort_order
)
VALUES 
((SELECT id FROM rg_resource_rule_header 
  WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT034') 
  AND rule_type = 'STRUCT_RULES'),
 'thickness', '厚度(mm)', '<=', NULL, 1.5, NULL, 'STRUCT_RULE', 1);

-- 暂不上机：膠片 [適用界面]
INSERT INTO rg_resource_rule_condition (
    rule_header_id, 
    param_code, 
    param_name, 
    operator, 
    value_min, 
    value_max, 
    value_text,
    rule_type, 
    sort_order,
    is_blocking
)
VALUES 
((SELECT id FROM rg_resource_rule_header 
  WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT034') 
  AND rule_type = 'STRUCT_RULES'),
 'surface_type', '适用界面', 'NOT_IN', NULL, NULL, '膠片', 'STRUCT_RULE', 10, true);


-- ============================================
-- WC08-DCT033 自动打子啤 - 适用界面规则
-- ============================================
-- 可上机类型：板紙 [適用界面]（可选规则，满足就PASS，不满足但不违反阻塞规则时不影响判断）
INSERT INTO rg_resource_rule_condition (
    rule_header_id, 
    condition_group,
    param_code, 
    param_name, 
    operator, 
    value_min, 
    value_max, 
    value_text,
    rule_type, 
    sort_order,
    is_required
)
VALUES 
((SELECT id FROM rg_resource_rule_header 
  WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT033') 
  AND rule_type = 'STRUCT_RULES'),
 'OPTIONAL_1', 'surface_type', '适用界面', 'IN', NULL, NULL, '板紙', 'STRUCT_RULE', 10, false);

-- 可上机类型：非印刷的裱紙類（流程中没有印刷任务，且適用界面=裱纸）（可选规则组）
INSERT INTO rg_resource_rule_condition (
    rule_header_id, 
    condition_group,
    param_code, 
    param_name, 
    operator, 
    value_min, 
    value_max, 
    value_text,
    rule_type, 
    sort_order,
    is_required
)
VALUES 
((SELECT id FROM rg_resource_rule_header 
  WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT033') 
  AND rule_type = 'STRUCT_RULES'),
 'OPTIONAL_2', 'workflow_contains_task', '流程包含任务', 'NOT_IN', NULL, NULL, 'PRINTING', 'STRUCT_RULE', 11, false),
((SELECT id FROM rg_resource_rule_header 
  WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT033') 
  AND rule_type = 'STRUCT_RULES'),
 'OPTIONAL_2', 'surface_type', '适用界面', 'IN', NULL, NULL, '裱紙', 'STRUCT_RULE', 12, false);

-- 暂不上机：膠片/皮類/布類/貼紙/牛油紙/光柵片/磁膠/海棉膠/坑紙 [適用界面]
INSERT INTO rg_resource_rule_condition (
    rule_header_id, 
    param_code, 
    param_name, 
    operator, 
    value_min, 
    value_max, 
    value_text,
    rule_type, 
    sort_order,
    is_blocking
)
VALUES 
((SELECT id FROM rg_resource_rule_header 
  WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT033') 
  AND rule_type = 'STRUCT_RULES'),
 'surface_type', '适用界面', 'NOT_IN', NULL, NULL, '膠片,皮類,布類,貼紙,牛油紙,光柵片,磁膠,海棉膠,坑紙', 'STRUCT_RULE', 13, true);


-- ============================================
-- WC08-DCT030 手落啤机 - 只要不超出生产尺寸全都可以上机
-- ============================================
-- 这个规则已经在原始数据中通过尺寸限制实现了，不需要额外规则


-- ============================================
-- WC08-DCT014 手落大尺寸啤机 - 全部可以上机
-- ============================================
-- 这个机台没有特殊限制，所有产品都可以上机，不需要额外规则


