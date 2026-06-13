-- 查询兼容性数据及其状态说明
-- 展示如何获取包含中文说明的兼容性信息

-- 1. 查询所有兼容性数据及其说明
SELECT 
    cpc.product_name AS "物料型号系列",
    cpt.type_name AS "布面纸类型",
    cpt.category AS "分类",
    cpc.compatibility_status AS "状态码",
    csc.status_name AS "状态说明",
    csc.description AS "详细说明",
    cpc.created_at AS "创建时间"
FROM cloth_paper_compatibility cpc
JOIN cloth_paper_types cpt ON cpc.cloth_paper_type_id = cpt.id
JOIN compatibility_status_config csc ON cpc.compatibility_status = csc.status_code
ORDER BY cpc.product_name, cpt.category, cpt.sort_order;

-- 2. 按系列查询兼容性统计
SELECT 
    cpc.product_name AS "物料型号系列",
    COUNT(*) AS "总数量",
    SUM(CASE WHEN cpc.compatibility_status = 'V' THEN 1 ELSE 0 END) AS "可以加工",
    SUM(CASE WHEN cpc.compatibility_status = 'X' THEN 1 ELSE 0 END) AS "不可以加工",
    SUM(CASE WHEN cpc.compatibility_status = '▷' THEN 1 ELSE 0 END) AS "需要打底处理",
    SUM(CASE WHEN cpc.compatibility_status = 'NA' THEN 1 ELSE 0 END) AS "不确定"
FROM cloth_paper_compatibility cpc
GROUP BY cpc.product_name
ORDER BY cpc.product_name;

-- 3. 查询特定系列的详细兼容性信息
SELECT 
    cpt.type_name AS "布面纸类型",
    cpt.category AS "分类",
    csc.status_name AS "兼容性状态",
    csc.description AS "说明"
FROM cloth_paper_compatibility cpc
JOIN cloth_paper_types cpt ON cpc.cloth_paper_type_id = cpt.id
JOIN compatibility_status_config csc ON cpc.compatibility_status = csc.status_code
WHERE cpc.product_name = 'L187'  -- 可以替换为其他系列
ORDER BY cpt.category, cpt.sort_order;

-- 4. 查询需要打底处理的布面纸类型
SELECT 
    cpc.product_name AS "物料型号系列",
    cpt.type_name AS "布面纸类型",
    cpt.category AS "分类",
    csc.description AS "处理说明"
FROM cloth_paper_compatibility cpc
JOIN cloth_paper_types cpt ON cpc.cloth_paper_type_id = cpt.id
JOIN compatibility_status_config csc ON cpc.compatibility_status = csc.status_code
WHERE cpc.compatibility_status = '▷'
ORDER BY cpc.product_name, cpt.category, cpt.sort_order;

-- 5. 查询不能加工的布面纸类型
SELECT 
    cpc.product_name AS "物料型号系列",
    cpt.type_name AS "布面纸类型",
    cpt.category AS "分类"
FROM cloth_paper_compatibility cpc
JOIN cloth_paper_types cpt ON cpc.cloth_paper_type_id = cpt.id
WHERE cpc.compatibility_status = 'X'
ORDER BY cpc.product_name, cpt.category, cpt.sort_order;

