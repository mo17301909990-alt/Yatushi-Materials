-- 检查并修复序列问题
-- 这个脚本会检查序列是否存在，如果不存在则创建，然后重置序列值

-- 1. 检查序列是否存在，如果不存在则创建
DO $$
BEGIN
    -- 检查 specifications_id_seq 序列
    IF NOT EXISTS (SELECT 1 FROM pg_sequences WHERE sequencename = 'specifications_id_seq') THEN
        CREATE SEQUENCE specifications_id_seq;
        RAISE NOTICE '创建了 specifications_id_seq 序列';
    ELSE
        RAISE NOTICE 'specifications_id_seq 序列已存在';
    END IF;
    
    -- 检查 pricing_id_seq 序列
    IF NOT EXISTS (SELECT 1 FROM pg_sequences WHERE sequencename = 'pricing_id_seq') THEN
        CREATE SEQUENCE pricing_id_seq;
        RAISE NOTICE '创建了 pricing_id_seq 序列';
    ELSE
        RAISE NOTICE 'pricing_id_seq 序列已存在';
    END IF;
    
    -- 检查 products_id_seq 序列
    IF NOT EXISTS (SELECT 1 FROM pg_sequences WHERE sequencename = 'products_id_seq') THEN
        CREATE SEQUENCE products_id_seq;
        RAISE NOTICE '创建了 products_id_seq 序列';
    ELSE
        RAISE NOTICE 'products_id_seq 序列已存在';
    END IF;
    
    -- 检查 leo_gp_numbers_id_seq 序列
    IF NOT EXISTS (SELECT 1 FROM pg_sequences WHERE sequencename = 'leo_gp_numbers_id_seq') THEN
        CREATE SEQUENCE leo_gp_numbers_id_seq;
        RAISE NOTICE '创建了 leo_gp_numbers_id_seq 序列';
    ELSE
        RAISE NOTICE 'leo_gp_numbers_id_seq 序列已存在';
    END IF;
END $$;

-- 2. 重置序列值
DO $$
DECLARE
    max_id INTEGER;
BEGIN
    -- 重置 specifications 序列
    SELECT COALESCE(MAX(id), 0) INTO max_id FROM specifications;
    PERFORM setval('specifications_id_seq', max_id + 1, false);
    RAISE NOTICE 'specifications 序列重置为: %', max_id + 1;
    
    -- 重置 pricing 序列
    SELECT COALESCE(MAX(id), 0) INTO max_id FROM pricing;
    PERFORM setval('pricing_id_seq', max_id + 1, false);
    RAISE NOTICE 'pricing 序列重置为: %', max_id + 1;
    
    -- 重置 products 序列
    SELECT COALESCE(MAX(id), 0) INTO max_id FROM products;
    PERFORM setval('products_id_seq', max_id + 1, false);
    RAISE NOTICE 'products 序列重置为: %', max_id + 1;
    
    -- 重置 leo_gp_numbers 序列
    SELECT COALESCE(MAX(id), 0) INTO max_id FROM leo_gp_numbers;
    PERFORM setval('leo_gp_numbers_id_seq', max_id + 1, false);
    RAISE NOTICE 'leo_gp_numbers 序列重置为: %', max_id + 1;
END $$;

-- 3. 显示当前状态
SELECT 
    'specifications' as table_name,
    (SELECT MAX(id) FROM specifications) as max_id,
    nextval('specifications_id_seq') as next_sequence_value
UNION ALL
SELECT 
    'pricing' as table_name,
    (SELECT MAX(id) FROM pricing) as max_id,
    nextval('pricing_id_seq') as next_sequence_value
UNION ALL
SELECT 
    'products' as table_name,
    (SELECT MAX(id) FROM products) as max_id,
    nextval('products_id_seq') as next_sequence_value
UNION ALL
SELECT 
    'leo_gp_numbers' as table_name,
    (SELECT MAX(id) FROM leo_gp_numbers) as max_id,
    nextval('leo_gp_numbers_id_seq') as next_sequence_value;

