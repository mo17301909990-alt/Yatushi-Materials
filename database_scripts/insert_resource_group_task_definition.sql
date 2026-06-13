-- ============================================
-- 资源组选择系统 - 任务定义数据
-- ============================================

-- 清空现有数据
DELETE FROM rg_task_definition;

-- 插入任务定义
INSERT INTO rg_task_definition (task_code, task_name, description, sort_order) VALUES
('PM', '啤机', '啤机主工序/模切', 1),
('TK', '烫金', '烫金加工', 2),
('SS', '丝印', '丝印加工', 3),
('GL', '过胶', '过胶/覆膜', 4),
('BP', '裱纸', '裱纸/裱坑', 5);

