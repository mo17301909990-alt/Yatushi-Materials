-- ============================================
-- 资源组选择系统 - 数据库表结构
-- 创建时间: 2025-11-27
-- 共5张表
-- ============================================

-- 删除已存在的表（按依赖顺序）
DROP TABLE IF EXISTS rg_resource_rule_condition CASCADE;
DROP TABLE IF EXISTS rg_resource_rule_header CASCADE;
DROP TABLE IF EXISTS rg_resource_group_task_map CASCADE;
DROP TABLE IF EXISTS rg_resource_group CASCADE;
DROP TABLE IF EXISTS rg_task_definition CASCADE;

-- ============================================
-- 1. 任务定义表
-- ============================================
CREATE TABLE rg_task_definition (
    id SERIAL PRIMARY KEY,
    task_code VARCHAR(20) UNIQUE NOT NULL,      -- PM/TK/SS/GL/BP
    task_name VARCHAR(100) NOT NULL,            -- 啤机/烫金/丝印/过胶/裱纸
    description TEXT,
    sort_order INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE rg_task_definition IS '任务定义表';
COMMENT ON COLUMN rg_task_definition.task_code IS '任务编码';
COMMENT ON COLUMN rg_task_definition.task_name IS '任务名称';

-- ============================================
-- 2. 资源组主表
-- ============================================
CREATE TABLE rg_resource_group (
    id SERIAL PRIMARY KEY,
    resource_group_code VARCHAR(50) UNIQUE NOT NULL,  -- WC08-DCT028
    name VARCHAR(200) NOT NULL,                       -- 自動啤機
    work_center_code VARCHAR(20) NOT NULL,            -- WC08
    work_center_name VARCHAR(100),                    -- 啤机
    family VARCHAR(50),                               -- 賀咭/拼圖/空
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE rg_resource_group IS '资源组主表';
COMMENT ON COLUMN rg_resource_group.resource_group_code IS '资源组编码';
COMMENT ON COLUMN rg_resource_group.work_center_code IS '工作中心编码';
COMMENT ON COLUMN rg_resource_group.family IS '家族分类';

CREATE INDEX idx_rg_resource_group_work_center ON rg_resource_group(work_center_code);
CREATE INDEX idx_rg_resource_group_active ON rg_resource_group(is_active);

-- ============================================
-- 3. 资源组-任务映射表
-- ============================================
CREATE TABLE rg_resource_group_task_map (
    id SERIAL PRIMARY KEY,
    resource_group_id INTEGER NOT NULL REFERENCES rg_resource_group(id) ON DELETE CASCADE,
    task_code VARCHAR(20) NOT NULL REFERENCES rg_task_definition(task_code),
    support_type VARCHAR(20) DEFAULT 'YES',   -- YES / CONDITIONAL
    remark TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(resource_group_id, task_code)
);

COMMENT ON TABLE rg_resource_group_task_map IS '资源组-任务映射表';
COMMENT ON COLUMN rg_resource_group_task_map.support_type IS '支持类型: YES=支持, CONDITIONAL=有条件支持';

CREATE INDEX idx_rg_task_map_resource_group ON rg_resource_group_task_map(resource_group_id);
CREATE INDEX idx_rg_task_map_task_code ON rg_resource_group_task_map(task_code);

-- ============================================
-- 4. 规则头表
-- ============================================
CREATE TABLE rg_resource_rule_header (
    id SERIAL PRIMARY KEY,
    resource_group_id INTEGER NOT NULL REFERENCES rg_resource_group(id) ON DELETE CASCADE,
    rule_type VARCHAR(30) NOT NULL,           -- STRUCT_RULES / TEXT_RULES
    rule_name VARCHAR(200),                   -- 规则名称
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE rg_resource_rule_header IS '规则头表';
COMMENT ON COLUMN rg_resource_rule_header.rule_type IS '规则类型: STRUCT_RULES=结构化规则, TEXT_RULES=文本规则';

CREATE INDEX idx_rg_rule_header_resource_group ON rg_resource_rule_header(resource_group_id);
CREATE INDEX idx_rg_rule_header_type ON rg_resource_rule_header(rule_type);

-- ============================================
-- 5. 规则条件表
-- ============================================
CREATE TABLE rg_resource_rule_condition (
    id SERIAL PRIMARY KEY,
    rule_header_id INTEGER NOT NULL REFERENCES rg_resource_rule_header(id) ON DELETE CASCADE,
    condition_group VARCHAR(50),              -- 条件分组（同组内AND，不同组OR）
    param_code VARCHAR(50),                   -- 参数编码: sheet_count/thickness/gsm/width/height
    param_name VARCHAR(100),                  -- 参数名称: 印张石数/厚度/克重/宽度/高度
    operator VARCHAR(20),                     -- 运算符: >=/<=/BETWEEN
    value_min DECIMAL(15,2),                  -- 最小值（NULL表示无下限）
    value_max DECIMAL(15,2),                  -- 最大值（NULL表示无上限）
    value_text TEXT,                          -- 文本值（用于TEXT_RULES存原文）
    rule_type VARCHAR(30) NOT NULL,           -- STRUCT_RULE / TEXT_RULE
    is_blocking BOOLEAN DEFAULT FALSE,        -- 是否阻塞规则（暂不上机原因）
    sort_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE rg_resource_rule_condition IS '规则条件表';
COMMENT ON COLUMN rg_resource_rule_condition.param_code IS '参数编码';
COMMENT ON COLUMN rg_resource_rule_condition.value_min IS '最小值，NULL表示无下限';
COMMENT ON COLUMN rg_resource_rule_condition.value_max IS '最大值，NULL表示无上限';
COMMENT ON COLUMN rg_resource_rule_condition.value_text IS '文本规则原文';
COMMENT ON COLUMN rg_resource_rule_condition.is_blocking IS '是否为阻塞规则（暂不上机原因）';

CREATE INDEX idx_rg_rule_condition_header ON rg_resource_rule_condition(rule_header_id);
CREATE INDEX idx_rg_rule_condition_param ON rg_resource_rule_condition(param_code);
CREATE INDEX idx_rg_rule_condition_type ON rg_resource_rule_condition(rule_type);

