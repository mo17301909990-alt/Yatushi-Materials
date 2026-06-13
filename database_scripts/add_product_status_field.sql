-- 为products表添加物料状态字段
-- 用于标记物料状态：可用、废弃

-- 1. 添加status字段
ALTER TABLE public.products 
ADD COLUMN IF NOT EXISTS status character varying(10) DEFAULT '可用';

-- 2. 添加约束，确保status只能是'可用'或'废弃'
ALTER TABLE public.products 
ADD CONSTRAINT check_product_status 
CHECK (status IN ('可用', '废弃'));

-- 3. 为现有数据设置默认值（如果字段已存在但数据为空）
UPDATE public.products 
SET status = '可用' 
WHERE status IS NULL;

-- 4. 创建索引以提高查询性能
CREATE INDEX IF NOT EXISTS idx_products_status ON public.products(status);

-- 5. 添加注释
COMMENT ON COLUMN public.products.status IS '物料状态标记：可用、废弃';

-- 6. 验证字段是否添加成功
SELECT 
    column_name, 
    data_type, 
    column_default,
    is_nullable
FROM information_schema.columns 
WHERE table_name = 'products' 
    AND column_name = 'status';

