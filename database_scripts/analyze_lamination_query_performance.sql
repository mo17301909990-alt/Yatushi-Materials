-- ========================================
-- 过胶查询性能分析
-- ========================================

-- 1. 分析过胶查询的执行计划
\echo '分析过胶查询执行计划:'
EXPLAIN (ANALYZE, BUFFERS, FORMAT TEXT) 
SELECT DISTINCT
    p.name,
    p.model_number,
    p.hot_stamping_paper_type,
    lc.foil_series,
    lc.compatibility
FROM products p
INNER JOIN lamination_compatibility lc ON (
    (
        (lc.model_number IS NOT NULL AND lc.model_number != '' AND lc.model_number = p.model_number)
        OR (lc.model_number IS NULL OR lc.model_number = '' AND lc.foil_series = p.name)
    )
    AND lc.post_processing_step_id = 1
    AND lc.lamination_material_id = 6
    AND lc.compatibility = 'V'
    AND (lc.product_type IS NULL OR lc.product_type = p.hot_stamping_paper_type)
);

-- 2. 分析简化的过胶查询
\echo '分析简化过胶查询:'
EXPLAIN (ANALYZE, BUFFERS, FORMAT TEXT)
SELECT p.name, lc.foil_series
FROM products p
INNER JOIN lamination_compatibility lc ON lc.foil_series = p.name
WHERE lc.post_processing_step_id = 1
  AND lc.lamination_material_id = 6
  AND lc.compatibility = 'V';

-- 3. 检查索引使用情况
\echo '检查索引使用统计:'
SELECT 
    schemaname,
    tablename,
    indexname,
    idx_scan as scans,
    idx_tup_read as tuples_read,
    idx_tup_fetch as tuples_fetched,
    CASE 
        WHEN idx_scan = 0 THEN '未使用'
        WHEN idx_scan < 10 THEN '很少使用'
        WHEN idx_scan < 100 THEN '偶尔使用'
        ELSE '经常使用'
    END as usage_level
FROM pg_stat_user_indexes 
WHERE tablename IN ('lamination_compatibility', 'products')
ORDER BY idx_scan DESC;

-- 4. 检查表统计信息
\echo '检查表统计信息:'
SELECT 
    schemaname,
    tablename,
    n_tup_ins as inserts,
    n_tup_upd as updates,
    n_tup_del as deletes,
    n_live_tup as live_tuples,
    n_dead_tup as dead_tuples,
    last_vacuum,
    last_autovacuum,
    last_analyze,
    last_autoanalyze
FROM pg_stat_user_tables 
WHERE tablename IN ('lamination_compatibility', 'products')
ORDER BY tablename;

-- 5. 检查索引膨胀情况
\echo '检查索引大小和膨胀:'
SELECT 
    schemaname,
    tablename,
    indexname,
    pg_size_pretty(pg_relation_size(indexrelid)) as index_size,
    pg_size_pretty(pg_relation_size(relid)) as table_size
FROM pg_stat_user_indexes 
WHERE tablename IN ('lamination_compatibility', 'products')
AND indexname LIKE 'idx_%'
ORDER BY pg_relation_size(indexrelid) DESC;

-- 6. 建议的查询优化
\echo '查询优化建议:'
\echo '1. 确保统计信息是最新的:'
\echo '   ANALYZE lamination_compatibility;'
\echo '   ANALYZE products;'
\echo ''
\echo '2. 如果查询仍然较慢，考虑:'
\echo '   - 检查work_mem设置'
\echo '   - 考虑分区表'
\echo '   - 使用物化视图'
\echo ''
\echo '3. 监控索引使用情况:'
\echo '   定期检查pg_stat_user_indexes'
