package com.it.yts_project.enums;

/**
 * 适用性枚举
 * 用于表示某个特性是否适用
 */
public enum ApplicabilityEnum {
    /**
     * 适用
     */
    APPLICABLE("V"),

    /**
     * 不适用
     */
    NOT_APPLICABLE("X"),

    /**
     * 不确定
     */
    UNCERTAIN("NA_Enum"),

    /**
     * 需要作「丝印打底处理」再烫金
     */
    REQUIRES_SILK_SCREEN_PROCESSING("▷"); // ▷ 是→的Unicode编码

    private final String code;

    ApplicabilityEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    /**
     * 根据代码获取枚举值
     * @param code 代码
     * @return 枚举值，如果找不到则返回null
     */
    public static ApplicabilityEnum fromCode(String code) {
        if (code == null) {
            return null;
        }

        for (ApplicabilityEnum value : ApplicabilityEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
