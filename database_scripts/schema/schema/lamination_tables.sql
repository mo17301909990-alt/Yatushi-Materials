CREATE TABLE IF NOT EXISTS lamination_material_products (
    id SERIAL PRIMARY KEY,
    material_code VARCHAR(100) NOT NULL,
    stock_code VARCHAR(200) DEFAULT NULL,
    material_name VARCHAR(500) NOT NULL,
    usage_text TEXT DEFAULT NULL,
    material_type VARCHAR(200) DEFAULT NULL,
    thickness_film VARCHAR(100) DEFAULT NULL,
    thickness_glue VARCHAR(100) DEFAULT NULL,
    size_info VARCHAR(200) DEFAULT NULL,
    color VARCHAR(200) DEFAULT NULL,
    shape VARCHAR(200) DEFAULT NULL,
    responsible_person VARCHAR(200) DEFAULT NULL,
    category VARCHAR(200) DEFAULT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS lamination_material_compatibility (
    id SERIAL PRIMARY KEY,
    product_id INTEGER NOT NULL REFERENCES lamination_material_products(id),
    post_processing_step VARCHAR(500) NOT NULL,
    compatibility_status VARCHAR(10) NOT NULL,
    remark TEXT DEFAULT NULL,
    display_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
