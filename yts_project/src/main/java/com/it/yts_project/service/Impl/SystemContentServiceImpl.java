package com.it.yts_project.service.Impl;

import com.it.yts_project.dto.SystemContentUpdateRequest;
import com.it.yts_project.mapper.SystemContentMapper;
import com.it.yts_project.model.SystemContent;
import com.it.yts_project.service.SystemContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SystemContentServiceImpl implements SystemContentService {

    public static final String KEY_HOT_STAMPING_MATERIAL_GUIDE = "hot_stamping_material_guide";

    @Autowired
    private SystemContentMapper systemContentMapper;

    @Override
    public SystemContent getByKey(String contentKey) {
        return systemContentMapper.findByContentKey(contentKey);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SystemContent updateByKey(String contentKey, SystemContentUpdateRequest req, Integer operatorId) {
        if (contentKey == null || contentKey.isBlank()) {
            throw new IllegalArgumentException("contentKey is required");
        }
        String title = req.getTitle() != null ? req.getTitle().trim() : "";
        String body = req.getBodyHtml() != null ? req.getBodyHtml() : "";
        systemContentMapper.upsert(contentKey, title, body, operatorId);
        return systemContentMapper.findByContentKey(contentKey);
    }
}
