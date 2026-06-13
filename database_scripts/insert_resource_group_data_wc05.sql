-- ============================================
-- 资源组选择系统 - 烫金资源组数据 (WC05)
-- ============================================

-- ============================================
-- WC05-HOT023 平壓平燙金機
-- ============================================
INSERT INTO rg_resource_group (resource_group_code, name, work_center_code, work_center_name, family)
VALUES ('WC05-HOT023', '平壓平燙金機', 'WC05', '烫金', NULL);

INSERT INTO rg_resource_group_task_map (resource_group_id, task_code, support_type)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT023'), 'TK', 'YES');

INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT023'), 'STRUCT_RULES', '硬性条件');

INSERT INTO rg_resource_rule_condition (rule_header_id, param_code, param_name, operator, value_min, value_max, rule_type, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT023') AND rule_type = 'STRUCT_RULES'),
 'sheet_count', '印张石数', '>=', 2000, NULL, 'STRUCT_RULE', 1),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT023') AND rule_type = 'STRUCT_RULES'),
 'width', '宽度(mm)', 'BETWEEN', 450, 1060, 'STRUCT_RULE', 2),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT023') AND rule_type = 'STRUCT_RULES'),
 'height', '高度(mm)', 'BETWEEN', 360, 760, 'STRUCT_RULE', 3);

INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT023'), 'TEXT_RULES', '培训表原文');

INSERT INTO rg_resource_rule_condition (rule_header_id, rule_type, is_blocking, value_text, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT023') AND rule_type = 'TEXT_RULES'),
 'TEXT_RULE', false, 
'1.墨紙/等版燙金
2.布紋紙
3.雙凹凸(納印部面)
4.雙紋
5.油膜厚燙金版宽<=5mm且版稀~A5尺寸(非雙紋)
6.雙面印刷部門料長
7.先光油后燙金可以選用(非實地)
8.滾壓後燙金版寬<=5mm且版稀~10mm(非雙紋)
9.雙送紙實地(用影分金纸)
10.背大面积燙金且重複
11.先光油后燙金只选用100-150g
12.直接在有油墨的位置燙金，只燙金版宽<=5mm的平烫纸平烫
13.上机纸尺寸最大1060mm*760mm
14.上机纸尺寸最小450mm*360mm(除非平壓機台最小紙張400mm*350mm)
15.数量限制>=2000石以上', 1);


-- ============================================
-- WC05-HOT024 滾筒燙金機
-- ============================================
INSERT INTO rg_resource_group (resource_group_code, name, work_center_code, work_center_name, family)
VALUES ('WC05-HOT024', '滾筒燙金機', 'WC05', '烫金', NULL);

INSERT INTO rg_resource_group_task_map (resource_group_id, task_code, support_type)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT024'), 'TK', 'YES');

INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT024'), 'STRUCT_RULES', '硬性条件');

INSERT INTO rg_resource_rule_condition (rule_header_id, param_code, param_name, operator, value_min, value_max, rule_type, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT024') AND rule_type = 'STRUCT_RULES'),
 'gsm', '克重(g)', '<=', NULL, 157, 'STRUCT_RULE', 1),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT024') AND rule_type = 'STRUCT_RULES'),
 'width', '宽度(mm)', 'BETWEEN', 292, 640, 'STRUCT_RULE', 2),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT024') AND rule_type = 'STRUCT_RULES'),
 'height', '高度(mm)', 'BETWEEN', 406, 560, 'STRUCT_RULE', 3);

INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT024'), 'TEXT_RULES', '培训表原文');

INSERT INTO rg_resource_rule_condition (rule_header_id, rule_type, is_blocking, value_text, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT024') AND rule_type = 'TEXT_RULES'),
 'TEXT_RULE', false, 
'适合上滾筒機：
1.大实地/大线条/大文字均可白燙金(雪埕<=157g粘纸十滾筒，非雪紙不受限制)
2.事业部印刷(非實地用)
3.油膜厚燙金版宽<=5mm且且版稀10mm(非雙紋)
4.选择後燙金版寬>=5mm以上(非雙紋)
5.本规格用背實紋(用影分金纸)背大面积燙金且重複
6.印纸纸及金(非雙紋)背大面积燙金且重複
7.雙送紙實地
8.上机纸尺寸最大尺寸640*560mm(上等纸)
9.上机纸尺寸最小尺寸292.1*406.4mm(賀咭)', 1),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT024') AND rule_type = 'TEXT_RULES'),
 'TEXT_RULE', true, 
'不要上滾筒機：
1.墨紙/等版燙金
2.布紋紙
3.有金膜及麥穗的印件', 2);


-- ============================================
-- WC05-HOT025 平面燙金機
-- ============================================
INSERT INTO rg_resource_group (resource_group_code, name, work_center_code, work_center_name, family)
VALUES ('WC05-HOT025', '平面燙金機', 'WC05', '烫金', NULL);

INSERT INTO rg_resource_group_task_map (resource_group_id, task_code, support_type)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT025'), 'TK', 'YES');

INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT025'), 'STRUCT_RULES', '硬性条件');

