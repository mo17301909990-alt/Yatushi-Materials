-- ============================================
-- 资源组选择系统 - 过胶资源组数据 (WC04)
-- ============================================

-- ============================================
-- WC04-LAM015 水性过胶機
-- ============================================
INSERT INTO rg_resource_group (resource_group_code, name, work_center_code, work_center_name, family)
VALUES ('WC04-LAM015', '水性过胶機', 'WC04', '过胶', NULL);

INSERT INTO rg_resource_group_task_map (resource_group_id, task_code, support_type)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC04-LAM015'), 'GL', 'YES');

INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC04-LAM015'), 'TEXT_RULES', '培训表原文');

INSERT INTO rg_resource_rule_condition (rule_header_id, rule_type, is_blocking, value_text, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC04-LAM015') AND rule_type = 'TEXT_RULES'),
 'TEXT_RULE', false, 
'依據印件印刷位置的就近原則選用', 1);


-- ============================================
-- WC04-LAM014 預塗过胶機
-- ============================================
INSERT INTO rg_resource_group (resource_group_code, name, work_center_code, work_center_name, family)
VALUES ('WC04-LAM014', '預塗过胶機', 'WC04', '过胶', NULL);

INSERT INTO rg_resource_group_task_map (resource_group_id, task_code, support_type)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC04-LAM014'), 'GL', 'YES');

INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC04-LAM014'), 'TEXT_RULES', '培训表原文');

INSERT INTO rg_resource_rule_condition (rule_header_id, rule_type, is_blocking, value_text, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC04-LAM014') AND rule_type = 'TEXT_RULES'),
 'TEXT_RULE', false, 
'依據印件印刷位置的就近原則選用', 1);


-- ============================================
-- WC04-LAM016 手落过胶機
-- ============================================
INSERT INTO rg_resource_group (resource_group_code, name, work_center_code, work_center_name, family)
VALUES ('WC04-LAM016', '手落过胶機', 'WC04', '过胶', NULL);

INSERT INTO rg_resource_group_task_map (resource_group_id, task_code, support_type)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC04-LAM016'), 'GL', 'YES');

INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC04-LAM016'), 'TEXT_RULES', '培训表原文');

INSERT INTO rg_resource_rule_condition (rule_header_id, rule_type, is_blocking, value_text, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC04-LAM016') AND rule_type = 'TEXT_RULES'),
 'TEXT_RULE', false, 
'出樣用', 1);


-- ============================================
-- WC04-LAM017 裱雙面膠機
-- ============================================
INSERT INTO rg_resource_group (resource_group_code, name, work_center_code, work_center_name, family)
VALUES ('WC04-LAM017', '裱雙面膠機', 'WC04', '过胶', NULL);

INSERT INTO rg_resource_group_task_map (resource_group_id, task_code, support_type)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC04-LAM017'), 'GL', 'YES');

INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC04-LAM017'), 'TEXT_RULES', '培训表原文');

INSERT INTO rg_resource_rule_condition (rule_header_id, rule_type, is_blocking, value_text, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC04-LAM017') AND rule_type = 'TEXT_RULES'),
 'TEXT_RULE', false, 
'光柵片裱雙面膠', 1);


-- ============================================
-- 资源组选择系统 - 裱纸资源组数据 (WC10)
-- ============================================

-- ============================================
-- WC10-MPPO09 平張裱紙機(裱坑機)
-- ============================================
INSERT INTO rg_resource_group (resource_group_code, name, work_center_code, work_center_name, family)
VALUES ('WC10-MPPO09', '平張裱紙機(裱坑機)', 'WC10', '裱纸', NULL);

INSERT INTO rg_resource_group_task_map (resource_group_id, task_code, support_type)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC10-MPPO09'), 'BP', 'YES');

INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC10-MPPO09'), 'STRUCT_RULES', '硬性条件');

