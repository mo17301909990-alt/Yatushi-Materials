package com.it.yts_project.service.Impl;

import com.it.yts_project.mapper.HotStampingPatternBaseMapper;
import com.it.yts_project.model.HotStampingPatternBase;
import com.it.yts_project.service.HotStampingPatternBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotStampingPatternBaseServiceImpl implements HotStampingPatternBaseService {

    @Autowired
    private HotStampingPatternBaseMapper hotStampingPatternBaseMapper;

    @Override
    public List<HotStampingPatternBase> getActivePatterns() {
        return hotStampingPatternBaseMapper.getActivePatterns();
    }

    @Override
    public List<HotStampingPatternBase> getAllPatterns() {
        return hotStampingPatternBaseMapper.getAllPatterns();
    }

    @Override
    public HotStampingPatternBase getPatternById(Long id) {
        return hotStampingPatternBaseMapper.getPatternById(id);
    }
}
