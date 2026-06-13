-- ============================================
-- 站内通信系统数据库表结构（JeecgBoot 标准）
-- PostgreSQL 版本
-- ============================================

-- 1. 消息主表（sys_announcement）
CREATE TABLE IF NOT EXISTS sys_announcement (
    id            VARCHAR(32) PRIMARY KEY,
    title         VARCHAR(200) NOT NULL,
    msg_content   TEXT NOT NULL,
    sender        VARCHAR(50),
    priority      VARCHAR(10),
    msg_category  VARCHAR(10) DEFAULT '1',  -- 1:通知公告, 2:系统消息
    msg_type      VARCHAR(10) DEFAULT 'USER', -- ALL:全体用户, USER:指定用户
    send_status   INTEGER DEFAULT 0,  -- 0:未发布, 1:已发布, 2:撤回
    send_time     TIMESTAMP,
    start_time    TIMESTAMP,
    end_time      TIMESTAMP,
    bus_type      VARCHAR(64),
    bus_id        VARCHAR(64),
    user_ids      TEXT,  -- 指定用户ID列表，逗号分隔（msg_type='USER'时使用）
    create_by     VARCHAR(50),
    create_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_by     VARCHAR(50),
    update_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 索引
CREATE INDEX IF NOT EXISTS idx_send_status ON sys_announcement(send_status);
CREATE INDEX IF NOT EXISTS idx_msg_category ON sys_announcement(msg_category);
CREATE INDEX IF NOT EXISTS idx_start_time ON sys_announcement(start_time);
CREATE INDEX IF NOT EXISTS idx_end_time ON sys_announcement(end_time);
CREATE INDEX IF NOT EXISTS idx_create_time ON sys_announcement(create_time);
CREATE INDEX IF NOT EXISTS idx_send_status_time ON sys_announcement(send_status, start_time, end_time);

-- 添加注释
COMMENT ON TABLE sys_announcement IS '系统消息表（JeecgBoot标准）';
COMMENT ON COLUMN sys_announcement.id IS '主键ID';
COMMENT ON COLUMN sys_announcement.title IS '公告标题';
COMMENT ON COLUMN sys_announcement.msg_content IS '消息内容';
COMMENT ON COLUMN sys_announcement.sender IS '发送人';
COMMENT ON COLUMN sys_announcement.priority IS '优先级（LOW/NORMAL/HIGH）';
COMMENT ON COLUMN sys_announcement.msg_category IS '消息类型：1-通知公告，2-系统消息';
COMMENT ON COLUMN sys_announcement.msg_type IS '发布类型：ALL-全体用户，USER-指定用户';
COMMENT ON COLUMN sys_announcement.send_status IS '发布状态：0-未发布，1-已发布，2-撤回';
COMMENT ON COLUMN sys_announcement.send_time IS '发布时间';
COMMENT ON COLUMN sys_announcement.start_time IS '生效开始时间';
COMMENT ON COLUMN sys_announcement.end_time IS '生效结束时间';
COMMENT ON COLUMN sys_announcement.bus_type IS '业务类型（用于跳转）';
COMMENT ON COLUMN sys_announcement.bus_id IS '业务ID（用于跳转）';
COMMENT ON COLUMN sys_announcement.user_ids IS '指定用户ID列表，逗号分隔';
COMMENT ON COLUMN sys_announcement.create_by IS '创建人';
COMMENT ON COLUMN sys_announcement.create_time IS '创建时间';
COMMENT ON COLUMN sys_announcement.update_by IS '更新人';
COMMENT ON COLUMN sys_announcement.update_time IS '更新时间';

-- 2. 用户消息关联表（sys_announcement_send）
-- ⚠️ 重要：必须设置唯一键，避免并发死锁（JeecgBoot 3.7.1 的教训）
CREATE TABLE IF NOT EXISTS sys_announcement_send (
    id           VARCHAR(32) PRIMARY KEY,
    annt_id      VARCHAR(32) NOT NULL,
    user_id      INTEGER NOT NULL,  -- 关联 users.id
    read_flag    INTEGER DEFAULT 0,  -- 0:未读, 1:已读
    read_time    TIMESTAMP,
    create_time  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_annt_user UNIQUE (annt_id, user_id),  -- ⚠️ 必须的唯一键
    CONSTRAINT fk_annt_send_annt FOREIGN KEY (annt_id) REFERENCES sys_announcement(id) ON DELETE CASCADE,
    CONSTRAINT fk_annt_send_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 索引
CREATE INDEX IF NOT EXISTS idx_annt_id ON sys_announcement_send(annt_id);
CREATE INDEX IF NOT EXISTS idx_user_id ON sys_announcement_send(user_id);
CREATE INDEX IF NOT EXISTS idx_read_flag ON sys_announcement_send(read_flag);

-- 添加注释
COMMENT ON TABLE sys_announcement_send IS '用户消息关联表（JeecgBoot标准）';
COMMENT ON COLUMN sys_announcement_send.id IS '主键ID';
COMMENT ON COLUMN sys_announcement_send.annt_id IS '消息ID（关联sys_announcement.id）';
COMMENT ON COLUMN sys_announcement_send.user_id IS '用户ID（关联users.id）';
COMMENT ON COLUMN sys_announcement_send.read_flag IS '是否已读：0-未读，1-已读';
COMMENT ON COLUMN sys_announcement_send.read_time IS '阅读时间';
COMMENT ON COLUMN sys_announcement_send.create_time IS '创建时间';

-- 3. 创建更新时间触发器函数
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.update_time = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- 为 sys_announcement 表添加更新时间触发器
DROP TRIGGER IF EXISTS update_sys_announcement_updated_at ON sys_announcement;
CREATE TRIGGER update_sys_announcement_updated_at
    BEFORE UPDATE ON sys_announcement
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- 4. 插入测试数据（可选）
-- 插入一条示例公告
INSERT INTO sys_announcement (
    id, title, msg_content, sender, priority, msg_category, msg_type, 
    send_status, start_time, create_by, create_time
) VALUES (
    'test_announcement_001',
    '欢迎使用站内通信系统',
    '欢迎使用站内通信系统！您可以通过此系统接收公告、系统消息和提醒。',
    '系统管理员',
    'NORMAL',
    '1',
    'ALL',
    0,  -- 草稿状态
    CURRENT_TIMESTAMP,
    'admin',
    CURRENT_TIMESTAMP
) ON CONFLICT (id) DO NOTHING;

