-- PostgreSQL 布面纸类型表创建和数据插入脚本
-- 用于重构列式设计为行式设计，支持管理员动态扩展

-- 1. 创建布面纸类型表
CREATE TABLE IF NOT EXISTS cloth_paper_types (
    id SERIAL PRIMARY KEY,
    type_name VARCHAR(100) UNIQUE NOT NULL,  -- 如 'JHT-8001', 'JHT-8004'
    category VARCHAR(50),                    -- 如 '尼龍絲', '涤纶'
    sort_order INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. 创建布面纸兼容性表（使用外键关联）
CREATE TABLE IF NOT EXISTS cloth_paper_compatibility (
    id SERIAL PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,  -- 物料型号系列，如 'L187', 'SY+', 'G1', 'ST', 'GN', 'LA'
    cloth_paper_type_id INTEGER NOT NULL,  -- 布面纸类型ID，外键关联cloth_paper_types表
    compatibility_status CHAR(1) NOT NULL CHECK (compatibility_status IN ('V', 'X', 'NA', '▷')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(product_name, cloth_paper_type_id)  -- 确保每个物料型号系列每个布面纸类型只有一条记录
);

-- 添加外键约束
ALTER TABLE cloth_paper_compatibility 
ADD CONSTRAINT fk_cloth_paper_compatibility_type 
FOREIGN KEY (cloth_paper_type_id) REFERENCES cloth_paper_types(id);

-- 3. 添加表注释
COMMENT ON TABLE cloth_paper_types IS '布面纸类型配置表';
COMMENT ON TABLE cloth_paper_compatibility IS '布面纸与物料型号兼容性关系表';

COMMENT ON COLUMN cloth_paper_types.type_name IS '布面纸类型名称，包含代码和描述';
COMMENT ON COLUMN cloth_paper_types.category IS '布面纸分类';
COMMENT ON COLUMN cloth_paper_types.sort_order IS '排序顺序';
COMMENT ON COLUMN cloth_paper_types.is_active IS '是否启用';

COMMENT ON COLUMN cloth_paper_compatibility.product_name IS '物料型号系列，对应products表的name字段';
COMMENT ON COLUMN cloth_paper_compatibility.cloth_paper_type_id IS '布面纸类型ID，外键关联cloth_paper_types表';
COMMENT ON COLUMN cloth_paper_compatibility.compatibility_status IS '兼容性状态：V-适用，X-不适用，NA-不确定，▷-需要打底处理';

-- 4. 创建索引
CREATE INDEX IF NOT EXISTS idx_cloth_paper_types_category ON cloth_paper_types(category);
CREATE INDEX IF NOT EXISTS idx_cloth_paper_types_active ON cloth_paper_types(is_active);
CREATE INDEX IF NOT EXISTS idx_cloth_paper_compatibility_product ON cloth_paper_compatibility(product_name);
CREATE INDEX IF NOT EXISTS idx_cloth_paper_compatibility_type ON cloth_paper_compatibility(cloth_paper_type_id);

-- 5. 清空现有数据（如果需要重新插入）
-- DELETE FROM cloth_paper_compatibility;
-- DELETE FROM cloth_paper_types;

-- 6. 插入布面纸类型数据
INSERT INTO cloth_paper_types (type_name, category, sort_order, is_active) VALUES
-- 粉紙面系列
('單粉咭', '粉紙面', 1, TRUE),
('雙粉咭', '粉紙面', 2, TRUE),
('單粉紙', '粉紙面', 3, TRUE),
('啞粉紙', '粉紙面', 4, TRUE),

-- 無粉面系列
('無粉咭', '無粉面', 5, TRUE),
('書紙', '無粉面', 6, TRUE),
('花紙', '無粉面', 7, TRUE),

-- 膠片系列
('普通PET/PVC/APET', '膠片', 8, TRUE),
('防刮APET-防刮面不可燙金', '膠片', 9, TRUE),

-- 牛油紙系列
('GTF', '牛油紙', 10, TRUE),
('ZTF', '牛油紙', 11, TRUE),

-- 尼龍絲系列
('JHT-8001', '尼龍絲', 12, TRUE),
('JHT-8004', '尼龍絲', 13, TRUE),
('JHT-8013', '尼龍絲', 14, TRUE),

-- 涤纶系列
('JHT-8002', '涤纶', 15, TRUE),
('JHT-8010', '涤纶', 16, TRUE),
('JHT-8014', '涤纶', 17, TRUE),
('JHT-8015', '涤纶', 18, TRUE),
('LTS-8003', '涤纶', 19, TRUE),
('ESM', '涤纶', 20, TRUE),

-- 仿棉系列
('JHT-8003', '仿棉', 21, TRUE),
('JHT-8017', '仿棉', 22, TRUE),

-- 涤棉系列
('JHT-8006', '涤棉', 23, TRUE),
('JHT-8008', '涤棉', 24, TRUE),
('JHT-8011', '涤棉', 25, TRUE),
('JHT-8016', '涤棉', 26, TRUE),
('JHT-8018', '涤棉', 27, TRUE),
('JHT-8019', '涤棉', 28, TRUE),
('JHT-910', '涤棉', 29, TRUE),

-- 純棉系列
('JHT-8007', '純棉', 30, TRUE),
('JHT-8009', '純棉', 31, TRUE),
('JHT-8012', '純棉', 32, TRUE),

-- 棉布系列
('JHT0001-0103', '棉布', 33, TRUE),

-- 絲光棉系列
('JHT0104-0195', '絲光棉', 34, TRUE),
('JHT0199-0209', '絲光棉', 35, TRUE),
('JHT0211', '絲光棉', 36, TRUE),
('JHT0213', '絲光棉', 37, TRUE),

-- 絹布系列
('JHT0196-0198', '絹布', 38, TRUE),
('JHT0265-0311', '絹布', 39, TRUE),

-- 閃光布系列
('JHT0216', '閃光布', 40, TRUE),
('JHT0218', '閃光布', 41, TRUE),
('JHT0312-0330', '閃光布', 42, TRUE),

-- 絲綢布系列
('JHT0219-0264', '絲綢布', 43, TRUE),

-- 麻壓布系列
('JHT0334-0351', '麻壓布', 44, TRUE),

-- 壓紋布系列
('JHT0352-0386', '壓紋布', 45, TRUE),

-- 絲光麻布系列
('JHT0387-0407', '絲光麻布', 46, TRUE),

-- 粗布系列
('JHT0419-0429', '粗布', 47, TRUE);

-- 7. 验证插入结果
SELECT 
    id,
    type_name,
    category,
    sort_order,
    is_active,
    created_at
FROM cloth_paper_types 
ORDER BY sort_order;

-- 8. 统计信息
SELECT 
    category,
    COUNT(*) as type_count
FROM cloth_paper_types 
WHERE is_active = TRUE
GROUP BY category
ORDER BY MIN(sort_order);