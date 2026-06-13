package com.it.yts_project.agent;

/**
 * 用户意图枚举。
 * <p>Agent 编排器根据此枚举路由到对应的 Skill。</p>
 *
 * <ul>
 *   <li>MATCH / QUERY / COMPARE / COMPATIBILITY — 只读查询意图</li>
 *   <li>ADMIN_MODIFY — 超级管理员写意图（仅 ADMIN 角色可用）</li>
 *   <li>FEEDBACK / GREETING / HELP — 辅助意图</li>
 * </ul>
 */
public enum Intent {

    /** 找产品 / 匹配 */
    MATCH,
    /** 查信息 / 查询 */
    QUERY,
    /** 对比产品 */
    COMPARE,
    /** 兼容性检查 */
    COMPATIBILITY,
    /** 管理员修改物料/价格/配置（仅超级管理员） */
    ADMIN_MODIFY,
    /** 反馈 / 评价结果 */
    FEEDBACK,
    /** 问候 */
    GREETING,
    /** 帮助信息 */
    HELP
}
