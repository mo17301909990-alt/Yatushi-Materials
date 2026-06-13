-- ============================================
-- 资源组选择系统 - 丝印资源组数据 (WC06)
-- ============================================

-- ============================================
-- WC06-SSPO16 自动丝印机
-- ============================================
INSERT INTO rg_resource_group (resource_group_code, name, work_center_code, work_center_name, family)
VALUES ('WC06-SSPO16', '自动丝印机', 'WC06', '丝印', NULL);

INSERT INTO rg_resource_group_task_map (resource_group_id, task_code, support_type)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC06-SSPO16'), 'SS', 'YES');

INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC06-SSPO16'), 'STRUCT_RULES', '硬性条件');

INSERT INTO rg_resource_rule_condition (rule_header_id, param_code, param_name, operator, value_min, value_max, rule_type, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC06-SSPO16') AND rule_type = 'STRUCT_RULES'),
 'sheet_count', '印张石数', '>=', 2000, NULL, 'STRUCT_RULE', 1),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC06-SSPO16') AND rule_type = 'STRUCT_RULES'),
 'gsm', '克重(g)', 'BETWEEN', 90, 450, 'STRUCT_RULE', 2),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC06-SSPO16') AND rule_type = 'STRUCT_RULES'),
 'width', '宽度(mm)', 'BETWEEN', 370, 1020, 'STRUCT_RULE', 3),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC06-SSPO16') AND rule_type = 'STRUCT_RULES'),
 'height', '高度(mm)', 'BETWEEN', 330, 710, 'STRUCT_RULE', 4);

INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC06-SSPO16'), 'TEXT_RULES', '培训表原文');

INSERT INTO rg_resource_rule_condition (rule_header_id, rule_type, is_blocking, value_text, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC06-SSPO16') AND rule_type = 'TEXT_RULES'),
 'TEXT_RULE', false, 
'1.自动丝印>=2,000石,单只丝印面积大於A4尺寸
2.壓紋(245g-400g); 磨砂UV、鑽石UV、過UV焗爐
3.過膠後印件120g以上,其餘印件不限克數(<90g或>450g咨詢加工部門科長)
4.雙面UV滿金粉只可以選一面上自動機(首選面版),另一面選手落丝印機
5.生产最大尺寸:1020mm*710mm
6.生产最小尺寸:370mm*330mm', 1);


-- ============================================
-- WC06-SSPO17 手落丝印機
-- ============================================
INSERT INTO rg_resource_group (resource_group_code, name, work_center_code, work_center_name, family)
VALUES ('WC06-SSPO17', '手落丝印機', 'WC06', '丝印', NULL);

INSERT INTO rg_resource_group_task_map (resource_group_id, task_code, support_type)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC06-SSPO17'), 'SS', 'YES');

INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC06-SSPO17'), 'STRUCT_RULES', '硬性条件');

INSERT INTO rg_resource_rule_condition (rule_header_id, param_code, param_name, operator, value_min, value_max, rule_type, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC06-SSPO17') AND rule_type = 'STRUCT_RULES'),
 'sheet_count', '印张石数', '<=', NULL, 2000, 'STRUCT_RULE', 1),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC06-SSPO17') AND rule_type = 'STRUCT_RULES'),
 'width', '宽度(mm)', 'BETWEEN', 50, 711, 'STRUCT_RULE', 2),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC06-SSPO17') AND rule_type = 'STRUCT_RULES'),
 'height', '高度(mm)', 'BETWEEN', 127, 1000, 'STRUCT_RULE', 3);

INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC06-SSPO17'), 'TEXT_RULES', '培训表原文');

