package com.it.yts_project.enums;

/**
 * 消息类型枚举
 */
public enum MsgCategoryEnum {
    /**
     * 通知公告
     */
    NOTICE("1", "通知公告"),

    /**
     * 系统消息
     */
    SYSTEM("2", "系统消息");

    private final String code;
    private final String desc;

    MsgCategoryEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 根据代码获取枚举值
     */
    public static MsgCategoryEnum fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (MsgCategoryEnum value : MsgCategoryEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 判断是否为通知公告
     */
    public boolean isNotice() {
        return this == NOTICE;
    }

    /**
     * 判断是否为系统消息
     */
    public boolean isSystem() {
        return this == SYSTEM;
    }
}

