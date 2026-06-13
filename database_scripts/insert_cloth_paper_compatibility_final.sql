-- 布面纸与物料型号兼容性数据最终插入脚本
-- 使用外键ID关联，基于「布面紙+燙金」組合應用表

-- 清空现有兼容性数据（如果需要重新插入）
-- DELETE FROM cloth_paper_compatibility;

-- 插入兼容性数据
-- 根据表格，所有布面纸类型与物料型号系列的兼容性都是空的（需要后续填写）
-- 这里先插入基础结构，兼容性状态设为'NA'（不确定）

INSERT INTO cloth_paper_compatibility (product_name, cloth_paper_type_id, compatibility_status) VALUES

-- L187系列兼容性（普通金纸系列）
('L187', 12, 'NA'),  -- JHT-8001
('L187', 13, 'NA'),  -- JHT-8004
('L187', 14, 'NA'),  -- JHT-8013
('L187', 15, 'NA'),  -- JHT-8002
('L187', 16, 'NA'),  -- JHT-8010
('L187', 17, 'NA'),  -- JHT-8014
('L187', 18, 'NA'),  -- JHT-8015
('L187', 19, 'NA'),  -- LTS-8003
('L187', 20, 'NA'),  -- ESM
('L187', 21, 'NA'),  -- JHT-8003
('L187', 22, 'NA'),  -- JHT-8017
('L187', 23, 'NA'),  -- JHT-8006
('L187', 24, 'NA'),  -- JHT-8008
('L187', 25, 'NA'),  -- JHT-8011
('L187', 26, 'NA'),  -- JHT-8016
('L187', 27, 'NA'),  -- JHT-8018
('L187', 28, 'NA'),  -- JHT-8019
('L187', 29, 'NA'),  -- JHT-910
('L187', 30, 'NA'),  -- JHT-8007
('L187', 31, 'NA'),  -- JHT-8009
('L187', 32, 'NA'),  -- JHT-8012
('L187', 33, 'NA'),  -- JHT0001-0103
('L187', 34, 'NA'),  -- JHT0104-0195
('L187', 35, 'NA'),  -- JHT0199-0209
('L187', 36, 'NA'),  -- JHT0211
('L187', 37, 'NA'),  -- JHT0213
('L187', 38, 'NA'),  -- JHT0196-0198
('L187', 39, 'NA'),  -- JHT0265-0311
('L187', 40, 'NA'),  -- JHT0216
('L187', 41, 'NA'),  -- JHT0218
('L187', 42, 'NA'),  -- JHT0312-0330
('L187', 43, 'NA'),  -- JHT0219-0264
('L187', 44, 'NA'),  -- JHT0334-0351
('L187', 45, 'NA'),  -- JHT0352-0386
('L187', 46, 'NA'),  -- JHT0387-0407
('L187', 47, 'NA'),  -- JHT0419-0429

-- SY+系列兼容性（普通金纸系列）
('SY+', 12, 'NA'),  -- JHT-8001
('SY+', 13, 'NA'),  -- JHT-8004
('SY+', 14, 'NA'),  -- JHT-8013
('SY+', 15, 'NA'),  -- JHT-8002
('SY+', 16, 'NA'),  -- JHT-8010
('SY+', 17, 'NA'),  -- JHT-8014
('SY+', 18, 'NA'),  -- JHT-8015
('SY+', 19, 'NA'),  -- LTS-8003
('SY+', 20, 'NA'),  -- ESM
('SY+', 21, 'NA'),  -- JHT-8003
('SY+', 22, 'NA'),  -- JHT-8017
('SY+', 23, 'NA'),  -- JHT-8006
('SY+', 24, 'NA'),  -- JHT-8008
('SY+', 25, 'NA'),  -- JHT-8011
('SY+', 26, 'NA'),  -- JHT-8016
('SY+', 27, 'NA'),  -- JHT-8018
('SY+', 28, 'NA'),  -- JHT-8019
('SY+', 29, 'NA'),  -- JHT-910
('SY+', 30, 'NA'),  -- JHT-8007
('SY+', 31, 'NA'),  -- JHT-8009
('SY+', 32, 'NA'),  -- JHT-8012
('SY+', 33, 'NA'),  -- JHT0001-0103
('SY+', 34, 'NA'),  -- JHT0104-0195
('SY+', 35, 'NA'),  -- JHT0199-0209
('SY+', 36, 'NA'),  -- JHT0211
('SY+', 37, 'NA'),  -- JHT0213
('SY+', 38, 'NA'),  -- JHT0196-0198
('SY+', 39, 'NA'),  -- JHT0265-0311
('SY+', 40, 'NA'),  -- JHT0216
('SY+', 41, 'NA'),  -- JHT0218
('SY+', 42, 'NA'),  -- JHT0312-0330
('SY+', 43, 'NA'),  -- JHT0219-0264
('SY+', 44, 'NA'),  -- JHT0334-0351
('SY+', 45, 'NA'),  -- JHT0352-0386
('SY+', 46, 'NA'),  -- JHT0387-0407
('SY+', 47, 'NA'),  -- JHT0419-0429

