-- 创建烫金类型选项表
-- 用于存储烫金工艺类型和位置选项
CREATE TABLE hot_stamping_type_options (
    id SERIAL PRIMARY KEY,
    stamping_type VARCHAR(50) NOT NULL,           -- 烫金类型：平面烫金、立体烫金、磨砂烫金、折光烫金
    position_type VARCHAR(30),                    -- 位置类型：於中間位、到邊位
    description TEXT,                             -- 详细描述
    is_active BOOLEAN DEFAULT TRUE,               -- 是否启用
    sort_order INTEGER DEFAULT 0,                -- 排序
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 添加表注释
COMMENT ON TABLE hot_stamping_type_options IS '烫金类型选项表';
COMMENT ON COLUMN hot_stamping_type_options.stamping_type IS '烫金类型：平面烫金、立体烫金、磨砂烫金、折光烫金';
COMMENT ON COLUMN hot_stamping_type_options.position_type IS '位置类型：於中間位、到邊位';
COMMENT ON COLUMN hot_stamping_type_options.description IS '详细描述';
COMMENT ON COLUMN hot_stamping_type_options.is_active IS '是否启用';
COMMENT ON COLUMN hot_stamping_type_options.sort_order IS '排序顺序';

-- 创建索引
CREATE INDEX idx_type_stamping_type ON hot_stamping_type_options(stamping_type);
CREATE INDEX idx_type_position_type ON hot_stamping_type_options(position_type);
CREATE INDEX idx_type_active ON hot_stamping_type_options(is_active);
CREATE INDEX idx_type_sort_order ON hot_stamping_type_options(sort_order);

-- 插入烫金类型选项数据
INSERT INTO hot_stamping_type_options (
    stamping_type, position_type, description, sort_order
) VALUES

-- 平面燙金（包括放中间和放旁边，以及默认）
('平面燙金', NULL, '平面燙金工艺（默认）', 1),
('平面燙金', '於中間位', '平面燙金工艺，在中间位置进行烫金', 2),
('平面燙金', '到邊位', '平面燙金工艺，烫金到边缘位置', 3),

-- 立體燙金（默认）
('立體燙金', NULL, '立體燙金工艺（默认）', 4),

-- 磨砂燙金（默认）
('磨砂燙金', NULL, '磨砂燙金工艺（默认）', 5),

-- 折光燙金（默认）
('折光燙金', NULL, '折光燙金工艺（默认）', 6),

-- 燙金後擊凸（默认）
('燙金後擊凸', NULL, '燙金後擊凸工艺（默认）', 7),

-- 燙金後壓紋（默认）
('燙金後壓紋', NULL, '燙金後壓紋工艺（默认）', 8),

-- 有紋燙金（默认）
('有紋燙金', NULL, '有紋燙金工艺（默认）', 9);

-- 创建视图：按烫金类型分组
CREATE VIEW v_hot_stamping_types_by_category AS
SELECT 
    stamping_type,
    COUNT(*) as option_count,
    STRING_AGG(
        CASE 
            WHEN position_type IS NULL THEN stamping_type
            ELSE stamping_type || '-' || position_type
        END, 
        ', ' ORDER BY sort_order
    ) as all_options
FROM hot_stamping_type_options 
WHERE is_active = TRUE
GROUP BY stamping_type
ORDER BY MIN(sort_order);

-- 添加视图注释
COMMENT ON VIEW v_hot_stamping_types_by_category IS '按烫金类型分组的选项视图';

-- 创建视图：按位置类型分组
CREATE VIEW v_hot_stamping_types_by_position AS
SELECT 
    position_type,
    COUNT(*) as option_count,
    STRING_AGG(
        CASE 
            WHEN position_type IS NULL THEN stamping_type
            ELSE stamping_type || '-' || position_type
        END, 
        ', ' ORDER BY sort_order
    ) as all_options
FROM hot_stamping_type_options 
WHERE is_active = TRUE
GROUP BY position_type
ORDER BY MIN(sort_order);

-- 添加视图注释
COMMENT ON VIEW v_hot_stamping_types_by_position IS '按位置类型分组的选项视图';

-- 创建视图：完整选项列表
CREATE VIEW v_hot_stamping_types_list AS
SELECT 
    id,
    CASE 
        WHEN position_type IS NULL THEN stamping_type
        ELSE stamping_type || '-' || position_type
    END as option_name,
    stamping_type,
    position_type,
    description,
    sort_order
FROM hot_stamping_type_options 
WHERE is_active = TRUE
ORDER BY sort_order;

-- 添加视图注释
COMMENT ON VIEW v_hot_stamping_types_list IS '烫金类型完整选项列表视图';

-- 创建函数：获取所有烫金类型选项名称
CREATE OR REPLACE FUNCTION get_all_stamping_type_names()
RETURNS TABLE (option_name VARCHAR(100)) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        CASE 
            WHEN hto.position_type IS NULL THEN hto.stamping_type
            ELSE hto.stamping_type || '-' || hto.position_type
        END as option_name
    FROM hot_stamping_type_options hto
    WHERE hto.is_active = TRUE
    ORDER BY hto.sort_order;
END;
$$ LANGUAGE plpgsql;

-- 添加函数注释
COMMENT ON FUNCTION get_all_stamping_type_names() IS '获取所有烫金类型选项名称';

-- 创建函数：根据烫金类型获取选项
CREATE OR REPLACE FUNCTION get_options_by_stamping_type(input_stamping_type VARCHAR)
RETURNS TABLE (
    option_name VARCHAR(100),
    stamping_type VARCHAR(50),
    position_type VARCHAR(30),
    description TEXT
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        CASE 
            WHEN hto.position_type IS NULL THEN hto.stamping_type
            ELSE hto.stamping_type || '-' || hto.position_type
        END as option_name,
        hto.stamping_type,
        hto.position_type,
        hto.description
    FROM hot_stamping_type_options hto
    WHERE hto.is_active = TRUE
    AND hto.stamping_type = input_stamping_type
    ORDER BY hto.sort_order;
END;
$$ LANGUAGE plpgsql;

-- 添加函数注释
COMMENT ON FUNCTION get_options_by_stamping_type(VARCHAR) IS '根据烫金类型获取选项';

-- 创建函数：根据位置类型获取选项
CREATE OR REPLACE FUNCTION get_options_by_position_type(input_position_type VARCHAR)
RETURNS TABLE (
    option_name VARCHAR(100),
    stamping_type VARCHAR(50),
    position_type VARCHAR(30),
    description TEXT
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        CASE 
            WHEN hto.position_type IS NULL THEN hto.stamping_type
            ELSE hto.stamping_type || '-' || hto.position_type
        END as option_name,
        hto.stamping_type,
        hto.position_type,
        hto.description
    FROM hot_stamping_type_options hto
    WHERE hto.is_active = TRUE
    AND hto.position_type = input_position_type
    ORDER BY hto.sort_order;
END;
$$ LANGUAGE plpgsql;

-- 添加函数注释
COMMENT ON FUNCTION get_options_by_position_type(VARCHAR) IS '根据位置类型获取选项';

-- 创建函数：添加新的烫金类型选项
CREATE OR REPLACE FUNCTION add_stamping_type_option(
    p_stamping_type VARCHAR,
    p_position_type VARCHAR DEFAULT NULL,
    p_description TEXT DEFAULT NULL,
    p_sort_order INTEGER DEFAULT 0
)
RETURNS INTEGER AS $$
DECLARE
    new_id INTEGER;
BEGIN
    INSERT INTO hot_stamping_type_options (
        stamping_type, position_type, description, sort_order
    ) VALUES (
        p_stamping_type, p_position_type, p_description, p_sort_order
    ) RETURNING id INTO new_id;
    
    RETURN new_id;
END;
$$ LANGUAGE plpgsql;

-- 添加函数注释
COMMENT ON FUNCTION add_stamping_type_option(VARCHAR, VARCHAR, TEXT, INTEGER) IS '添加新的烫金类型选项';

-- 创建函数：更新烫金类型选项
CREATE OR REPLACE FUNCTION update_stamping_type_option(
    p_id INTEGER,
    p_stamping_type VARCHAR DEFAULT NULL,
    p_position_type VARCHAR DEFAULT NULL,
    p_description TEXT DEFAULT NULL,
    p_sort_order INTEGER DEFAULT NULL
)
RETURNS BOOLEAN AS $$
BEGIN
    UPDATE hot_stamping_type_options 
    SET 
        stamping_type = COALESCE(p_stamping_type, stamping_type),
        position_type = COALESCE(p_position_type, position_type),
        description = COALESCE(p_description, description),
        sort_order = COALESCE(p_sort_order, sort_order),
        updated_at = CURRENT_TIMESTAMP
    WHERE id = p_id;
    
    RETURN FOUND;
END;
$$ LANGUAGE plpgsql;

-- 添加函数注释
COMMENT ON FUNCTION update_stamping_type_option(INTEGER, VARCHAR, VARCHAR, TEXT, INTEGER) IS '更新烫金类型选项';

-- 创建函数：删除烫金类型选项（软删除）
CREATE OR REPLACE FUNCTION delete_stamping_type_option(p_id INTEGER)
RETURNS BOOLEAN AS $$
BEGIN
    UPDATE hot_stamping_type_options 
    SET is_active = FALSE, updated_at = CURRENT_TIMESTAMP
    WHERE id = p_id;
    
    RETURN FOUND;
END;
$$ LANGUAGE plpgsql;

-- 添加函数注释
COMMENT ON FUNCTION delete_stamping_type_option(INTEGER) IS '删除烫金类型选项（软删除）';

-- 验证数据完整性
DO $$
DECLARE
    record_count INTEGER;
    stamping_types INTEGER;
    position_types INTEGER;
BEGIN
    SELECT COUNT(*) INTO record_count FROM hot_stamping_type_options;
    SELECT COUNT(DISTINCT stamping_type) INTO stamping_types FROM hot_stamping_type_options;
    SELECT COUNT(DISTINCT position_type) INTO position_types FROM hot_stamping_type_options;
    
    RAISE NOTICE '烫金类型选项表已创建，共 % 条记录', record_count;
    
    -- 验证烫金类型完整性
    IF stamping_types = 4 THEN
        RAISE NOTICE '烫金类型完整性验证通过：平面烫金、立体烫金、磨砂烫金、折光烫金';
    ELSE
        RAISE WARNING '烫金类型不完整，实际类型数：%', stamping_types;
    END IF;
    
    -- 验证位置类型完整性
    IF position_types = 2 THEN
        RAISE NOTICE '位置类型完整性验证通过：於中間位、到邊位';
    ELSE
        RAISE WARNING '位置类型不完整，实际类型数：%', position_types;
    END IF;
    
    -- 验证关键选项是否存在
    IF (SELECT COUNT(*) FROM hot_stamping_type_options WHERE option_name = '平面烫金-於中間位') > 0 THEN
        RAISE NOTICE '关键选项验证通过：平面烫金-於中間位';
    ELSE
        RAISE WARNING '关键选项缺失：平面烫金-於中間位';
    END IF;
    
    IF (SELECT COUNT(*) FROM hot_stamping_type_options WHERE option_name = '立体烫金-到邊位') > 0 THEN
        RAISE NOTICE '关键选项验证通过：立体烫金-到邊位';
    ELSE
        RAISE WARNING '关键选项缺失：立体烫金-到邊位';
    END IF;
END $$;
