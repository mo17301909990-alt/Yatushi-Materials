-- 更新布面纸兼容性状态脚本
-- 基于「布面紙+燙金」組合應用表的实际测试结果

-- 注意：以下数据需要根据实际测试结果进行更新
-- 兼容性状态说明：
-- 'V' = 可以直接燙金
-- 'X' = 不可以燙金  
-- 'NA' = 不确定
-- '▷' = 需要作「絲印打底處理」再燙金

-- 示例：更新尼龍絲系列的兼容性状态
-- 根据实际测试结果，尼龍絲系列可能需要打底处理
UPDATE cloth_paper_compatibility 
SET compatibility_status = '▷'  -- 需要打底处理
WHERE product_name IN ('L187', 'SY+', 'G1', 'ST', 'GN') 
  AND cloth_paper_type_id IN (
    SELECT id FROM cloth_paper_types 
    WHERE category = '尼龍絲'
  );

-- 示例：更新涤纶系列的兼容性状态
-- 根据实际测试结果，涤纶系列可能需要打底处理
UPDATE cloth_paper_compatibility 
SET compatibility_status = '▷'  -- 需要打底处理
WHERE product_name IN ('L187', 'SY+', 'G1', 'ST', 'GN') 
  AND cloth_paper_type_id IN (
    SELECT id FROM cloth_paper_types 
    WHERE category = '涤纶'
  );

-- 示例：更新仿棉系列的兼容性状态
-- 根据实际测试结果，仿棉系列可能需要打底处理
UPDATE cloth_paper_compatibility 
SET compatibility_status = '▷'  -- 需要打底处理
WHERE product_name IN ('L187', 'SY+', 'G1', 'ST', 'GN') 
  AND cloth_paper_type_id IN (
    SELECT id FROM cloth_paper_types 
    WHERE category = '仿棉'
  );

-- 示例：更新涤棉系列的兼容性状态
-- 根据实际测试结果，涤棉系列可能需要打底处理
UPDATE cloth_paper_compatibility 
SET compatibility_status = '▷'  -- 需要打底处理
WHERE product_name IN ('L187', 'SY+', 'G1', 'ST', 'GN') 
  AND cloth_paper_type_id IN (
    SELECT id FROM cloth_paper_types 
    WHERE category = '涤棉'
  );

-- 示例：更新麻棉系列的兼容性状态
-- 根据实际测试结果，麻棉系列可能需要打底处理
UPDATE cloth_paper_compatibility 
SET compatibility_status = '▷'  -- 需要打底处理
WHERE product_name IN ('L187', 'SY+', 'G1', 'ST', 'GN') 
  AND cloth_paper_type_id IN (
    SELECT id FROM cloth_paper_types 
    WHERE category = '麻棉'
  );

-- 示例：更新純棉系列的兼容性状态
-- 根据实际测试结果，純棉系列可能需要打底处理
UPDATE cloth_paper_compatibility 
SET compatibility_status = '▷'  -- 需要打底处理
WHERE product_name IN ('L187', 'SY+', 'G1', 'ST', 'GN') 
  AND cloth_paper_type_id IN (
    SELECT id FROM cloth_paper_types 
    WHERE category = '純棉'
  );

-- 示例：更新棉布系列的兼容性状态
-- 根据实际测试结果，棉布系列可能需要打底处理
UPDATE cloth_paper_compatibility 
SET compatibility_status = '▷'  -- 需要打底处理
WHERE product_name IN ('L187', 'SY+', 'G1', 'ST', 'GN') 
  AND cloth_paper_type_id IN (
    SELECT id FROM cloth_paper_types 
    WHERE category = '棉布'
  );

-- 示例：更新絲光棉系列的兼容性状态
-- 根据实际测试结果，絲光棉系列可能需要打底处理
UPDATE cloth_paper_compatibility 
SET compatibility_status = '▷'  -- 需要打底处理
WHERE product_name IN ('L187', 'SY+', 'G1', 'ST', 'GN') 
  AND cloth_paper_type_id IN (
    SELECT id FROM cloth_paper_types 
    WHERE category = '絲光棉'
  );

