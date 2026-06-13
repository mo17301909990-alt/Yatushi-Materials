-- 填充烫金图案面积兼容性数据
-- 根据表格数据填充 hot_stamping_patternx_area_compatibility 表
-- 包含33个产品系列和5个烫金图案的兼容性数据

-- 清空现有数据（可选，根据需要取消注释）
-- DELETE FROM public.hot_stamping_patternx_area_compatibility;

-- 插入烫金图案面积兼容性数据
INSERT INTO public.hot_stamping_patternx_area_compatibility (
    product_name,
    hot_stamping_patternx_area_id,
    compatibility_status,
    paper_type,
    created_at,
    updated_at
) VALUES

-- 普通金紙系列 - 漸變、網點 (id=26)
('SY1', 26, 'V', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SY6-', 26, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SY6', 26, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SY6+', 26, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SSY6', 26, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('G1', 26, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SA', 26, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SB', 26, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('TA', 26, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('TB', 26, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('S19', 26, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('A19', 26, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('AF', 26, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SP', 26, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('KA', 26, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('KB', 26, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('L817/GB', 26, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('E8', 26, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 普通耐磨系列 - 漸變、網點 (id=26)
('ST', 26, 'X', '普通耐磨', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SST', 26, 'X', '普通耐磨', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('GN', 26, 'X', '普通耐磨', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('GN2', 26, 'X', '普通耐磨', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 高耐磨系列 - 漸變、網點 (id=26)
('TB815', 26, 'X', '高耐磨', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 鐳射燙金紙系列 - 漸變、網點 (id=26)
('LA', 26, 'X', '鐳射燙金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('LLW', 26, 'X', '鐳射燙金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('LLO', 26, 'X', '鐳射燙金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('KM', 26, 'X', '鐳射燙金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('G6', 26, 'X', '鐳射燙金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('API', 26, 'X', '鐳射燙金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 色箔系列 - 漸變、網點 (id=26)
('V', 26, 'X', '色箔', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('VB', 26, 'X', '色箔', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 客人指定系列 - 漸變、網點 (id=26)
('YZT 鐳射', 26, 'X', '客人指定', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('YZT 普通', 26, 'X', '客人指定', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 普通金紙系列 - 細線條、字母 (id=27)
('SY1', 27, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SY6-', 27, 'V', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SY6', 27, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SY6+', 27, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SSY6', 27, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('G1', 27, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SA', 27, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SB', 27, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('TA', 27, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('TB', 27, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('S19', 27, 'V', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('A19', 27, 'V', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('AF', 27, 'V', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SP', 27, 'V', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('KA', 27, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('KB', 27, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('L817/GB', 27, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('E8', 27, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 普通耐磨系列 - 細線條、字母 (id=27)
('ST', 27, 'X', '普通耐磨', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SST', 27, 'X', '普通耐磨', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('GN', 27, 'X', '普通耐磨', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('GN2', 27, 'X', '普通耐磨', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 高耐磨系列 - 細線條、字母 (id=27)
('TB815', 27, 'V', '高耐磨', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 鐳射燙金紙系列 - 細線條、字母 (id=27)
('LA', 27, 'X', '鐳射燙金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('LLW', 27, 'X', '鐳射燙金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('LLO', 27, 'X', '鐳射燙金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('KM', 27, 'X', '鐳射燙金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('G6', 27, 'X', '鐳射燙金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('API', 27, 'X', '鐳射燙金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 色箔系列 - 細線條、字母 (id=27)
('V', 27, 'X', '色箔', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('VB', 27, 'X', '色箔', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 客人指定系列 - 細線條、字母 (id=27)
('YZT 鐳射', 27, 'X', '客人指定', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('YZT 普通', 27, 'V', '客人指定', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 普通金紙系列 - 中小面積、線條、字母 (id=28)
('SY1', 28, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SY6-', 28, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SY6', 28, 'V', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SY6+', 28, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SSY6', 28, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('G1', 28, 'V', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SA', 28, 'V', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SB', 28, 'V', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('TA', 28, 'V', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('TB', 28, 'V', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('S19', 28, 'V', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('A19', 28, 'V', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('AF', 28, 'V', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SP', 28, 'V', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('KA', 28, 'V', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('KB', 28, 'V', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('L817/GB', 28, 'V', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('E8', 28, 'V', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 普通耐磨系列 - 中小面積、線條、字母 (id=28)
('ST', 28, 'V', '普通耐磨', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SST', 28, 'V', '普通耐磨', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('GN', 28, 'V', '普通耐磨', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('GN2', 28, 'V', '普通耐磨', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 高耐磨系列 - 中小面積、線條、字母 (id=28)
('TB815', 28, 'V', '高耐磨', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 鐳射燙金紙系列 - 中小面積、線條、字母 (id=28)
('LA', 28, 'V', '鐳射燙金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('LLW', 28, 'V', '鐳射燙金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('LLO', 28, 'V', '鐳射燙金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('KM', 28, 'V', '鐳射燙金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('G6', 28, 'V', '鐳射燙金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('API', 28, 'V', '鐳射燙金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 色箔系列 - 中小面積、線條、字母 (id=28)
('V', 28, 'V', '色箔', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('VB', 28, 'V', '色箔', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 客人指定系列 - 中小面積、線條、字母 (id=28)
('YZT 鐳射', 28, 'V', '客人指定', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('YZT 普通', 28, 'V', '客人指定', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 普通金紙系列 - 中大面積、線條、字母 (id=29)
('SY1', 29, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SY6-', 29, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SY6', 29, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SY6+', 29, 'V', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SSY6', 29, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('G1', 29, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SA', 29, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SB', 29, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('TA', 29, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('TB', 29, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('S19', 29, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('A19', 29, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('AF', 29, 'V', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SP', 29, 'V', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('KA', 29, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('KB', 29, 'V', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('L817/GB', 29, 'V', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('E8', 29, 'V', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 普通耐磨系列 - 中大面積、線條、字母 (id=29)
('ST', 29, 'V', '普通耐磨', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SST', 29, 'V', '普通耐磨', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('GN', 29, 'V', '普通耐磨', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('GN2', 29, 'V', '普通耐磨', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 高耐磨系列 - 中大面積、線條、字母 (id=29)
('TB815', 29, 'X', '高耐磨', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 鐳射燙金紙系列 - 中大面積、線條、字母 (id=29)
('LA', 29, 'X', '鐳射燙金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('LLW', 29, 'V', '鐳射燙金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('LLO', 29, 'X', '鐳射燙金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('KM', 29, 'V', '鐳射燙金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('G6', 29, 'V', '鐳射燙金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('API', 29, 'V', '鐳射燙金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 色箔系列 - 中大面積、線條、字母 (id=29)
('V', 29, 'X', '色箔', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('VB', 29, 'X', '色箔', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 客人指定系列 - 中大面積、線條、字母 (id=29)
('YZT 鐳射', 29, 'V', '客人指定', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('YZT 普通', 29, 'X', '客人指定', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 普通金紙系列 - 大面積 (id=25)
('SY1', 25, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SY6-', 25, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SY6', 25, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SY6+', 25, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SSY6', 25, 'V', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('G1', 25, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SA', 25, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SB', 25, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('TA', 25, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('TB', 25, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('S19', 25, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('A19', 25, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('AF', 25, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SP', 25, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('KA', 25, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('KB', 25, 'X', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('L817/GB', 25, 'V', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('E8', 25, 'V', '普通金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 普通耐磨系列 - 大面積 (id=25)
('ST', 25, 'V', '普通耐磨', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SST', 25, 'V', '普通耐磨', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('GN', 25, 'V', '普通耐磨', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('GN2', 25, 'V', '普通耐磨', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 高耐磨系列 - 大面積 (id=25)
('TB815', 25, 'X', '高耐磨', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 鐳射燙金紙系列 - 大面積 (id=25)
('LA', 25, 'X', '鐳射燙金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('LLW', 25, 'X', '鐳射燙金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('LLO', 25, 'X', '鐳射燙金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('KM', 25, 'X', '鐳射燙金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('G6', 25, 'X', '鐳射燙金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('API', 25, 'X', '鐳射燙金紙', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 色箔系列 - 大面積 (id=25)
('V', 25, 'X', '色箔', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('VB', 25, 'X', '色箔', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 客人指定系列 - 大面積 (id=25)
('YZT 鐳射', 25, 'V', '客人指定', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('YZT 普通', 25, 'X', '客人指定', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 数据验证查询
-- 验证插入的记录数量
SELECT 
    '总记录数' as 统计项,
    COUNT(*) as 数量
FROM public.hot_stamping_patternx_area_compatibility;

-- 验证每个烫金图案的记录数
SELECT 
    hot_stamping_patternx_area_id,
    CASE 
        WHEN hot_stamping_patternx_area_id = 25 THEN '大面積'
        WHEN hot_stamping_patternx_area_id = 26 THEN '漸變、網點'
        WHEN hot_stamping_patternx_area_id = 27 THEN '細線條、字母'
        WHEN hot_stamping_patternx_area_id = 28 THEN '中小面積、線條、字母'
        WHEN hot_stamping_patternx_area_id = 29 THEN '中大面積、線條、字母'
    END as 烫金图案名称,
    COUNT(*) as 记录数
FROM public.hot_stamping_patternx_area_compatibility
GROUP BY hot_stamping_patternx_area_id
ORDER BY hot_stamping_patternx_area_id;

-- 验证每个纸类型的记录数
SELECT 
    paper_type,
    COUNT(*) as 记录数
FROM public.hot_stamping_patternx_area_compatibility
GROUP BY paper_type
ORDER BY paper_type;

-- 验证兼容性状态分布
SELECT 
    compatibility_status,
    COUNT(*) as 记录数
FROM public.hot_stamping_patternx_area_compatibility
GROUP BY compatibility_status
ORDER BY compatibility_status;

-- 验证每个烫金图案的兼容性分布
SELECT 
    hot_stamping_patternx_area_id,
    CASE 
        WHEN hot_stamping_patternx_area_id = 25 THEN '大面積'
        WHEN hot_stamping_patternx_area_id = 26 THEN '漸變、網點'
        WHEN hot_stamping_patternx_area_id = 27 THEN '細線條、字母'
        WHEN hot_stamping_patternx_area_id = 28 THEN '中小面積、線條、字母'
        WHEN hot_stamping_patternx_area_id = 29 THEN '中大面積、線條、字母'
    END as 烫金图案名称,
    SUM(CASE WHEN compatibility_status = 'V' THEN 1 ELSE 0 END) as 兼容记录数,
    SUM(CASE WHEN compatibility_status = 'X' THEN 1 ELSE 0 END) as 不兼容记录数
FROM public.hot_stamping_patternx_area_compatibility
GROUP BY hot_stamping_patternx_area_id
ORDER BY hot_stamping_patternx_area_id;

-- 验证每个纸类型的兼容性分布
SELECT 
    paper_type,
    SUM(CASE WHEN compatibility_status = 'V' THEN 1 ELSE 0 END) as 兼容记录数,
    SUM(CASE WHEN compatibility_status = 'X' THEN 1 ELSE 0 END) as 不兼容记录数
FROM public.hot_stamping_patternx_area_compatibility
GROUP BY paper_type
ORDER BY paper_type;





