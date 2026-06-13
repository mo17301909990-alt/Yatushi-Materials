-- 创建耐磨金纸映射「跳过 Match 耐磨规则」配置表
-- 用于按烫金纸类型（product.hot_stamping_paper_type）配置在 Match 烫金筛选中跳过耐磨金纸映射规则

CREATE TABLE IF NOT EXISTS wear_resistant_gold_paper_skip_config (
    id SERIAL PRIMARY KEY,
    hot_stamping_paper_type VARCHAR(255) NOT NULL UNIQUE,
    remarks TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE wear_resistant_gold_paper_skip_config IS '耐磨金纸映射跳过配置表：按烫金纸类型在 Match 中跳过耐磨映射';
COMMENT ON COLUMN wear_resistant_gold_paper_skip_config.id IS '主键ID';
COMMENT ON COLUMN wear_resistant_gold_paper_skip_config.hot_stamping_paper_type IS '烫金纸类型（对应 product.hot_stamping_paper_type）';
COMMENT ON COLUMN wear_resistant_gold_paper_skip_config.remarks IS '备注';
COMMENT ON COLUMN wear_resistant_gold_paper_skip_config.created_at IS '创建时间';
COMMENT ON COLUMN wear_resistant_gold_paper_skip_config.updated_at IS '更新时间';

CREATE INDEX IF NOT EXISTS idx_wrgp_skip_paper_type
    ON wear_resistant_gold_paper_skip_config(hot_stamping_paper_type);



