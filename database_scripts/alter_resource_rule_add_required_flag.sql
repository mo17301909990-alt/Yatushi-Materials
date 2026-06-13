-- ============================================
-- 为规则条件表添加 is_required 字段
-- 用于区分硬性规则（必须满足）和可选规则（满足其中一个即可）
-- ============================================

ALTER TABLE rg_resource_rule_condition 
ADD COLUMN IF NOT EXISTS is_required BOOLEAN DEFAULT TRUE;

COMMENT ON COLUMN rg_resource_rule_condition.is_required IS '是否为硬性规则：TRUE=必须满足（不满足就FAIL），FALSE=可选规则（满足就PASS，不满足但不违反阻塞规则时不影响判断）';

-- 更新现有规则：阻塞规则和尺寸/厚度等数值限制规则默认为硬性规则
UPDATE rg_resource_rule_condition 
SET is_required = TRUE 
WHERE is_required IS NULL;

