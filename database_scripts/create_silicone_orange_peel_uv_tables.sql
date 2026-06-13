-- ============================================================
-- 硅胶桔纹UV (Orange Peel UV) 数据库表结构
-- 源文件: 硅胶/硅胶桔纹UV(Orange Peel UV)标准书-20230718.xlsx
-- ============================================================

-- 产品表
CREATE TABLE IF NOT EXISTS silicone_orange_peel_uv_product (
    id              SERIAL PRIMARY KEY,
    material_code   VARCHAR(100)    DEFAULT NULL,
    supplier_code   VARCHAR(100)    DEFAULT NULL,
    stock_code      VARCHAR(100)    DEFAULT NULL,
    material_name   VARCHAR(200)    DEFAULT NULL,
    usage_text      TEXT            DEFAULT NULL,
    material_type   VARCHAR(100)    DEFAULT NULL,
    thickness       VARCHAR(100)    DEFAULT NULL,
    size_info       VARCHAR(200)    DEFAULT NULL,
    color           VARCHAR(100)    DEFAULT NULL,
    shape           VARCHAR(100)    DEFAULT NULL,
    responsible_person VARCHAR(100) DEFAULT NULL,
    min_paper_size  VARCHAR(100)    DEFAULT NULL,
    max_paper_size  VARCHAR(100)    DEFAULT NULL,
    min_dot         VARCHAR(100)    DEFAULT NULL,
    max_dot         VARCHAR(100)    DEFAULT NULL,
    min_line        VARCHAR(100)    DEFAULT NULL,
    max_line        VARCHAR(100)    DEFAULT NULL,
    min_spacing     VARCHAR(100)    DEFAULT NULL,
    max_spacing     VARCHAR(100)    DEFAULT NULL,
    min_pattern_area VARCHAR(100)   DEFAULT NULL,
    max_pattern_area VARCHAR(100)   DEFAULT NULL,
    structure_application TEXT      DEFAULT NULL,
    single_side     VARCHAR(10)     DEFAULT NULL,
    double_side     VARCHAR(10)     DEFAULT NULL,
    cover_page      VARCHAR(10)     DEFAULT NULL,
    book_spine      VARCHAR(10)     DEFAULT NULL,
    pit_position    VARCHAR(10)     DEFAULT NULL,
    inner_page      VARCHAR(10)     DEFAULT NULL,
    interface_paper_base_thickness VARCHAR(100) DEFAULT NULL,
    interface_paper_suitable   TEXT DEFAULT NULL,
    interface_paper_unsuitable TEXT DEFAULT NULL,
    interface_ink_suitable     TEXT DEFAULT NULL,
    interface_ink_unsuitable   TEXT DEFAULT NULL,
    interface_coating_suitable TEXT DEFAULT NULL,
    interface_coating_unsuitable TEXT DEFAULT NULL,
    design_info     TEXT            DEFAULT NULL,
    applicable_interface TEXT       DEFAULT NULL,
    notes           TEXT            DEFAULT NULL,
    is_active       BOOLEAN         DEFAULT TRUE,
    created_at      TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_sopuv_product_material_code ON silicone_orange_peel_uv_product(material_code);
CREATE INDEX IF NOT EXISTS idx_sopuv_product_material_name ON silicone_orange_peel_uv_product(material_name);
CREATE INDEX IF NOT EXISTS idx_sopuv_product_is_active ON silicone_orange_peel_uv_product(is_active);

COMMENT ON TABLE silicone_orange_peel_uv_product IS '硅胶_桔纹UV产品表';

-- 兼容性表
CREATE TABLE IF NOT EXISTS silicone_orange_peel_uv_compatibility (
    id                    SERIAL PRIMARY KEY,
    product_id            INT           NOT NULL,
    post_processing_step  VARCHAR(200)  DEFAULT NULL,
    compatibility_status  VARCHAR(50)   DEFAULT NULL,
    display_order         INT           DEFAULT 0,
    created_at            TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (product_id, post_processing_step)
);

CREATE INDEX IF NOT EXISTS idx_sopuv_compat_product_id ON silicone_orange_peel_uv_compatibility(product_id);

COMMENT ON TABLE silicone_orange_peel_uv_compatibility IS '硅胶_桔纹UV后加工兼容性表';
