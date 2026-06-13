-- 更新布面纸兼容性表的唯一性约束
-- 允许相同的燙金紙系列和布面纸类型组合，只要它们的烫金纸性能类型不同

-- 1. 删除旧的唯一性约束（可能有不同的名称）
DO $$
DECLARE
    constraint_name text;
BEGIN
    -- 查找并删除旧的唯一性约束
    FOR constraint_name IN 
        SELECT conname 
        FROM pg_constraint 
        WHERE conrelid = 'cloth_paper_compatibility'::regclass 
          AND contype = 'u'
          AND array_length(conkey, 1) = 2  -- 只删除包含2个字段的约束（旧的约束）
    LOOP
        EXECUTE 'ALTER TABLE cloth_paper_compatibility DROP CONSTRAINT IF EXISTS ' || constraint_name;
    END LOOP;
END $$;

-- 2. 创建唯一索引（使用 COALESCE 处理 NULL 值）
-- 将 NULL 视为空字符串，确保唯一性约束正确工作
CREATE UNIQUE INDEX IF NOT EXISTS cloth_paper_compatibility_unique_idx 
ON cloth_paper_compatibility (product_name, cloth_paper_type_id, COALESCE(paper_type, ''));

-- 3. 验证索引
SELECT 
    indexname AS index_name,
    indexdef AS index_definition
FROM pg_indexes
WHERE tablename = 'cloth_paper_compatibility'
  AND indexname = 'cloth_paper_compatibility_unique_idx';

-- 4. 验证约束（如果有的话）
SELECT 
    conname AS constraint_name,
    contype AS constraint_type,
    pg_get_constraintdef(oid) AS constraint_definition
FROM pg_constraint
WHERE conrelid = 'cloth_paper_compatibility'::regclass
  AND contype = 'u'
ORDER BY conname;

