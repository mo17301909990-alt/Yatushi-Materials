-- 插入布面纸类型数据
-- 基于布面纸分类表生成

-- 清空现有数据（如果需要）
-- DELETE FROM cloth_paper_types;

-- 插入布面纸类型数据
INSERT INTO cloth_paper_types (type_name, category, sort_order, is_active) VALUES
-- 尼龍絲系列
('尼龍絲JHT-8001', '尼龍絲', 1, TRUE),
('尼龍絲JHT-8004', '尼龍絲', 2, TRUE),
('尼龍絲JHT-8013', '尼龍絲', 3, TRUE),

-- 涤纶系列
('涤纶JHT-8002', '涤纶', 4, TRUE),
('涤纶JHT-8010', '涤纶', 5, TRUE),
('涤纶JHT-8014', '涤纶', 6, TRUE),
('涤纶JHT-8015', '涤纶', 7, TRUE),
('涤纶LTS-8003', '涤纶', 8, TRUE),
('涤纶ESM', '涤纶', 9, TRUE),

-- 仿棉系列
('仿棉JHT-8003', '仿棉', 10, TRUE),
('仿棉JHT-8017', '仿棉', 11, TRUE),

-- 涤棉系列
('涤棉JHT-8006', '涤棉', 12, TRUE),
('涤棉JHT-8008', '涤棉', 13, TRUE),
('涤棉JHT-8011', '涤棉', 14, TRUE),
('涤棉JHT-8016', '涤棉', 15, TRUE),
('涤棉JHT-8018', '涤棉', 16, TRUE),
('涤棉JHT-8019', '涤棉', 17, TRUE),
('涤棉JHT-910', '涤棉', 18, TRUE),
('涤棉JHT-8007', '涤棉', 19, TRUE),
('涤棉JHT-8009', '涤棉', 20, TRUE),
('涤棉JHT-8012', '涤棉', 21, TRUE),

-- 棉布系列
('棉布JHT0001~0103', '棉布', 22, TRUE),

-- 丝光棉系列
('丝光棉JHT0104~0195', '丝光棉', 23, TRUE),
('丝光棉JHT0199~0209', '丝光棉', 24, TRUE),
('丝光棉JHT0211', '丝光棉', 25, TRUE),
('丝光棉JHT0213', '丝光棉', 26, TRUE),

-- 网布系列
('网布JHT0196~0198', '网布', 27, TRUE),
('网布JHT0265~0311', '网布', 28, TRUE),

-- 闪光布系列
('闪光布JHT0216', '闪光布', 29, TRUE),
('闪光布JHT0218', '闪光布', 30, TRUE),
('闪光布JHT0312~0330', '闪光布', 31, TRUE),
('闪光布JHT0219~0264', '闪光布', 32, TRUE),

-- 麻布系列
('麻布JHT0334~0351', '麻布', 33, TRUE),

-- 塑纹布系列
('塑纹布JHT0352~0386', '塑纹布', 34, TRUE),

-- 丝光麻布系列
('丝光麻布JHT0387~0407', '丝光麻布', 35, TRUE),

-- 粗布系列
('粗布JHT0419~0429', '粗布', 36, TRUE);

-- 验证插入结果
SELECT 
    id,
    type_name,
    category,
    sort_order,
    is_active,
    created_at
FROM cloth_paper_types 
ORDER BY sort_order;

