-- ============================================================
-- 硅胶_皱纹UV 物料标准书 数据库表结构
-- 源文件: 硅胶/硅胶皱纹UV(Wrinkle Frost)标准书-20210811.xlsx
-- ============================================================

-- 产品表
CREATE TABLE IF NOT EXISTS silicone_wrinkle_uv_product (
    id              INT             AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    material_code   VARCHAR(100)    DEFAULT NULL COMMENT '物料编码',
    supplier_code   VARCHAR(100)    DEFAULT NULL COMMENT '供应商编码',
    stock_code      VARCHAR(100)    DEFAULT NULL COMMENT '库存编码',
    material_name   VARCHAR(200)    DEFAULT NULL COMMENT '物料名称',
    usage           TEXT            DEFAULT NULL COMMENT '物料用途',
    category        VARCHAR(100)    DEFAULT NULL COMMENT '材质类别',
    color           VARCHAR(100)    DEFAULT NULL COMMENT '颜色',
    responsible_person VARCHAR(100) DEFAULT NULL COMMENT '测试员',
    min_sheet_size  VARCHAR(100)    DEFAULT NULL COMMENT '最小用纸尺寸',
    max_sheet_size  VARCHAR(100)    DEFAULT NULL COMMENT '最大用纸尺寸',
    min_spacing     VARCHAR(100)    DEFAULT NULL COMMENT '最小间距',
    design_info     TEXT            DEFAULT NULL COMMENT '设计信息(点/线/面积/结构应用)',
    applicable_interface TEXT       DEFAULT NULL COMMENT '适用界面',
    notes           TEXT            DEFAULT NULL COMMENT '注意事项',
    is_active       BOOLEAN         DEFAULT TRUE COMMENT '是否启用',
    created_at      TIMESTAMP       DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      TIMESTAMP       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_material_code (material_code),
    INDEX idx_material_name (material_name),
    INDEX idx_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='硅胶_皱纹UV产品表';

-- 兼容性表
CREATE TABLE IF NOT EXISTS silicone_wrinkle_uv_compatibility (
    id                    INT           AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    product_id            INT           NOT NULL COMMENT '产品ID',
    post_processing_step  VARCHAR(200)  DEFAULT NULL COMMENT '后加工工序名称',
    compatibility_status  VARCHAR(50)   DEFAULT NULL COMMENT '兼容性状态(V/X/▷)',
    display_order         INT           DEFAULT 0 COMMENT '显示顺序',
    created_at            TIMESTAMP     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_product_step (product_id, post_processing_step),
    INDEX idx_product_id (product_id),
    CONSTRAINT fk_silicone_wrinkle_uv_compatibility_product
        FOREIGN KEY (product_id) REFERENCES silicone_wrinkle_uv_product(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='硅胶_皱纹UV兼容性表';