INSERT INTO rg_resource_rule_condition (rule_header_id, param_code, param_name, operator, value_min, value_max, rule_type, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT025') AND rule_type = 'STRUCT_RULES'),
 'sheet_count', '印张石数', '<=', NULL, 2000, 'STRUCT_RULE', 1),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT025') AND rule_type = 'STRUCT_RULES'),
 'width', '宽度(mm)', '<=', NULL, 711, 'STRUCT_RULE', 2),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT025') AND rule_type = 'STRUCT_RULES'),
 'height', '高度(mm)', '<=', NULL, 1016, 'STRUCT_RULE', 3);

INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT025'), 'TEXT_RULES', '培训表原文');

INSERT INTO rg_resource_rule_condition (rule_header_id, rule_type, is_blocking, value_text, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT025') AND rule_type = 'TEXT_RULES'),
 'TEXT_RULE', false, 
'1.仅立叶燙金/仅位快速燙金
2.熱熔材質(如:對皮/布面底/皮革底/瓦布類)
3.日本文具產品/GHD客户
4.本机纸版首位/窄門立燙燙
5.超出单只发金规限时可以班/手术纸/軟組(雙紋)
6.生产最大尺寸711.2*1016mm
7.生产最小尺寸：無限制
8.最大上機生産面積683.8*965.2mm
9.数量石数：<2000石内工种', 1);


-- ============================================
-- WC05-HOT026 單只燙/刮金機
-- ============================================
INSERT INTO rg_resource_group (resource_group_code, name, work_center_code, work_center_name, family)
VALUES ('WC05-HOT026', '單只燙/刮金機', 'WC05', '烫金', NULL);

INSERT INTO rg_resource_group_task_map (resource_group_id, task_code, support_type)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT026'), 'TK', 'YES');

INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT026'), 'STRUCT_RULES', '硬性条件');

INSERT INTO rg_resource_rule_condition (rule_header_id, param_code, param_name, operator, value_min, value_max, rule_type, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT026') AND rule_type = 'STRUCT_RULES'),
 'sheet_count', '印张石数', '<=', NULL, 5000, 'STRUCT_RULE', 1),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT026') AND rule_type = 'STRUCT_RULES'),
 'gsm', '克重(g)', 'BETWEEN', 100, 450, 'STRUCT_RULE', 2),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT026') AND rule_type = 'STRUCT_RULES'),
 'width', '宽度(mm)', 'BETWEEN', 102, 330, 'STRUCT_RULE', 3),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT026') AND rule_type = 'STRUCT_RULES'),
 'height', '高度(mm)', 'BETWEEN', 127, 457, 'STRUCT_RULE', 4);

INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT026'), 'TEXT_RULES', '培训表原文');

INSERT INTO rg_resource_rule_condition (rule_header_id, rule_type, is_blocking, value_text, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT026') AND rule_type = 'TEXT_RULES'),
 'TEXT_RULE', false, 
'1.用纸皮<=100g-450g
2.凹件符合雙紋條件/数量为5000石以下/在5000石以上要讓上大機生产
3.手動位于/皮凹3皮覆皮1皮版皮(皮凹皮只提皮皮15mm皮平尺)/且数量为2000石以下
4.手動皮不皮上平烫機/皮電迫平到
5.印刷要光色不選上平烫機/迫陷止影兒
6.生产最大尺寸330.2*457.2mm
7.生产最小尺寸102*127mm
8.最大上機生産面積/立燙皮230*380mm/平燙230*457mm(因新纸不同)', 1);


-- ============================================
-- WC05-HOT027 風琴招燙金機
-- ============================================
INSERT INTO rg_resource_group (resource_group_code, name, work_center_code, work_center_name, family)
VALUES ('WC05-HOT027', '風琴招燙金機', 'WC05', '烫金', NULL);

INSERT INTO rg_resource_group_task_map (resource_group_id, task_code, support_type)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT027'), 'TK', 'YES');

INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT027'), 'STRUCT_RULES', '硬性条件');

INSERT INTO rg_resource_rule_condition (rule_header_id, param_code, param_name, operator, value_min, value_max, rule_type, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT027') AND rule_type = 'STRUCT_RULES'),
 'sheet_count', '印张石数', '<=', NULL, 5000, 'STRUCT_RULE', 1),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT027') AND rule_type = 'STRUCT_RULES'),
 'width', '宽度(mm)', 'BETWEEN', 63, 444, 'STRUCT_RULE', 2),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT027') AND rule_type = 'STRUCT_RULES'),
 'height', '高度(mm)', 'BETWEEN', 88, 317, 'STRUCT_RULE', 3);

INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT027'), 'TEXT_RULES', '培训表原文');

INSERT INTO rg_resource_rule_condition (rule_header_id, rule_type, is_blocking, value_text, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC05-HOT027') AND rule_type = 'TEXT_RULES'),
 'TEXT_RULE', false, 
'1.内页/手油紙
2.凹件符合雙紋條件件/数量为5000石以下/在5000石以上要讓上大機生产
3.手動位于/皮凹3皮覆皮1皮版皮(皮凹皮只提皮皮15mm皮平尺)/且数量为2000石以下
4.手動皮不皮上平烫機/皮電迫平到
5.印刷要光色不選上平烫機/迫陷止影兒
6.生产最大尺寸444.5*317.5mm
7.生产最小尺寸63.5*88.9mm(<=140gsm以下薄纸皮只能达到380*450mm)
8.最大上機生産面積292.1*419mm', 1);