INSERT INTO rg_resource_rule_condition (rule_header_id, rule_type, is_blocking, value_text, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC06-SSPO17') AND rule_type = 'TEXT_RULES'),
 'TEXT_RULE', false, 
'1.手落丝印<2,000石
2.壓紋(不限克數)
3.做兩轉或以上的丝印加工,第1轉用自動機,第2轉或以上用手落機生产
4.坑紙、包裝後、啤後(無損牙)、擊紋後、擊凸後
5.牛油紙、膠片、坑紙、布紋紙、布面紙
6.水性光油滿/罩金粉、發泡漿、發泡漿罩金粉、謀金打底油、嫩UV、珍珠油墨、過水見透明油墨、有色凸字油(假啫)
7.日本文具
8.生产最大尺寸:711mm*1000mm(薄紙(<=90g)~丝印油墨535mm*785mm)
9.生产最小尺寸:50.8mm*127mm
10.最大上機生産面積:600mm*900mm', 1);


-- ============================================
-- WC06-SSPO18 單只閃粉機
-- ============================================
INSERT INTO rg_resource_group (resource_group_code, name, work_center_code, work_center_name, family)
VALUES ('WC06-SSPO18', '單只閃粉機', 'WC06', '丝印', NULL);

INSERT INTO rg_resource_group_task_map (resource_group_id, task_code, support_type)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC06-SSPO18'), 'SS', 'YES');

INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC06-SSPO18'), 'STRUCT_RULES', '硬性条件');

INSERT INTO rg_resource_rule_condition (rule_header_id, param_code, param_name, operator, value_min, value_max, rule_type, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC06-SSPO18') AND rule_type = 'STRUCT_RULES'),
 'gsm', '克重(g)', 'BETWEEN', 150, 450, 'STRUCT_RULE', 1),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC06-SSPO18') AND rule_type = 'STRUCT_RULES'),
 'width', '宽度(mm)', 'BETWEEN', 165, 229, 'STRUCT_RULE', 2),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC06-SSPO18') AND rule_type = 'STRUCT_RULES'),
 'height', '高度(mm)', 'BETWEEN', 254, 454, 'STRUCT_RULE', 3);

INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC06-SSPO18'), 'TEXT_RULES', '培训表原文');

INSERT INTO rg_resource_rule_condition (rule_header_id, rule_type, is_blocking, value_text, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC06-SSPO18') AND rule_type = 'TEXT_RULES'),
 'TEXT_RULE', false, 
'1.150g<=印張克數<=450g
2.印刷熒光色不可上單只閃粉機,以防磨花
3.生产最大尺寸:229mm*454mm
4.生产最小尺寸:165.1mm*254mm
5.最大上機生産面積:229mm*419mm', 1),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC06-SSPO18') AND rule_type = 'TEXT_RULES'),
 'TEXT_RULE', true, 
'印刷熒光色不可上單只閃粉機,以防磨花', 2);


-- ============================================
-- WC06-SSPO19 丝印連自啤機
-- ============================================
INSERT INTO rg_resource_group (resource_group_code, name, work_center_code, work_center_name, family)
VALUES ('WC06-SSPO19', '丝印連自啤機', 'WC06', '丝印', NULL);

INSERT INTO rg_resource_group_task_map (resource_group_id, task_code, support_type)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC06-SSPO19'), 'SS', 'YES');

INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC06-SSPO19'), 'STRUCT_RULES', '硬性条件');

INSERT INTO rg_resource_rule_condition (rule_header_id, param_code, param_name, operator, value_min, value_max, rule_type, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC06-SSPO19') AND rule_type = 'STRUCT_RULES'),
 'sheet_count', '印张石数', '>=', 5000, NULL, 'STRUCT_RULE', 1),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC06-SSPO19') AND rule_type = 'STRUCT_RULES'),
 'gsm', '克重(g)', 'BETWEEN', 180, 450, 'STRUCT_RULE', 2),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC06-SSPO19') AND rule_type = 'STRUCT_RULES'),
 'width', '宽度(mm)', 'BETWEEN', 400, 1050, 'STRUCT_RULE', 3),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC06-SSPO19') AND rule_type = 'STRUCT_RULES'),
 'height', '高度(mm)', 'BETWEEN', 360, 750, 'STRUCT_RULE', 4);

INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC06-SSPO19'), 'TEXT_RULES', '培训表原文');

INSERT INTO rg_resource_rule_condition (rule_header_id, rule_type, is_blocking, value_text, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC06-SSPO19') AND rule_type = 'TEXT_RULES'),
 'TEXT_RULE', false, 
'1.印張石數>20000(低峰期1-3月、9-12月上機數量條件下降到5000石以上)
2.180g<=印張克數<=450g
3.可上機類型:
  A:丝印為面加工,啤機啤面(UV滿、UV清)
  B:UV滿閃粉需220g以上的印張
  C:貼啫排版面積不超過所排印張面積的一半(獨立排貼啫不建議排)
4.生产最大尺寸:1050*750mm
5.生产最小尺寸:400*360mm', 1),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC06-SSPO19') AND rule_type = 'TEXT_RULES'),
 'TEXT_RULE', true, 
'暂不上機類型:
A:合業啫、日文版啫
B:印張加工為多轉閃粉', 2);

