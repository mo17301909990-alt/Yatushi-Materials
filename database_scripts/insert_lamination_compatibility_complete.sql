-- ==============================================
-- 过胶材质兼容性完整数据插入脚本
-- 基于图片中的完整表格数据
-- ==============================================

-- 1. 插入界面类型选项数据
INSERT INTO interface_type_options (interface_name, interface_code, display_order, is_active, description) VALUES
('粉紙面+普通油墨', 'POWDER_PAPER_NORMAL_INK', 1, TRUE, '粉紙面配合普通油墨印刷'),
('粉紙面+LED UV墨', 'POWDER_PAPER_LED_UV_INK', 2, TRUE, '粉紙面配合LED UV墨印刷'),
('粉紙面+快乾油墨', 'POWDER_PAPER_QUICK_DRY_INK', 3, TRUE, '粉紙面配合快乾油墨印刷');

-- 2. 插入后加工步骤选项数据
INSERT INTO post_processing_options (step_name, step_code, display_order, is_active, description) VALUES
('無任何加工', 'NO_PROCESSING', 1, TRUE, '不进行任何后加工处理'),
('擊凹/凸、絲印UV+擊凹/凸', 'EMBOSS_DEBOSS_UV', 2, TRUE, '击凹/凸工艺，可配合丝印UV'),
('啤折切、絲印UV+啤折切', 'DIE_CUT_FOLD_UV', 3, TRUE, '啤折切工艺，可配合丝印UV'),
('踩坑位、絲印UV+踩坑位', 'CREASING_UV', 4, TRUE, '踩坑位工艺，可配合丝印UV'),
('壓紋', 'EMBOSSING', 5, TRUE, '压纹工艺');

-- 3. 插入过胶材质选项数据
INSERT INTO lamination_material_options (material_name, material_code, display_order, is_active, description, pid) VALUES
-- 普通預塗菲林
('普通預塗菲林-光膠', 'NORMAL_PRECOATED_GLOSS', 1, TRUE, '普通预涂菲林-光胶', NULL),
('普通預塗菲林-啞膠', 'NORMAL_PRECOATED_MATTE', 2, TRUE, '普通预涂菲林-哑胶', NULL),
('平價耐磨啞膠', 'ECONOMIC_WEAR_RESISTANT_MATTE', 3, TRUE, '平价耐磨哑胶', NULL),

-- 超粘預塗菲林
('超粘預塗菲林-光膠', 'SUPER_ADHESIVE_PRECOATED_GLOSS', 4, TRUE, '超粘预涂菲林-光胶', NULL),
('超粘預塗菲林-啞膠', 'SUPER_ADHESIVE_PRECOATED_MATTE', 5, TRUE, '超粘预涂菲林-哑胶', NULL),
('高價耐磨啞膠', 'PREMIUM_WEAR_RESISTANT_MATTE', 6, TRUE, '高价耐磨哑胶', NULL),

-- 水性菲林
('預塗柔感膠', 'PRECOATED_SOFT_TOUCH', 7, TRUE, '预涂柔感胶', NULL),

-- 特殊菲林
('抗靜電光膠', 'ANTI_STATIC_GLOSS', 8, TRUE, '抗静电光胶', NULL),
('抗靜電啞膠', 'ANTI_STATIC_MATTE', 9, TRUE, '抗静电哑胶', NULL),
('含相逢水性', 'XIANG_FENG_WATER_BASED', 10, TRUE, '含相逢水性', NULL);

-- 4. 插入过胶兼容性数据
-- 根据表格数据，所有组合都是兼容的（V），所以这里创建完整的兼容性矩阵

-- 定义所有烫金纸系列
WITH foil_series AS (
    SELECT unnest(ARRAY['SY6-', 'SY1-', 'SY+', 'SB', 'SA', 'S19', 'S16', 'LL', 'LA', 'L817', 'KM', 'KB', 'KA', 'G6', 'G1', 'E8', 'A23']) AS series_name
),
-- 定义界面类型映射
interface_mapping AS (
    SELECT 
        series_name,
        CASE 
            WHEN series_name IN ('LL') THEN 1  -- 粉紙面+普通油墨
            WHEN series_name IN ('LA') THEN 2  -- 粉紙面+LED UV墨
            WHEN series_name IN ('L817', 'KM', 'KB', 'KA', 'G6', 'G1', 'E8', 'A23') THEN 3  -- 粉紙面+快乾油墨
            ELSE 1  -- 默认归类到粉紙面+普通油墨
        END AS interface_type_id
    FROM foil_series
),
-- 生成所有组合
all_combinations AS (
    SELECT 
        im.series_name,
        im.interface_type_id,
        ppo.id AS post_processing_step_id,
        lmo.id AS lamination_material_id
    FROM interface_mapping im
    CROSS JOIN post_processing_options ppo
    CROSS JOIN lamination_material_options lmo
    WHERE ppo.is_active = TRUE AND lmo.is_active = TRUE
)
-- 插入兼容性数据（根据表格，所有组合都是兼容的）
INSERT INTO lamination_compatibility (foil_series, interface_type_id, post_processing_step_id, lamination_material_id, compatibility)
SELECT 
    series_name,
    interface_type_id,
    post_processing_step_id,
    lamination_material_id,
    'V'  -- 根据表格数据，所有组合都是兼容的
FROM all_combinations;

-- 创建索引以提高查询性能
CREATE INDEX IF NOT EXISTS idx_lamination_compatibility_foil_series ON lamination_compatibility(foil_series);
CREATE INDEX IF NOT EXISTS idx_lamination_compatibility_interface_type ON lamination_compatibility(interface_type_id);
CREATE INDEX IF NOT EXISTS idx_lamination_compatibility_post_processing ON lamination_compatibility(post_processing_step_id);
CREATE INDEX IF NOT EXISTS idx_lamination_compatibility_lamination_material ON lamination_compatibility(lamination_material_id);
CREATE INDEX IF NOT EXISTS idx_lamination_compatibility_compatibility ON lamination_compatibility(compatibility);

-- 验证数据插入结果
SELECT 
    '界面类型选项' AS table_name,
    COUNT(*) AS record_count
FROM interface_type_options
UNION ALL
SELECT 
    '后加工步骤选项' AS table_name,
    COUNT(*) AS record_count
FROM post_processing_options
UNION ALL
SELECT 
    '过胶材质选项' AS table_name,
    COUNT(*) AS record_count
FROM lamination_material_options
UNION ALL
SELECT 
    '过胶兼容性' AS table_name,
    COUNT(*) AS record_count
FROM lamination_compatibility;

