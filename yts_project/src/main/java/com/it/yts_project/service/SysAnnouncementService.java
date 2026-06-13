package com.it.yts_project.service;

import com.it.yts_project.model.SysAnnouncement;

import java.util.List;
import java.util.Map;

public interface SysAnnouncementService {

    /**
     * 保存消息（草稿）
     */
    void save(SysAnnouncement announcement);

    /**
     * 更新消息（草稿或已发布，管理员可以编辑已发布的公告）
     */
    void update(SysAnnouncement announcement);

    /**
     * 删除消息（仅草稿或已撤回）
     */
    void delete(String id);

    /**
     * 根据ID查询消息
     */
    SysAnnouncement getById(String id);

    /**
     * 查询消息列表（管理员）
     */
    Map<String, Object> list(String title, String msgCategory, Integer sendStatus, Integer pageNo, Integer pageSize);

    /**
     * 发布消息（生成 delivery，批量插入）
     */
    Map<String, Object> doRelease(String id);

    /**
     * 撤回消息
     */
    void doRevoke(String id);

    /**
     * 查询用户消息列表
     */
    Map<String, Object> listByUser(Integer userId, String msgCategory, Integer readFlag, String startTime, String endTime, Integer pageNo, Integer pageSize);

    /**
     * 获取用户未读消息数量
     */
    Integer getUnreadCount(Integer userId);

    /**
     * 标记消息为已读
     */
    void read(String id, Integer userId);

    /**
     * 批量标记为已读
     */
    void batchRead(List<String> ids, Integer userId);

    /**
     * 用户删除消息
     */
    void deleteByUser(String id, Integer userId);
}

