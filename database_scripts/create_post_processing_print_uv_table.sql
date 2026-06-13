-- ========================================
-- 创建后加工印刷UV选项兼容性表
-- ========================================

-- 如果表已存在则删除
DROP TABLE IF EXISTS public.post_processing_print_uv;

-- 创建表
CREATE TABLE public.post_processing_print_uv (
    id SERIAL PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL COMMENT '产品系列(对应product表的name)',
    product_model_number VARCHAR(255) NOT NULL COMMENT '产品型号(对应product表的model_number)',
    company_number VARCHAR(255) NOT NULL COMMENT '公司编号(对应leo_gp_numbers表的company_number)',
    compatibility_status CHAR(1) NOT NULL DEFAULT 'V' COMMENT '兼容性状态：V-适用，X-不适用，N-不确定',
    paper_type VARCHAR(255) NULL COMMENT '纸张类型（对应hot_stamping_paper_type）',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 添加注释
COMMENT ON TABLE public.post_processing_print_uv IS '后加工印刷UV选项兼容性表';
COMMENT ON COLUMN public.post_processing_print_uv.id IS '主键ID';
COMMENT ON COLUMN public.post_processing_print_uv.product_name IS '产品系列(对应product表的name)';
COMMENT ON COLUMN public.post_processing_print_uv.product_model_number IS '产品型号(对应product表的model_number)';
COMMENT ON COLUMN public.post_processing_print_uv.company_number IS '公司编号(对应leo_gp_numbers表的company_number)';
COMMENT ON COLUMN public.post_processing_print_uv.compatibility_status IS '兼容性状态：V-适用，X-不适用，N-不确定';
COMMENT ON COLUMN public.post_processing_print_uv.paper_type IS '纸张类型（对应hot_stamping_paper_type）';
COMMENT ON COLUMN public.post_processing_print_uv.created_at IS '创建时间';
COMMENT ON COLUMN public.post_processing_print_uv.updated_at IS '更新时间';

-- 创建索引
CREATE INDEX idx_post_processing_print_uv_product_name ON public.post_processing_print_uv(product_name);
CREATE INDEX idx_post_processing_print_uv_product_model_number ON public.post_processing_print_uv(product_model_number);
CREATE INDEX idx_post_processing_print_uv_company_number ON public.post_processing_print_uv(company_number);
CREATE INDEX idx_post_processing_print_uv_compatibility_status ON public.post_processing_print_uv(compatibility_status);
CREATE INDEX idx_post_processing_print_uv_paper_type ON public.post_processing_print_uv(paper_type);

-- 创建复合索引用于优化查询
CREATE INDEX idx_post_processing_print_uv_composite ON public.post_processing_print_uv(
    company_number, 
    product_name, 
    product_model_number, 
    paper_type
);

-- 显示表结构
\d public.post_processing_print_uv;
