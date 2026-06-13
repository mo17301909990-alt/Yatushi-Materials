package com.it.yts_project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户消息关联实体类（JeecgBoot 标准）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysAnnouncementSend {
    private String id;          // 主键ID
    private String anntId;      // 消息ID（关联sys_announcement.id）
    private Integer userId;     // 用户ID（关联users.id）
    private Integer readFlag;   // 是否已读：0-未读，1-已读
    private Date readTime;      // 阅读时间
    private Date createTime;    // 创建时间
}