-- 示例：更新絹布系列的兼容性状态
-- 根据实际测试结果，絹布系列可能需要打底处理
UPDATE cloth_paper_compatibility 
SET compatibility_status = '▷'  -- 需要打底处理
WHERE product_name IN ('L187', 'SY+', 'G1', 'ST', 'GN') 
  AND cloth_paper_type_id IN (
    SELECT id FROM cloth_paper_types 
    WHERE category = '絹布'
  );

-- 示例：更新閃光布系列的兼容性状态
-- 根据实际测试结果，閃光布系列可能需要打底处理
UPDATE cloth_paper_compatibility 
SET compatibility_status = '▷'  -- 需要打底处理
WHERE product_name IN ('L187', 'SY+', 'G1', 'ST', 'GN') 
  AND cloth_paper_type_id IN (
    SELECT id FROM cloth_paper_types 
    WHERE category = '閃光布'
  );

-- 示例：更新絲綢布系列的兼容性状态
-- 根据实际测试结果，絲綢布系列可能需要打底处理
UPDATE cloth_paper_compatibility 
SET compatibility_status = '▷'  -- 需要打底处理
WHERE product_name IN ('L187', 'SY+', 'G1', 'ST', 'GN') 
  AND cloth_paper_type_id IN (
    SELECT id FROM cloth_paper_types 
    WHERE category = '絲綢布'
  );

-- 示例：更新麻壓布系列的兼容性状态
-- 根据实际测试结果，麻壓布系列可能需要打底处理
UPDATE cloth_paper_compatibility 
SET compatibility_status = '▷'  -- 需要打底处理
WHERE product_name IN ('L187', 'SY+', 'G1', 'ST', 'GN') 
  AND cloth_paper_type_id IN (
    SELECT id FROM cloth_paper_types 
    WHERE category = '麻壓布'
  );

-- 示例：更新壓紋布系列的兼容性状态
-- 根据实际测试结果，壓紋布系列可能需要打底处理
UPDATE cloth_paper_compatibility 
SET compatibility_status = '▷'  -- 需要打底处理
WHERE product_name IN ('L187', 'SY+', 'G1', 'ST', 'GN') 
  AND cloth_paper_type_id IN (
    SELECT id FROM cloth_paper_types 
    WHERE category = '壓紋布'
  );

-- 示例：更新絲光麻布系列的兼容性状态
-- 根据实际测试结果，絲光麻布系列可能需要打底处理
UPDATE cloth_paper_compatibility 
SET compatibility_status = '▷'  -- 需要打底处理
WHERE product_name IN ('L187', 'SY+', 'G1', 'ST', 'GN') 
  AND cloth_paper_type_id IN (
    SELECT id FROM cloth_paper_types 
    WHERE category = '絲光麻布'
  );

-- 示例：更新粗布系列的兼容性状态
-- 根据实际测试结果，粗布系列可能需要打底处理
UPDATE cloth_paper_compatibility 
SET compatibility_status = '▷'  -- 需要打底处理
WHERE product_name IN ('L187', 'SY+', 'G1', 'ST', 'GN') 
  AND cloth_paper_type_id IN (
    SELECT id FROM cloth_paper_types 
    WHERE category = '粗布'
  );

-- 验证更新结果
SELECT 
    cpc.product_name as 物料型号系列,
    cpt.category as 布面纸系列,
    cpt.type_name as 布面纸类型,
    cpc.compatibility_status as 兼容性状态,
    cpc.updated_at as 更新时间
FROM cloth_paper_compatibility cpc
JOIN cloth_paper_types cpt ON cpc.cloth_paper_type_id = cpt.id
ORDER BY cpc.product_name, cpt.category, cpt.type_name;

-- 统计各兼容性状态的数量
SELECT 
    compatibility_status as 兼容性状态,
    COUNT(*) as 数量
FROM cloth_paper_compatibility 
GROUP BY compatibility_status
ORDER BY compatibility_status;

-- 按物料型号系列统计兼容性状态
SELECT 
    product_name as 物料型号系列,
    compatibility_status as 兼容性状态,
    COUNT(*) as 数量
FROM cloth_paper_compatibility 
GROUP BY product_name, compatibility_status
ORDER BY product_name, compatibility_status;
