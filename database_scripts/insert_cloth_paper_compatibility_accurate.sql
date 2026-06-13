-- 布面纸与物料型号兼容性数据精确插入脚本
-- 基于「布面紙+燙金」組合應用表，只包含表格中明确列出的物料型号系列

-- 清空现有兼容性数据（如果需要重新插入）
-- DELETE FROM cloth_paper_compatibility;

-- 插入兼容性数据
-- 根据表格，所有布面纸材质与物料型号系列的兼容性都是空的（需要后续填写）
-- 这里先插入基础结构，兼容性状态设为'NA'（不确定）

INSERT INTO cloth_paper_compatibility (product_name, cloth_paper_category, compatibility_status) VALUES

-- L187系列兼容性（普通金纸系列）
('L187', '尼龍絲', 'NA'),
('L187', '涤纶', 'NA'),
('L187', '仿棉', 'NA'),
('L187', '涤棉', 'NA'),
('L187', '麻棉', 'NA'),
('L187', '純棉', 'NA'),
('L187', '棉布', 'NA'),
('L187', '絲光棉', 'NA'),
('L187', '絹布', 'NA'),
('L187', '閃光布', 'NA'),
('L187', '絲綢布', 'NA'),
('L187', '麻壓布', 'NA'),
('L187', '壓紋布', 'NA'),
('L187', '絲光麻布', 'NA'),
('L187', '粗布', 'NA'),

-- SY+系列兼容性（普通金纸系列）
('SY+', '尼龍絲', 'NA'),
('SY+', '涤纶', 'NA'),
('SY+', '仿棉', 'NA'),
('SY+', '涤棉', 'NA'),
('SY+', '麻棉', 'NA'),
('SY+', '純棉', 'NA'),
('SY+', '棉布', 'NA'),
('SY+', '絲光棉', 'NA'),
('SY+', '絹布', 'NA'),
('SY+', '閃光布', 'NA'),
('SY+', '絲綢布', 'NA'),
('SY+', '麻壓布', 'NA'),
('SY+', '壓紋布', 'NA'),
('SY+', '絲光麻布', 'NA'),
('SY+', '粗布', 'NA'),

-- G1系列兼容性（耐磨金纸系列）
('G1', '尼龍絲', 'NA'),
('G1', '涤纶', 'NA'),
('G1', '仿棉', 'NA'),
('G1', '涤棉', 'NA'),
('G1', '麻棉', 'NA'),
('G1', '純棉', 'NA'),
('G1', '棉布', 'NA'),
('G1', '絲光棉', 'NA'),
('G1', '絹布', 'NA'),
('G1', '閃光布', 'NA'),
('G1', '絲綢布', 'NA'),
('G1', '麻壓布', 'NA'),
('G1', '壓紋布', 'NA'),
('G1', '絲光麻布', 'NA'),
('G1', '粗布', 'NA'),

-- ST系列兼容性（耐磨金纸系列）
('ST', '尼龍絲', 'NA'),
('ST', '涤纶', 'NA'),
('ST', '仿棉', 'NA'),
('ST', '涤棉', 'NA'),
('ST', '麻棉', 'NA'),
('ST', '純棉', 'NA'),
('ST', '棉布', 'NA'),
('ST', '絲光棉', 'NA'),
('ST', '絹布', 'NA'),
('ST', '閃光布', 'NA'),
('ST', '絲綢布', 'NA'),
('ST', '麻壓布', 'NA'),
('ST', '壓紋布', 'NA'),
('ST', '絲光麻布', 'NA'),
('ST', '粗布', 'NA'),

-- GN系列兼容性（耐磨金纸系列）
('GN', '尼龍絲', 'NA'),
('GN', '涤纶', 'NA'),
('GN', '仿棉', 'NA'),
('GN', '涤棉', 'NA'),
('GN', '麻棉', 'NA'),
('GN', '純棉', 'NA'),
('GN', '棉布', 'NA'),
('GN', '絲光棉', 'NA'),
('GN', '絹布', 'NA'),
('GN', '閃光布', 'NA'),
('GN', '絲綢布', 'NA'),
('GN', '麻壓布', 'NA'),
('GN', '壓紋布', 'NA'),
('GN', '絲光麻布', 'NA'),
('GN', '粗布', 'NA'),

-- LA系列兼容性（鐳射金紙系列）
('LA', '尼龍絲', 'NA'),
('LA', '涤纶', 'NA'),
('LA', '仿棉', 'NA'),
('LA', '涤棉', 'NA'),
('LA', '麻棉', 'NA'),
('LA', '純棉', 'NA'),
('LA', '棉布', 'NA'),
('LA', '絲光棉', 'NA'),
('LA', '絹布', 'NA'),
('LA', '閃光布', 'NA'),
('LA', '絲綢布', 'NA'),
('LA', '麻壓布', 'NA'),
('LA', '壓紋布', 'NA'),
('LA', '絲光麻布', 'NA'),
('LA', '粗布', 'NA');

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

-- 显示所有组合
SELECT 
    product_name as 物料型号,
    cloth_paper_category as 布面纸系列,
    compatibility_status as 兼容性状态
FROM cloth_paper_compatibility 
ORDER BY product_name, cloth_paper_category;
