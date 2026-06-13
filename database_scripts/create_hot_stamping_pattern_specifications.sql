-- 创建烫金图案规格表
-- 用于存储烫金图案的面积分类、图案类型和适用场景
CREATE TABLE hot_stamping_pattern_specifications (
    id SERIAL PRIMARY KEY,
    area_category VARCHAR(20) NOT NULL,           -- 面积分类：大面積、中面積、小面積
    area_range VARCHAR(50) NOT NULL,              -- 面积范围描述
    pattern_type VARCHAR(50) NOT NULL,             -- 图案类型：漸變、網點、細線條、字母等
    applicable_scenarios TEXT,                    -- 适用场景描述
    min_size_mm INTEGER,                          -- 最小尺寸(mm)
    max_size_mm INTEGER,                          -- 最大尺寸(mm)
    is_active BOOLEAN DEFAULT TRUE,               -- 是否启用
    sort_order INTEGER DEFAULT 0,                -- 排序
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 添加表注释
COMMENT ON TABLE hot_stamping_pattern_specifications IS '烫金图案规格表';
COMMENT ON COLUMN hot_stamping_pattern_specifications.area_category IS '面积分类：大面積、中面積、小面積';
COMMENT ON COLUMN hot_stamping_pattern_specifications.area_range IS '面积范围描述';
COMMENT ON COLUMN hot_stamping_pattern_specifications.pattern_type IS '图案类型：漸變、網點、細線條、字母等';
COMMENT ON COLUMN hot_stamping_pattern_specifications.applicable_scenarios IS '适用场景描述';
COMMENT ON COLUMN hot_stamping_pattern_specifications.min_size_mm IS '最小尺寸(mm)';
COMMENT ON COLUMN hot_stamping_pattern_specifications.max_size_mm IS '最大尺寸(mm)';
COMMENT ON COLUMN hot_stamping_pattern_specifications.is_active IS '是否启用';
COMMENT ON COLUMN hot_stamping_pattern_specifications.sort_order IS '排序顺序';

-- 创建索引
CREATE INDEX idx_pattern_area_category ON hot_stamping_pattern_specifications(area_category);
CREATE INDEX idx_pattern_pattern_type ON hot_stamping_pattern_specifications(pattern_type);
CREATE INDEX idx_pattern_active ON hot_stamping_pattern_specifications(is_active);
CREATE INDEX idx_pattern_sort_order ON hot_stamping_pattern_specifications(sort_order);

-- 插入烫金图案规格数据
INSERT INTO hot_stamping_pattern_specifications (area_category, area_range, pattern_type, applicable_scenarios, min_size_mm, max_size_mm, sort_order) VALUES

-- 大面积图案规格
('大面積', '≥100mmx100mm', '漸變', '大面積漸變圖案', 100, NULL, 1),
('大面積', '≥100mmx100mm', '網點', '大面積網點圖案', 100, NULL, 2),
('大面積', '≥100mmx100mm', '細線條', '大面積細線條圖案', 100, NULL, 3),
('大面積', '≥100mmx100mm', '字母', '大面積字母圖案', 100, NULL, 4),
('大面積', '≥100mmx100mm', '線條', '大面積線條圖案', 100, NULL, 5),
('大面積', '≥100mmx100mm', '字母', '大面積字母圖案', 100, NULL, 6),

-- 中面积图案规格
('中面積', '100mmx100mm<X≥50mmx50mm', '漸變', '中面積漸變圖案', 50, 100, 7),
('中面積', '100mmx100mm<X≥50mmx50mm', '網點', '中面積網點圖案', 50, 100, 8),
('中面積', '100mmx100mm<X≥50mmx50mm', '細線條', '中面積細線條圖案', 50, 100, 9),
('中面積', '100mmx100mm<X≥50mmx50mm', '字母', '中面積字母圖案', 50, 100, 10),
('中面積', '100mmx100mm<X≥50mmx50mm', '線條', '中面積線條圖案', 50, 100, 11),
('中面積', '100mmx100mm<X≥50mmx50mm', '字母', '中面積字母圖案', 50, 100, 12),

-- 小面积图案规格
('小面積', '<50mmx50mm', '漸變', '小面積漸變圖案', NULL, 50, 13),
('小面積', '<50mmx50mm', '網點', '小面積網點圖案', NULL, 50, 14),
('小面積', '<50mmx50mm', '細線條', '小面積細線條圖案', NULL, 50, 15),
('小面積', '<50mmx50mm', '字母', '小面積字母圖案', NULL, 50, 16),
('小面積', '<50mmx50mm', '線條', '小面積線條圖案', NULL, 50, 17),
('小面積', '<50mmx50mm', '字母', '小面積字母圖案', NULL, 50, 18);

-- 创建视图：按面积分类汇总
CREATE VIEW v_hot_stamping_pattern_summary AS
SELECT 
    area_category,
    COUNT(*) as pattern_count,
    STRING_AGG(DISTINCT pattern_type, ', ') as pattern_types,
    MIN(min_size_mm) as min_size,
    MAX(max_size_mm) as max_size
FROM hot_stamping_pattern_specifications 
WHERE is_active = TRUE
GROUP BY area_category
ORDER BY sort_order;

-- 添加视图注释
COMMENT ON VIEW v_hot_stamping_pattern_summary IS '烫金图案规格汇总视图';

-- 创建函数：根据尺寸获取图案规格
CREATE OR REPLACE FUNCTION get_pattern_specification_by_size(input_size_mm INTEGER)
RETURNS TABLE (
    area_category VARCHAR(20),
    area_range VARCHAR(50),
    pattern_type VARCHAR(50),
    applicable_scenarios TEXT
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        hps.area_category,
        hps.area_range,
        hps.pattern_type,
        hps.applicable_scenarios
    FROM hot_stamping_pattern_specifications hps
    WHERE hps.is_active = TRUE
    AND (
        (hps.min_size_mm IS NULL OR input_size_mm >= hps.min_size_mm)
        AND (hps.max_size_mm IS NULL OR input_size_mm <= hps.max_size_mm)
    )
    ORDER BY hps.sort_order;
END;
$$ LANGUAGE plpgsql;

-- 添加函数注释
COMMENT ON FUNCTION get_pattern_specification_by_size(INTEGER) IS '根据尺寸获取适用的烫金图案规格';

-- 验证数据完整性
DO $$
DECLARE
    record_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO record_count FROM hot_stamping_pattern_specifications;
    RAISE NOTICE '烫金图案规格表已创建，共插入 % 条记录', record_count;
    
    -- 验证面积分类完整性
    IF (SELECT COUNT(DISTINCT area_category) FROM hot_stamping_pattern_specifications) = 3 THEN
        RAISE NOTICE '面积分类完整性验证通过：大面積、中面積、小面積';
    ELSE
        RAISE WARNING '面积分类不完整';
    END IF;
    
    -- 验证图案类型完整性
    IF (SELECT COUNT(DISTINCT pattern_type) FROM hot_stamping_pattern_specifications) >= 4 THEN
        RAISE NOTICE '图案类型完整性验证通过';
    ELSE
        RAISE WARNING '图案类型不完整';
    END IF;
END $$;