-- G1系列兼容性（耐磨金纸系列）
('G1', 12, 'NA'),  -- JHT-8001
('G1', 13, 'NA'),  -- JHT-8004
('G1', 14, 'NA'),  -- JHT-8013
('G1', 15, 'NA'),  -- JHT-8002
('G1', 16, 'NA'),  -- JHT-8010
('G1', 17, 'NA'),  -- JHT-8014
('G1', 18, 'NA'),  -- JHT-8015
('G1', 19, 'NA'),  -- LTS-8003
('G1', 20, 'NA'),  -- ESM
('G1', 21, 'NA'),  -- JHT-8003
('G1', 22, 'NA'),  -- JHT-8017
('G1', 23, 'NA'),  -- JHT-8006
('G1', 24, 'NA'),  -- JHT-8008
('G1', 25, 'NA'),  -- JHT-8011
('G1', 26, 'NA'),  -- JHT-8016
('G1', 27, 'NA'),  -- JHT-8018
('G1', 28, 'NA'),  -- JHT-8019
('G1', 29, 'NA'),  -- JHT-910
('G1', 30, 'NA'),  -- JHT-8007
('G1', 31, 'NA'),  -- JHT-8009
('G1', 32, 'NA'),  -- JHT-8012
('G1', 33, 'NA'),  -- JHT0001-0103
('G1', 34, 'NA'),  -- JHT0104-0195
('G1', 35, 'NA'),  -- JHT0199-0209
('G1', 36, 'NA'),  -- JHT0211
('G1', 37, 'NA'),  -- JHT0213
('G1', 38, 'NA'),  -- JHT0196-0198
('G1', 39, 'NA'),  -- JHT0265-0311
('G1', 40, 'NA'),  -- JHT0216
('G1', 41, 'NA'),  -- JHT0218
('G1', 42, 'NA'),  -- JHT0312-0330
('G1', 43, 'NA'),  -- JHT0219-0264
('G1', 44, 'NA'),  -- JHT0334-0351
('G1', 45, 'NA'),  -- JHT0352-0386
('G1', 46, 'NA'),  -- JHT0387-0407
('G1', 47, 'NA'),  -- JHT0419-0429

-- ST系列兼容性（耐磨金纸系列）
('ST', 12, 'NA'),  -- JHT-8001
('ST', 13, 'NA'),  -- JHT-8004
('ST', 14, 'NA'),  -- JHT-8013
('ST', 15, 'NA'),  -- JHT-8002
('ST', 16, 'NA'),  -- JHT-8010
('ST', 17, 'NA'),  -- JHT-8014
('ST', 18, 'NA'),  -- JHT-8015
('ST', 19, 'NA'),  -- LTS-8003
('ST', 20, 'NA'),  -- ESM
('ST', 21, 'NA'),  -- JHT-8003
('ST', 22, 'NA'),  -- JHT-8017
('ST', 23, 'NA'),  -- JHT-8006
('ST', 24, 'NA'),  -- JHT-8008
('ST', 25, 'NA'),  -- JHT-8011
('ST', 26, 'NA'),  -- JHT-8016
('ST', 27, 'NA'),  -- JHT-8018
('ST', 28, 'NA'),  -- JHT-8019
('ST', 29, 'NA'),  -- JHT-910
('ST', 30, 'NA'),  -- JHT-8007
('ST', 31, 'NA'),  -- JHT-8009
('ST', 32, 'NA'),  -- JHT-8012
('ST', 33, 'NA'),  -- JHT0001-0103
('ST', 34, 'NA'),  -- JHT0104-0195
('ST', 35, 'NA'),  -- JHT0199-0209
('ST', 36, 'NA'),  -- JHT0211
('ST', 37, 'NA'),  -- JHT0213
('ST', 38, 'NA'),  -- JHT0196-0198
('ST', 39, 'NA'),  -- JHT0265-0311
('ST', 40, 'NA'),  -- JHT0216
('ST', 41, 'NA'),  -- JHT0218
('ST', 42, 'NA'),  -- JHT0312-0330
('ST', 43, 'NA'),  -- JHT0219-0264
('ST', 44, 'NA'),  -- JHT0334-0351
('ST', 45, 'NA'),  -- JHT0352-0386
('ST', 46, 'NA'),  -- JHT0387-0407
('ST', 47, 'NA'),  -- JHT0419-0429

