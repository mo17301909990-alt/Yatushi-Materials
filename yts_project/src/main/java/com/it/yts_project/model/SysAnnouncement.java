package com.it.yts_project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 系统消息实体类（JeecgBoot 标准）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysAnnouncement {
    private String id;              // 主键ID
    private String title;            // 公告标题
    private String msgContent;       // 消息内容
    private String sender;           // 发送人
    private String priority;         // 优先级（LOW/NORMAL/HIGH）
    private String msgCategory;      // 消息类型：1-通知公告，2-系统消息
    private String msgType;          // 发布类型：ALL-全体用户，USER-指定用户
    private Integer sendStatus;      // 发布状态：0-未发布，1-已发布，2-撤回
    private Date sendTime;           // 发布时间
    private Date startTime;          // 生效开始时间
    private Date endTime;            // 生效结束时间
    private String busType;          // 业务类型（用于跳转）
    private String busId;            // 业务ID（用于跳转）
    private String userIds;          // 指定用户ID列表，逗号分隔
    private String createBy;         // 创建人
    private Date createTime;          // 创建时间
    private String updateBy;          // 更新人
    private Date updateTime;         // 更新时间
    private Integer readFlag;         // 阅读状态：0-未读，1-已读（仅用于查询结果，不存储在 sys_announcement 表中）
    private Date readTime;            // 阅读时间（仅用于查询结果，不存储在 sys_announcement 表中）
}

