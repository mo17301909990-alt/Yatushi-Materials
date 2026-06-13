package com.it.yts_project.service;

import com.it.yts_project.model.SysAnnouncementSend;

import java.util.List;

public interface SysAnnouncementSendService {

    /**
     * 批量插入用户消息关联
     */
    void batchInsert(List<SysAnnouncementSend> sendList);

    /**
     * 标记为已读
     */
    void markAsRead(String anntId, Integer userId);

    /**
     * 批量标记为已读
     */
    void batchMarkAsRead(List<String> anntIds, Integer userId);

    /**
     * 删除用户消息关联
     */
    void deleteByAnntIdAndUserId(String anntId, Integer userId);

    /**
     * 统计用户未读消息数
     */
    int countUnreadByUserId(Integer userId);
}

