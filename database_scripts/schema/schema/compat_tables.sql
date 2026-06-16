CREATE TABLE IF NOT EXISTS {table} (
    id SERIAL PRIMARY KEY,
    product_id INTEGER NOT NULL REFERENCES {product_table}(id),
    post_processing_step VARCHAR(500) NOT NULL,
    compatibility_status VARCHAR(10) NOT NULL,
    remark TEXT DEFAULT NULL,
    display_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
