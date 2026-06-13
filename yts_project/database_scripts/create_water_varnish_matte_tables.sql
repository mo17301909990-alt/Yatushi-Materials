-- ============================================
-- UV油_哑光水油 数据库建表脚本
-- 类别: water_varnish_matte
-- 源文件: UV油墨/哑光水油标准书-20231117 (6).xlsx
-- ============================================

-- 1. 产品主表
CREATE TABLE IF NOT EXISTS `water_varnish_matte_product` (
    `id`                 INT            NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `material_code`      VARCHAR(100)   DEFAULT NULL COMMENT '物料型號/編號',
    `supplier_code`      VARCHAR(100)   DEFAULT NULL COMMENT '採購部申請編號',
    `stock_code`         VARCHAR(100)   DEFAULT NULL COMMENT '物料編碼',
    `material_name`      VARCHAR(200)   DEFAULT NULL COMMENT '物料名稱',
    `usage`              TEXT           DEFAULT NULL COMMENT '物料用途',
    `material_type`      VARCHAR(100)   DEFAULT NULL COMMENT '材質',
    `color`              VARCHAR(100)   DEFAULT NULL COMMENT '顏色',
    `responsible_person` VARCHAR(100)   DEFAULT NULL COMMENT '測試員(中文名)',
    `min_sheet_size`     VARCHAR(100)   DEFAULT NULL COMMENT '用紙尺寸-最小(mm)',
    `max_sheet_size`     VARCHAR(100)   DEFAULT NULL COMMENT '用紙尺寸-最大(mm)',
    `min_point`          VARCHAR(100)   DEFAULT NULL COMMENT '點-最小(mm)',
    `max_point`          VARCHAR(100)   DEFAULT NULL COMMENT '點-最大(mm)',
    `min_line`           VARCHAR(100)   DEFAULT NULL COMMENT '線-最小(mm)',
    `max_line`           VARCHAR(100)   DEFAULT NULL COMMENT '線-最大(mm)',
    `min_spacing`        VARCHAR(100)   DEFAULT NULL COMMENT '間距-最小(mm)',
    `max_spacing`        VARCHAR(100)   DEFAULT NULL COMMENT '間距-最大(mm)',
    `min_area`           VARCHAR(100)   DEFAULT NULL COMMENT '單個圖案加工面積-最小(mm²)',
    `max_area`           VARCHAR(100)   DEFAULT NULL COMMENT '單個圖案加工面積-最大(mm²)',
    `structure_application` TEXT        DEFAULT NULL COMMENT '結構應用(適用產品)',
    `applicable_interface` TEXT         DEFAULT NULL COMMENT '適用界面資訊',
    `notes`              TEXT           DEFAULT NULL COMMENT '注意事項、限制的備注與說明',
    `gloss`              VARCHAR(100)   DEFAULT NULL COMMENT '光澤',
    `writing_function`   TEXT           DEFAULT NULL COMMENT '表面寫字功能說明',
    `is_active`          TINYINT(1)     DEFAULT 1 COMMENT '是否激活',
    `created_at`         TIMESTAMP      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`         TIMESTAMP      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='UV油_哑光水油产品主表';

-- 2. 兼容性表
CREATE TABLE IF NOT EXISTS `water_varnish_matte_compatibility` (
    `id`                    INT          NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `product_id`            INT          NOT NULL COMMENT '产品ID,关联water_varnish_matte_product.id',
    `post_processing_step`  VARCHAR(200) NOT NULL COMMENT '后加工工序步骤名称',
    `compatibility_status`  VARCHAR(50)  DEFAULT NULL COMMENT '兼容性状态: V=兼容, X=不兼容, ▷=有条件兼容',
    `display_order`         INT          DEFAULT 0 COMMENT '显示顺序',
    `created_at`            TIMESTAMP    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_product_step` (`product_id`, `post_processing_step`),
    CONSTRAINT `fk_wvm_compatibility_product` FOREIGN KEY (`product_id`) REFERENCES `water_varnish_matte_product`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='UV油_哑光水油后加工兼容性表';
