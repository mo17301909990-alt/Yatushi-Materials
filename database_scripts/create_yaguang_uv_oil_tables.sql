-- ============================================
-- 哑光UV油 (Yaguang UV Oil) 数据库建表脚本
-- 源文件: 哑光UV油标准书-20250730 (10)(1).xlsx
-- ============================================

-- 产品表
CREATE TABLE IF NOT EXISTS yaguang_uv_oil_product (
    id              SERIAL PRIMARY KEY,
    material_code   VARCHAR(100)    DEFAULT NULL,
    supplier_code   VARCHAR(100)    DEFAULT NULL,
    stock_code      VARCHAR(100)    DEFAULT NULL,
    material_name   VARCHAR(200)    DEFAULT NULL,
    usage           TEXT            DEFAULT NULL,
    category        VARCHAR(100)    DEFAULT NULL,
    thickness       VARCHAR(100)    DEFAULT NULL,
    size            VARCHAR(200)    DEFAULT NULL,
    color           VARCHAR(100)    DEFAULT NULL,
    shape           VARCHAR(100)    DEFAULT NULL,
    responsible_person VARCHAR(100) DEFAULT NULL,
    min_sheet_size  VARCHAR(100)    DEFAULT NULL,
    max_sheet_size  VARCHAR(100)    DEFAULT NULL,
    min_dot         VARCHAR(100)    DEFAULT NULL,
    max_dot         VARCHAR(100)    DEFAULT NULL,
    min_line        VARCHAR(100)    DEFAULT NULL,
    max_line        VARCHAR(100)    DEFAULT NULL,
    min_spacing     VARCHAR(100)    DEFAULT NULL,
    max_spacing     VARCHAR(100)    DEFAULT NULL,
    min_pattern_area VARCHAR(100)   DEFAULT NULL,
    max_pattern_area VARCHAR(100)   DEFAULT NULL,
    applicable_product TEXT         DEFAULT NULL,
    structure_single_side VARCHAR(10)  DEFAULT NULL,
    structure_double_side VARCHAR(10)  DEFAULT NULL,
    structure_cover     VARCHAR(10)    DEFAULT NULL,
    structure_spine     VARCHAR(10)    DEFAULT NULL,
    structure_deboss    VARCHAR(10)    DEFAULT NULL,
    structure_inner_page VARCHAR(10)   DEFAULT NULL,
    substrate_thickness VARCHAR(100)   DEFAULT NULL,
    paper_surface_applicable   TEXT    DEFAULT NULL,
    paper_surface_inapplicable TEXT    DEFAULT NULL,
    ink_surface_applicable     TEXT    DEFAULT NULL,
    ink_surface_inapplicable   TEXT    DEFAULT NULL,
    coating_surface_applicable TEXT    DEFAULT NULL,
    coating_surface_inapplicable TEXT DEFAULT NULL,
    writing_standard_pen VARCHAR(10)   DEFAULT NULL,
    writing_customer_pen VARCHAR(10)   DEFAULT NULL,
    notes           TEXT            DEFAULT NULL,
    is_active       BOOLEAN         DEFAULT TRUE,
    created_at      TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_yguv_product_material_code ON yaguang_uv_oil_product(material_code);
CREATE INDEX IF NOT EXISTS idx_yguv_product_material_name ON yaguang_uv_oil_product(material_name);
CREATE INDEX IF NOT EXISTS idx_yguv_product_is_active ON yaguang_uv_oil_product(is_active);

COMMENT ON TABLE yaguang_uv_oil_product IS '哑光UV油产品表';

-- 兼容性表（含工序大类和步骤名称）
CREATE TABLE IF NOT EXISTS yaguang_uv_oil_compatibility (
    id                    SERIAL PRIMARY KEY,
    product_id            INT           NOT NULL,
    process_category      VARCHAR(100)  DEFAULT NULL,
    step_name             VARCHAR(200)  DEFAULT NULL,
    compatibility_status  VARCHAR(50)   DEFAULT NULL,
    display_order         INT           DEFAULT 0,
    created_at            TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (product_id, step_name)
);

CREATE INDEX IF NOT EXISTS idx_yguv_compat_product_id ON yaguang_uv_oil_compatibility(product_id);

COMMENT ON TABLE yaguang_uv_oil_compatibility IS '哑光UV油后加工兼容性表';
COMMENT ON COLUMN yaguang_uv_oil_compatibility.process_category IS '工序大类: 印刷/烫金/过胶/丝印/植毛/啤/手工/其他';
COMMENT ON COLUMN yaguang_uv_oil_compatibility.step_name IS '步骤名称';
