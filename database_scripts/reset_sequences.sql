-- 重置所有表的序列值，解决主键冲突问题
-- 这个脚本会重置所有表的序列值，使其与现有数据的最大ID同步

-- 检查并重置 specifications 表的序列
DO $$
DECLARE
    max_id INTEGER;
BEGIN
    -- 获取 specifications 表的最大ID
    SELECT COALESCE(MAX(id), 0) INTO max_id FROM specifications;
    
    -- 重置序列值
    PERFORM setval('specifications_id_seq', max_id + 1, false);
    
    RAISE NOTICE 'specifications 序列已重置为: %', max_id + 1;
END $$;

-- 检查并重置 pricing 表的序列
DO $$
DECLARE
    max_id INTEGER;
BEGIN
    -- 获取 pricing 表的最大ID
    SELECT COALESCE(MAX(id), 0) INTO max_id FROM pricing;
    
    -- 重置序列值
    PERFORM setval('pricing_id_seq', max_id + 1, false);
    
    RAISE NOTICE 'pricing 序列已重置为: %', max_id + 1;
END $$;

-- 检查并重置 products 表的序列
DO $$
DECLARE
    max_id INTEGER;
BEGIN
    -- 获取 products 表的最大ID
    SELECT COALESCE(MAX(id), 0) INTO max_id FROM products;
    
    -- 重置序列值
    PERFORM setval('products_id_seq', max_id + 1, false);
    
    RAISE NOTICE 'products 序列已重置为: %', max_id + 1;
END $$;

-- 检查并重置 leo_gp_numbers 表的序列
DO $$
DECLARE
    max_id INTEGER;
BEGIN
    -- 获取 leo_gp_numbers 表的最大ID
    SELECT COALESCE(MAX(id), 0) INTO max_id FROM leo_gp_numbers;
    
    -- 重置序列值
    PERFORM setval('leo_gp_numbers_id_seq', max_id + 1, false);
    
    RAISE NOTICE 'leo_gp_numbers 序列已重置为: %', max_id + 1;
END $$;

-- 显示修复后的序列值
SELECT 
    'specifications' as table_name,
    nextval('specifications_id_seq') - 1 as sequence_value,
    (SELECT MAX(id) FROM specifications) as max_id
UNION ALL
SELECT 
    'pricing' as table_name,
    nextval('pricing_id_seq') - 1 as sequence_value,
    (SELECT MAX(id) FROM pricing) as max_id
UNION ALL
SELECT 
    'products' as table_name,
    nextval('products_id_seq') - 1 as sequence_value,
    (SELECT MAX(id) FROM products) as max_id
UNION ALL
SELECT 
    'leo_gp_numbers' as table_name,
    nextval('leo_gp_numbers_id_seq') - 1 as sequence_value,
    (SELECT MAX(id) FROM leo_gp_numbers) as max_id;
