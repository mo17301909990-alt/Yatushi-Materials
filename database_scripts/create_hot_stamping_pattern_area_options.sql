-- 创建烫金图案面积选项表
-- 用于存储烫金图案的面积分类和图案类型选项
-- 包含大面积、中面积、小面积分类和对应的图案类型

CREATE TABLE public.hot_stamping_pattern_area_options (
    id SERIAL PRIMARY KEY,
    option_name VARCHAR(100) NOT NULL UNIQUE,        -- 完整选项名称
    area_category VARCHAR(20) NOT NULL,               -- 面积分类：大面積、中面積、小面積
    area_range VARCHAR(50) NOT NULL,                 -- 面积范围描述
    pattern_type VARCHAR(50) NOT NULL,                -- 图案类型：漸變、網點、細線條、字母等
    min_size_mm INTEGER,                             -- 最小尺寸(mm)
    max_size_mm INTEGER,                             -- 最大尺寸(mm)
    description TEXT,                                 -- 详细描述
    is_active BOOLEAN DEFAULT TRUE,                  -- 是否启用
    sort_order INTEGER DEFAULT 0,                   -- 排序
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 添加表注释
COMMENT ON TABLE public.hot_stamping_pattern_area_options IS '烫金图案面积选项表';
COMMENT ON COLUMN public.hot_stamping_pattern_area_options.option_name IS '完整选项名称';
COMMENT ON COLUMN public.hot_stamping_pattern_area_options.area_category IS '面积分类：大面積、中面積、小面積';
COMMENT ON COLUMN public.hot_stamping_pattern_area_options.area_range IS '面积范围描述';
COMMENT ON COLUMN public.hot_stamping_pattern_area_options.pattern_type IS '图案类型：漸變、網點、細線條、字母等';
COMMENT ON COLUMN public.hot_stamping_pattern_area_options.min_size_mm IS '最小尺寸(mm)';
COMMENT ON COLUMN public.hot_stamping_pattern_area_options.max_size_mm IS '最大尺寸(mm)';
COMMENT ON COLUMN public.hot_stamping_pattern_area_options.description IS '详细描述';
COMMENT ON COLUMN public.hot_stamping_pattern_area_options.is_active IS '是否启用';
COMMENT ON COLUMN public.hot_stamping_pattern_area_options.sort_order IS '排序顺序';

-- 创建索引
CREATE INDEX idx_pattern_area_category ON public.hot_stamping_pattern_area_options(area_category);
CREATE INDEX idx_pattern_pattern_type ON public.hot_stamping_pattern_area_options(pattern_type);
CREATE INDEX idx_pattern_active ON public.hot_stamping_pattern_area_options(is_active);
CREATE INDEX idx_pattern_sort_order ON public.hot_stamping_pattern_area_options(sort_order);

-- 插入烫金图案面积选项数据
INSERT INTO public.hot_stamping_pattern_area_options (
    option_name,
    area_category,
    area_range,
    pattern_type,
    min_size_mm,
    max_size_mm,
    description,
    sort_order
) VALUES

-- 大面积分类 (≥100mm×100mm)
('大面積', '大面積', '≥100mm×100mm', '大面積', 100, NULL, '大面积烫金图案，适用于大面积装饰', 1),

-- 中面积分类 (50mm×50mm < X ≤ 100mm×100mm)
('中面積', '中面積', '50mm×50mm < X ≤ 100mm×100mm', '中面積', 50, 100, '中面积烫金图案，适用于中等面积装饰', 2),

-- 小面积分类 (<50mm×50mm)
('小面積', '小面積', '<50mm×50mm', '小面積', NULL, 50, '小面积烫金图案，适用于精细装饰', 3),

-- 图案类型选项
('漸變、網點', '小面積', '<50mm×50mm', '漸變、網點', NULL, 50, '渐变网点图案，适用于小面积精细效果', 4),
('細線條、字母', '小面積', '<50mm×50mm', '細線條、字母', NULL, 50, '细线条字母图案，适用于小面积精细文字', 5),
('中小面積、線條、字母', '中面積', '50mm×50mm < X ≤ 100mm×100mm', '中小面積、線條、字母', 50, 100, '中小面积线条字母图案，适用于中等面积文字装饰', 6),
('中大面積、線條、字母', '大面積', '≥100mm×100mm', '中大面積、線條、字母', 100, NULL, '中大面积线条字母图案，适用于大面积文字装饰', 7),
('大面積', '大面積', '≥100mm×100mm', '大面積', 100, NULL, '大面积图案，适用于大面积装饰效果', 8);

-- 创建视图：按面积分类分组的选项
CREATE VIEW v_hot_stamping_patterns_by_area AS
SELECT 
    area_category,
    COUNT(*) as option_count,
    STRING_AGG(option_name, ', ' ORDER BY sort_order) as all_options,
    MIN(min_size_mm) as min_size,
    MAX(max_size_mm) as max_size
FROM public.hot_stamping_pattern_area_options 
WHERE is_active = TRUE
GROUP BY area_category
ORDER BY 
    CASE area_category
        WHEN '小面積' THEN 1
        WHEN '中面積' THEN 2
        WHEN '大面積' THEN 3
    END;

-- 添加视图注释
COMMENT ON VIEW v_hot_stamping_patterns_by_area IS '按面积分类分组的烫金图案选项视图';

-- 创建视图：按图案类型分组的选项
CREATE VIEW v_hot_stamping_patterns_by_type AS
SELECT 
    pattern_type,
    COUNT(*) as option_count,
    STRING_AGG(option_name, ', ' ORDER BY sort_order) as all_options,
    STRING_AGG(DISTINCT area_category, ', ' ORDER BY area_category) as applicable_areas
FROM public.hot_stamping_pattern_area_options 
WHERE is_active = TRUE
GROUP BY pattern_type
ORDER BY MIN(sort_order);

-- 添加视图注释
COMMENT ON VIEW v_hot_stamping_patterns_by_type IS '按图案类型分组的烫金图案选项视图';

-- 创建视图：完整选项列表
CREATE VIEW v_hot_stamping_patterns_list AS
SELECT 
    id,
    option_name,
    area_category,
    area_range,
    pattern_type,
    min_size_mm,
    max_size_mm,
    description,
    sort_order
FROM public.hot_stamping_pattern_area_options 
WHERE is_active = TRUE
ORDER BY sort_order;

-- 添加视图注释
COMMENT ON VIEW v_hot_stamping_patterns_list IS '烫金图案完整选项列表视图';

-- 创建函数：根据面积分类获取选项
CREATE OR REPLACE FUNCTION get_patterns_by_area_category(input_area_category VARCHAR)
RETURNS TABLE (
    option_name VARCHAR(100),
    area_range VARCHAR(50),
    pattern_type VARCHAR(50),
    min_size_mm INTEGER,
    max_size_mm INTEGER,
    description TEXT
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        hpao.option_name,
        hpao.area_range,
        hpao.pattern_type,
        hpao.min_size_mm,
        hpao.max_size_mm,
        hpao.description
    FROM public.hot_stamping_pattern_area_options hpao
    WHERE hpao.is_active = TRUE
    AND hpao.area_category = input_area_category
    ORDER BY hpao.sort_order;
END;
$$ LANGUAGE plpgsql;

-- 添加函数注释
COMMENT ON FUNCTION get_patterns_by_area_category(VARCHAR) IS '根据面积分类获取烫金图案选项';

-- 创建函数：根据图案类型获取选项
CREATE OR REPLACE FUNCTION get_patterns_by_pattern_type(input_pattern_type VARCHAR)
RETURNS TABLE (
    option_name VARCHAR(100),
    area_category VARCHAR(20),
    area_range VARCHAR(50),
    min_size_mm INTEGER,
    max_size_mm INTEGER,
    description TEXT
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        hpao.option_name,
        hpao.area_category,
        hpao.area_range,
        hpao.min_size_mm,
        hpao.max_size_mm,
        hpao.description
    FROM public.hot_stamping_pattern_area_options hpao
    WHERE hpao.is_active = TRUE
    AND hpao.pattern_type = input_pattern_type
    ORDER BY hpao.sort_order;
END;
$$ LANGUAGE plpgsql;

-- 添加函数注释
COMMENT ON FUNCTION get_patterns_by_pattern_type(VARCHAR) IS '根据图案类型获取烫金图案选项';

-- 创建函数：根据尺寸范围获取适用选项
CREATE OR REPLACE FUNCTION get_patterns_by_size(input_size_mm INTEGER)
RETURNS TABLE (
    option_name VARCHAR(100),
    area_category VARCHAR(20),
    area_range VARCHAR(50),
    pattern_type VARCHAR(50),
    description TEXT
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        hpao.option_name,
        hpao.area_category,
        hpao.area_range,
        hpao.pattern_type,
        hpao.description
    FROM public.hot_stamping_pattern_area_options hpao
    WHERE hpao.is_active = TRUE
    AND (
        (hpao.min_size_mm IS NULL OR input_size_mm >= hpao.min_size_mm)
        AND (hpao.max_size_mm IS NULL OR input_size_mm <= hpao.max_size_mm)
    )
    ORDER BY hpao.sort_order;
END;
$$ LANGUAGE plpgsql;

-- 添加函数注释
COMMENT ON FUNCTION get_patterns_by_size(INTEGER) IS '根据尺寸范围获取适用的烫金图案选项';

-- 创建函数：添加新的烫金图案选项
CREATE OR REPLACE FUNCTION add_pattern_area_option(
    p_option_name VARCHAR,
    p_area_category VARCHAR,
    p_area_range VARCHAR,
    p_pattern_type VARCHAR,
    p_min_size_mm INTEGER DEFAULT NULL,
    p_max_size_mm INTEGER DEFAULT NULL,
    p_description TEXT DEFAULT NULL,
    p_sort_order INTEGER DEFAULT 0
)
RETURNS INTEGER AS $$
DECLARE
    new_id INTEGER;
BEGIN
    INSERT INTO public.hot_stamping_pattern_area_options (
        option_name, area_category, area_range, pattern_type,
        min_size_mm, max_size_mm, description, sort_order
    ) VALUES (
        p_option_name, p_area_category, p_area_range, p_pattern_type,
        p_min_size_mm, p_max_size_mm, p_description, p_sort_order
    ) RETURNING id INTO new_id;
    
    RETURN new_id;
END;
$$ LANGUAGE plpgsql;

-- 添加函数注释
COMMENT ON FUNCTION add_pattern_area_option(VARCHAR, VARCHAR, VARCHAR, VARCHAR, INTEGER, INTEGER, TEXT, INTEGER) IS '添加新的烫金图案面积选项';

-- 创建函数：更新烫金图案选项
CREATE OR REPLACE FUNCTION update_pattern_area_option(
    p_id INTEGER,
    p_option_name VARCHAR DEFAULT NULL,
    p_area_category VARCHAR DEFAULT NULL,
    p_area_range VARCHAR DEFAULT NULL,
    p_pattern_type VARCHAR DEFAULT NULL,
    p_min_size_mm INTEGER DEFAULT NULL,
    p_max_size_mm INTEGER DEFAULT NULL,
    p_description TEXT DEFAULT NULL,
    p_sort_order INTEGER DEFAULT NULL
)
RETURNS BOOLEAN AS $$
BEGIN
    UPDATE public.hot_stamping_pattern_area_options 
    SET 
        option_name = COALESCE(p_option_name, option_name),
        area_category = COALESCE(p_area_category, area_category),
        area_range = COALESCE(p_area_range, area_range),
        pattern_type = COALESCE(p_pattern_type, pattern_type),
        min_size_mm = COALESCE(p_min_size_mm, min_size_mm),
        max_size_mm = COALESCE(p_max_size_mm, max_size_mm),
        description = COALESCE(p_description, description),
        sort_order = COALESCE(p_sort_order, sort_order),
        updated_at = CURRENT_TIMESTAMP
    WHERE id = p_id;
    
    RETURN FOUND;
END;
$$ LANGUAGE plpgsql;

-- 添加函数注释
COMMENT ON FUNCTION update_pattern_area_option(INTEGER, VARCHAR, VARCHAR, VARCHAR, VARCHAR, INTEGER, INTEGER, TEXT, INTEGER) IS '更新烫金图案面积选项';

-- 创建函数：删除烫金图案选项（软删除）
CREATE OR REPLACE FUNCTION delete_pattern_area_option(p_id INTEGER)
RETURNS BOOLEAN AS $$
BEGIN
    UPDATE public.hot_stamping_pattern_area_options 
    SET is_active = FALSE, updated_at = CURRENT_TIMESTAMP
    WHERE id = p_id;
    
    RETURN FOUND;
END;
$$ LANGUAGE plpgsql;

-- 添加函数注释
COMMENT ON FUNCTION delete_pattern_area_option(INTEGER) IS '删除烫金图案面积选项（软删除）';

-- 验证数据完整性
DO $$
DECLARE
    record_count INTEGER;
    area_categories INTEGER;
    pattern_types INTEGER;
BEGIN
    SELECT COUNT(*) INTO record_count FROM public.hot_stamping_pattern_area_options;
    SELECT COUNT(DISTINCT area_category) INTO area_categories FROM public.hot_stamping_pattern_area_options;
    SELECT COUNT(DISTINCT pattern_type) INTO pattern_types FROM public.hot_stamping_pattern_area_options;
    
    RAISE NOTICE '烫金图案面积选项表已创建，共 % 条记录', record_count;
    
    -- 验证面积分类完整性
    IF area_categories = 3 THEN
        RAISE NOTICE '面积分类完整性验证通过：大面積、中面積、小面積';
    ELSE
        RAISE WARNING '面积分类不完整，实际分类数：%', area_categories;
    END IF;
    
    -- 验证图案类型完整性
    IF pattern_types >= 5 THEN
        RAISE NOTICE '图案类型完整性验证通过，共 % 种类型', pattern_types;
    ELSE
        RAISE WARNING '图案类型不完整，实际类型数：%', pattern_types;
    END IF;
    
    -- 验证关键选项是否存在
    IF (SELECT COUNT(*) FROM public.hot_stamping_pattern_area_options WHERE option_name = '大面積') > 0 THEN
        RAISE NOTICE '关键选项验证通过：大面積';
    ELSE
        RAISE WARNING '关键选项缺失：大面積';
    END IF;
    
    IF (SELECT COUNT(*) FROM public.hot_stamping_pattern_area_options WHERE option_name = '漸變、網點') > 0 THEN
        RAISE NOTICE '关键选项验证通过：漸變、網點';
    ELSE
        RAISE WARNING '关键选项缺失：漸變、網點';
    END IF;
    
    -- 验证尺寸范围逻辑
    IF (SELECT COUNT(*) FROM public.hot_stamping_pattern_area_options WHERE area_category = '小面積' AND max_size_mm <= 50) > 0 THEN
        RAISE NOTICE '小面积尺寸范围验证通过';
    ELSE
        RAISE WARNING '小面积尺寸范围可能有问题';
    END IF;
    
    IF (SELECT COUNT(*) FROM public.hot_stamping_pattern_area_options WHERE area_category = '大面積' AND min_size_mm >= 100) > 0 THEN
        RAISE NOTICE '大面积尺寸范围验证通过';
    ELSE
        RAISE WARNING '大面积尺寸范围可能有问题';
    END IF;
END $$;





