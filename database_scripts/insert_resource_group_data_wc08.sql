-- ============================================
-- 资源组选择系统 - 啤机资源组数据 (WC08)
-- ============================================

-- ============================================
-- WC08-DCT029 全自动啤机
-- ============================================
INSERT INTO rg_resource_group (resource_group_code, name, work_center_code, work_center_name, family)
VALUES ('WC08-DCT029', '全自动啤机', 'WC08', '啤机', NULL);

-- 任务映射
INSERT INTO rg_resource_group_task_map (resource_group_id, task_code, support_type)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT029'), 'PM', 'YES');

-- 规则头 - 结构化规则
INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT029'), 'STRUCT_RULES', '硬性条件');

-- 结构化规则条件
INSERT INTO rg_resource_rule_condition (rule_header_id, param_code, param_name, operator, value_min, value_max, rule_type, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT029') AND rule_type = 'STRUCT_RULES'),
 'sheet_count', '印张石数', '>=', 5000, NULL, 'STRUCT_RULE', 1),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT029') AND rule_type = 'STRUCT_RULES'),
 'thickness', '厚度(mm)', '<=', NULL, 0.75, 'STRUCT_RULE', 2),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT029') AND rule_type = 'STRUCT_RULES'),
 'gsm', '克重(g)', 'BETWEEN', 140, 500, 'STRUCT_RULE', 3),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT029') AND rule_type = 'STRUCT_RULES'),
 'width', '宽度(mm)', 'BETWEEN', 400, 1060, 'STRUCT_RULE', 4),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT029') AND rule_type = 'STRUCT_RULES'),
 'height', '高度(mm)', 'BETWEEN', 350, 750, 'STRUCT_RULE', 5);

-- 规则头 - 文本规则
INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT029'), 'TEXT_RULES', '培训表原文');

-- 文本规则 - 可上机
INSERT INTO rg_resource_rule_condition (rule_header_id, rule_type, is_blocking, value_text, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT029') AND rule_type = 'TEXT_RULES'),
 'TEXT_RULE', false, 
'1.印張石數:
  - 贴青事业部-印张>=10,000石
  - 精平装单部-印张>=5,000石
  - 盒子/月卡主-印张>=5,000石
2.用纸140-500g(除纸皮外)
3.有清外围的印件
4.啤切厚度纸度<=0.75mm
5.清廢針間距>=25mm
6.生产尺寸400*350mm<=生产尺寸<=1060*750mm', 1);

-- 文本规则 - 暂不上机
INSERT INTO rg_resource_rule_condition (rule_header_id, rule_type, is_blocking, value_text, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT029') AND rule_type = 'TEXT_RULES'),
 'TEXT_RULE', true, 
'1.窝目贴青
2.立體吊牌(含:含有貼頁/散件的印張)
3.扣的产品
4.局页、散件
5.特殊加工后高低不平的（例如：植毛、金粉、壓紋等）
6.倒扣的产品，如：内页、扇頁倒扣
7.啤轨道形状的产品
8.<=190g以下有啤线内文不能生产
9.内文展圆尺寸宽度小於150mm
10.排版只数>=12只(多过本当1只)
11.包纸油膠
12.小透膠包纸排版只数>2只', 2);


-- ============================================
-- WC08-DCT028 自动啤/手啤
-- ============================================
INSERT INTO rg_resource_group (resource_group_code, name, work_center_code, work_center_name, family)
VALUES ('WC08-DCT028', '自动啤/手啤', 'WC08', '啤机', NULL);

INSERT INTO rg_resource_group_task_map (resource_group_id, task_code, support_type)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT028'), 'PM', 'YES');

INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT028'), 'STRUCT_RULES', '硬性条件');

INSERT INTO rg_resource_rule_condition (rule_header_id, param_code, param_name, operator, value_min, value_max, rule_type, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT028') AND rule_type = 'STRUCT_RULES'),
 'sheet_count', '印张石数', '>=', 2000, NULL, 'STRUCT_RULE', 1),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT028') AND rule_type = 'STRUCT_RULES'),
 'thickness', '厚度(mm)', '<=', NULL, 1.0, 'STRUCT_RULE', 2),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT028') AND rule_type = 'STRUCT_RULES'),
 'width', '宽度(mm)', 'BETWEEN', 400, 1060, 'STRUCT_RULE', 3),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT028') AND rule_type = 'STRUCT_RULES'),
 'height', '高度(mm)', 'BETWEEN', 350, 750, 'STRUCT_RULE', 4);

INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT028'), 'TEXT_RULES', '培训表原文');

INSERT INTO rg_resource_rule_condition (rule_header_id, rule_type, is_blocking, value_text, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT028') AND rule_type = 'TEXT_RULES'),
 'TEXT_RULE', false, 
'1.印張石數>=2000石
2.纸张厚度<=1mm
3.丝印不帶金/鉄粉
4.普通擊凸
5.不需清外围，只清裡面的印張
6.清廢面積>5mm
7.單張
8.信封
9.生产尺寸400*350mm<=生产尺寸<=1060*750mm', 1),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT028') AND rule_type = 'TEXT_RULES'),
 'TEXT_RULE', true, 
'1.特殊加工后高低不平的（例如：植毛、金粉、壓紋等）
2.壓紋紙(公司内部壓紋纸)
3.非印刷用紙不選擇', 2);


-- ============================================
-- WC08-DCT035 自动啤板纸机
-- ============================================
INSERT INTO rg_resource_group (resource_group_code, name, work_center_code, work_center_name, family)
VALUES ('WC08-DCT035', '自动啤板纸机', 'WC08', '啤机', NULL);

INSERT INTO rg_resource_group_task_map (resource_group_id, task_code, support_type)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT035'), 'PM', 'CONDITIONAL');

INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT035'), 'STRUCT_RULES', '硬性条件');

INSERT INTO rg_resource_rule_condition (rule_header_id, param_code, param_name, operator, value_min, value_max, rule_type, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT035') AND rule_type = 'STRUCT_RULES'),
 'thickness', '厚度(mm)', '<=', NULL, 2.87, 'STRUCT_RULE', 1),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT035') AND rule_type = 'STRUCT_RULES'),
 'gsm', '克重(g)', '<=', NULL, 1700, 'STRUCT_RULE', 2),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT035') AND rule_type = 'STRUCT_RULES'),
 'width', '宽度(mm)', '>=', 558, NULL, 'STRUCT_RULE', 3);

INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT035'), 'TEXT_RULES', '培训表原文');

INSERT INTO rg_resource_rule_condition (rule_header_id, rule_type, is_blocking, value_text, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT035') AND rule_type = 'TEXT_RULES'),
 'TEXT_RULE', false, 
'1.厚度<=2.87mm(1700gsm)
2.盒類裱紙（為青紙且非印刷時）:
  - 板紙對開或以上用法且出紙數量>=2令并且板紙原開
  - 出紙數量<10令
  - 當板紙尺寸(非入機邊)<558MM
注：
1.灰纸<350GSM最好纹路 避免纸张易膨胀 如不能避开此板纸 本司需发纸纸
2.三层对(word文件)非为非印刷且为薄纸与板纸对裱,必须使用滚筒裱纸,此情况不受石数限制', 1);


-- ============================================
-- WC08-DCT032 圆压圆模切机
-- ============================================
INSERT INTO rg_resource_group (resource_group_code, name, work_center_code, work_center_name, family)
VALUES ('WC08-DCT032', '圆压圆模切机', 'WC08', '啤机', '賀咭');

INSERT INTO rg_resource_group_task_map (resource_group_id, task_code, support_type)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT032'), 'PM', 'YES');

INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT032'), 'STRUCT_RULES', '硬性条件');

INSERT INTO rg_resource_rule_condition (rule_header_id, param_code, param_name, operator, value_min, value_max, rule_type, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT032') AND rule_type = 'STRUCT_RULES'),
 'thickness', '厚度(mm)', '<=', NULL, 1.5, 'STRUCT_RULE', 1),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT032') AND rule_type = 'STRUCT_RULES'),
 'width', '宽度(mm)', 'BETWEEN', 200, 550, 'STRUCT_RULE', 2),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT032') AND rule_type = 'STRUCT_RULES'),
 'height', '高度(mm)', 'BETWEEN', 275, 400, 'STRUCT_RULE', 3);

INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT032'), 'TEXT_RULES', '培训表原文');

INSERT INTO rg_resource_rule_condition (rule_header_id, rule_type, is_blocking, value_text, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT032') AND rule_type = 'TEXT_RULES'),
 'TEXT_RULE', false, 
'1.纸张厚度<=1.5mm
2.满底机
3.日本机
4.彩色仅限:
  - 单张贴
  - 植毛面积小(面积炸辅充)的单张贴
5.平裂、無后加工
6.模切最大厚度0.5mm
7.壓痕最大厚度0.4mm
8.所留纸邊位>=12mm
9.生产尺寸200*275mm<=生产尺寸<=550*400mm', 1),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT032') AND rule_type = 'TEXT_RULES'),
 'TEXT_RULE', true, 
