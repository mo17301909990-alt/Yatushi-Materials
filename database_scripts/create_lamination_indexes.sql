-- ========================================
-- 过胶兼容性查询索引优化
-- ========================================
-- 基于过胶查询的JOIN条件和WHERE条件创建索引

-- 1. lamination_compatibility表的复合索引
-- 用于过胶兼容性查询的主要条件
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_lamination_compatibility_query 
ON lamination_compatibility (
    post_processing_step_id,
    lamination_material_id,
    compatibility,
    foil_series,
    product_type
);

-- 2. lamination_compatibility表的model_number索引
-- 用于model_number匹配的查询
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_lamination_compatibility_model_number 
ON lamination_compatibility (model_number, post_processing_step_id, lamination_material_id, compatibility)
WHERE model_number IS NOT NULL AND model_number != '';

-- 3. lamination_compatibility表的foil_series索引
-- 用于foil_series匹配的查询
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_lamination_compatibility_foil_series 
ON lamination_compatibility (foil_series, post_processing_step_id, lamination_material_id, compatibility)
WHERE foil_series IS NOT NULL AND foil_series != '';

-- 4. products表的name索引（如果不存在）
-- 用于与lamination_compatibility.foil_series的JOIN
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_products_name 
ON products (name);

-- 5. products表的model_number索引（如果不存在）
-- 用于与lamination_compatibility.model_number的JOIN
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_products_model_number 
ON products (model_number);

-- 6. products表的复合索引
-- 用于过胶查询中的产品类型匹配
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_products_name_type 
ON products (name, hot_stamping_paper_type);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_products_model_type 
ON products (model_number, hot_stamping_paper_type);

-- 7. 其他关联表的索引优化
-- specifications表
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_specifications_project_id 
ON specifications (project_id);

-- pricing表
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_pricing_project_id 
ON pricing (project_id);

-- leo_gp_numbers表
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_leo_gp_numbers_project_id 
ON leo_gp_numbers (project_id);


-- hot_stamping_compatibility表
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_hot_stamping_compatibility_paper_performance 
ON hot_stamping_compatibility (paper_performance);

-- cloth_paper_compatibility表
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_cloth_paper_compatibility_product_name 
ON cloth_paper_compatibility (product_name);

-- ========================================
-- 索引使用情况查询
-- ========================================

-- 查看索引创建状态
SELECT 
    schemaname,
    tablename,
    indexname,
    indexdef
FROM pg_indexes 
WHERE tablename IN (
    'lamination_compatibility', 
    'products', 
    'specifications', 
    'pricing', 
    'leo_gp_numbers', 
    'hot_stamping_compatibility',
    'cloth_paper_compatibility'
)
AND indexname LIKE 'idx_%'
ORDER BY tablename, indexname;

-- 查看索引大小
SELECT 
    schemaname,
    tablename,
    indexname,
    pg_size_pretty(pg_relation_size(indexrelid)) as index_size
FROM pg_stat_user_indexes 
WHERE schemaname = 'public'
AND indexname LIKE 'idx_%'
ORDER BY pg_relation_size(indexrelid) DESC;

-- 查看索引使用统计
SELECT 
    schemaname,
    tablename,
    indexname,
    idx_scan as index_scans,
    idx_tup_read as tuples_read,
    idx_tup_fetch as tuples_fetched
FROM pg_stat_user_indexes 
WHERE schemaname = 'public'
AND indexname LIKE 'idx_%'
ORDER BY idx_scan DESC;
