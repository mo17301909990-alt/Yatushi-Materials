-- ========================================
-- 过胶查询索引创建 - 分步执行版本
-- ========================================

-- 步骤1: 创建lamination_compatibility表的核心索引
-- 这是最重要的索引，用于过胶兼容性查询
\echo '创建lamination_compatibility核心查询索引...'
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_lamination_compatibility_query 
ON lamination_compatibility (
    post_processing_step_id,
    lamination_material_id,
    compatibility,
    foil_series,
    product_type
);

-- 步骤2: 创建model_number相关索引
\echo '创建model_number匹配索引...'
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_lamination_compatibility_model_number 
ON lamination_compatibility (model_number, post_processing_step_id, lamination_material_id, compatibility)
WHERE model_number IS NOT NULL AND model_number != '';

-- 步骤3: 创建foil_series相关索引  
\echo '创建foil_series匹配索引...'
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_lamination_compatibility_foil_series 
ON lamination_compatibility (foil_series, post_processing_step_id, lamination_material_id, compatibility)
WHERE foil_series IS NOT NULL AND foil_series != '';

-- 步骤4: 创建products表索引
\echo '创建products表相关索引...'
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_products_name 
ON products (name);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_products_model_number 
ON products (model_number);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_products_name_type 
ON products (name, hot_stamping_paper_type);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_products_model_type 
ON products (model_number, hot_stamping_paper_type);

-- 步骤5: 创建关联表索引（如果不存在）
\echo '创建关联表索引...'
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_specifications_project_id 
ON specifications (project_id);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_pricing_project_id 
ON pricing (project_id);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_leo_gp_numbers_project_id 
ON leo_gp_numbers (project_id);


CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_hot_stamping_compatibility_paper_performance 
ON hot_stamping_compatibility (paper_performance);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_cloth_paper_compatibility_product_name 
ON cloth_paper_compatibility (product_name);

\echo '所有索引创建完成！'

-- 验证索引创建结果
\echo '验证索引创建结果:'
SELECT 
    tablename,
    indexname,
    indexdef
FROM pg_indexes 
WHERE tablename IN ('lamination_compatibility', 'products')
AND indexname LIKE 'idx_lamination%' OR indexname LIKE 'idx_products_%'
ORDER BY tablename, indexname;