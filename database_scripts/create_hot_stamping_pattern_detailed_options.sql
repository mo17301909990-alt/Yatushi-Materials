-- 创建烫金图案详细选项表
-- 用于存储烫金图案的详细规格选项，包括线条粗细、实地尺寸、混合图案等
CREATE TABLE hot_stamping_pattern_detailed_options (
    id SERIAL PRIMARY KEY,
    pattern_category VARCHAR(30) NOT NULL,        -- 图案分类：淨線條/字母、淨實地、混合圖案
    line_thickness_range VARCHAR(50),             -- 线条粗细范围：≦0.5mm、0.5mm<X≦5mm、5mm<X≦10mm
    solid_area_range VARCHAR(50),                  -- 实地尺寸范围：>10mm、10mm<X≦20mm、>20mm
    mixed_pattern_description TEXT,                 -- 混合图案描述：≦0.5mm線條+實地、>0.5mm線條+實地等
    paper_type VARCHAR(50) NOT NULL,               -- 纸张类型：普通金紙、普通金紙（不作區分）
    min_line_thickness DECIMAL(5,2),               -- 最小线条粗细(mm)
    max_line_thickness DECIMAL(5,2),                -- 最大线条粗细(mm)
    min_solid_area DECIMAL(5,2),                    -- 最小实地尺寸(mm)
    max_solid_area DECIMAL(5,2),                   -- 最大实地尺寸(mm)
    is_active BOOLEAN DEFAULT TRUE,               -- 是否启用
    sort_order INTEGER DEFAULT 0,                 -- 排序
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 添加表注释
COMMENT ON TABLE hot_stamping_pattern_detailed_options IS '烫金图案详细选项表';
COMMENT ON COLUMN hot_stamping_pattern_detailed_options.pattern_category IS '图案分类：淨線條/字母、淨實地、混合圖案';
COMMENT ON COLUMN hot_stamping_pattern_detailed_options.line_thickness_range IS '线条粗细范围描述';
COMMENT ON COLUMN hot_stamping_pattern_detailed_options.solid_area_range IS '实地尺寸范围描述';
COMMENT ON COLUMN hot_stamping_pattern_detailed_options.mixed_pattern_description IS '混合图案详细描述';
COMMENT ON COLUMN hot_stamping_pattern_detailed_options.paper_type IS '纸张类型：普通金紙、普通金紙（不作區分）';
COMMENT ON COLUMN hot_stamping_pattern_detailed_options.min_line_thickness IS '最小线条粗细(mm)';
COMMENT ON COLUMN hot_stamping_pattern_detailed_options.max_line_thickness IS '最大线条粗细(mm)';
COMMENT ON COLUMN hot_stamping_pattern_detailed_options.min_solid_area IS '最小实地尺寸(mm)';
COMMENT ON COLUMN hot_stamping_pattern_detailed_options.max_solid_area IS '最大实地尺寸(mm)';
COMMENT ON COLUMN hot_stamping_pattern_detailed_options.is_active IS '是否启用';
COMMENT ON COLUMN hot_stamping_pattern_detailed_options.sort_order IS '排序顺序';

-- 创建索引
CREATE INDEX idx_detailed_pattern_category ON hot_stamping_pattern_detailed_options(pattern_category);
CREATE INDEX idx_detailed_paper_type ON hot_stamping_pattern_detailed_options(paper_type);
CREATE INDEX idx_detailed_line_thickness ON hot_stamping_pattern_detailed_options(min_line_thickness, max_line_thickness);
CREATE INDEX idx_detailed_solid_area ON hot_stamping_pattern_detailed_options(min_solid_area, max_solid_area);
CREATE INDEX idx_detailed_active ON hot_stamping_pattern_detailed_options(is_active);
CREATE INDEX idx_detailed_sort_order ON hot_stamping_pattern_detailed_options(sort_order);

-- 插入烫金图案详细选项数据
INSERT INTO hot_stamping_pattern_detailed_options (
    pattern_category, line_thickness_range, solid_area_range, mixed_pattern_description, 
    paper_type, min_line_thickness, max_line_thickness, min_solid_area, max_solid_area, sort_order
) VALUES

-- 普通金紙 - 淨線條/字母
('淨線條/字母', '≦0.5mm', NULL, NULL, '普通金紙', 0.0, 0.5, NULL, NULL, 1),
('淨線條/字母', '0.5mm<X≦5mm', NULL, NULL, '普通金紙', 0.5, 5.0, NULL, NULL, 2),
('淨線條/字母', '5mm<X≦10mm', NULL, NULL, '普通金紙', 5.0, 10.0, NULL, NULL, 3),

-- 普通金紙 - 淨實地
('淨實地', NULL, '>10mm', NULL, '普通金紙', NULL, NULL, 10.0, NULL, 4),

-- 普通金紙 - 混合圖案
('混合圖案', '≦0.5mm', NULL, '≦0.5mm線條+實地', '普通金紙', 0.0, 0.5, NULL, NULL, 5),
('混合圖案', '>0.5mm', NULL, '>0.5mm線條+實地', '普通金紙', 0.5, NULL, NULL, NULL, 6),

-- 普通金紙（不作區分）- 淨線條/字母
('淨線條/字母', '≦0.5mm', NULL, NULL, '普通金紙（不作區分）', 0.0, 0.5, NULL, NULL, 7),
('淨線條/字母', '0.5mm<X≦5mm', NULL, NULL, '普通金紙（不作區分）', 0.5, 5.0, NULL, NULL, 8),
('淨線條/字母', '5mm<X≦10mm', NULL, NULL, '普通金紙（不作區分）', 5.0, 10.0, NULL, NULL, 9),

-- 普通金紙（不作區分）- 淨實地
('淨實地', NULL, '10mm<X≦20mm', NULL, '普通金紙（不作區分）', NULL, NULL, 10.0, 20.0, 10),
('淨實地', NULL, '>20mm', NULL, '普通金紙（不作區分）', NULL, NULL, 20.0, NULL, 11),

-- 普通金紙（不作區分）- 混合圖案
('混合圖案', '≦0.5mm', NULL, '≦0.5mm線條+實地', '普通金紙（不作區分）', 0.0, 0.5, NULL, NULL, 12),
('混合圖案', '>0.5mm', '≦20mm', '>0.5mm線條+≦20mm 實地', '普通金紙（不作區分）', 0.5, NULL, NULL, 20.0, 13),
('混合圖案', '>0.5mm', '>20mm', '>0.5mm線條+>20mm實地', '普通金紙（不作區分）', 0.5, NULL, 20.0, NULL, 14);

-- 创建视图：按图案分类汇总
CREATE VIEW v_hot_stamping_detailed_summary AS
SELECT 
    pattern_category,
    paper_type,
    COUNT(*) as option_count,
    STRING_AGG(DISTINCT line_thickness_range, ', ') as line_thickness_options,
    STRING_AGG(DISTINCT solid_area_range, ', ') as solid_area_options,
    STRING_AGG(DISTINCT mixed_pattern_description, ', ') as mixed_pattern_options
FROM hot_stamping_pattern_detailed_options 
WHERE is_active = TRUE
GROUP BY pattern_category, paper_type
ORDER BY paper_type, pattern_category, sort_order;

-- 添加视图注释
COMMENT ON VIEW v_hot_stamping_detailed_summary IS '烫金图案详细选项汇总视图';

-- 创建视图：按纸张类型汇总
CREATE VIEW v_hot_stamping_by_paper_type AS
SELECT 
    paper_type,
    COUNT(*) as total_options,
    COUNT(CASE WHEN pattern_category = '淨線條/字母' THEN 1 END) as line_letter_options,
    COUNT(CASE WHEN pattern_category = '淨實地' THEN 1 END) as solid_area_options,
    COUNT(CASE WHEN pattern_category = '混合圖案' THEN 1 END) as mixed_pattern_options
FROM hot_stamping_pattern_detailed_options 
WHERE is_active = TRUE
GROUP BY paper_type
ORDER BY paper_type;

-- 添加视图注释
COMMENT ON VIEW v_hot_stamping_by_paper_type IS '按纸张类型汇总烫金图案选项';

-- 创建函数：根据线条粗细获取适用选项
CREATE OR REPLACE FUNCTION get_pattern_options_by_line_thickness(input_thickness DECIMAL)
RETURNS TABLE (
    pattern_category VARCHAR(30),
    line_thickness_range VARCHAR(50),
    paper_type VARCHAR(50),
    mixed_pattern_description TEXT
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        hpd.pattern_category,
        hpd.line_thickness_range,
        hpd.paper_type,
        hpd.mixed_pattern_description
    FROM hot_stamping_pattern_detailed_options hpd
    WHERE hpd.is_active = TRUE
    AND hpd.line_thickness_range IS NOT NULL
    AND (
        (hpd.min_line_thickness IS NULL OR input_thickness >= hpd.min_line_thickness)
        AND (hpd.max_line_thickness IS NULL OR input_thickness <= hpd.max_line_thickness)
    )
    ORDER BY hpd.paper_type, hpd.sort_order;
END;
$$ LANGUAGE plpgsql;

-- 添加函数注释
COMMENT ON FUNCTION get_pattern_options_by_line_thickness(DECIMAL) IS '根据线条粗细获取适用的烫金图案选项';

-- 创建函数：根据实地尺寸获取适用选项
CREATE OR REPLACE FUNCTION get_pattern_options_by_solid_area(input_area DECIMAL)
RETURNS TABLE (
    pattern_category VARCHAR(30),
    solid_area_range VARCHAR(50),
    paper_type VARCHAR(50),
    mixed_pattern_description TEXT
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        hpd.pattern_category,
        hpd.solid_area_range,
        hpd.paper_type,
        hpd.mixed_pattern_description
    FROM hot_stamping_pattern_detailed_options hpd
    WHERE hpd.is_active = TRUE
    AND hpd.solid_area_range IS NOT NULL
    AND (
        (hpd.min_solid_area IS NULL OR input_area >= hpd.min_solid_area)
        AND (hpd.max_solid_area IS NULL OR input_area <= hpd.max_solid_area)
    )
    ORDER BY hpd.paper_type, hpd.sort_order;
END;
$$ LANGUAGE plpgsql;

-- 添加函数注释
COMMENT ON FUNCTION get_pattern_options_by_solid_area(DECIMAL) IS '根据实地尺寸获取适用的烫金图案选项';

-- 创建关联视图：结合基础规格和详细选项
CREATE VIEW v_hot_stamping_complete_options AS
SELECT 
    hps.id as basic_id,
    hps.area_category,
    hps.area_range,
    hps.pattern_type,
    hpd.id as detailed_id,
    hpd.pattern_category,
    hpd.line_thickness_range,
    hpd.solid_area_range,
    hpd.mixed_pattern_description,
    hpd.paper_type,
    hps.applicable_scenarios,
    hps.is_active
FROM hot_stamping_pattern_specifications hps
LEFT JOIN hot_stamping_pattern_detailed_options hpd ON hps.pattern_type = hpd.pattern_category
WHERE hps.is_active = TRUE AND (hpd.is_active = TRUE OR hpd.id IS NULL)
ORDER BY hps.sort_order, hpd.sort_order;

-- 添加视图注释
COMMENT ON VIEW v_hot_stamping_complete_options IS '烫金图案完整选项关联视图';

-- 验证数据完整性
DO $$
DECLARE
    record_count INTEGER;
    category_count INTEGER;
    paper_type_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO record_count FROM hot_stamping_pattern_detailed_options;
    RAISE NOTICE '烫金图案详细选项表已创建，共插入 % 条记录', record_count;
    
    -- 验证图案分类完整性
    SELECT COUNT(DISTINCT pattern_category) INTO category_count FROM hot_stamping_pattern_detailed_options;
    IF category_count = 3 THEN
        RAISE NOTICE '图案分类完整性验证通过：淨線條/字母、淨實地、混合圖案';
    ELSE
        RAISE WARNING '图案分类不完整，实际分类数：%', category_count;
    END IF;
    
    -- 验证纸张类型完整性
    SELECT COUNT(DISTINCT paper_type) INTO paper_type_count FROM hot_stamping_pattern_detailed_options;
    IF paper_type_count = 2 THEN
        RAISE NOTICE '纸张类型完整性验证通过：普通金紙、普通金紙（不作區分）';
    ELSE
        RAISE WARNING '纸张类型不完整，实际类型数：%', paper_type_count;
    END IF;
    
    -- 验证线条粗细范围完整性
    IF (SELECT COUNT(*) FROM hot_stamping_pattern_detailed_options WHERE line_thickness_range IS NOT NULL) >= 6 THEN
        RAISE NOTICE '线条粗细范围完整性验证通过';
    ELSE
        RAISE WARNING '线条粗细范围不完整';
    END IF;
    
    -- 验证实地尺寸范围完整性
    IF (SELECT COUNT(*) FROM hot_stamping_pattern_detailed_options WHERE solid_area_range IS NOT NULL) >= 3 THEN
        RAISE NOTICE '实地尺寸范围完整性验证通过';
    ELSE
        RAISE WARNING '实地尺寸范围不完整';
    END IF;
    
    -- 验证混合图案描述完整性
    IF (SELECT COUNT(*) FROM hot_stamping_pattern_detailed_options WHERE mixed_pattern_description IS NOT NULL) >= 4 THEN
        RAISE NOTICE '混合图案描述完整性验证通过';
    ELSE
        RAISE WARNING '混合图案描述不完整';
    END IF;
END $$;

