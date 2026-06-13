-- 备选方案：在现有兼容性表中添加状态说明字段
-- 如果选择此方案，可以添加 description 字段到 cloth_paper_compatibility 表

-- 1. 添加状态说明字段
ALTER TABLE cloth_paper_compatibility 
ADD COLUMN IF NOT EXISTS status_description VARCHAR(100);

-- 2. 更新现有数据的状态说明
UPDATE cloth_paper_compatibility 
SET status_description = CASE 
    WHEN compatibility_status = 'V' THEN '可以加工'
    WHEN compatibility_status = 'X' THEN '不可以加工'
    WHEN compatibility_status = 'NA' THEN '不确定'
    WHEN compatibility_status = '▷' THEN '需要打底处理'
    ELSE '未知状态'
END;

-- 3. 添加字段注释
COMMENT ON COLUMN cloth_paper_compatibility.status_description IS '兼容性状态的中文说明';

-- 4. 验证更新结果
SELECT 
    product_name,
    compatibility_status,
    status_description,
    COUNT(*) as count
FROM cloth_paper_compatibility 
GROUP BY product_name, compatibility_status, status_description
ORDER BY product_name, compatibility_status;

-- 5. 查询示例：获取包含说明的兼容性数据
SELECT 
    product_name AS "物料型号系列",
    compatibility_status AS "状态码",
    status_description AS "状态说明",
    COUNT(*) AS "数量"
FROM cloth_paper_compatibility 
GROUP BY product_name, compatibility_status, status_description
ORDER BY product_name, compatibility_status;

