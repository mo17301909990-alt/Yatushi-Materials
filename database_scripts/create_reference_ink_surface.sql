-- ================================================
-- 印刷油墨面字典表 (ink_surface)
-- 工艺大类 / 子类别 / 详细名称 / 显示排序
-- ================================================

CREATE TABLE IF NOT EXISTS reference_ink_surface (
    id SERIAL PRIMARY KEY,
    category VARCHAR(50) NOT NULL,
    sub_category VARCHAR(50) DEFAULT NULL,
    detail_name VARCHAR(100) NOT NULL,
    display_order INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE reference_ink_surface IS '印刷油墨面字典';

INSERT INTO reference_ink_surface (category, sub_category, detail_name, display_order) VALUES
('印刷油墨', '', '柯式印刷', 1),
('印刷油墨', '', 'UV印刷', 2),
('印刷油墨', '', '水性墨', 3),
('印刷油墨', '', '油性墨', 4),
('印刷油墨', '', '熒光墨', 5),
('印刷油墨', '', '金銀墨', 6),
('印刷油墨', '', '珍珠墨', 7),
('印刷油墨', '', '夜光墨', 8),
('印刷油墨', '', '香味墨', 9),
('印刷油墨', '', '防偽墨', 10),
('印刷油墨', '', '可變數據印刷', 11),
('印刷油墨', '', '數碼印刷', 12),
('過油', '光油', '光水油', 13),
('過油', '光油', '光UV', 14),
('過油', '啞油', '啞水油', 15),
('過油', '啞油', '啞UV', 16),
('過油', '特殊', '吸塑油', 17);
