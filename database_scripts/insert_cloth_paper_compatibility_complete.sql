-- 布面纸与物料型号兼容性数据完整插入脚本
-- 基于「布面紙+燙金」組合應用表和products表中的物料型号

-- 清空现有兼容性数据（如果需要重新插入）
-- DELETE FROM cloth_paper_compatibility;

-- 获取所有布面纸系列
WITH cloth_paper_categories AS (
    SELECT DISTINCT category FROM cloth_paper_types WHERE is_active = TRUE
),
-- 获取所有物料型号系列（从products表）
product_series AS (
    SELECT DISTINCT name FROM products WHERE name IN (
        'L187', 'SY+', 'G1', 'ST', 'GN', 'LA', 'A23', 'A38', 'AF', 'SY6-', 'SY1-', 'SB', 'SA', 'S19', 'S16', 'LL',
        'L817', 'KM', 'KB', 'KA', 'G6', 'E8', 'ST606', 'ST618', 'ST5734', 'ST684', 'ST677-1', 'STA604-4', 'ST677-2',
        'ST621', 'ST618-B', 'ST618-A', 'ST650A', 'ST697-5', 'ST650-2', 'ST652-4', 'ST656-1', 'ST631', 'STA638-1',
        'ST635-3', 'ST638', 'ST640', 'ST5736', 'ST5739', 'SY170+', 'SH111+', 'SY0156+', 'SY0700+', 'SH120+',
        'SY0149+', 'SY0174+', 'SY0182+', 'SY0183+', 'SY0201+', 'SY0205+', 'SY0634+', 'SY0635+', 'SYZ122+',
        'SY0691+', 'SYF160+', 'SY0720+', 'SY0751+', 'SY1110+', 'SY2152+', 'SY5734+', 'SY5738+', 'SY5739+'
    )
)
-- 插入所有组合的兼容性数据
INSERT INTO cloth_paper_compatibility (product_name, cloth_paper_category, compatibility_status)
SELECT 
    ps.name,
    cpc.category,
    'NA'  -- 初始状态为不确定
FROM product_series ps
CROSS JOIN cloth_paper_categories cpc
ON CONFLICT (product_name, cloth_paper_category) DO NOTHING;

-- 验证插入结果
SELECT 
    product_name,
    cloth_paper_category,
    compatibility_status,
    created_at
FROM cloth_paper_compatibility 
ORDER BY product_name, cloth_paper_category;

-- 统计信息
SELECT 
    product_name,
    COUNT(*) as compatibility_count
FROM cloth_paper_compatibility 
GROUP BY product_name
ORDER BY product_name;

-- 按布面纸系列统计
SELECT 
    cloth_paper_category,
    COUNT(*) as compatibility_count
FROM cloth_paper_compatibility 
GROUP BY cloth_paper_category
ORDER BY cloth_paper_category;
