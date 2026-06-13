-- 部署状态说明功能
-- 包含创建配置表和更新现有数据的完整流程

-- ===========================================
-- 第一部分：创建状态说明配置表（推荐方案）
-- ===========================================

-- 1. 创建状态说明配置表
CREATE TABLE IF NOT EXISTS compatibility_status_config (
    id SERIAL PRIMARY KEY,
    status_code CHAR(1) NOT NULL UNIQUE,
    status_name VARCHAR(50) NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. 添加表注释
COMMENT ON TABLE compatibility_status_config IS '兼容性状态说明配置表';
COMMENT ON COLUMN compatibility_status_config.status_code IS '状态码：V, X, NA, ▷';
COMMENT ON COLUMN compatibility_status_config.status_name IS '状态名称';
COMMENT ON COLUMN compatibility_status_config.description IS '详细说明';
COMMENT ON COLUMN compatibility_status_config.is_active IS '是否启用';

-- 3. 插入状态说明数据
INSERT INTO compatibility_status_config (status_code, status_name, description) VALUES
('V', '可以加工', '可以直接进行烫金加工，无需额外处理'),
('X', '不可以加工', '不能进行烫金加工，不适用'),
('NA', '不确定', '兼容性不确定，需要进一步测试'),
('▷', '需要打底处理', '需要先进行丝印打底处理，然后再烫金')
ON CONFLICT (status_code) DO UPDATE SET
    status_name = EXCLUDED.status_name,
    description = EXCLUDED.description,
    updated_at = CURRENT_TIMESTAMP;

-- 4. 创建索引
CREATE INDEX IF NOT EXISTS idx_compatibility_status_config_code ON compatibility_status_config(status_code);
CREATE INDEX IF NOT EXISTS idx_compatibility_status_config_active ON compatibility_status_config(is_active);

-- ===========================================
-- 第二部分：验证数据完整性
-- ===========================================

-- 5. 验证状态配置数据
SELECT 
    '状态配置验证' AS "检查项",
    COUNT(*) AS "配置数量",
    STRING_AGG(status_code, ', ') AS "状态码列表"
FROM compatibility_status_config 
WHERE is_active = TRUE;

-- 6. 验证兼容性数据完整性
SELECT 
    '兼容性数据验证' AS "检查项",
    COUNT(*) AS "总记录数",
    COUNT(DISTINCT product_name) AS "系列数量",
    COUNT(DISTINCT cloth_paper_type_id) AS "布面纸类型数量"
FROM cloth_paper_compatibility;

-- 7. 检查是否有未配置的状态码
SELECT 
    '未配置状态检查' AS "检查项",
    cpc.compatibility_status AS "未配置状态码",
    COUNT(*) AS "记录数量"
FROM cloth_paper_compatibility cpc
LEFT JOIN compatibility_status_config csc ON cpc.compatibility_status = csc.status_code
WHERE csc.status_code IS NULL
GROUP BY cpc.compatibility_status;

-- ===========================================
-- 第三部分：示例查询
-- ===========================================

-- 8. 查询包含状态说明的兼容性数据（示例）
SELECT 
    cpc.product_name AS "物料型号系列",
    cpt.type_name AS "布面纸类型",
    cpt.category AS "分类",
    cpc.compatibility_status AS "状态码",
    csc.status_name AS "状态说明",
    csc.description AS "详细说明"
FROM cloth_paper_compatibility cpc
JOIN cloth_paper_types cpt ON cpc.cloth_paper_type_id = cpt.id
JOIN compatibility_status_config csc ON cpc.compatibility_status = csc.status_code
WHERE cpc.product_name IN ('L187', 'SY+', 'G1')  -- 示例查询
ORDER BY cpc.product_name, cpt.category, cpt.sort_order
LIMIT 10;

-- 9. 按系列统计兼容性分布
SELECT 
    cpc.product_name AS "物料型号系列",
    csc.status_name AS "状态说明",
    COUNT(*) AS "数量"
FROM cloth_paper_compatibility cpc
JOIN compatibility_status_config csc ON cpc.compatibility_status = csc.status_code
GROUP BY cpc.product_name, csc.status_name, csc.status_code
ORDER BY cpc.product_name, csc.status_code;

-- ===========================================
-- 第四部分：部署完成确认
-- ===========================================

-- 10. 部署完成确认
SELECT 
    '部署完成' AS "状态",
    '状态说明功能已成功部署' AS "说明",
    CURRENT_TIMESTAMP AS "部署时间";

