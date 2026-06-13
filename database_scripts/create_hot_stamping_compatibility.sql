-- 创建烫金工艺兼容性表
CREATE TABLE hot_stamping_compatibility (
    id SERIAL PRIMARY KEY,
    paper_performance VARCHAR(50) NOT NULL,
    product_type VARCHAR(50) NOT NULL,
    pattern_characteristic TEXT NOT NULL,
    hot_stamping_type VARCHAR(100) NOT NULL,
    compatibility CHAR(1) NOT NULL,
    pattern_type VARCHAR(20) NOT NULL,
    line_thickness VARCHAR(50),
    solid_area_size VARCHAR(50),
    reference_code VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 添加字段注释
COMMENT ON TABLE hot_stamping_compatibility IS '烫金工艺兼容性表';
COMMENT ON COLUMN hot_stamping_compatibility.paper_performance IS '烫金纸性能类型';
COMMENT ON COLUMN hot_stamping_compatibility.product_type IS '主要应用产品类型';
COMMENT ON COLUMN hot_stamping_compatibility.pattern_characteristic IS '烫金图案特征描述';
COMMENT ON COLUMN hot_stamping_compatibility.hot_stamping_type IS '烫金类型';
COMMENT ON COLUMN hot_stamping_compatibility.compatibility IS '兼容性标识 V:兼容 X:不兼容';
COMMENT ON COLUMN hot_stamping_compatibility.pattern_type IS '图案类型分类';
COMMENT ON COLUMN hot_stamping_compatibility.line_thickness IS '线条粗细范围';
COMMENT ON COLUMN hot_stamping_compatibility.solid_area_size IS '实地尺寸范围';
COMMENT ON COLUMN hot_stamping_compatibility.reference_code IS '内部参考编号';

-- 创建索引
CREATE INDEX idx_compatibility_paper_performance ON hot_stamping_compatibility(paper_performance);
CREATE INDEX idx_compatibility_product_type ON hot_stamping_compatibility(product_type);
CREATE INDEX idx_compatibility_pattern_type ON hot_stamping_compatibility(pattern_type);
CREATE INDEX idx_compatibility_hot_stamping_type ON hot_stamping_compatibility(hot_stamping_type);
CREATE INDEX idx_compatibility_compatibility ON hot_stamping_compatibility(compatibility);

-- 插入兼容性数据
INSERT INTO hot_stamping_compatibility (paper_performance, product_type, pattern_characteristic, hot_stamping_type, compatibility, pattern_type, line_thickness, solid_area_size, reference_code) VALUES

-- 普通金紙 - 精平裝/咭書
('普通金紙', '精平裝/咭書', '淨線條/字母≤0.5mm', '平面烫金-於中間位', 'V', 'LINE', '≤0.5mm', NULL, NULL),
('普通金紙', '精平裝/咭書', '淨線條/字母≤0.5mm', '平面烫金-到邊位', 'V', 'LINE', '≤0.5mm', NULL, NULL),
('普通金紙', '精平裝/咭書', '淨線條/字母≤0.5mm', '立體燙金(含燙金+擊凸，且重疊)', 'X', 'LINE', '≤0.5mm', NULL, NULL),
('普通金紙', '精平裝/咭書', '淨線條/字母≤0.5mm', '磨砂烫金', 'X', 'LINE', '≤0.5mm', NULL, NULL),
('普通金紙', '精平裝/咭書', '淨線條/字母≤0.5mm', '折光烫金', 'X', 'LINE', '≤0.5mm', NULL, NULL),

('普通金紙', '精平裝/咭書', '淨線條/字母0.5mm<X≤5mm', '平面烫金-於中間位', 'V', 'LINE', '0.5mm<X≤5mm', NULL, NULL),
('普通金紙', '精平裝/咭書', '淨線條/字母0.5mm<X≤5mm', '平面烫金-到邊位', 'V', 'LINE', '0.5mm<X≤5mm', NULL, NULL),
('普通金紙', '精平裝/咭書', '淨線條/字母0.5mm<X≤5mm', '立體燙金(含燙金+擊凸，且重疊)', 'X', 'LINE', '0.5mm<X≤5mm', NULL, NULL),
('普通金紙', '精平裝/咭書', '淨線條/字母0.5mm<X≤5mm', '磨砂烫金', 'X', 'LINE', '0.5mm<X≤5mm', NULL, NULL),
('普通金紙', '精平裝/咭書', '淨線條/字母0.5mm<X≤5mm', '折光烫金', 'X', 'LINE', '0.5mm<X≤5mm', NULL, NULL),

('普通金紙', '精平裝/咭書', '淨線條/字母5mm<X≤10mm', '平面烫金-於中間位', 'V', 'LINE', '5mm<X≤10mm', NULL, NULL),
('普通金紙', '精平裝/咭書', '淨線條/字母5mm<X≤10mm', '平面烫金-到邊位', 'V', 'LINE', '5mm<X≤10mm', NULL, NULL),
('普通金紙', '精平裝/咭書', '淨線條/字母5mm<X≤10mm', '立體燙金(含燙金+擊凸，且重疊)', 'V', 'LINE', '5mm<X≤10mm', NULL, NULL),
('普通金紙', '精平裝/咭書', '淨線條/字母5mm<X≤10mm', '磨砂烫金', 'V', 'LINE', '5mm<X≤10mm', NULL, NULL),
('普通金紙', '精平裝/咭書', '淨線條/字母5mm<X≤10mm', '折光烫金', 'V', 'LINE', '5mm<X≤10mm', NULL, NULL),

-- 普通金紙 - 賀咭/紙袋
('普通金紙', '賀咭/紙袋', '淨線條/字母≤0.5mm', '平面烫金-於中間位', 'V', 'LINE', '≤0.5mm', NULL, NULL),
('普通金紙', '賀咭/紙袋', '淨線條/字母≤0.5mm', '平面烫金-到邊位', 'V', 'LINE', '≤0.5mm', NULL, NULL),
('普通金紙', '賀咭/紙袋', '淨線條/字母≤0.5mm', '立體燙金(含燙金+擊凸，且重疊)', 'X', 'LINE', '≤0.5mm', NULL, NULL),
('普通金紙', '賀咭/紙袋', '淨線條/字母≤0.5mm', '磨砂烫金', 'X', 'LINE', '≤0.5mm', NULL, NULL),
('普通金紙', '賀咭/紙袋', '淨線條/字母≤0.5mm', '折光烫金', 'X', 'LINE', '≤0.5mm', NULL, NULL),

('普通金紙', '賀咭/紙袋', '淨線條/字母0.5mm<X≤5mm', '平面烫金-於中間位', 'V', 'LINE', '0.5mm<X≤5mm', NULL, NULL),
('普通金紙', '賀咭/紙袋', '淨線條/字母0.5mm<X≤5mm', '平面烫金-到邊位', 'V', 'LINE', '0.5mm<X≤5mm', NULL, NULL),
('普通金紙', '賀咭/紙袋', '淨線條/字母0.5mm<X≤5mm', '立體燙金(含燙金+擊凸，且重疊)', 'X', 'LINE', '0.5mm<X≤5mm', NULL, NULL),
('普通金紙', '賀咭/紙袋', '淨線條/字母0.5mm<X≤5mm', '磨砂烫金', 'X', 'LINE', '0.5mm<X≤5mm', NULL, NULL),
('普通金紙', '賀咭/紙袋', '淨線條/字母0.5mm<X≤5mm', '折光烫金', 'X', 'LINE', '0.5mm<X≤5mm', NULL, NULL),

('普通金紙', '賀咭/紙袋', '淨線條/字母5mm<X≤10mm', '平面烫金-於中間位', 'V', 'LINE', '5mm<X≤10mm', NULL, NULL),
('普通金紙', '賀咭/紙袋', '淨線條/字母5mm<X≤10mm', '平面烫金-到邊位', 'X', 'LINE', '5mm<X≤10mm', NULL, NULL),
('普通金紙', '賀咭/紙袋', '淨線條/字母5mm<X≤10mm', '立體燙金(含燙金+擊凸，且重疊)', 'X', 'LINE', '5mm<X≤10mm', NULL, NULL),
('普通金紙', '賀咭/紙袋', '淨線條/字母5mm<X≤10mm', '磨砂烫金', 'X', 'LINE', '5mm<X≤10mm', NULL, NULL),
('普通金紙', '賀咭/紙袋', '淨線條/字母5mm<X≤10mm', '折光烫金', 'X', 'LINE', '5mm<X≤10mm', NULL, NULL),


('普通金紙', '賀咭/紙袋', '淨實地10mm<X≤20mm', '平面烫金-於中間位', 'V', 'SOLID', NULL, '10mm<X≤20mm', NULL),
('普通金紙', '賀咭/紙袋', '淨實地10mm<X≤20mm', '平面烫金-到邊位', 'X', 'SOLID', NULL, '10mm<X≤20mm', NULL),
('普通金紙', '賀咭/紙袋', '淨實地10mm<X≤20mm', '立體燙金(含燙金+擊凸，且重疊)', 'X', 'SOLID', NULL, '10mm<X≤20mm', NULL),
('普通金紙', '賀咭/紙袋', '淨實地10mm<X≤20mm', '磨砂烫金', 'X', 'SOLID', NULL, '10mm<X≤20mm', NULL),
('普通金紙', '賀咭/紙袋', '淨實地10mm<X≤20mm', '折光烫金', 'X', 'SOLID', NULL, '10mm<X≤20mm', NULL),

('普通金紙', '賀咭/紙袋', '混合圖案≤0.5mm線條+實地', '平面烫金-於中間位', 'V', 'MIXED', '≤0.5mm', '實地', NULL),
('普通金紙', '賀咭/紙袋', '混合圖案≤0.5mm線條+實地', '平面烫金-到邊位', 'V', 'MIXED', '≤0.5mm', '實地', NULL),
('普通金紙', '賀咭/紙袋', '混合圖案≤0.5mm線條+實地', '立體燙金(含燙金+擊凸，且重疊)', 'V', 'MIXED', '≤0.5mm', '實地', NULL),
('普通金紙', '賀咭/紙袋', '混合圖案≤0.5mm線條+實地', '磨砂烫金', 'V', 'MIXED', '≤0.5mm', '實地', NULL),
('普通金紙', '賀咭/紙袋', '混合圖案≤0.5mm線條+實地', '折光烫金', 'V', 'MIXED', '≤0.5mm', '實地', NULL),

('普通金紙', '賀咭/紙袋', '混合圖案>0.5mm線條+≤20mm 實地', '平面烫金-於中間位', 'V', 'MIXED', '>0.5mm', '≤20mm', NULL),
('普通金紙', '賀咭/紙袋', '混合圖案>0.5mm線條+≤20mm 實地', '平面烫金-到邊位', 'X', 'MIXED', '>0.5mm', '≤20mm', NULL),
('普通金紙', '賀咭/紙袋', '混合圖案>0.5mm線條+≤20mm 實地', '立體燙金(含燙金+擊凸，且重疊)', 'X', 'MIXED', '>0.5mm', '≤20mm', NULL),
('普通金紙', '賀咭/紙袋', '混合圖案>0.5mm線條+≤20mm 實地', '磨砂烫金', 'X', 'MIXED', '>0.5mm', '≤20mm', NULL),
('普通金紙', '賀咭/紙袋', '混合圖案>0.5mm線條+≤20mm 實地', '折光烫金', 'X', 'MIXED', '>0.5mm', '≤20mm', NULL),

-- 普通耐磨 - 精平裝/咭書
('普通耐磨', '精平裝/咭書', '淨線條/字母0.5mm<X≤5mm', '平面烫金-於中間位', 'X', 'LINE', '0.5mm<X≤5mm', NULL, NULL),
('普通耐磨', '精平裝/咭書', '淨線條/字母0.5mm<X≤5mm', '平面烫金-到邊位', 'X', 'LINE', '0.5mm<X≤5mm', NULL, NULL),
('普通耐磨', '精平裝/咭書', '淨線條/字母0.5mm<X≤5mm', '立體燙金(含燙金+擊凸，且重疊)', 'V', 'LINE', '0.5mm<X≤5mm', NULL, NULL),
('普通耐磨', '精平裝/咭書', '淨線條/字母0.5mm<X≤5mm', '磨砂烫金', 'V', 'LINE', '0.5mm<X≤5mm', NULL, NULL),
('普通耐磨', '精平裝/咭書', '淨線條/字母0.5mm<X≤5mm', '折光烫金', 'V', 'LINE', '0.5mm<X≤5mm', NULL, NULL),

('普通耐磨', '精平裝/咭書', '淨線條/字母5mm<X≤10mm', '平面烫金-於中間位', 'V', 'LINE', '5mm<X≤10mm', NULL, NULL),
('普通耐磨', '精平裝/咭書', '淨線條/字母5mm<X≤10mm', '平面烫金-到邊位', 'V', 'LINE', '5mm<X≤10mm', NULL, NULL),
('普通耐磨', '精平裝/咭書', '淨線條/字母5mm<X≤10mm', '立體燙金(含燙金+擊凸，且重疊)', 'V', 'LINE', '5mm<X≤10mm', NULL, NULL),
('普通耐磨', '精平裝/咭書', '淨線條/字母5mm<X≤10mm', '磨砂烫金', 'V', 'LINE', '5mm<X≤10mm', NULL, NULL),
('普通耐磨', '精平裝/咭書', '淨線條/字母5mm<X≤10mm', '折光烫金', 'V', 'LINE', '5mm<X≤10mm', NULL, NULL),

('普通耐磨', '精平裝/咭書', '淨實地>10mm', '平面烫金-於中間位', 'V', 'SOLID', NULL, '>10mm', NULL),
('普通耐磨', '精平裝/咭書', '淨實地>10mm', '平面烫金-到邊位', 'V', 'SOLID', NULL, '>10mm', NULL),
('普通耐磨', '精平裝/咭書', '淨實地>10mm', '立體燙金(含燙金+擊凸，且重疊)', 'V', 'SOLID', NULL, '>10mm', NULL),
('普通耐磨', '精平裝/咭書', '淨實地>10mm', '磨砂烫金', 'V', 'SOLID', NULL, '>10mm', NULL),
('普通耐磨', '精平裝/咭書', '淨實地>10mm', '折光烫金', 'V', 'SOLID', NULL, '>10mm', NULL),

('普通耐磨', '精平裝/咭書', '混合圖案>0.5mm線條+實地', '平面烫金-於中間位', 'V', 'MIXED', '>0.5mm', '實地', NULL),
('普通耐磨', '精平裝/咭書', '混合圖案>0.5mm線條+實地', '平面烫金-到邊位', 'V', 'MIXED', '>0.5mm', '實地', NULL),
('普通耐磨', '精平裝/咭書', '混合圖案>0.5mm線條+實地', '立體燙金(含燙金+擊凸，且重疊)', 'X', 'MIXED', '>0.5mm', '實地', NULL),
('普通耐磨', '精平裝/咭書', '混合圖案>0.5mm線條+實地', '磨砂烫金', 'X', 'MIXED', '>0.5mm', '實地', NULL),
('普通耐磨', '精平裝/咭書', '混合圖案>0.5mm線條+實地', '折光烫金', 'X', 'MIXED', '>0.5mm', '實地', NULL),

-- 普通耐磨 - 賀咭/紙袋
('普通耐磨', '賀咭/紙袋', '淨線條/字母0.5mm<X≤5mm', '平面烫金-於中間位', 'X', 'LINE', '0.5mm<X≤5mm', NULL, NULL),
('普通耐磨', '賀咭/紙袋', '淨線條/字母0.5mm<X≤5mm', '平面烫金-到邊位', 'X', 'LINE', '0.5mm<X≤5mm', NULL, NULL),
('普通耐磨', '賀咭/紙袋', '淨線條/字母0.5mm<X≤5mm', '立體燙金(含燙金+擊凸，且重疊)', 'V', 'LINE', '0.5mm<X≤5mm', NULL, NULL),
('普通耐磨', '賀咭/紙袋', '淨線條/字母0.5mm<X≤5mm', '磨砂烫金', 'V', 'LINE', '0.5mm<X≤5mm', NULL, NULL),
('普通耐磨', '賀咭/紙袋', '淨線條/字母0.5mm<X≤5mm', '折光烫金', 'V', 'LINE', '0.5mm<X≤5mm', NULL, NULL),

('普通耐磨', '賀咭/紙袋', '淨線條/字母5mm<X≤10mm', '平面烫金-於中間位', 'X', 'LINE', '5mm<X≤10mm', NULL, NULL),
('普通耐磨', '賀咭/紙袋', '淨線條/字母5mm<X≤10mm', '平面烫金-到邊位', 'V', 'LINE', '5mm<X≤10mm', NULL, NULL),
('普通耐磨', '賀咭/紙袋', '淨線條/字母5mm<X≤10mm', '立體燙金(含燙金+擊凸，且重疊)', 'V', 'LINE', '5mm<X≤10mm', NULL, NULL),
('普通耐磨', '賀咭/紙袋', '淨線條/字母5mm<X≤10mm', '磨砂烫金', 'V', 'LINE', '5mm<X≤10mm', NULL, NULL),
('普通耐磨', '賀咭/紙袋', '淨線條/字母5mm<X≤10mm', '折光烫金', 'V', 'LINE', '5mm<X≤10mm', NULL, NULL),

('普通耐磨', '賀咭/紙袋', '淨實地10mm<X≤20mm', '平面烫金-於中間位', 'X', 'SOLID', NULL, '10mm<X≤20mm', NULL),
('普通耐磨', '賀咭/紙袋', '淨實地10mm<X≤20mm', '平面烫金-到邊位', 'V', 'SOLID', NULL, '10mm<X≤20mm', NULL),
('普通耐磨', '賀咭/紙袋', '淨實地10mm<X≤20mm', '立體燙金(含燙金+擊凸，且重疊)', 'V', 'SOLID', NULL, '10mm<X≤20mm', NULL),
('普通耐磨', '賀咭/紙袋', '淨實地10mm<X≤20mm', '磨砂烫金', 'V', 'SOLID', NULL, '10mm<X≤20mm', NULL),
('普通耐磨', '賀咭/紙袋', '淨實地10mm<X≤20mm', '折光烫金', 'V', 'SOLID', NULL, '10mm<X≤20mm', NULL),

('普通耐磨', '賀咭/紙袋', '淨實地>20mm', '平面烫金-於中間位', 'V', 'SOLID', NULL, '>20mm', NULL),
('普通耐磨', '賀咭/紙袋', '淨實地>20mm', '平面烫金-到邊位', 'V', 'SOLID', NULL, '>20mm', NULL),
('普通耐磨', '賀咭/紙袋', '淨實地>20mm', '立體燙金(含燙金+擊凸，且重疊)', 'V', 'SOLID', NULL, '>20mm', NULL),
('普通耐磨', '賀咭/紙袋', '淨實地>20mm', '磨砂烫金', 'V', 'SOLID', NULL, '>20mm', NULL),
('普通耐磨', '賀咭/紙袋', '淨實地>20mm', '折光烫金', 'V', 'SOLID', NULL, '>20mm', NULL),

('普通耐磨', '賀咭/紙袋', '混合圖案>0.5mm線條+≤20mm 實地', '平面烫金-於中間位', 'X', 'MIXED', '>0.5mm', '≤20mm', NULL),
('普通耐磨', '賀咭/紙袋', '混合圖案>0.5mm線條+≤20mm 實地', '平面烫金-到邊位', 'V', 'MIXED', '>0.5mm', '≤20mm', NULL),
('普通耐磨', '賀咭/紙袋', '混合圖案>0.5mm線條+≤20mm 實地', '立體燙金(含燙金+擊凸，且重疊)', 'V', 'MIXED', '>0.5mm', '≤20mm', NULL),
('普通耐磨', '賀咭/紙袋', '混合圖案>0.5mm線條+≤20mm 實地', '磨砂烫金', 'V', 'MIXED', '>0.5mm', '≤20mm', NULL),
('普通耐磨', '賀咭/紙袋', '混合圖案>0.5mm線條+≤20mm 實地', '折光烫金', 'V', 'MIXED', '>0.5mm', '≤20mm', NULL),

('普通耐磨', '賀咭/紙袋', '混合圖案>0.5mm線條+>20mm 實地', '平面烫金-於中間位', 'V', 'MIXED', '>0.5mm', '>20mm', NULL),
('普通耐磨', '賀咭/紙袋', '混合圖案>0.5mm線條+>20mm 實地', '平面烫金-到邊位', 'V', 'MIXED', '>0.5mm', '>20mm', NULL),
('普通耐磨', '賀咭/紙袋', '混合圖案>0.5mm線條+>20mm 實地', '立體燙金(含燙金+擊凸，且重疊)', 'V', 'MIXED', '>0.5mm', '>20mm', NULL),
('普通耐磨', '賀咭/紙袋', '混合圖案>0.5mm線條+>20mm 實地', '磨砂烫金', 'V', 'MIXED', '>0.5mm', '>20mm', NULL),
('普通耐磨', '賀咭/紙袋', '混合圖案>0.5mm線條+>20mm 實地', '折光烫金', 'V', 'MIXED', '>0.5mm', '>20mm', NULL),

-- 高耐磨 - 精平裝/咭書
('高耐磨', '精平裝/咭書', '混合圖案≤0.5mm線條+實地', '平面烫金-於中間位', 'V', 'MIXED', '≤0.5mm', '實地', NULL),
('高耐磨', '精平裝/咭書', '混合圖案≤0.5mm線條+實地', '平面烫金-到邊位', 'V', 'MIXED', '≤0.5mm', '實地', NULL),
('高耐磨', '精平裝/咭書', '混合圖案≤0.5mm線條+實地', '立體燙金(含燙金+擊凸，且重疊)', 'V', 'MIXED', '≤0.5mm', '實地', NULL),
('高耐磨', '精平裝/咭書', '混合圖案≤0.5mm線條+實地', '磨砂烫金', 'V', 'MIXED', '≤0.5mm', '實地', NULL),
('高耐磨', '精平裝/咭書', '混合圖案≤0.5mm線條+實地', '折光烫金', 'V', 'MIXED', '≤0.5mm', '實地', NULL),

('高耐磨', '精平裝/咭書', '混合圖案>0.5mm線條+實地', '平面烫金-於中間位', 'X', 'MIXED', '>0.5mm', '實地', NULL),
('高耐磨', '精平裝/咭書', '混合圖案>0.5mm線條+實地', '平面烫金-到邊位', 'X', 'MIXED', '>0.5mm', '實地', NULL),
('高耐磨', '精平裝/咭書', '混合圖案>0.5mm線條+實地', '立體燙金(含燙金+擊凸，且重疊)', 'V', 'MIXED', '>0.5mm', '實地', NULL),
('高耐磨', '精平裝/咭書', '混合圖案>0.5mm線條+實地', '磨砂烫金', 'V', 'MIXED', '>0.5mm', '實地', NULL),
('高耐磨', '精平裝/咭書', '混合圖案>0.5mm線條+實地', '折光烫金', 'V', 'MIXED', '>0.5mm', '實地', NULL),

-- 高耐磨 - 包裝 (不作區分)
('高耐磨', '包裝', '不作區分', '平面烫金-於中間位', 'V', 'ALL', NULL, NULL, NULL),
('高耐磨', '包裝', '不作區分', '平面烫金-到邊位', 'V', 'ALL', NULL, NULL, NULL),
('高耐磨', '包裝', '不作區分', '立體燙金(含燙金+擊凸，且重疊)', 'V', 'ALL', NULL, NULL, NULL),
('高耐磨', '包裝', '不作區分', '磨砂烫金', 'V', 'ALL', NULL, NULL, NULL),
('高耐磨', '包裝', '不作區分', '折光烫金', 'V', 'ALL', NULL, NULL, NULL);
