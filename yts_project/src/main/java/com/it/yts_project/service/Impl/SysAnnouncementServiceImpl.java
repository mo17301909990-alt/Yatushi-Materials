package com.it.yts_project.service.Impl;

import com.it.yts_project.constants.AnnouncementConstants;
import com.it.yts_project.enums.MsgCategoryEnum;
import com.it.yts_project.enums.MsgTypeEnum;
import com.it.yts_project.enums.ReadFlagEnum;
import com.it.yts_project.enums.SendStatusEnum;
import com.it.yts_project.mapper.SysAnnouncementMapper;
import com.it.yts_project.mapper.SysAnnouncementSendMapper;
import com.it.yts_project.model.SysAnnouncement;
import com.it.yts_project.model.SysAnnouncementSend;
import com.it.yts_project.service.SysAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class SysAnnouncementServiceImpl implements SysAnnouncementService {

    @Autowired
    private SysAnnouncementMapper announcementMapper;

    @Autowired
    private SysAnnouncementSendMapper announcementSendMapper;

    @Override
    @Transactional
    public void save(SysAnnouncement announcement) {
        // 生成ID（使用UUID）
        if (announcement.getId() == null || announcement.getId().isEmpty()) {
            announcement.setId(UUID.randomUUID().toString().replace("-", ""));
        }
        // 默认状态为草稿
        if (announcement.getSendStatus() == null) {
            announcement.setSendStatus(SendStatusEnum.DRAFT.getCode());
        }
        announcementMapper.insert(announcement);
    }

    @Override
    @Transactional
    public void update(SysAnnouncement announcement) {
        SysAnnouncement existing = announcementMapper.selectById(announcement.getId());
        if (existing == null) {
            throw new RuntimeException("消息不存在");
        }
        // 允许更新草稿状态的消息，或者已发布的消息（管理员可以编辑已发布的公告）
        // 已撤回的消息不允许编辑
        if (SendStatusEnum.REVOKED.getCode().equals(existing.getSendStatus())) {
            throw new RuntimeException("已撤回的消息不能编辑");
        }
        // 更新消息内容，但保持原有的发布状态
        announcement.setSendStatus(existing.getSendStatus());
        announcement.setSendTime(existing.getSendTime());
        announcementMapper.updateById(announcement);
    }

    @Override
    @Transactional
    public void delete(String id) {
        SysAnnouncement announcement = announcementMapper.selectById(id);
        if (announcement == null) {
            throw new RuntimeException("消息不存在");
        }
        // 只能删除草稿或已撤回的消息
        if (SendStatusEnum.PUBLISHED.getCode().equals(announcement.getSendStatus())) {
            throw new RuntimeException("已发布的消息不能删除，请先撤回");
        }
        // 删除关联记录
        announcementSendMapper.deleteByAnntId(id);
        // 删除消息
        announcementMapper.deleteById(id);
    }

    @Override
    public SysAnnouncement getById(String id) {
        return announcementMapper.selectById(id);
    }

    @Override
    public Map<String, Object> list(String title, String msgCategory, Integer sendStatus, Integer pageNo, Integer pageSize) {
        Integer offset = (pageNo - 1) * pageSize;
        List<SysAnnouncement> records = announcementMapper.selectList(title, msgCategory, sendStatus, offset, pageSize);
        Integer total = announcementMapper.countList(title, msgCategory, sendStatus);

        Map<String, Object> result = new HashMap<>();
        result.put("records", records);
        result.put("total", total);
        result.put("size", pageSize);
        result.put("current", pageNo);
        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> doRelease(String id) {
        SysAnnouncement announcement = announcementMapper.selectById(id);
        if (announcement == null) {
            throw new RuntimeException("消息不存在");
        }
        if (SendStatusEnum.PUBLISHED.getCode().equals(announcement.getSendStatus())) {
            throw new RuntimeException("消息已发布");
        }

        // 更新消息状态
        announcement.setSendStatus(SendStatusEnum.PUBLISHED.getCode());
        announcement.setSendTime(new Date());
        announcementMapper.updateById(announcement);

        // 解析消息类型和发送范围
        MsgCategoryEnum msgCategory = MsgCategoryEnum.fromCode(announcement.getMsgCategory());
        MsgTypeEnum msgType = MsgTypeEnum.fromCode(announcement.getMsgType());

        // 所有消息类型都需要创建派发记录，以便跟踪用户读取状态
        int deliveryCount = 0;
        List<Integer> targetUserIds = new ArrayList<>();
        
        if (MsgTypeEnum.ALL.equals(msgType)) {
            // 全体用户
            targetUserIds = announcementMapper.selectAllActiveUserIds();
        } else if (MsgTypeEnum.USER.equals(msgType) && announcement.getUserIds() != null) {
            // 指定用户
            String[] userIdArray = announcement.getUserIds().split(",");
            for (String userIdStr : userIdArray) {
                try {
                    targetUserIds.add(Integer.parseInt(userIdStr.trim()));
                } catch (NumberFormatException e) {
                    // 忽略无效的用户ID
                }
            }
        }

        // 批量插入 delivery 记录（分批处理，避免长事务）
        if (!targetUserIds.isEmpty()) {
            int batchSize = AnnouncementConstants.BATCH_SIZE;
            for (int i = 0; i < targetUserIds.size(); i += batchSize) {
                int end = Math.min(i + batchSize, targetUserIds.size());
                List<Integer> batch = targetUserIds.subList(i, end);
                
                List<SysAnnouncementSend> sendList = new ArrayList<>();
                for (Integer userId : batch) {
                    SysAnnouncementSend send = new SysAnnouncementSend();
                    send.setId(UUID.randomUUID().toString().replace("-", ""));
                    send.setAnntId(id);
                    send.setUserId(userId);
                    send.setReadFlag(ReadFlagEnum.UNREAD.getCode());
                    sendList.add(send);
                }
                announcementSendMapper.batchInsert(sendList);
                deliveryCount += sendList.size();
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("deliveryCount", deliveryCount);
        result.put("pushCount", 0);  // TODO: WebSocket 推送计数
        return result;
    }

    @Override
    @Transactional
    public void doRevoke(String id) {
        SysAnnouncement announcement = announcementMapper.selectById(id);
        if (announcement == null) {
            throw new RuntimeException("消息不存在");
        }
        if (!SendStatusEnum.PUBLISHED.getCode().equals(announcement.getSendStatus())) {
            throw new RuntimeException("只能撤回已发布的消息");
        }
        // 更新状态为已撤回
        announcement.setSendStatus(SendStatusEnum.REVOKED.getCode());
        announcementMapper.updateById(announcement);
    }

    @Override
    public Map<String, Object> listByUser(Integer userId, String msgCategory, Integer readFlag, String startTime, String endTime, Integer pageNo, Integer pageSize) {
        Integer offset = (pageNo - 1) * pageSize;
        List<SysAnnouncement> records = announcementMapper.selectListByUser(userId, msgCategory, readFlag, startTime, endTime, offset, pageSize);
        Integer total = announcementMapper.countListByUser(userId, msgCategory, readFlag, startTime, endTime);

        Map<String, Object> result = new HashMap<>();
        result.put("records", records);
        result.put("total", total);
        result.put("size", pageSize);
        result.put("current", pageNo);
        return result;
    }

    @Override
    public Integer getUnreadCount(Integer userId) {
        return announcementSendMapper.countUnreadByUserId(userId);
    }

    @Override
    @Transactional
    public void read(String id, Integer userId) {
        // 检查是否存在关联记录
        SysAnnouncementSend send = announcementSendMapper.selectByAnntIdAndUserId(id, userId);
        if (send == null) {
            // 如果不存在，先创建
            send = new SysAnnouncementSend();
            send.setId(UUID.randomUUID().toString().replace("-", ""));
            send.setAnntId(id);
            send.setUserId(userId);
            send.setReadFlag(ReadFlagEnum.READ.getCode());
            send.setReadTime(new Date());
            announcementSendMapper.insert(send);
        } else {
            // 如果存在，标记为已读
            announcementSendMapper.markAsRead(id, userId);
        }
    }

    @Override
    @Transactional
    public void batchRead(List<String> ids, Integer userId) {
        // 先检查哪些消息没有派发记录（通知公告场景），需要先创建
        for (String id : ids) {
            SysAnnouncementSend existing = announcementSendMapper.selectByAnntIdAndUserId(id, userId);
            if (existing == null) {
                // 创建已读记录（通知公告首次标记已读时）
                SysAnnouncementSend send = new SysAnnouncementSend();
                send.setId(UUID.randomUUID().toString().replace("-", ""));
                send.setAnntId(id);
                send.setUserId(userId);
                send.setReadFlag(ReadFlagEnum.READ.getCode());
                send.setReadTime(new Date());
                announcementSendMapper.insert(send);
            }
        }
        // 批量更新已存在的记录
        announcementSendMapper.batchMarkAsRead(ids, userId);
    }

    @Override
    @Transactional
    public void deleteByUser(String id, Integer userId) {
        announcementSendMapper.deleteByAnntIdAndUserId(id, userId);
    }
}

