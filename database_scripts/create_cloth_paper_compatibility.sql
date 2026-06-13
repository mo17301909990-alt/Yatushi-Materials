-- 创建布面纸兼容性表（在cloth_paper_types表创建后执行）
-- 用于存储布面纸与产品的兼容性关系

-- 创建兼容性表
CREATE TABLE IF NOT EXISTS cloth_paper_compatibility (
    id SERIAL PRIMARY KEY,
    project_id INTEGER NOT NULL,
    cloth_paper_type_id INTEGER,
    compatibility_status CHAR(1) NOT NULL CHECK (compatibility_status IN ('V', 'X', 'NA', '▷')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 添加外键约束
ALTER TABLE cloth_paper_compatibility 
ADD CONSTRAINT fk_cloth_paper_compatibility_type 
FOREIGN KEY (cloth_paper_type_id) REFERENCES cloth_paper_types(id);

-- 添加表注释
COMMENT ON TABLE cloth_paper_compatibility IS '布面纸兼容性关系表';
COMMENT ON COLUMN cloth_paper_compatibility.project_id IS '关联产品ID';
COMMENT ON COLUMN cloth_paper_compatibility.cloth_paper_type_id IS '布面纸类型ID';
COMMENT ON COLUMN cloth_paper_compatibility.compatibility_status IS '兼容性状态：V-适用，X-不适用，NA-不确定，▷-需要打底处理';

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_cloth_paper_compatibility_project ON cloth_paper_compatibility(project_id);
CREATE INDEX IF NOT EXISTS idx_cloth_paper_compatibility_type ON cloth_paper_compatibility(cloth_paper_type_id);

-- 验证表创建
SELECT 
    table_name, 
    column_name, 
    data_type, 
    is_nullable
FROM information_schema.columns 
WHERE table_name = 'cloth_paper_compatibility'
ORDER BY ordinal_position;

