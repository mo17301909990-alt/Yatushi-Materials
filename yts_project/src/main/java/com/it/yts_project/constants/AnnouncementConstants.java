package com.it.yts_project.constants;

/**
 * 消息管理常量类
 * 统一管理消息相关的常量值，便于维护和扩展
 */
public class AnnouncementConstants {

    /**
     * 消息类型常量（对应 MsgCategoryEnum）
     */
    public static class MsgCategory {
        /** 通知公告 */
        public static final String NOTICE = "1";
        /** 系统消息 */
        public static final String SYSTEM = "2";
    }

    /**
     * 消息发送范围常量（对应 MsgTypeEnum）
     */
    public static class MsgType {
        /** 全体用户 */
        public static final String ALL = "ALL";
        /** 指定用户 */
        public static final String USER = "USER";
    }

    /**
     * 发布状态常量（对应 SendStatusEnum）
     */
    public static class SendStatus {
        /** 未发布（草稿） */
        public static final Integer DRAFT = 0;
        /** 已发布 */
        public static final Integer PUBLISHED = 1;
        /** 已撤回 */
        public static final Integer REVOKED = 2;
    }

    /**
     * 优先级常量（对应 PriorityEnum）
     */
    public static class Priority {
        /** 低优先级 */
        public static final String LOW = "LOW";
        /** 普通优先级 */
        public static final String NORMAL = "NORMAL";
        /** 高优先级（重要） */
        public static final String HIGH = "HIGH";
    }

    /**
     * 阅读状态常量（对应 ReadFlagEnum）
     */
    public static class ReadFlag {
        /** 未读 */
        public static final Integer UNREAD = 0;
        /** 已读 */
        public static final Integer READ = 1;
    }

    /**
     * 批量处理大小
     */
    public static final int BATCH_SIZE = 100;

    private AnnouncementConstants() {
        // 工具类，禁止实例化
    }
}

