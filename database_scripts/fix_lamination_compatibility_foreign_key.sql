-- ==============================================
-- 修复过胶兼容性表外键约束问题
-- 允许 post_processing_step_id 为 NULL
-- ==============================================

-- 1. 删除现有的外键约束
ALTER TABLE lamination_compatibility 
DROP CONSTRAINT IF EXISTS fk_lamination_compatibility_post_processing;

-- 2. 修改字段允许为 NULL
ALTER TABLE lamination_compatibility 
ALTER COLUMN post_processing_step_id DROP NOT NULL;

-- 3. 重新添加外键约束（允许 NULL 值）
ALTER TABLE lamination_compatibility 
ADD CONSTRAINT fk_lamination_compatibility_post_processing 
FOREIGN KEY (post_processing_step_id) REFERENCES post_processing_options(id) ON DELETE CASCADE;

-- 4. 确保 post_processing_options 表有数据
-- 如果表为空，插入默认数据
INSERT INTO post_processing_options (step_name, step_code, display_order, is_active, description) VALUES
('無任何加工', 'NO_PROCESSING', 1, TRUE, '不进行任何后加工处理'),
('過膠', 'LAMINATING', 2, TRUE, '过胶工艺'),
('擊凹/凸、絲印UV+擊凹/凸', 'EMBOSS_DEBOSS_UV', 3, TRUE, '击凹/凸工艺，可配合丝印UV'),
('啤折切、絲印UV+啤折切', 'DIE_CUT_FOLD_UV', 4, TRUE, '啤折切工艺，可配合丝印UV'),
('踩坑位、絲印UV+踩坑位', 'CREASING_UV', 5, TRUE, '踩坑位工艺，可配合丝印UV'),
('壓紋', 'EMBOSSING', 6, TRUE, '压纹工艺')
ON CONFLICT (step_name) DO NOTHING;

-- 5. 确保 interface_type_options 表有数据
INSERT INTO interface_type_options (interface_name, interface_code, display_order, is_active, description) VALUES
('粉紙面+普通油墨', 'POWDER_PAPER_NORMAL_INK', 1, TRUE, '粉紙面配合普通油墨印刷'),
('粉紙面+LED UV墨', 'POWDER_PAPER_LED_UV_INK', 2, TRUE, '粉紙面配合LED UV墨印刷'),
('粉紙面+快乾油墨', 'POWDER_PAPER_QUICK_DRY_INK', 3, TRUE, '粉紙面配合快乾油墨印刷')
ON CONFLICT (interface_name) DO NOTHING;

-- 6. 确保 lamination_material_options 表有数据
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
('水性菲林-光膠', 'PRECOATED_SOFT_TOUCH_GLOSS', 7, TRUE, '水性菲林-光胶', NULL),
('水性菲林-啞膠', 'PRECOATED_SOFT_TOUCH_MATTE', 8, TRUE, '水性菲林-哑胶', NULL),
-- 特殊菲林
('預塗柔感膠(含預塗、水性)', 'ANTI_STATIC_GLOSS', 9, TRUE, '特殊菲林-預塗柔感膠(含預塗、水性)', NULL),
('抗靜電光/啞膠(含預塗、水性)', 'ANTI_STATIC_MATTE', 10, TRUE, '特殊菲林-抗靜電光/啞膠(含預塗、水性)', NULL)
ON CONFLICT (material_name) DO NOTHING;

-- 7. 添加注释
COMMENT ON COLUMN lamination_compatibility.post_processing_step_id IS '后加工步骤ID（关联post_processing_options表，可为NULL表示无后加工）';
