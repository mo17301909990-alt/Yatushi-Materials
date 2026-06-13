-- 创建耐磨金纸映射表
-- 用于存储耐磨金纸与产品系列、型号的兼容性映射关系

CREATE TABLE IF NOT EXISTS wear_resistant_gold_paper_mapping (
    id SERIAL PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    product_model_number VARCHAR(255),
    hot_stamping_type VARCHAR(255) NOT NULL DEFAULT '燙金後擊凸',
    gold_paper_type VARCHAR(255) NOT NULL,
    compatibility_status CHAR(1) NOT NULL CHECK (compatibility_status IN ('V', 'X', 'NA', '▷')),
    remarks TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(product_name, COALESCE(product_model_number, ''), gold_paper_type)
);

-- 添加表注释
COMMENT ON TABLE wear_resistant_gold_paper_mapping IS '耐磨金纸映射表';
COMMENT ON COLUMN wear_resistant_gold_paper_mapping.id IS '主键ID';
COMMENT ON COLUMN wear_resistant_gold_paper_mapping.product_name IS '燙金紙系列';
COMMENT ON COLUMN wear_resistant_gold_paper_mapping.product_model_number IS '产品型号（可为空，表示系列级映射）';
COMMENT ON COLUMN wear_resistant_gold_paper_mapping.hot_stamping_type IS '烫金类型（固定为"燙金後擊凸"）';
COMMENT ON COLUMN wear_resistant_gold_paper_mapping.gold_paper_type IS '耐磨金纸类型';
COMMENT ON COLUMN wear_resistant_gold_paper_mapping.compatibility_status IS '兼容性状态：V-适用, X-不适用, NA-不确定, ▷-需要打底处理';
COMMENT ON COLUMN wear_resistant_gold_paper_mapping.remarks IS '备注';
COMMENT ON COLUMN wear_resistant_gold_paper_mapping.created_at IS '创建时间';
COMMENT ON COLUMN wear_resistant_gold_paper_mapping.updated_at IS '更新时间';

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_wrgpm_product_name ON wear_resistant_gold_paper_mapping(product_name);
CREATE INDEX IF NOT EXISTS idx_wrgpm_product_model_number ON wear_resistant_gold_paper_mapping(product_model_number);
CREATE INDEX IF NOT EXISTS idx_wrgpm_gold_paper_type ON wear_resistant_gold_paper_mapping(gold_paper_type);
CREATE INDEX IF NOT EXISTS idx_wrgpm_hot_stamping_type ON wear_resistant_gold_paper_mapping(hot_stamping_type);
CREATE INDEX IF NOT EXISTS idx_wrgpm_compatibility_status ON wear_resistant_gold_paper_mapping(compatibility_status);