-- GN系列兼容性（耐磨金纸系列）
('GN', 12, 'NA'),  -- JHT-8001
('GN', 13, 'NA'),  -- JHT-8004
('GN', 14, 'NA'),  -- JHT-8013
('GN', 15, 'NA'),  -- JHT-8002
('GN', 16, 'NA'),  -- JHT-8010
('GN', 17, 'NA'),  -- JHT-8014
('GN', 18, 'NA'),  -- JHT-8015
('GN', 19, 'NA'),  -- LTS-8003
('GN', 20, 'NA'),  -- ESM
('GN', 21, 'NA'),  -- JHT-8003
('GN', 22, 'NA'),  -- JHT-8017
('GN', 23, 'NA'),  -- JHT-8006
('GN', 24, 'NA'),  -- JHT-8008
('GN', 25, 'NA'),  -- JHT-8011
('GN', 26, 'NA'),  -- JHT-8016
('GN', 27, 'NA'),  -- JHT-8018
('GN', 28, 'NA'),  -- JHT-8019
('GN', 29, 'NA'),  -- JHT-910
('GN', 30, 'NA'),  -- JHT-8007
('GN', 31, 'NA'),  -- JHT-8009
('GN', 32, 'NA'),  -- JHT-8012
('GN', 33, 'NA'),  -- JHT0001-0103
('GN', 34, 'NA'),  -- JHT0104-0195
('GN', 35, 'NA'),  -- JHT0199-0209
('GN', 36, 'NA'),  -- JHT0211
('GN', 37, 'NA'),  -- JHT0213
('GN', 38, 'NA'),  -- JHT0196-0198
('GN', 39, 'NA'),  -- JHT0265-0311
('GN', 40, 'NA'),  -- JHT0216
('GN', 41, 'NA'),  -- JHT0218
('GN', 42, 'NA'),  -- JHT0312-0330
('GN', 43, 'NA'),  -- JHT0219-0264
('GN', 44, 'NA'),  -- JHT0334-0351
('GN', 45, 'NA'),  -- JHT0352-0386
('GN', 46, 'NA'),  -- JHT0387-0407
('GN', 47, 'NA'),  -- JHT0419-0429

-- LA系列兼容性（鐳射金紙系列）
('LA', 12, 'NA'),  -- JHT-8001
('LA', 13, 'NA'),  -- JHT-8004
('LA', 14, 'NA'),  -- JHT-8013
('LA', 15, 'NA'),  -- JHT-8002
('LA', 16, 'NA'),  -- JHT-8010
('LA', 17, 'NA'),  -- JHT-8014
('LA', 18, 'NA'),  -- JHT-8015
('LA', 19, 'NA'),  -- LTS-8003
('LA', 20, 'NA'),  -- ESM
('LA', 21, 'NA'),  -- JHT-8003
('LA', 22, 'NA'),  -- JHT-8017
('LA', 23, 'NA'),  -- JHT-8006
('LA', 24, 'NA'),  -- JHT-8008
('LA', 25, 'NA'),  -- JHT-8011
('LA', 26, 'NA'),  -- JHT-8016
('LA', 27, 'NA'),  -- JHT-8018
('LA', 28, 'NA'),  -- JHT-8019
('LA', 29, 'NA'),  -- JHT-910
('LA', 30, 'NA'),  -- JHT-8007
('LA', 31, 'NA'),  -- JHT-8009
('LA', 32, 'NA'),  -- JHT-8012
('LA', 33, 'NA'),  -- JHT0001-0103
('LA', 34, 'NA'),  -- JHT0104-0195
('LA', 35, 'NA'),  -- JHT0199-0209
('LA', 36, 'NA'),  -- JHT0211
('LA', 37, 'NA'),  -- JHT0213
('LA', 38, 'NA'),  -- JHT0196-0198
('LA', 39, 'NA'),  -- JHT0265-0311
('LA', 40, 'NA'),  -- JHT0216
('LA', 41, 'NA'),  -- JHT0218
('LA', 42, 'NA'),  -- JHT0312-0330
('LA', 43, 'NA'),  -- JHT0219-0264
('LA', 44, 'NA'),  -- JHT0334-0351
('LA', 45, 'NA'),  -- JHT0352-0386
('LA', 46, 'NA'),  -- JHT0387-0407
('LA', 47, 'NA');  -- JHT0419-0429

-- 验证插入结果（带JOIN查询显示完整信息）
SELECT 
    cpc.product_name as 物料型号系列,
    cpt.category as 布面纸系列,
    cpt.type_name as 布面纸类型,
    cpc.compatibility_status as 兼容性状态,
    cpc.created_at
FROM cloth_paper_compatibility cpc
JOIN cloth_paper_types cpt ON cpc.cloth_paper_type_id = cpt.id
ORDER BY cpc.product_name, cpt.category, cpt.type_name;

-- 统计信息
SELECT 
    product_name as 物料型号系列,
    COUNT(*) as 兼容性记录数
FROM cloth_paper_compatibility 
GROUP BY product_name
ORDER BY product_name;

-- 按布面纸系列统计
SELECT 
    cpt.category as 布面纸系列,
    COUNT(*) as 兼容性记录数
FROM cloth_paper_compatibility cpc
JOIN cloth_paper_types cpt ON cpc.cloth_paper_type_id = cpt.id
GROUP BY cpt.category
ORDER BY cpt.category;
