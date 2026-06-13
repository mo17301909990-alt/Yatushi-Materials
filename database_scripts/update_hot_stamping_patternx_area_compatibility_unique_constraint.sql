-- 更新烫金图案区域兼容性表的唯一性约束
-- 允许相同的燙金紙系列和图案区域组合，只要它们的烫金纸性能类型不同

-- 1. 删除旧的唯一性约束（如果有的话）
DO $$
DECLARE
    constraint_name text;
BEGIN
    -- 查找并删除旧的唯一性约束
    FOR constraint_name IN 
        SELECT conname 
        FROM pg_constraint 
        WHERE conrelid = 'hot_stamping_patternx_area_compatibility'::regclass 
          AND contype = 'u'
    LOOP
        EXECUTE 'ALTER TABLE hot_stamping_patternx_area_compatibility DROP CONSTRAINT IF EXISTS ' || constraint_name;
    END LOOP;
END $$;

-- 2. 创建唯一索引（使用 COALESCE 处理 NULL 值）
-- 将 NULL 视为空字符串，确保唯一性约束正确工作
CREATE UNIQUE INDEX IF NOT EXISTS hot_stamping_patternx_area_compatibility_unique_idx 
ON hot_stamping_patternx_area_compatibility (product_name, hot_stamping_patternx_area_id, COALESCE(paper_type, ''));

-- 3. 验证索引
SELECT 
    indexname AS index_name,
    indexdef AS index_definition
FROM pg_indexes
WHERE tablename = 'hot_stamping_patternx_area_compatibility'
  AND indexname = 'hot_stamping_patternx_area_compatibility_unique_idx';

-- 4. 验证约束（如果有的话）
SELECT 
    conname AS constraint_name,
    contype AS constraint_type,
    pg_get_constraintdef(oid) AS constraint_definition
FROM pg_constraint
WHERE conrelid = 'hot_stamping_patternx_area_compatibility'::regclass
  AND contype = 'u'
ORDER BY conname;

