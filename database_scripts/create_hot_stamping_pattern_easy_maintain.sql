-- 创建便于维护的烫金图案选项表
-- 简化表结构，使用视图自动组合选项名称，便于修改维护
CREATE TABLE hot_stamping_pattern_base (
    id SERIAL PRIMARY KEY,
    option_name VARCHAR(200) NOT NULL,            -- 完整选项名称：淨線條/字母≤0.5mm
    pattern_type VARCHAR(30) NOT NULL,            -- 图案类型：淨線條/字母、淨實地、混合圖案
    line_thickness_min DECIMAL(5,2),              -- 最小线条粗细(mm)
    line_thickness_max DECIMAL(5,2),               -- 最大线条粗细(mm)
    solid_area_min DECIMAL(5,2),                  -- 最小实地尺寸(mm)
    solid_area_max DECIMAL(5,2),                  -- 最大实地尺寸(mm)
    is_active BOOLEAN DEFAULT TRUE,               -- 是否启用
    sort_order INTEGER DEFAULT 0,                -- 排序
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
W
-- 添加表注释
COMMENT ON TABLE hot_stamping_pattern_base IS '烫金图案基础数据表（便于维护）';
COMMENT ON COLUMN hot_stamping_pattern_base.option_name IS '完整选项名称，如：淨線條/字母≤0.5mm';
COMMENT ON COLUMN hot_stamping_pattern_base.pattern_type IS '图案类型：淨線條/字母、淨實地、混合圖案';
COMMENT ON COLUMN hot_stamping_pattern_base.line_thickness_min IS '最小线条粗细(mm)';
COMMENT ON COLUMN hot_stamping_pattern_base.line_thickness_max IS '最大线条粗细(mm)';
COMMENT ON COLUMN hot_stamping_pattern_base.solid_area_min IS '最小实地尺寸(mm)';
COMMENT ON COLUMN hot_stamping_pattern_base.solid_area_max IS '最大实地尺寸(mm)';
COMMENT ON COLUMN hot_stamping_pattern_base.is_active IS '是否启用';
COMMENT ON COLUMN hot_stamping_pattern_base.sort_order IS '排序顺序';

-- 创建索引
CREATE INDEX idx_base_option_name ON hot_stamping_pattern_base(option_name);
CREATE INDEX idx_base_pattern_type ON hot_stamping_pattern_base(pattern_type);
CREATE INDEX idx_base_line_thickness ON hot_stamping_pattern_base(line_thickness_min, line_thickness_max);
CREATE INDEX idx_base_solid_area ON hot_stamping_pattern_base(solid_area_min, solid_area_max);
CREATE INDEX idx_base_active ON hot_stamping_pattern_base(is_active);
CREATE INDEX idx_base_sort_order ON hot_stamping_pattern_base(sort_order);

-- 插入基础数据（包含完整选项名称）
INSERT INTO hot_stamping_pattern_base (
    option_name, pattern_type, line_thickness_min, line_thickness_max, solid_area_min, solid_area_max, 
    sort_order
) VALUES

-- 淨線條/字母
('淨線條/字母≤0.5mm', '淨線條/字母', 0.0, 0.5, NULL, NULL, 1),
('淨線條/字母0.5mm<X≤5mm', '淨線條/字母', 0.5, 5.0, NULL, NULL, 2),
('淨線條/字母5mm<X≤10mm', '淨線條/字母', 5.0, 10.0, NULL, NULL, 3),

-- 淨實地
('淨實地>10mm', '淨實地', NULL, NULL, 10.0, NULL, 4),
('淨實地10mm<X≤20mm', '淨實地', NULL, NULL, 10.0, 20.0, 5),
('淨實地>20mm', '淨實地', NULL, NULL, 20.0, NULL, 6),

-- 混合圖案
('混合圖案≤0.5mm線條+實地', '混合圖案', 0.0, 0.5, NULL, NULL, 7),
('混合圖案>0.5mm線條+實地', '混合圖案', 0.5, NULL, NULL, NULL, 8),
('混合圖案>0.5mm線條+≤20mm 實地', '混合圖案', 0.5, NULL, NULL, 20.0, 9),
('混合圖案>0.5mm線條+>20mm實地', '混合圖案', 0.5, NULL, 20.0, NULL, 10);

-- 创建简化视图：直接使用表中的option_name字段
CREATE VIEW v_hot_stamping_options AS
SELECT 
    id,
    option_name,
    pattern_type,
    line_thickness_min,
    line_thickness_max,
    solid_area_min,
    solid_area_max,
    is_active,
    sort_order
FROM hot_stamping_pattern_base
WHERE is_active = TRUE
ORDER BY sort_order;

-- 添加视图注释
COMMENT ON VIEW v_hot_stamping_options IS '烫金图案选项视图（直接使用option_name字段）';

-- 创建便捷的更新函数
CREATE OR REPLACE FUNCTION update_pattern_option(
    p_id INTEGER,
    p_line_thickness_min DECIMAL DEFAULT NULL,
    p_line_thickness_max DECIMAL DEFAULT NULL,
    p_solid_area_min DECIMAL DEFAULT NULL,
    p_solid_area_max DECIMAL DEFAULT NULL,
    p_sort_order INTEGER DEFAULT NULL
)
RETURNS BOOLEAN AS $$
BEGIN
    UPDATE hot_stamping_pattern_base 
    SET 
        line_thickness_min = COALESCE(p_line_thickness_min, line_thickness_min),
        line_thickness_max = COALESCE(p_line_thickness_max, line_thickness_max),
        solid_area_min = COALESCE(p_solid_area_min, solid_area_min),
        solid_area_max = COALESCE(p_solid_area_max, solid_area_max),
        sort_order = COALESCE(p_sort_order, sort_order),
        updated_at = CURRENT_TIMESTAMP
    WHERE id = p_id;
    
    RETURN FOUND;
END;
$$ LANGUAGE plpgsql;

-- 添加函数注释
COMMENT ON FUNCTION update_pattern_option(INTEGER, DECIMAL, DECIMAL, DECIMAL, DECIMAL, INTEGER) IS '便捷更新烫金图案选项';

-- 创建添加新选项的函数
CREATE OR REPLACE FUNCTION add_pattern_option(
    p_option_name VARCHAR,
    p_pattern_type VARCHAR,
    p_line_thickness_min DECIMAL DEFAULT NULL,
    p_line_thickness_max DECIMAL DEFAULT NULL,
    p_solid_area_min DECIMAL DEFAULT NULL,
    p_solid_area_max DECIMAL DEFAULT NULL,
    p_sort_order INTEGER DEFAULT 0
)
RETURNS INTEGER AS $$
DECLARE
    new_id INTEGER;
BEGIN
    INSERT INTO hot_stamping_pattern_base (
        option_name, pattern_type, line_thickness_min, line_thickness_max, 
        solid_area_min, solid_area_max, sort_order
    ) VALUES (
        p_option_name, p_pattern_type, p_line_thickness_min, p_line_thickness_max,
        p_solid_area_min, p_solid_area_max, p_sort_order
    ) RETURNING id INTO new_id;
    
    RETURN new_id;
END;
$$ LANGUAGE plpgsql;

-- 添加函数注释
COMMENT ON FUNCTION add_pattern_option(VARCHAR, VARCHAR, DECIMAL, DECIMAL, DECIMAL, DECIMAL, INTEGER) IS '添加新的烫金图案选项';

-- 创建删除选项的函数
CREATE OR REPLACE FUNCTION delete_pattern_option(p_id INTEGER)
RETURNS BOOLEAN AS $$
BEGIN
    UPDATE hot_stamping_pattern_base 
    SET is_active = FALSE, updated_at = CURRENT_TIMESTAMP
    WHERE id = p_id;
    
    RETURN FOUND;
END;
$$ LANGUAGE plpgsql;

-- 添加函数注释
COMMENT ON FUNCTION delete_pattern_option(INTEGER) IS '删除烫金图案选项（软删除）';

-- 创建查询所有选项名称的函数
CREATE OR REPLACE FUNCTION get_all_option_names()
RETURNS TABLE (option_name VARCHAR(200)) AS $$
BEGIN
    RETURN QUERY
    SELECT v.option_name
    FROM v_hot_stamping_options v
    ORDER BY v.paper_type, v.sort_order;
END;
$$ LANGUAGE plpgsql;

-- 添加函数注释
COMMENT ON FUNCTION get_all_option_names() IS '获取所有烫金选项名称列表';

-- 创建按图案类型查询的函数
CREATE OR REPLACE FUNCTION get_options_by_pattern_type(input_pattern_type VARCHAR)
RETURNS TABLE (
    option_name VARCHAR(200),
    pattern_type VARCHAR(30),
    line_thickness_min DECIMAL(5,2),
    line_thickness_max DECIMAL(5,2),
    solid_area_min DECIMAL(5,2),
    solid_area_max DECIMAL(5,2)
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        v.option_name,
        v.pattern_type,
        v.line_thickness_min,
        v.line_thickness_max,
        v.solid_area_min,
        v.solid_area_max
    FROM v_hot_stamping_options v
    WHERE v.pattern_type = input_pattern_type
    ORDER BY v.sort_order;
END;
$$ LANGUAGE plpgsql;

-- 添加函数注释
COMMENT ON FUNCTION get_options_by_pattern_type(VARCHAR) IS '根据图案类型获取烫金选项';

-- 验证数据完整性
DO $$
DECLARE
    record_count INTEGER;
    view_count INTEGER;
    pattern_types INTEGER;
BEGIN
    SELECT COUNT(*) INTO record_count FROM hot_stamping_pattern_base;
    SELECT COUNT(*) INTO view_count FROM v_hot_stamping_options;
    SELECT COUNT(DISTINCT pattern_type) INTO pattern_types FROM hot_stamping_pattern_base;
    
    RAISE NOTICE '烫金图案基础表已创建，共 % 条记录', record_count;
    RAISE NOTICE '视图生成 % 个选项名称', view_count;
    
    -- 验证图案类型完整性
    IF pattern_types = 3 THEN
        RAISE NOTICE '图案类型完整性验证通过：淨線條/字母、淨實地、混合圖案';
    ELSE
        RAISE WARNING '图案类型不完整，实际类型数：%', pattern_types;
    END IF;
    
    -- 验证关键选项是否存在
    IF (SELECT COUNT(*) FROM hot_stamping_pattern_base WHERE option_name = '淨線條/字母≤0.5mm') > 0 THEN
        RAISE NOTICE '关键选项验证通过：淨線條/字母≤0.5mm';
    ELSE
        RAISE WARNING '关键选项缺失：淨線條/字母≤0.5mm';
    END IF;
    
    IF (SELECT COUNT(*) FROM hot_stamping_pattern_base WHERE option_name = '混合圖案>0.5mm線條+≤20mm 實地') > 0 THEN
        RAISE NOTICE '关键选项验证通过：混合圖案>0.5mm線條+≤20mm 實地';
    ELSE
        RAISE WARNING '关键选项缺失：混合圖案>0.5mm線條+≤20mm 實地';
    END IF;
END $$;
