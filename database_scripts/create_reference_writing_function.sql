-- ================================================
-- 表面书写功能字典表 (writing_function)
-- 笔类型 / 功能说明 / 显示排序
-- ================================================

CREATE TABLE IF NOT EXISTS reference_writing_function (
    id SERIAL PRIMARY KEY,
    category VARCHAR(50) NOT NULL,
    sub_category VARCHAR(50) DEFAULT NULL,
    detail_name VARCHAR(100) NOT NULL,
    display_order INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE reference_writing_function IS '表面书写功能字典';

INSERT INTO reference_writing_function (category, sub_category, detail_name, display_order) VALUES
('原配筆', '中性的', '原配中性筆', 1),
('原配筆', '油性的', '原配油性筆', 2),
('原配筆', '水性的', '原配水性筆', 3),
('原配筆', '鉛筆', '原配鉛筆', 4),
('客戶指定筆', '中性的', '客戶中性筆', 5),
('客戶指定筆', '油性的', '客戶油性筆', 6),
('客戶指定筆', '水性的', '客戶水性筆', 7),
('客戶指定筆', '鉛筆', '客戶鉛筆', 8),
('特殊要求', '', '可擦寫', 9),
('特殊要求', '', '耐刮擦', 10),
('特殊要求', '', '防水', 11),
('特殊要求', '', '可酒精擦拭', 12);
