-- ========================================
-- 插入后加工印刷UV选项兼容性测试数据
-- ========================================

-- 清空现有数据
DELETE FROM public.post_processing_print_uv;

-- 插入测试数据
INSERT INTO public.post_processing_print_uv (
    product_name,
    product_model_number,
    company_number,
    compatibility_status,
    paper_type
) VALUES 
-- 产品系列A的印刷UV兼容性
('产品系列A', 'A001', 'COMP001', 'V', '普通金纸'),
('产品系列A', 'A001', 'COMP002', 'V', '耐磨金纸'),
('产品系列A', 'A001', 'COMP003', 'X', '普通金纸'),
('产品系列A', 'A001', 'COMP004', 'V', NULL), -- 适用所有纸张类型

-- 产品系列A的通用规则（不指定具体型号）
('产品系列A', '', 'COMP001', 'V', '普通金纸'),
('产品系列A', '', 'COMP002', 'V', '耐磨金纸'),
('产品系列A', '', 'COMP003', 'X', '普通金纸'),
('产品系列A', '', 'COMP004', 'V', NULL), -- 适用所有纸张类型

-- 产品系列B的印刷UV兼容性
('产品系列B', 'B001', 'COMP001', 'V', '普通金纸'),
('产品系列B', 'B001', 'COMP002', 'X', '耐磨金纸'),
('产品系列B', 'B001', 'COMP003', 'V', '普通金纸'),
('产品系列B', 'B001', 'COMP004', 'V', '普通金纸'),

-- 产品系列B的通用规则
('产品系列B', '', 'COMP001', 'V', '普通金纸'),
('产品系列B', '', 'COMP002', 'X', '耐磨金纸'),
('产品系列B', '', 'COMP003', 'V', '普通金纸'),
('产品系列B', '', 'COMP004', 'V', '普通金纸'),

-- 产品系列C的印刷UV兼容性
('产品系列C', 'C001', 'COMP001', 'X', '普通金纸'),
('产品系列C', 'C001', 'COMP002', 'V', '耐磨金纸'),
('产品系列C', 'C001', 'COMP003', 'V', '普通金纸'),
('产品系列C', 'C001', 'COMP004', 'X', '普通金纸'),

-- 产品系列C的通用规则
('产品系列C', '', 'COMP001', 'X', '普通金纸'),
('产品系列C', '', 'COMP002', 'V', '耐磨金纸'),
('产品系列C', '', 'COMP003', 'V', '普通金纸'),
('产品系列C', '', 'COMP004', 'X', '普通金纸'),

-- 特殊测试数据：空字符串测试
('产品系列D', '', 'COMP001', 'V', ''),
('产品系列D', '', 'COMP002', 'X', ''),
('产品系列D', '', 'COMP003', 'V', ''),
('产品系列D', '', 'COMP004', 'V', '');

-- 显示插入的数据
SELECT 
    id,
    product_name,
    product_model_number,
    company_number,
    compatibility_status,
    paper_type,
    created_at
FROM public.post_processing_print_uv
ORDER BY 
    product_name,
    product_model_number,
    company_number;
