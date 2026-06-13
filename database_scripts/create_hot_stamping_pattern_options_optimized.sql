-- 创建优化的烫金图案选项表
-- 直接支持查询格式：淨線條/字母≤0.5mm、淨實地>10mm 等
CREATE TABLE hot_stamping_pattern_options (
    id SERIAL PRIMARY KEY,
    option_name VARCHAR(100) NOT NULL UNIQUE,      -- 完整选项名称：淨線條/字母≤0.5mm
    pattern_type VARCHAR(30) NOT NULL,            -- 图案类型：淨線條/字母、淨實地、混合圖案
    line_thickness_range VARCHAR(50),             -- 线条粗细范围：≤0.5mm、0.5mm<X≤5mm、5mm<X≤10mm
    solid_area_range VARCHAR(50),                 -- 实地尺寸范围：>10mm、10mm<X≤20mm、>20mm
    mixed_pattern_description TEXT,                -- 混合图案描述：≤0.5mm線條+實地、>0.5mm線條+實地等
    paper_type VARCHAR(50) NOT NULL,              -- 纸张类型：普通金紙、普通金紙（不作區分）
    min_line_thickness DECIMAL(5,2),              -- 最小线条粗细(mm)
    max_line_thickness DECIMAL(5,2),               -- 最大线条粗细(mm)
    min_solid_area DECIMAL(5,2),                   -- 最小实地尺寸(mm)
    max_solid_area DECIMAL(5,2),                  -- 最大实地尺寸(mm)
    is_active BOOLEAN DEFAULT TRUE,               -- 是否启用
    sort_order INTEGER DEFAULT 0,                -- 排序
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 添加表注释
COMMENT ON TABLE hot_stamping_pattern_options IS '烫金图案选项表（优化版）';
COMMENT ON COLUMN hot_stamping_pattern_options.option_name IS '完整选项名称，如：淨線條/字母≤0.5mm';
COMMENT ON COLUMN hot_stamping_pattern_options.pattern_type IS '图案类型：淨線條/字母、淨實地、混合圖案';
COMMENT ON COLUMN hot_stamping_pattern_options.line_thickness_range IS '线条粗细范围描述';
COMMENT ON COLUMN hot_stamping_pattern_options.solid_area_range IS '实地尺寸范围描述';
COMMENT ON COLUMN hot_stamping_pattern_options.mixed_pattern_description IS '混合图案详细描述';
COMMENT ON COLUMN hot_stamping_pattern_options.paper_type IS '纸张类型：普通金紙、普通金紙（不作區分）';
COMMENT ON COLUMN hot_stamping_pattern_options.min_line_thickness IS '最小线条粗细(mm)';
COMMENT ON COLUMN hot_stamping_pattern_options.max_line_thickness IS '最大线条粗细(mm)';
COMMENT ON COLUMN hot_stamping_pattern_options.min_solid_area IS '最小实地尺寸(mm)';
COMMENT ON COLUMN hot_stamping_pattern_options.max_solid_area IS '最大实地尺寸(mm)';
COMMENT ON COLUMN hot_stamping_pattern_options.is_active IS '是否启用';
COMMENT ON COLUMN hot_stamping_pattern_options.sort_order IS '排序顺序';

-- 创建索引
CREATE INDEX idx_options_name ON hot_stamping_pattern_options(option_name);
CREATE INDEX idx_options_pattern_type ON hot_stamping_pattern_options(pattern_type);
CREATE INDEX idx_options_paper_type ON hot_stamping_pattern_options(paper_type);
CREATE INDEX idx_options_line_thickness ON hot_stamping_pattern_options(min_line_thickness, max_line_thickness);
CREATE INDEX idx_options_solid_area ON hot_stamping_pattern_options(min_solid_area, max_solid_area);
CREATE INDEX idx_options_active ON hot_stamping_pattern_options(is_active);
CREATE INDEX idx_options_sort_order ON hot_stamping_pattern_options(sort_order);

-- 插入烫金图案选项数据（按照您需要的查询格式）
INSERT INTO hot_stamping_pattern_options (
    option_name, pattern_type, line_thickness_range, solid_area_range, mixed_pattern_description, 
    paper_type, min_line_thickness, max_line_thickness, min_solid_area, max_solid_area, sort_order
) VALUES

-- 普通金紙 - 淨線條/字母
('淨線條/字母≤0.5mm', '淨線條/字母', '≤0.5mm', NULL, NULL, '普通金紙', 0.0, 0.5, NULL, NULL, 1),
('淨線條/字母0.5mm<X≤5mm', '淨線條/字母', '0.5mm<X≤5mm', NULL, NULL, '普通金紙', 0.5, 5.0, NULL, NULL, 2),
('淨線條/字母5mm<X≤10mm', '淨線條/字母', '5mm<X≤10mm', NULL, NULL, '普通金紙', 5.0, 10.0, NULL, NULL, 3),

-- 普通金紙 - 淨實地
('淨實地>10mm', '淨實地', NULL, '>10mm', NULL, '普通金紙', NULL, NULL, 10.0, NULL, 4),

-- 普通金紙 - 混合圖案
('混合圖案≤0.5mm線條+實地', '混合圖案', '≤0.5mm', NULL, '≤0.5mm線條+實地', '普通金紙', 0.0, 0.5, NULL, NULL, 5),
('混合圖案>0.5mm線條+實地', '混合圖案', '>0.5mm', NULL, '>0.5mm線條+實地', '普通金紙', 0.5, NULL, NULL, NULL, 6),

-- 普通金紙（不作區分）- 淨線條/字母
('淨線條/字母≤0.5mm', '淨線條/字母', '≤0.5mm', NULL, NULL, '普通金紙（不作區分）', 0.0, 0.5, NULL, NULL, 7),
('淨線條/字母0.5mm<X≤5mm', '淨線條/字母', '0.5mm<X≤5mm', NULL, NULL, '普通金紙（不作區分）', 0.5, 5.0, NULL, NULL, 8),
('淨線條/字母5mm<X≤10mm', '淨線條/字母', '5mm<X≤10mm', NULL, NULL, '普通金紙（不作區分）', 5.0, 10.0, NULL, NULL, 9),

-- 普通金紙（不作區分）- 淨實地
('淨實地10mm<X≤20mm', '淨實地', NULL, '10mm<X≤20mm', NULL, '普通金紙（不作區分）', NULL, NULL, 10.0, 20.0, 10),
('淨實地>20mm', '淨實地', NULL, '>20mm', NULL, '普通金紙（不作區分）', NULL, NULL, 20.0, NULL, 11),

-- 普通金紙（不作區分）- 混合圖案
('混合圖案≤0.5mm線條+實地', '混合圖案', '≤0.5mm', NULL, '≤0.5mm線條+實地', '普通金紙（不作區分）', 0.0, 0.5, NULL, NULL, 12),
('混合圖案>0.5mm線條+≤20mm 實地', '混合圖案', '>0.5mm', '≤20mm', '>0.5mm線條+≤20mm 實地', '普通金紙（不作區分）', 0.5, NULL, NULL, 20.0, 13),
('混合圖案>0.5mm線條+>20mm實地', '混合圖案', '>0.5mm', '>20mm', '>0.5mm線條+>20mm實地', '普通金紙（不作區分）', 0.5, NULL, 20.0, NULL, 14);

-- 创建视图：按图案类型分组显示
CREATE VIEW v_hot_stamping_options_by_type AS
SELECT 
    pattern_type,
    COUNT(*) as option_count,
    STRING_AGG(option_name, ', ' ORDER BY sort_order) as all_options
FROM hot_stamping_pattern_options 
WHERE is_active = TRUE
GROUP BY pattern_type
ORDER BY MIN(sort_order);

-- 添加视图注释
COMMENT ON VIEW v_hot_stamping_options_by_type IS '按图案类型分组的烫金选项视图';

-- 创建视图：按纸张类型分组显示
CREATE VIEW v_hot_stamping_options_by_paper AS
SELECT 
    paper_type,
    COUNT(*) as option_count,
    STRING_AGG(option_name, ', ' ORDER BY sort_order) as all_options
FROM hot_stamping_pattern_options 
WHERE is_active = TRUE
GROUP BY paper_type
ORDER BY paper_type;

-- 添加视图注释
COMMENT ON VIEW v_hot_stamping_options_by_paper IS '按纸张类型分组的烫金选项视图';

-- 创建视图：完整选项列表（按您需要的格式）
CREATE VIEW v_hot_stamping_options_list AS
SELECT 
    option_name,
    pattern_type,
    paper_type,
    line_thickness_range,
    solid_area_range,
    mixed_pattern_description,
    sort_order
FROM hot_stamping_pattern_options 
WHERE is_active = TRUE
ORDER BY paper_type, sort_order;

-- 添加视图注释
COMMENT ON VIEW v_hot_stamping_options_list IS '烫金选项完整列表视图';

-- 创建函数：根据图案类型获取选项
CREATE OR REPLACE FUNCTION get_options_by_pattern_type(input_pattern_type VARCHAR)
RETURNS TABLE (
    option_name VARCHAR(100),
    pattern_type VARCHAR(30),
    paper_type VARCHAR(50),
    line_thickness_range VARCHAR(50),
    solid_area_range VARCHAR(50),
    mixed_pattern_description TEXT
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        hpo.option_name,
        hpo.pattern_type,
        hpo.paper_type,
        hpo.line_thickness_range,
        hpo.solid_area_range,
        hpo.mixed_pattern_description
    FROM hot_stamping_pattern_options hpo
    WHERE hpo.is_active = TRUE
    AND hpo.pattern_type = input_pattern_type
    ORDER BY hpo.paper_type, hpo.sort_order;
END;
$$ LANGUAGE plpgsql;

-- 添加函数注释
COMMENT ON FUNCTION get_options_by_pattern_type(VARCHAR) IS '根据图案类型获取烫金选项';

-- 创建函数：根据纸张类型获取选项
CREATE OR REPLACE FUNCTION get_options_by_paper_type(input_paper_type VARCHAR)
RETURNS TABLE (
    option_name VARCHAR(100),
    pattern_type VARCHAR(30),
    paper_type VARCHAR(50),
    line_thickness_range VARCHAR(50),
    solid_area_range VARCHAR(50),
    mixed_pattern_description TEXT
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        hpo.option_name,
        hpo.pattern_type,
        hpo.paper_type,
        hpo.line_thickness_range,
        hpo.solid_area_range,
        hpo.mixed_pattern_description
    FROM hot_stamping_pattern_options hpo
    WHERE hpo.is_active = TRUE
    AND hpo.paper_type = input_paper_type
    ORDER BY hpo.sort_order;
END;
$$ LANGUAGE plpgsql;

-- 添加函数注释
COMMENT ON FUNCTION get_options_by_paper_type(VARCHAR) IS '根据纸张类型获取烫金选项';

-- 创建函数：获取所有选项名称列表（按您需要的格式）
CREATE OR REPLACE FUNCTION get_all_option_names()
RETURNS TABLE (option_name VARCHAR(100)) AS $$
BEGIN
    RETURN QUERY
    SELECT hpo.option_name
    FROM hot_stamping_pattern_options hpo
    WHERE hpo.is_active = TRUE
    ORDER BY hpo.paper_type, hpo.sort_order;
END;
$$ LANGUAGE plpgsql;

-- 添加函数注释
COMMENT ON FUNCTION get_all_option_names() IS '获取所有烫金选项名称列表';

-- 验证数据完整性
DO $$
DECLARE
    record_count INTEGER;
    unique_names INTEGER;
    pattern_types INTEGER;
    paper_types INTEGER;
BEGIN
    SELECT COUNT(*) INTO record_count FROM hot_stamping_pattern_options;
    RAISE NOTICE '烫金图案选项表已创建，共插入 % 条记录', record_count;
    
    -- 验证选项名称唯一性
    SELECT COUNT(DISTINCT option_name) INTO unique_names FROM hot_stamping_pattern_options;
    IF unique_names = record_count THEN
        RAISE NOTICE '选项名称唯一性验证通过';
    ELSE
        RAISE WARNING '选项名称不唯一，实际唯一数：%', unique_names;
    END IF;
    
    -- 验证图案类型完整性
    SELECT COUNT(DISTINCT pattern_type) INTO pattern_types FROM hot_stamping_pattern_options;
    IF pattern_types = 3 THEN
        RAISE NOTICE '图案类型完整性验证通过：淨線條/字母、淨實地、混合圖案';
    ELSE
        RAISE WARNING '图案类型不完整，实际类型数：%', pattern_types;
    END IF;
    
    -- 验证纸张类型完整性
    SELECT COUNT(DISTINCT paper_type) INTO paper_types FROM hot_stamping_pattern_options;
    IF paper_types = 2 THEN
        RAISE NOTICE '纸张类型完整性验证通过：普通金紙、普通金紙（不作區分）';
    ELSE
        RAISE WARNING '纸张类型不完整，实际类型数：%', paper_types;
    END IF;
    
    -- 验证关键选项是否存在
    IF (SELECT COUNT(*) FROM hot_stamping_pattern_options WHERE option_name LIKE '%淨線條/字母≤0.5mm%') > 0 THEN
        RAISE NOTICE '关键选项验证通过：淨線條/字母≤0.5mm';
    ELSE
        RAISE WARNING '关键选项缺失：淨線條/字母≤0.5mm';
    END IF;
    
    IF (SELECT COUNT(*) FROM hot_stamping_pattern_options WHERE option_name LIKE '%混合圖案>0.5mm線條+≤20mm 實地%') > 0 THEN
        RAISE NOTICE '关键选项验证通过：混合圖案>0.5mm線條+≤20mm 實地';
    ELSE
        RAISE WARNING '关键选项缺失：混合圖案>0.5mm線條+≤20mm 實地';
    END IF;
END $$;

