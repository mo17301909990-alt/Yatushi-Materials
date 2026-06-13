-- ========================================
-- 过胶兼容性查询索引优化
-- ========================================
-- 创建时间: 2024年
-- 目的: 优化过胶兼容性查询的性能
-- 影响: 提升 GoldFoilProductMapper.xml 中过胶查询的性能
-- ========================================

-- 1. 删除可能存在的旧索引（如果需要重建）
DROP INDEX IF EXISTS idx_lc_foil_series;
DROP INDEX IF EXISTS idx_lc_post_processing;
DROP INDEX IF EXISTS idx_lc_lamination_material;
DROP INDEX IF EXISTS idx_lc_interface_type;
DROP INDEX IF EXISTS idx_lc_compatibility;

-- ========================================
-- 2. 核心复合索引 - 支持完整的查询条件
-- ========================================

-- 索引1: 优先支持 model_number 精确匹配的查询
-- 用于: (lc.model_number = p.model_number) 的情况
CREATE INDEX idx_lc_model_number_composite 
ON lamination_compatibility(
    model_number, 
    post_processing_step_id, 
    lamination_material_id, 
    compatibility,
    product_type
) 
WHERE model_number IS NOT NULL 
AND model_number != '' 
AND compatibility = 'V';

-- 索引2: 支持 foil_series 匹配的查询
-- 用于: (lc.foil_series = p.name) 的情况
CREATE INDEX idx_lc_foil_series_composite 
ON lamination_compatibility(
    foil_series, 
    post_processing_step_id, 
    lamination_material_id, 
    compatibility,
    product_type
) 
WHERE compatibility = 'V';

-- ========================================
-- 3. 辅助索引 - 支持部分查询和管理界面
-- ========================================

-- 索引3: 后加工步骤查询优化
CREATE INDEX idx_lc_post_processing_step 
ON lamination_compatibility(post_processing_step_id, compatibility) 
WHERE compatibility = 'V';

-- 索引4: 过胶材质查询优化
CREATE INDEX idx_lc_lamination_material_id 
ON lamination_compatibility(lamination_material_id, compatibility) 
WHERE compatibility = 'V';

-- 索引5: 兼容性状态查询（用于管理界面）
CREATE INDEX idx_lc_compatibility_status 
ON lamination_compatibility(compatibility, foil_series, model_number);

-- ========================================
-- 4. 覆盖索引 - 减少回表查询
-- ========================================

-- 索引6: 包含所有常用字段的覆盖索引
-- 当查询只需要这些字段时，可以避免回表
CREATE INDEX idx_lc_covering_index 
ON lamination_compatibility(
    foil_series,
    model_number,
    post_processing_step_id,
    lamination_material_id,
    compatibility,
    product_type,
    is_jiehuo
) 
WHERE compatibility = 'V';

-- ========================================
-- 5. 特殊场景索引
-- ========================================

-- 索引7: 支持 product_type 为 NULL 的查询
CREATE INDEX idx_lc_product_type_null 
ON lamination_compatibility(
    foil_series,
    model_number,
    post_processing_step_id,
    lamination_material_id,
    compatibility
) 
WHERE product_type IS NULL AND compatibility = 'V';

-- ========================================
-- 6. 关联表索引检查和创建
-- ========================================

-- 确保 products 表有必要的索引
CREATE INDEX IF NOT EXISTS idx_products_name 
ON products(name);

CREATE INDEX IF NOT EXISTS idx_products_model_number 
ON products(model_number);

CREATE INDEX IF NOT EXISTS idx_products_hot_stamping_paper_type 
ON products(hot_stamping_paper_type);

-- 确保选项表有必要的索引
CREATE INDEX IF NOT EXISTS idx_post_processing_options_id 
ON post_processing_options(id);

CREATE INDEX IF NOT EXISTS idx_lamination_material_options_id 
ON lamination_material_options(id);

-- ========================================
-- 7. 更新表统计信息
-- ========================================
ANALYZE lamination_compatibility;
ANALYZE products;
ANALYZE post_processing_options;
ANALYZE lamination_material_options;

-- ========================================
-- 8. 索引使用情况查询
-- ========================================
-- 运行此查询来检查索引是否被正确创建
SELECT 
    schemaname,
    relname as table_name,
    indexname,
    pg_size_pretty(pg_relation_size(indexrelid)) as index_size
FROM pg_stat_user_indexes 
WHERE schemaname = 'public'
AND relname = 'lamination_compatibility'
ORDER BY indexname;

-- 查看所有相关表的索引
SELECT 
    t.relname as table_name,
    i.relname as index_name,
    pg_size_pretty(pg_relation_size(i.oid)) as index_size
FROM pg_class t
JOIN pg_index ix ON t.oid = ix.indrelid
JOIN pg_class i ON i.oid = ix.indexrelid
WHERE t.relname IN ('lamination_compatibility', 'products', 'post_processing_options', 'lamination_material_options')
AND t.relkind = 'r'
ORDER BY t.relname, i.relname;

-- ========================================
-- 9. 查询性能测试
-- ========================================
-- 测试查询1: model_number 精确匹配
EXPLAIN (ANALYZE, BUFFERS) 
SELECT lc.* 
FROM lamination_compatibility lc 
WHERE lc.model_number = 'TEST_MODEL'
AND lc.post_processing_step_id = 1
AND lc.lamination_material_id = 1
AND lc.compatibility = 'V';

-- 测试查询2: foil_series 匹配
EXPLAIN (ANALYZE, BUFFERS) 
SELECT lc.* 
FROM lamination_compatibility lc 
WHERE lc.foil_series = 'TEST_SERIES'
AND lc.post_processing_step_id = 1
AND lc.lamination_material_id = 1
AND lc.compatibility = 'V';

-- 测试查询3: 完整的 JOIN 查询（模拟实际使用场景）
EXPLAIN (ANALYZE, BUFFERS) 
SELECT DISTINCT p.name, p.model_number
FROM products p
LEFT JOIN lamination_compatibility lc ON (
    (
        (lc.model_number IS NOT NULL AND lc.model_number != '' AND lc.model_number = p.model_number)
        OR (lc.model_number IS NULL OR lc.model_number = '' AND lc.foil_series = p.name)
    )
    AND lc.post_processing_step_id = 1
    AND lc.lamination_material_id = 1
    AND lc.compatibility = 'V'
    AND (lc.product_type IS NULL OR lc.product_type = p.hot_stamping_paper_type)
)
WHERE lc.id IS NOT NULL
LIMIT 100;

-- ========================================
-- 10. 索引维护建议
-- ========================================
/*
定期维护建议:
1. 每周运行 ANALYZE lamination_compatibility; 更新统计信息
2. 每月检查索引使用情况，删除未使用的索引
3. 监控查询性能，根据实际使用情况调整索引
4. 在数据量大幅增长时，考虑分区表策略

性能监控查询:
- 查看慢查询: SELECT * FROM pg_stat_statements WHERE query LIKE '%lamination_compatibility%' ORDER BY mean_time DESC;
- 查看索引使用: SELECT * FROM pg_stat_user_indexes WHERE relname = 'lamination_compatibility';
- 查看表大小: SELECT pg_size_pretty(pg_total_relation_size('lamination_compatibility'));
*/
