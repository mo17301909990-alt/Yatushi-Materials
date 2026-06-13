-- ============================================================
-- UV油_哑光UV油 (UV Oil Matte) 数据库表结构
-- ============================================================

-- 产品表
CREATE TABLE IF NOT EXISTS uv_oil_matte_product (
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

CREATE INDEX IF NOT EXISTS idx_uom_product_material_code ON uv_oil_matte_product(material_code);
CREATE INDEX IF NOT EXISTS idx_uom_product_material_name ON uv_oil_matte_product(material_name);
CREATE INDEX IF NOT EXISTS idx_uom_product_is_active ON uv_oil_matte_product(is_active);

COMMENT ON TABLE uv_oil_matte_product IS 'UV油_哑光UV油产品表';

-- 兼容性表
CREATE TABLE IF NOT EXISTS uv_oil_matte_compatibility (
    id                    SERIAL PRIMARY KEY,
    product_id            INT           NOT NULL,
    post_processing_step  VARCHAR(200)  DEFAULT NULL,
    compatibility_status  VARCHAR(50)   DEFAULT NULL,
    display_order         INT           DEFAULT 0,
    created_at            TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (product_id, post_processing_step)
);

CREATE INDEX IF NOT EXISTS idx_uom_compat_product_id ON uv_oil_matte_compatibility(product_id);

COMMENT ON TABLE uv_oil_matte_compatibility IS 'UV油_哑光UV油后加工兼容性表';
