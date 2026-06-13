-- 修复 specifications 表的序列值
-- 这个脚本会重置序列值，使其与现有数据的最大ID同步

-- 1. 首先查看当前序列值
SELECT currval('specifications_id_seq') as current_sequence_value;

-- 2. 查看表中最大的ID值
SELECT MAX(id) as max_id FROM specifications;

-- 3. 重置序列值，使其等于最大ID + 1
SELECT setval('specifications_id_seq', COALESCE((SELECT MAX(id) FROM specifications), 0) + 1, false);

-- 4. 验证序列值已正确设置
SELECT currval('specifications_id_seq') as new_sequence_value;

-- 5. 显示修复后的表信息
SELECT 
    'specifications' as table_name,
    COUNT(*) as total_records,
    MAX(id) as max_id,
    currval('specifications_id_seq') as sequence_value
FROM specifications;

