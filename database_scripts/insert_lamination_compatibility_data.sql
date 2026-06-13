-- ==============================================
-- 过胶材质兼容性数据插入脚本
-- ==============================================



CREATE TABLE IF NOT EXISTS lamination_material_options (
    id SERIAL PRIMARY KEY,
    material_name VARCHAR(100) NOT NULL UNIQUE,
    material_code VARCHAR(50) NOT NULL UNIQUE,
    display_order INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    description VARCHAR(200),
    pid INTEGER,  -- 修复了数据类型
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON TABLE lamination_material_options IS '过胶材质选项表';
COMMENT ON COLUMN lamination_material_options.id IS '主键ID';
COMMENT ON COLUMN lamination_material_options.material_name IS '材质名称（前端选项的text）';
COMMENT ON COLUMN lamination_material_options.material_code IS '材质编码（前端选项的value）';
COMMENT ON COLUMN lamination_material_options.display_order IS '显示顺序';
COMMENT ON COLUMN lamination_material_options.is_active IS '是否激活';
COMMENT ON COLUMN lamination_material_options.description IS '材质描述';
COMMENT ON COLUMN lamination_material_options.pid IS '父级ID（用于层级结构）';



-- ==============================================
-- 3. 选项表：后加工步骤选项表
-- ==============================================

CREATE TABLE IF NOT EXISTS post_processing_options (
    id SERIAL PRIMARY KEY,
    step_name VARCHAR(200) NOT NULL UNIQUE,
    step_code VARCHAR(50),
    display_order INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE post_processing_options IS '后加工步骤选项表';
COMMENT ON COLUMN post_processing_options.step_name IS '步骤名称';
COMMENT ON COLUMN post_processing_options.step_code IS '步骤编码';
COMMENT ON COLUMN post_processing_options.display_order IS '显示顺序';
COMMENT ON COLUMN post_processing_options.is_active IS '是否激活';
COMMENT ON COLUMN post_processing_options.description IS '步骤描述';



-- ==============================================
-- 4. 选项表：界面类型选项表
-- ==============================================

CREATE TABLE IF NOT EXISTS interface_type_options (
    id SERIAL PRIMARY KEY,
    interface_name VARCHAR(100) NOT NULL UNIQUE,
    interface_code VARCHAR(50),
    display_order INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE interface_type_options IS '界面类型选项表';
COMMENT ON COLUMN interface_type_options.interface_name IS '界面类型名称';
COMMENT ON COLUMN interface_type_options.interface_code IS '界面类型编码';
COMMENT ON COLUMN interface_type_options.display_order IS '显示顺序';
COMMENT ON COLUMN interface_type_options.is_active IS '是否激活';
COMMENT ON COLUMN interface_type_options.description IS '界面类型描述';


CREATE TABLE IF NOT EXISTS lamination_compatibility (
    id SERIAL PRIMARY KEY,
    foil_series VARCHAR(50) NOT NULL,
    interface_type_id INTEGER NOT NULL,
    post_processing_step_id INTEGER NOT NULL,
    lamination_material_id INTEGER NOT NULL,
    compatibility CHAR(1) NOT NULL DEFAULT 'V',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

        -- 唯一约束：同一产品+界面类型+后加工步骤+过胶材质的组合只能有一条记录
    CONSTRAINT unique_lamination_compatibility
      UNIQUE (foil_series, interface_type_id, post_processing_step_id, lamination_material_id),


        -- 外键约束：关联后加工步骤选项表
    CONSTRAINT fk_lamination_compatibility_post_processing 
        FOREIGN KEY (post_processing_step_id) REFERENCES post_processing_options(id) ON DELETE CASCADE,
    
    -- 外键约束：关联过胶材质选项表
    CONSTRAINT fk_lamination_compatibility_lamination_material 
        FOREIGN KEY (lamination_material_id) REFERENCES lamination_material_options(id) ON DELETE CASCADE,
    
    -- 检查约束：兼容性只能是V或X
    CONSTRAINT check_compatibility 
        CHECK (compatibility IN ('V', 'X'))
);

-- 添加字段注释
-- 添加字段注释
COMMENT ON TABLE lamination_compatibility IS '过胶材质兼容性表';
COMMENT ON COLUMN lamination_compatibility.foil_series IS '烫金纸系列（对应products表的name字段）';
COMMENT ON COLUMN lamination_compatibility.interface_type_id IS '界面类型ID（关联interface_type_options表）';
COMMENT ON COLUMN lamination_compatibility.post_processing_step_id IS '后加工步骤ID（关联post_processing_options表）';
COMMENT ON COLUMN lamination_compatibility.lamination_material_id IS '过胶材质ID（关联lamination_material_options表）';
-- special_film字段已删除，特殊菲林信息已包含在过胶材质中
COMMENT ON COLUMN lamination_compatibility.compatibility IS '兼容性标识 V:兼容 X:不兼容';

INSERT INTO lamination_material_options (material_name, material_code, display_order, is_active, description, pid) VALUES -- 普通預塗菲林 ('光/啞膠', 'NORMAL_PRECOATED_GLOSS', 1, TRUE, '普通预涂菲林-光/啞膠', 1), ('平價耐磨啞膠', 'ECONOMIC_WEAR_RESISTANT_MATTE', 3, TRUE, '普通预涂菲林-平价耐磨啞膠', 1), -- 超粘預塗菲林 ('光/啞膠', 'SUPER_ADHESIVE_PRECOATED_GLOSS', 4, TRUE, '超粘预涂菲林-光/啞膠', 2), ('高價耐磨啞膠', 'PREMIUM_WEAR_RESISTANT_MATTE', 6, TRUE, '超粘预涂菲林-高價耐磨啞膠', 2), -- 水性菲林 ('光/啞膠', 'PRECOATED_SOFT_TOUCH', 7, TRUE, '水性菲林-光/啞膠', 3), -- 特殊菲林 ('預塗柔感膠(含預塗、水性)', 'ANTI_STATIC_GLOSS', 8, TRUE, '特殊菲林-預塗柔感膠(含預塗、水性)', 4), ('抗靜電光/啞膠(含預塗、水性)', 'ANTI_STATIC_MATTE', 9, TRUE, '特殊菲林-抗靜電光/啞膠(含預塗、水性)', 4),





-----------------
-- 1. 插入界面类型选项数据
INSERT INTO interface_type_options (interface_name, interface_code, display_order, is_active, description) VALUES
('粉紙面+普通油墨', 'POWDER_PAPER_NORMAL_INK', 1, TRUE, '粉紙面配合普通油墨印刷'),
('粉紙面+LED UV墨', 'POWDER_PAPER_LED_UV_INK', 2, TRUE, '粉紙面配合LED UV墨印刷'),
('粉紙面+快乾油墨', 'POWDER_PAPER_QUICK_DRY_INK', 3, TRUE, '粉紙面配合快乾油墨印刷');

-- 2. 插入后加工步骤选项数据
INSERT INTO post_processing_options (step_name, step_code, display_order, is_active, description) VALUES
('無任何加工', 'NO_PROCESSING', 1, TRUE, '不进行任何后加工处理'),
('擊凹/凸、絲印UV+擊凹/凸', 'EMBOSS_DEBOSS_UV', 2, TRUE, '击凹/凸工艺，可配合丝印UV'),
('啤折切、絲印UV+啤折切', 'DIE_CUT_FOLD_UV', 3, TRUE, '啤折切工艺，可配合丝印UV'),
('踩坑位、絲印UV+踩坑位', 'CREASING_UV', 4, TRUE, '踩坑位工艺，可配合丝印UV'),
('壓紋', 'EMBOSSING', 5, TRUE, '压纹工艺');

-- 3. 插入过胶材质选项数据
INSERT INTO lamination_material_options (material_name, material_code, display_order, is_active, description, pid) VALUES
-- 普通預塗菲林
('普通預塗菲林-光膠', 'NORMAL_PRECOATED_GLOSS', 1, TRUE, '普通预涂菲林-光胶', NULL),
('普通預塗菲林-啞膠', 'NORMAL_PRECOATED_MATTE', 2, TRUE, '普通预涂菲林-哑胶', NULL),
('平價耐磨啞膠', 'ECONOMIC_WEAR_RESISTANT_MATTE', 3, TRUE, '平价耐磨哑胶', NULL),

-- 超粘預塗菲林
('超粘預塗菲林-光膠', 'SUPER_ADHESIVE_PRECOATED_GLOSS', 4, TRUE, '超粘预涂菲林-光胶', NULL),
('超粘預塗菲林-啞膠', 'SUPER_ADHESIVE_PRECOATED_MATTE', 5, TRUE, '超粘预涂菲林-哑胶', NULL),
('高價耐磨啞膠', 'PREMIUM_WEAR_RESISTANT_MATTE', 6, TRUE, '高价耐磨哑胶', NULL),

-- 水性菲林
('預塗柔感膠', 'PRECOATED_SOFT_TOUCH', 7, TRUE, '预涂柔感胶', NULL),

-- 特殊菲林
('抗靜電光膠', 'ANTI_STATIC_GLOSS', 8, TRUE, '抗静电光胶', NULL),
('抗靜電啞膠', 'ANTI_STATIC_MATTE', 9, TRUE, '抗静电哑胶', NULL),
('含相逢水性', 'XIANG_FENG_WATER_BASED', 10, TRUE, '含相逢水性', NULL);

-- 4. 插入过胶兼容性数据
-- 注意：这里需要根据实际的烫金纸系列名称进行调整
-- 假设烫金纸系列包括：SY6-, SY1-, SY+, SB, SA, S19, S16, LL, LA, L817, KM, KB, KA, G6, G1, E8, A23

-- 获取界面类型ID的变量（实际使用时需要替换为具体的ID值）
-- 界面类型ID: 1=粉紙面+普通油墨, 2=粉紙面+LED UV墨, 3=粉紙面+快乾油墨

-- 获取后加工步骤ID的变量
-- 后加工步骤ID: 1=無任何加工, 2=擊凹/凸, 3=啤折切, 4=踩坑位, 5=壓紋

-- 获取过胶材质ID的变量
-- 过胶材质ID: 1=普通預塗菲林-光膠, 2=普通預塗菲林-啞膠, 3=平價耐磨啞膠, 
--             4=超粘預塗菲林-光膠, 5=超粘預塗菲林-啞膠, 6=高價耐磨啞膠,
--             7=預塗柔感膠, 8=抗靜電光膠, 9=抗靜電啞膠, 10=含相逢水性

-- 根据表格数据插入兼容性记录
-- 由于表格数据量很大，这里提供部分示例数据，完整数据需要根据实际表格内容补充

INSERT INTO lamination_compatibility (foil_series, interface_type_id, post_processing_step_id, lamination_material_id, compatibility) VALUES

-- SY6- 系列 (假设属于粉紙面+普通油墨类别)
('SY6-', 1, 1, 1, 'V'),  -- 無任何加工 + 普通預塗菲林-光膠
('SY6-', 1, 1, 2, 'V'),  -- 無任何加工 + 普通預塗菲林-啞膠
('SY6-', 1, 1, 3, 'V'),  -- 無任何加工 + 平價耐磨啞膠
('SY6-', 1, 1, 4, 'V'),  -- 無任何加工 + 超粘預塗菲林-光膠
('SY6-', 1, 1, 5, 'V'),  -- 無任何加工 + 超粘預塗菲林-啞膠
('SY6-', 1, 1, 6, 'V'),  -- 無任何加工 + 高價耐磨啞膠
('SY6-', 1, 1, 7, 'V'),  -- 無任何加工 + 預塗柔感膠
('SY6-', 1, 1, 8, 'V'),  -- 無任何加工 + 抗靜電光膠
('SY6-', 1, 1, 9, 'V'),  -- 無任何加工 + 抗靜電啞膠
('SY6-', 1, 1, 10, 'V'), -- 無任何加工 + 含相逢水性

-- SY6- 系列 + 擊凹/凸
('SY6-', 1, 2, 1, 'V'),  -- 擊凹/凸 + 普通預塗菲林-光膠
('SY6-', 1, 2, 2, 'V'),  -- 擊凹/凸 + 普通預塗菲林-啞膠
('SY6-', 1, 2, 3, 'V'),  -- 擊凹/凸 + 平價耐磨啞膠
('SY6-', 1, 2, 4, 'V'),  -- 擊凹/凸 + 超粘預塗菲林-光膠
('SY6-', 1, 2, 5, 'V'),  -- 擊凹/凸 + 超粘預塗菲林-啞膠
('SY6-', 1, 2, 6, 'V'),  -- 擊凹/凸 + 高價耐磨啞膠
('SY6-', 1, 2, 7, 'V'),  -- 擊凹/凸 + 預塗柔感膠
('SY6-', 1, 2, 8, 'V'),  -- 擊凹/凸 + 抗靜電光膠
('SY6-', 1, 2, 9, 'V'),  -- 擊凹/凸 + 抗靜電啞膠
('SY6-', 1, 2, 10, 'V'), -- 擊凹/凸 + 含相逢水性

-- SY6- 系列 + 啤折切
('SY6-', 1, 3, 1, 'V'),  -- 啤折切 + 普通預塗菲林-光膠
('SY6-', 1, 3, 2, 'V'),  -- 啤折切 + 普通預塗菲林-啞膠
('SY6-', 1, 3, 3, 'V'),  -- 啤折切 + 平價耐磨啞膠
('SY6-', 1, 3, 4, 'V'),  -- 啤折切 + 超粘預塗菲林-光膠
('SY6-', 1, 3, 5, 'V'),  -- 啤折切 + 超粘預塗菲林-啞膠
('SY6-', 1, 3, 6, 'V'),  -- 啤折切 + 高價耐磨啞膠
('SY6-', 1, 3, 7, 'V'),  -- 啤折切 + 預塗柔感膠
('SY6-', 1, 3, 8, 'V'),  -- 啤折切 + 抗靜電光膠
('SY6-', 1, 3, 9, 'V'),  -- 啤折切 + 抗靜電啞膠
('SY6-', 1, 3, 10, 'V'), -- 啤折切 + 含相逢水性

-- SY6- 系列 + 踩坑位
('SY6-', 1, 4, 1, 'V'),  -- 踩坑位 + 普通預塗菲林-光膠
('SY6-', 1, 4, 2, 'V'),  -- 踩坑位 + 普通預塗菲林-啞膠
('SY6-', 1, 4, 3, 'V'),  -- 踩坑位 + 平價耐磨啞膠
('SY6-', 1, 4, 4, 'V'),  -- 踩坑位 + 超粘預塗菲林-光膠
('SY6-', 1, 4, 5, 'V'),  -- 踩坑位 + 超粘預塗菲林-啞膠
('SY6-', 1, 4, 6, 'V'),  -- 踩坑位 + 高價耐磨啞膠
('SY6-', 1, 4, 7, 'V'),  -- 踩坑位 + 預塗柔感膠
('SY6-', 1, 4, 8, 'V'),  -- 踩坑位 + 抗靜電光膠
('SY6-', 1, 4, 9, 'V'),  -- 踩坑位 + 抗靜電啞膠
('SY6-', 1, 4, 10, 'V'), -- 踩坑位 + 含相逢水性

-- SY6- 系列 + 壓紋
('SY6-', 1, 5, 1, 'V'),  -- 壓紋 + 普通預塗菲林-光膠
('SY6-', 1, 5, 2, 'V'),  -- 壓紋 + 普通預塗菲林-啞膠
('SY6-', 1, 5, 3, 'V'),  -- 壓紋 + 平價耐磨啞膠
('SY6-', 1, 5, 4, 'V'),  -- 壓紋 + 超粘預塗菲林-光膠
('SY6-', 1, 5, 5, 'V'),  -- 壓紋 + 超粘預塗菲林-啞膠
('SY6-', 1, 5, 6, 'V'),  -- 壓紋 + 高價耐磨啞膠
('SY6-', 1, 5, 7, 'V'),  -- 壓紋 + 預塗柔感膠
('SY6-', 1, 5, 8, 'V'),  -- 壓紋 + 抗靜電光膠
('SY6-', 1, 5, 9, 'V'),  -- 壓紋 + 抗靜電啞膠
('SY6-', 1, 5, 10, 'V'); -- 壓紋 + 含相逢水性

-- 注意：以上只是示例数据，实际使用时需要根据完整的表格数据补充所有烫金纸系列的兼容性记录
-- 包括：SY1-, SY+, SB, SA, S19, S16, LL, LA, L817, KM, KB, KA, G6, G1, E8, A23 等系列

-- 创建索引以提高查询性能
CREATE INDEX idx_lamination_compatibility_foil_series ON lamination_compatibility(foil_series);
CREATE INDEX idx_lamination_compatibility_interface_type ON lamination_compatibility(interface_type_id);
CREATE INDEX idx_lamination_compatibility_post_processing ON lamination_compatibility(post_processing_step_id);
CREATE INDEX idx_lamination_compatibility_lamination_material ON lamination_compatibility(lamination_material_id);
CREATE INDEX idx_lamination_compatibility_compatibility ON lamination_compatibility(compatibility);
