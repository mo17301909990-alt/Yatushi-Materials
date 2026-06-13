-- 创建兼容性状态说明配置表
-- 用于存储状态码对应的中文说明

-- 1. 创建状态说明配置表
CREATE TABLE IF NOT EXISTS compatibility_status_config (
    id SERIAL PRIMARY KEY,
    status_code CHAR(1) NOT NULL UNIQUE,  -- 状态码：V, X, NA, ▷
    status_name VARCHAR(50) NOT NULL,     -- 状态名称
    description TEXT,                      -- 详细说明
    is_active BOOLEAN DEFAULT TRUE,       -- 是否启用
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. 添加表注释
COMMENT ON TABLE compatibility_status_config IS '兼容性状态说明配置表';
COMMENT ON COLUMN compatibility_status_config.status_code IS '状态码';
COMMENT ON COLUMN compatibility_status_config.status_name IS '状态名称';
COMMENT ON COLUMN compatibility_status_config.description IS '详细说明';
COMMENT ON COLUMN compatibility_status_config.is_active IS '是否启用';

-- 3. 插入状态说明数据
INSERT INTO compatibility_status_config (status_code, status_name, description) VALUES
('V', '可以加工', '可以直接进行烫金加工，无需额外处理'),
('X', '不可以加工', '不能进行烫金加工，不适用'),
('NA', '不确定', '兼容性不确定，需要进一步测试'),
('▷', '需要打底处理', '需要先进行丝印打底处理，然后再烫金');

-- 4. 创建索引
CREATE INDEX IF NOT EXISTS idx_compatibility_status_config_code ON compatibility_status_config(status_code);
CREATE INDEX IF NOT EXISTS idx_compatibility_status_config_active ON compatibility_status_config(is_active);

-- 5. 验证数据
SELECT 
    id,
    status_code,
    status_name,
    description,
    is_active,
    created_at
FROM compatibility_status_config 
ORDER BY status_code;

-- 6. 提供查询示例：获取兼容性数据及其说明
SELECT 
    cpc.product_name,
    cpt.type_name,
    cpt.category,
    cpc.compatibility_status,
    csc.status_name,
    csc.description
FROM cloth_paper_compatibility cpc
JOIN cloth_paper_types cpt ON cpc.cloth_paper_type_id = cpt.id
JOIN compatibility_status_config csc ON cpc.compatibility_status = csc.status_code
WHERE cpc.product_name = 'L187'  -- 示例：查询L187系列的兼容性
ORDER BY cpt.category, cpt.sort_order;

