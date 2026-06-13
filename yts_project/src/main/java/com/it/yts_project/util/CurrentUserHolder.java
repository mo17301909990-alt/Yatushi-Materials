package com.it.yts_project.util;

/**
 * 当前请求用户上下文（由前端请求头 X-User-Id / X-User-Name 注入，供操作日志等使用）
 */
public final class CurrentUserHolder {

    private static final ThreadLocal<Integer> OPERATOR_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> OPERATOR_NAME = new ThreadLocal<>();

    public static void set(Integer operatorId, String operatorName) {
        OPERATOR_ID.set(operatorId);
        OPERATOR_NAME.set(operatorName);
    }

    public static Integer getOperatorId() {
        return OPERATOR_ID.get();
    }

    public static String getOperatorName() {
        return OPERATOR_NAME.get();
    }

    public static void clear() {
        OPERATOR_ID.remove();
        OPERATOR_NAME.remove();
    }
}
