package com.it.yts_project.enums;

/**
 * 消息阅读状态枚举
 */
public enum ReadFlagEnum {
    /**
     * 未读
     */
    UNREAD(0, "未读"),

    /**
     * 已读
     */
    READ(1, "已读");

    private final Integer code;
    private final String desc;

    ReadFlagEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 根据代码获取枚举值
     */
    public static ReadFlagEnum fromCode(Integer code) {
        if (code == null) {
            return UNREAD; // 默认为未读
        }
        for (ReadFlagEnum value : ReadFlagEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return UNREAD;
    }

    /**
     * 判断是否为未读
     */
    public boolean isUnread() {
        return this == UNREAD;
    }

    /**
     * 判断是否为已读
     */
    public boolean isRead() {
        return this == READ;
    }
}

