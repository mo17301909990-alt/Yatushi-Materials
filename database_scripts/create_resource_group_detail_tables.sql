-- ============================================
-- 资源组选择系统 - 工作中心与资源组详情表
-- ============================================

-- ============================================
-- 1. 工作中心主表
-- ============================================
CREATE TABLE rg_work_center (
    id SERIAL PRIMARY KEY,
    work_center_code VARCHAR(20) UNIQUE NOT NULL,   -- WC05 / WC08 / WC10 ...
    work_center_name VARCHAR(100) NOT NULL,         -- 烫金 / 啤机 / 裱纸 ...
    department_name VARCHAR(100),                   -- 所属事业部/车间（可选）
    description TEXT,                               -- 工作中心说明
    remark TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE rg_work_center IS '工作中心主表';
COMMENT ON COLUMN rg_work_center.work_center_code IS '工作中心编码（如 WC08）';
COMMENT ON COLUMN rg_work_center.work_center_name IS '工作中心名称（如 啤机）';

-- ============================================
-- 2. 资源组技术参数详情表
-- ============================================
CREATE TABLE rg_resource_group_detail (
    id SERIAL PRIMARY KEY,
    resource_group_id INTEGER NOT NULL
        REFERENCES rg_resource_group(id) ON DELETE CASCADE,

    -- 尺寸能力
    max_width_mm  DECIMAL(10,2),         -- 最大宽度（mm）
    max_height_mm DECIMAL(10,2),         -- 最大高度（mm）
    min_width_mm  DECIMAL(10,2),         -- 最小宽度（mm）
    min_height_mm DECIMAL(10,2),         -- 最小高度（mm）

    -- 厚度 / 克重能力
    min_thickness_mm DECIMAL(10,3),      -- 最小厚度（mm）
    max_thickness_mm DECIMAL(10,3),      -- 最大厚度（mm）
    min_gsm         DECIMAL(10,2),       -- 最小克重（g/m2）
    max_gsm         DECIMAL(10,2),       -- 最大克重（g/m2）

    -- 速度与设备参数
    min_speed_sheet_per_hour DECIMAL(10,2),  -- 最低生产速度（张/小时）
    max_speed_sheet_per_hour DECIMAL(10,2),  -- 最高生产速度（张/小时）
    design_speed_sheet_per_hour DECIMAL(10,2), -- 设计极限速度（张/小时）

    setup_time_min DECIMAL(10,2),        -- 标准换单/上机准备时间（分钟）
    changeover_time_min DECIMAL(10,2),   -- 机种切换时间（分钟，可选）

    -- 其他技术说明
    machine_model VARCHAR(100),          -- 机型型号（如 BOBST 106E）
    manufacturer VARCHAR(100),           -- 设备厂家
    year_of_make VARCHAR(10),            -- 设备年份（可选）
    capability_remark TEXT,              -- 能力说明（如 是否支持清废、是否支持连线）

    -- 预留扩展（存放 Excel 里一些零散字段，用 JSONB 比较灵活）
    extra_params JSONB,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE rg_resource_group_detail IS '资源组技术参数详情表';
COMMENT ON COLUMN rg_resource_group_detail.max_width_mm IS '最大生产宽度(mm)';
COMMENT ON COLUMN rg_resource_group_detail.max_height_mm IS '最大生产高度(mm)';
COMMENT ON COLUMN rg_resource_group_detail.min_thickness_mm IS '最小纸张厚度(mm)';
COMMENT ON COLUMN rg_resource_group_detail.max_thickness_mm IS '最大纸张厚度(mm)';
COMMENT ON COLUMN rg_resource_group_detail.min_gsm IS '最小克重(g/m2)';
COMMENT ON COLUMN rg_resource_group_detail.max_gsm IS '最大克重(g/m2)';
COMMENT ON COLUMN rg_resource_group_detail.max_speed_sheet_per_hour IS '最高生产速度(张/小时)';

-- ============================================
-- 3. 资源组产能表
-- ============================================
CREATE TABLE rg_resource_group_capacity (
    id SERIAL PRIMARY KEY,
    resource_group_id INTEGER NOT NULL
        REFERENCES rg_resource_group(id) ON DELETE CASCADE,

    capacity_mode VARCHAR(50) NOT NULL,         -- 产能模式：如 'STANDARD', 'OT', 'WEEKDAY', 'WEEKEND'
    shift_name VARCHAR(50),                     -- 班次名称：如 '白班', '夜班'
    work_hours_per_shift DECIMAL(10,2),         -- 每班工作小时数
    shifts_per_day DECIMAL(10,2),               -- 每天班次数（如 1, 1.5, 2）

    -- 产能（可以根据 Excel 按你习惯的单位存）
    capacity_sheet_per_hour DECIMAL(15,2),      -- 标准产能：张/小时
    capacity_sqm_per_hour   DECIMAL(15,2),      -- 标准产能：平方米/小时（可选）
    capacity_sheet_per_day  DECIMAL(15,2),      -- 折算日产能：张/天
    capacity_sqm_per_day    DECIMAL(15,2),      -- 折算日产能：平方米/天

    utilization_rate DECIMAL(5,2),              -- 稼动率/利用率（%），例如 80.00

    effective_from DATE,                        -- 生效起始日期
    effective_to   DATE,                        -- 生效结束日期（可 NULL 表示长期）

    remark TEXT,                                -- 例如"按单班制测算"，"含换单时间"等说明

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE rg_resource_group_capacity IS '资源组产能参数表';
COMMENT ON COLUMN rg_resource_group_capacity.capacity_mode IS '产能模式，如 STANDARD/OT 等';
COMMENT ON COLUMN rg_resource_group_capacity.capacity_sheet_per_hour IS '标准产能(张/小时)';
COMMENT ON COLUMN rg_resource_group_capacity.capacity_sheet_per_day IS '标准产能(张/天)';

-- ============================================
-- 4. 给 rg_resource_group 添加外键约束（可选）
-- ============================================
-- 如果需要更严谨的约束，可以执行：
-- ALTER TABLE rg_resource_group
--     ADD CONSTRAINT fk_rg_group_work_center
--     FOREIGN KEY (work_center_code)
--     REFERENCES rg_work_center(work_center_code);

-- ============================================
-- 5. 创建索引
-- ============================================
CREATE INDEX idx_rg_work_center_code ON rg_work_center(work_center_code);
CREATE INDEX idx_rg_resource_group_detail_rg_id ON rg_resource_group_detail(resource_group_id);
CREATE INDEX idx_rg_resource_group_capacity_rg_id ON rg_resource_group_capacity(resource_group_id);