'1.原自制模不能使用Horizon牌的0.8mm刀锌的刀具
2.发泡浆、金色凸字粉不适用此机台', 2);


-- ============================================
-- WC08-DCT033 自动打子啤
-- ============================================
INSERT INTO rg_resource_group (resource_group_code, name, work_center_code, work_center_name, family)
VALUES ('WC08-DCT033', '自动打子啤', 'WC08', '啤机', NULL);

INSERT INTO rg_resource_group_task_map (resource_group_id, task_code, support_type)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT033'), 'PM', 'YES');

INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT033'), 'STRUCT_RULES', '硬性条件');

INSERT INTO rg_resource_rule_condition (rule_header_id, param_code, param_name, operator, value_min, value_max, rule_type, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT033') AND rule_type = 'STRUCT_RULES'),
 'sheet_count', '印张石数', '>=', 2000, NULL, 'STRUCT_RULE', 1),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT033') AND rule_type = 'STRUCT_RULES'),
 'thickness', '厚度(mm)', '<=', NULL, 4.0, 'STRUCT_RULE', 2),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT033') AND rule_type = 'STRUCT_RULES'),
 'gsm', '克重(g)', '>=', 200, NULL, 'STRUCT_RULE', 3);

INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT033'), 'TEXT_RULES', '培训表原文');

INSERT INTO rg_resource_rule_condition (rule_header_id, rule_type, is_blocking, value_text, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT033') AND rule_type = 'TEXT_RULES'),
 'TEXT_RULE', false, 
'1.印張石數>=2000
2.200g<=用纸厚度<=4mm
3.可啤材料：膠片/皮製/布類/贴纸/牛油纸/光柵片/磁膠/海绵膠/纸皮(雙灰、E坑、三層瓦楞纸)', 1),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT033') AND rule_type = 'TEXT_RULES'),
 'TEXT_RULE', true, 
'1.有色仅限制不可以啤
2.膠片/皮製/布類/贴纸/牛油纸/光柵片/磁膠/海绵膠/纸皮(雙灰、E坑、三層瓦楞纸)不适用', 2);


-- ============================================
-- WC08-DCT030 手落啤机
-- ============================================
INSERT INTO rg_resource_group (resource_group_code, name, work_center_code, work_center_name, family)
VALUES ('WC08-DCT030', '手落啤机', 'WC08', '啤机', NULL);

INSERT INTO rg_resource_group_task_map (resource_group_id, task_code, support_type)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT030'), 'PM', 'YES');

INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT030'), 'STRUCT_RULES', '硬性条件');

INSERT INTO rg_resource_rule_condition (rule_header_id, param_code, param_name, operator, value_min, value_max, rule_type, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT030') AND rule_type = 'STRUCT_RULES'),
 'width', '宽度(mm)', '<=', NULL, 940, 'STRUCT_RULE', 1),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT030') AND rule_type = 'STRUCT_RULES'),
 'height', '高度(mm)', '<=', NULL, 690, 'STRUCT_RULE', 2);

INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT030'), 'TEXT_RULES', '培训表原文');

INSERT INTO rg_resource_rule_condition (rule_header_id, rule_type, is_blocking, value_text, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT030') AND rule_type = 'TEXT_RULES'),
 'TEXT_RULE', false, 
'1.上述机台不能啤的产品，都可在手落啤机生产
2.超出生产最大尺寸的印件
3.可啤材料：膠片/皮製/布類/贴纸/牛油纸/光柵片/磁膠/海绵膠/椰殼/玻璃珠板等
4.生产尺寸<940*690mm', 1);


-- ============================================
-- WC08-DCT014 手落大尺寸啤机
-- ============================================
INSERT INTO rg_resource_group (resource_group_code, name, work_center_code, work_center_name, family)
VALUES ('WC08-DCT014', '手落大尺寸啤机', 'WC08', '啤机', NULL);

INSERT INTO rg_resource_group_task_map (resource_group_id, task_code, support_type)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT014'), 'PM', 'YES');

INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT014'), 'TEXT_RULES', '培训表原文');

INSERT INTO rg_resource_rule_condition (rule_header_id, rule_type, is_blocking, value_text, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC08-DCT014') AND rule_type = 'TEXT_RULES'),
 'TEXT_RULE', false, 
'1.拼圆连续包装产品首选此机台
2.上述机台不成超出手落啤机尺寸不能啤的产品都在此机啤生产', 1);

