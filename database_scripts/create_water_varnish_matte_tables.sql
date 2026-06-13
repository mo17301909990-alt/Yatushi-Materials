-- ============================================
-- UV油_哑光水油 (Water Varnish Matte) 数据库建表脚本
-- 源文件: UV油墨/哑光水油标准书-20231117 (6).xlsx
-- ============================================

-- 产品表
CREATE TABLE IF NOT EXISTS water_varnish_matte_product (
    id                 SERIAL PRIMARY KEY,
    material_code      VARCHAR(100)   DEFAULT NULL,
    supplier_code      VARCHAR(100)   DEFAULT NULL,
    stock_code         VARCHAR(100)   DEFAULT NULL,
    material_name      VARCHAR(200)   DEFAULT NULL,
    usage              TEXT           DEFAULT NULL,
    material_type      VARCHAR(100)   DEFAULT NULL,
    color              VARCHAR(100)   DEFAULT NULL,
    responsible_person VARCHAR(100)   DEFAULT NULL,
    min_sheet_size     VARCHAR(100)   DEFAULT NULL,
    max_sheet_size     VARCHAR(100)   DEFAULT NULL,
    min_point          VARCHAR(100)   DEFAULT NULL,
    max_point          VARCHAR(100)   DEFAULT NULL,
    min_line           VARCHAR(100)   DEFAULT NULL,
    max_line           VARCHAR(100)   DEFAULT NULL,
    min_spacing        VARCHAR(100)   DEFAULT NULL,
    max_spacing        VARCHAR(100)   DEFAULT NULL,
    min_area           VARCHAR(100)   DEFAULT NULL,
    max_area           VARCHAR(100)   DEFAULT NULL,
    structure_application TEXT       DEFAULT NULL,
    applicable_interface TEXT        DEFAULT NULL,
    notes              TEXT           DEFAULT NULL,
    gloss              VARCHAR(100)   DEFAULT NULL,
    writing_function   TEXT           DEFAULT NULL,
    is_active          BOOLEAN        DEFAULT TRUE,
    created_at         TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP      DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_wvm_product_material_code ON water_varnish_matte_product(material_code);
CREATE INDEX IF NOT EXISTS idx_wvm_product_material_name ON water_varnish_matte_product(material_name);
CREATE INDEX IF NOT EXISTS idx_wvm_product_is_active ON water_varnish_matte_product(is_active);

COMMENT ON TABLE water_varnish_matte_product IS 'UV油_哑光水油产品表';

-- 兼容性表
CREATE TABLE IF NOT EXISTS water_varnish_matte_compatibility (
    id                    SERIAL PRIMARY KEY,
    product_id            INT           NOT NULL,
    post_processing_step  VARCHAR(200)  NOT NULL,
    compatibility_status  VARCHAR(50)   DEFAULT NULL,
    display_order         INT           DEFAULT 0,
    created_at            TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (product_id, post_processing_step)
);

CREATE INDEX IF NOT EXISTS idx_wvm_compat_product_id ON water_varnish_matte_compatibility(product_id);

COMMENT ON TABLE water_varnish_matte_compatibility IS 'UV油_哑光水油后加工兼容性表';
COMMENT ON COLUMN water_varnish_matte_compatibility.compatibility_status IS '兼容性状态: V=兼容, X=不兼容, ▷=有条件兼容';
