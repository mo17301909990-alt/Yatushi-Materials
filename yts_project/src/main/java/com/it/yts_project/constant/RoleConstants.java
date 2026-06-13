package com.it.yts_project.constant;

import java.util.Arrays;
import java.util.List;

/**
 * 角色相关常量
 */
public class RoleConstants {
    
    /**
     * 超级管理员角色key
     */
    public static final String ROLE_ADMIN = "ADMIN";
    
    /**
     * 系统管理员角色key
     */
    public static final String ROLE_SYSTEM = "OPERATOR";
    
    /**
     * 普通用户角色key
     */
    public static final String ROLE_USER = "USER";
    
    /**
     * 管理员角色列表（用于查询所有管理员用户）
     * 如需添加其他管理员角色，只需在此列表中添加对应的 role_key
     */
    public static final List<String> ADMIN_ROLE_KEYS = Arrays.asList(
        ROLE_ADMIN,
        ROLE_SYSTEM
    );
    
    private RoleConstants() {
        // 私有构造函数，防止实例化
    }
}