INSERT INTO rg_resource_rule_condition (rule_header_id, param_code, param_name, operator, value_min, value_max, rule_type, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC10-MPPO09') AND rule_type = 'STRUCT_RULES'),
 'thickness', '厚度(mm)', '<=', NULL, 2.87, 'STRUCT_RULE', 1),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC10-MPPO09') AND rule_type = 'STRUCT_RULES'),
 'gsm', '克重(g)', 'BETWEEN', 100, 1700, 'STRUCT_RULE', 2),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC10-MPPO09') AND rule_type = 'STRUCT_RULES'),
 'width', '宽度(mm)', 'BETWEEN', 305, 1000, 'STRUCT_RULE', 3),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC10-MPPO09') AND rule_type = 'STRUCT_RULES'),
 'height', '高度(mm)', 'BETWEEN', 505, 1200, 'STRUCT_RULE', 4);

INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC10-MPPO09'), 'TEXT_RULES', '培训表原文');

INSERT INTO rg_resource_rule_condition (rule_header_id, rule_type, is_blocking, value_text, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC10-MPPO09') AND rule_type = 'TEXT_RULES'),
 'TEXT_RULE', false, 
'裱紙機每次只能裱兩層紙,底紙上膠
上機紙厚度:
  面紙:
    - 無色仅限制纸張:100~800GSM
    - 有色仅限制纸張:140~800GSM
  底紙:
    - 坑纸張:210~1400GSM
    - 青纸纸張:180GSM-1400GSM
    - 粉灰贴纸張:250~1400GSM
    - 板纸纸張最厚:1700GSM(2.87MM)
上機尺寸:
  - 最大:長1000MM * 寬1200MM
  - 最小:長305MM * 寬505MM
盒類裱紙（為青紙且非印刷時）:
  1.板紙對開或以上用法且出紙數量>=2令并且板紙原開
  2.出紙數量<10令
  3.當板紙尺寸(非入機邊)<558MM', 1);


-- ============================================
-- WC10-MPPO08 滾筒裱紙機
-- ============================================
INSERT INTO rg_resource_group (resource_group_code, name, work_center_code, work_center_name, family)
VALUES ('WC10-MPPO08', '滾筒裱紙機', 'WC10', '裱纸', NULL);

INSERT INTO rg_resource_group_task_map (resource_group_id, task_code, support_type)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC10-MPPO08'), 'BP', 'YES');

INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC10-MPPO08'), 'STRUCT_RULES', '硬性条件');

INSERT INTO rg_resource_rule_condition (rule_header_id, param_code, param_name, operator, value_min, value_max, rule_type, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC10-MPPO08') AND rule_type = 'STRUCT_RULES'),
 'width', '宽度(mm)', '>=', 558, NULL, 'STRUCT_RULE', 1),
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC10-MPPO08') AND rule_type = 'STRUCT_RULES'),
 'height', '高度(mm)', '>=', 200, NULL, 'STRUCT_RULE', 2);

INSERT INTO rg_resource_rule_header (resource_group_id, rule_type, rule_name)
VALUES ((SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC10-MPPO08'), 'TEXT_RULES', '培训表原文');

INSERT INTO rg_resource_rule_condition (rule_header_id, rule_type, is_blocking, value_text, sort_order)
VALUES 
((SELECT id FROM rg_resource_rule_header WHERE resource_group_id = (SELECT id FROM rg_resource_group WHERE resource_group_code = 'WC10-MPPO08') AND rule_type = 'TEXT_RULES'),
 'TEXT_RULE', false, 
'盒類裱紙（為青紙且非印刷時）:
1.板紙對開或以上用法且出紙數量>=2令用滾筒裱
2.板紙原開,出紙數量>=10令用滾筒裱
3.滾筒裱紙機(即何氏裱紙機)最小的上機尺寸為558mm*200mm*1mm
4.當板紙尺寸(非入機邊)<558MM要平張裱如圖示
5.三層對(word文件)非為非印刷且為薄紙與板紙對裱,必須使用滾筒裱紙,此情況不受石數限制', 1);

