package com.it.yts_project.service.Impl;

import com.it.yts_project.mapper.HotStampingTypeOptionsMapper;
import com.it.yts_project.model.HotStampingTypeOptions;
import com.it.yts_project.service.HotStampingTypeOptionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotStampingTypeOptionsServiceImpl implements HotStampingTypeOptionsService {

    @Autowired
    private HotStampingTypeOptionsMapper hotStampingTypeOptionsMapper;

    @Override
    public List<HotStampingTypeOptions> getActiveTypes() {
        return hotStampingTypeOptionsMapper.getActiveTypes();
    }

    @Override
    public List<HotStampingTypeOptions> getAllTypes() {
        return hotStampingTypeOptionsMapper.getAllTypes();
    }

    @Override
    public HotStampingTypeOptions getTypeById(Long id) {
        return hotStampingTypeOptionsMapper.getTypeById(id);
    }
}
