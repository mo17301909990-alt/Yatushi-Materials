-- ========================================
-- 修改 post_processing_print_uv 表的 company_number 字段约束
-- 允许 company_number 为 NULL
-- ========================================

-- 修改 company_number 字段约束，允许 NULL 值
ALTER TABLE public.post_processing_print_uv 
ALTER COLUMN company_number DROP NOT NULL;

-- 添加注释说明
COMMENT ON COLUMN public.post_processing_print_uv.company_number IS '公司编号(对应leo_gp_numbers表的company_number)，允许为NULL';

-- 显示修改后的表结构
\d public.post_processing_print_uv;
