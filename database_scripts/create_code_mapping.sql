-- 物料编码映射表 — 连接 P0 知识库 ↔ 生产系统
-- Phase 0.3: 物料编码对齐
CREATE TABLE IF NOT EXISTS code_mapping (
    id SERIAL PRIMARY KEY,

    -- P0 知识库侧
    p0_table_name VARCHAR(200) NOT NULL,        -- 来源模块表名，如 silicone_glow_ink_product
    p0_row_id INTEGER NOT NULL,                  -- 来源行 ID
    p0_material_code VARCHAR(200),               -- P0 物料编码
    p0_material_name VARCHAR(500) NOT NULL,      -- P0 物料名称

    -- 生产系统侧
    target_type VARCHAR(50) NOT NULL,             -- 目标类型: material_catalog / paper_performance
    target_id INTEGER,                            -- material_catalog.id 或 NULL
    target_code VARCHAR(200),                     -- 目标编码
    target_name VARCHAR(500),                     -- 目标名称

    -- 映射质量
    match_type VARCHAR(20) NOT NULL DEFAULT 'auto',  -- auto / manual / verified
    confidence NUMERIC(3,2) DEFAULT 0,               -- 0~1 置信度
    notes TEXT,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- 同一 P0 记录不应重复映射到同一个目标
    UNIQUE(p0_table_name, p0_row_id, target_type)
);

CREATE INDEX IF NOT EXISTS idx_code_mapping_p0 ON code_mapping(p0_table_name, p0_row_id);
CREATE INDEX IF NOT EXISTS idx_code_mapping_target ON code_mapping(target_type, target_id);
CREATE INDEX IF NOT EXISTS idx_code_mapping_match ON code_mapping(match_type);
