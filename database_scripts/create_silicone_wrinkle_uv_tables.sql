-- ============================================================
-- 硅胶皱纹UV (Wrinkle UV) 数据库表结构
-- 源文件: 硅胶/硅胶皱纹UV(Wrinkle Frost)标准书-20210811.xlsx
-- ============================================================

-- 产品表
CREATE TABLE IF NOT EXISTS silicone_wrinkle_uv_product (
    id              SERIAL PRIMARY KEY,
    material_code   VARCHAR(100)    DEFAULT NULL,
    supplier_code   VARCHAR(100)    DEFAULT NULL,
    stock_code      VARCHAR(100)    DEFAULT NULL,
    material_name   VARCHAR(200)    DEFAULT NULL,
    usage           TEXT            DEFAULT NULL,
    category        VARCHAR(100)    DEFAULT NULL,
    color           VARCHAR(100)    DEFAULT NULL,
    responsible_person VARCHAR(100) DEFAULT NULL,
    min_sheet_size  VARCHAR(100)    DEFAULT NULL,
    max_sheet_size  VARCHAR(100)    DEFAULT NULL,
    min_spacing     VARCHAR(100)    DEFAULT NULL,
    design_info     TEXT            DEFAULT NULL,
    applicable_interface TEXT       DEFAULT NULL,
    notes           TEXT            DEFAULT NULL,
    is_active       BOOLEAN         DEFAULT TRUE,
    created_at      TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_swuv_product_material_code ON silicone_wrinkle_uv_product(material_code);
CREATE INDEX IF NOT EXISTS idx_swuv_product_material_name ON silicone_wrinkle_uv_product(material_name);
CREATE INDEX IF NOT EXISTS idx_swuv_product_is_active ON silicone_wrinkle_uv_product(is_active);

COMMENT ON TABLE silicone_wrinkle_uv_product IS '硅胶_皱纹UV产品表';

-- 兼容性表
CREATE TABLE IF NOT EXISTS silicone_wrinkle_uv_compatibility (
    id                    SERIAL PRIMARY KEY,
    product_id            INT           NOT NULL,
    post_processing_step  VARCHAR(200)  DEFAULT NULL,
    compatibility_status  VARCHAR(50)   DEFAULT NULL,
    display_order         INT           DEFAULT 0,
    created_at            TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (product_id, post_processing_step)
);

CREATE INDEX IF NOT EXISTS idx_swuv_compat_product_id ON silicone_wrinkle_uv_compatibility(product_id);

COMMENT ON TABLE silicone_wrinkle_uv_compatibility IS '硅胶_皱纹UV后加工兼容性表';
COMMENT ON COLUMN silicone_wrinkle_uv_compatibility.compatibility_status IS '兼容性状态(V/X/▷)';
