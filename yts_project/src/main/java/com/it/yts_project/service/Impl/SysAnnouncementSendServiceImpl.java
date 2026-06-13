package com.it.yts_project.service.Impl;

import com.it.yts_project.mapper.SysAnnouncementSendMapper;
import com.it.yts_project.model.SysAnnouncementSend;
import com.it.yts_project.service.SysAnnouncementSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysAnnouncementSendServiceImpl implements SysAnnouncementSendService {

    @Autowired
    private SysAnnouncementSendMapper announcementSendMapper;

    @Override
    @Transactional
    public void batchInsert(List<SysAnnouncementSend> sendList) {
        announcementSendMapper.batchInsert(sendList);
    }

    @Override
    @Transactional
    public void markAsRead(String anntId, Integer userId) {
        announcementSendMapper.markAsRead(anntId, userId);
    }

    @Override
    @Transactional
    public void batchMarkAsRead(List<String> anntIds, Integer userId) {
        announcementSendMapper.batchMarkAsRead(anntIds, userId);
    }

    @Override
    @Transactional
    public void deleteByAnntIdAndUserId(String anntId, Integer userId) {
        announcementSendMapper.deleteByAnntIdAndUserId(anntId, userId);
    }

    @Override
    public int countUnreadByUserId(Integer userId) {
        return announcementSendMapper.countUnreadByUserId(userId);
    }
}

