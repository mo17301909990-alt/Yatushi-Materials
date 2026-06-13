package com.it.yts_project.enums;

/**
 * 消息发送范围枚举
 */
public enum MsgTypeEnum {
    /**
     * 全体用户
     */
    ALL("ALL", "全体用户"),

    /**
     * 指定用户
     */
    USER("USER", "指定用户");

    private final String code;
    private final String desc;

    MsgTypeEnum(String code, String desc) {
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
    public static MsgTypeEnum fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (MsgTypeEnum value : MsgTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 判断是否为全体用户
     */
    public boolean isAll() {
        return this == ALL;
    }

    /**
     * 判断是否为指定用户
     */
    public boolean isUser() {
        return this == USER;
    }
}

