-- ============================================================
-- 管理员自修改 Agent 变更管理表
-- 变更记录 + 数据快照
-- ============================================================

-- 1. 数据快照表（变更前的原始数据备份）
CREATE TABLE IF NOT EXISTS admin_change_snapshots (
    id              BIGSERIAL       PRIMARY KEY,
    entity_type     VARCHAR(100)    NOT NULL,
    entity_id       BIGINT          NOT NULL,
    before_data     JSONB           NOT NULL,
    after_data      JSONB,
    checksum        VARCHAR(64),
    created_at      TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    created_by      INTEGER         NOT NULL,
    CONSTRAINT fk_snapshot_operator FOREIGN KEY (created_by)
        REFERENCES public.users(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_snapshots_entity
    ON admin_change_snapshots(entity_type, entity_id);
CREATE INDEX IF NOT EXISTS idx_snapshots_created_by
    ON admin_change_snapshots(created_by);

COMMENT ON TABLE admin_change_snapshots IS '管理员变更数据快照 — 变更前/后完整数据，用于回滚和审计';
COMMENT ON COLUMN admin_change_snapshots.entity_type IS '实体类型，如 pricing、specification、product、gold_foil_type 等';
COMMENT ON COLUMN admin_change_snapshots.before_data IS '变更前的完整数据 JSON';
COMMENT ON COLUMN admin_change_snapshots.after_data IS '变更后的完整数据 JSON（execute 时写入）';
COMMENT ON COLUMN admin_change_snapshots.checksum IS 'before_data 的 SHA-256，回滚时校验数据未被意外修改';

-- 2. 变更记录表（每次变更操作的主记录）
CREATE TABLE IF NOT EXISTS admin_change_records (
    id              BIGSERIAL       PRIMARY KEY,
    entity_type     VARCHAR(100)    NOT NULL,
    entity_id       BIGINT          NOT NULL,
    change_set      JSONB           NOT NULL,
    snapshot_ids    BIGINT[]        NOT NULL DEFAULT '{}',
    operator_id     INTEGER         NOT NULL,
    operator_name   VARCHAR(100),
    risk            VARCHAR(20)     NOT NULL DEFAULT 'low',
    reason          TEXT            NOT NULL DEFAULT '',
    status          VARCHAR(20)     NOT NULL DEFAULT 'pending',
    created_at      TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    committed_at    TIMESTAMPTZ,
    rolled_back_at  TIMESTAMPTZ,
    rollback_record_id BIGINT,
    error_message   TEXT,
    CONSTRAINT fk_change_operator FOREIGN KEY (operator_id)
        REFERENCES public.users(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_change_records_entity
    ON admin_change_records(entity_type, entity_id);
CREATE INDEX IF NOT EXISTS idx_change_records_operator
    ON admin_change_records(operator_id);
CREATE INDEX IF NOT EXISTS idx_change_records_status
    ON admin_change_records(status);
CREATE INDEX IF NOT EXISTS idx_change_records_created_at
    ON admin_change_records(created_at DESC);

COMMENT ON TABLE admin_change_records IS '管理员变更操作记录 — 每次修改对应一条记录，支持回滚';
COMMENT ON COLUMN admin_change_records.change_set IS '变更字段明细 JSON';
COMMENT ON COLUMN admin_change_records.snapshot_ids IS '关联的快照 ID 列表';
COMMENT ON COLUMN admin_change_records.risk IS '风险评估：low/medium/high';
COMMENT ON COLUMN admin_change_records.status IS '状态：pending/committed/rolled_back/failed';
COMMENT ON COLUMN admin_change_records.rollback_record_id IS '回滚操作产生的记录 ID，用于追溯回滚链路';
