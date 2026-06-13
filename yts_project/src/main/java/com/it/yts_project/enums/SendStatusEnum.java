package com.it.yts_project.enums;

/**
 * 消息发布状态枚举
 */
public enum SendStatusEnum {
    /**
     * 未发布（草稿）
     */
    DRAFT(0, "未发布"),

    /**
     * 已发布
     */
    PUBLISHED(1, "已发布"),

    /**
     * 已撤回
     */
    REVOKED(2, "已撤回");

    private final Integer code;
    private final String desc;

    SendStatusEnum(Integer code, String desc) {
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
    public static SendStatusEnum fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (SendStatusEnum value : SendStatusEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 判断是否为草稿
     */
    public boolean isDraft() {
        return this == DRAFT;
    }

    /**
     * 判断是否为已发布
     */
    public boolean isPublished() {
        return this == PUBLISHED;
    }

    /**
     * 判断是否为已撤回
     */
    public boolean isRevoked() {
        return this == REVOKED;
    }
}

