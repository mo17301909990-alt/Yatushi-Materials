-- ================================================
-- 产品家族字典表 (product_family)
-- 产品大类 / 产品系列 / 详细名称 / 显示排序
-- ================================================

CREATE TABLE IF NOT EXISTS reference_product_family (
    id SERIAL PRIMARY KEY,
    category VARCHAR(50) NOT NULL,
    sub_category VARCHAR(50) DEFAULT NULL,
    detail_name VARCHAR(100) NOT NULL,
    display_order INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE reference_product_family IS '产品家族字典';

INSERT INTO reference_product_family (category, sub_category, detail_name, display_order) VALUES
('書刊', '平裝書', '平裝書', 1),
('書刊', '精裝書', '精裝書', 2),
('書刊', '騎馬釘', '騎馬釘書刊', 3),
('書刊', '線裝', '線裝書', 4),
('書刊', '環裝', '環裝書', 5),
('書刊', '膠裝', '膠裝書', 6),
('書刊', '古線裝', '古線裝書', 7),
('包裝', '彩盒', '摺疊彩盒', 8),
('包裝', '彩盒', '天地盒', 9),
('包裝', '彩盒', '抽屜盒', 10),
('包裝', '彩盒', '書型盒', 11),
('包裝', '彩盒', '圓筒盒', 12),
('包裝', '彩盒', '異形盒', 13),
('包裝', '咭牌', '吊牌', 14),
('包裝', '咭牌', '價錢牌', 15),
('包裝', '咭牌', '展示牌', 16),
('包裝', '膠袋', '膠袋', 17),
('包裝', '紙袋', '紙袋', 18),
('宣傳品', '說明書', '說明書', 19),
('宣傳品', '目錄', '產品目錄', 20),
('宣傳品', '傳單', '傳單/單張', 21),
('宣傳品', '海報', '海報', 22),
('宣傳品', '賀卡', '賀卡', 23),
('宣傳品', '年曆', '年曆/檯曆', 24),
('標籤', '不乾膠', '不乾膠標籤', 25),
('標籤', '吊牌', '吊牌標籤', 26),
('標籤', '織標', '織嘜標籤', 27),
('文具', '筆記本', '筆記本', 28),
('文具', '文件夾', '文件夾', 29),
('文具', '信封', '信封', 30),
('文具', '信紙', '信紙', 31);
