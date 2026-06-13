package com.it.yts_project.enums;

/**
 * 消息优先级枚举
 */
public enum PriorityEnum {
    /**
     * 低优先级
     */
    LOW("LOW", "低"),

    /**
     * 普通优先级
     */
    NORMAL("NORMAL", "普通"),

    /**
     * 高优先级（重要）
     */
    HIGH("HIGH", "重要");

    private final String code;
    private final String desc;

    PriorityEnum(String code, String desc) {
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
    public static PriorityEnum fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (PriorityEnum value : PriorityEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 判断是否为高优先级
     */
    public boolean isHigh() {
        return this == HIGH;
    }
}

