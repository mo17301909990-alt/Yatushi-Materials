package com.it.yts_project.util;

/**
 * 当前请求的「变更详情」挂载点，供 Controller/Service 写入，AOP 记录日志时读取并落库。
 * 便于管理员在操作日志中看到「从 XXX 修改为 XXX」等具体内容。
 */
public final class ChangeDetailHolder {

    private static final ThreadLocal<String> DETAIL = new ThreadLocal<>();

    public static void attach(String detail) {
        DETAIL.set(detail);
    }

    /**
     * 取出并清除，避免线程复用污染
     */
    public static String getAndClear() {
        try {
            return DETAIL.get();
        } finally {
            DETAIL.remove();
        }
    }
}
