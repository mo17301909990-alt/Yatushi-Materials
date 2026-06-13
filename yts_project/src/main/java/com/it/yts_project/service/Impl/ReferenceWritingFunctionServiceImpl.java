package com.it.yts_project.service.Impl;

import com.it.yts_project.mapper.ReferenceWritingFunctionMapper;
import com.it.yts_project.model.ReferenceWritingFunction;
import com.it.yts_project.service.ReferenceWritingFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 表面写字功能字典表 Service 实现类
 */
@Service
public class ReferenceWritingFunctionServiceImpl implements ReferenceWritingFunctionService {

    @Autowired
    private ReferenceWritingFunctionMapper referenceWritingFunctionMapper;

    @Override
    public List<ReferenceWritingFunction> findAll() {
        return referenceWritingFunctionMapper.findAll();
    }

    @Override
    public ReferenceWritingFunction findById(Integer id) {
        return referenceWritingFunctionMapper.findById(id);
    }

    @Override
    public List<ReferenceWritingFunction> findByCategory(String category) {
        return referenceWritingFunctionMapper.findByCategory(category);
    }

    @Override
    public ReferenceWritingFunction create(ReferenceWritingFunction writingFunction) {
        if (writingFunction.getDisplayOrder() == null) {
            writingFunction.setDisplayOrder(0);
        }
        referenceWritingFunctionMapper.insert(writingFunction);
        return writingFunction;
    }

    @Override
    public ReferenceWritingFunction update(ReferenceWritingFunction writingFunction) {
        referenceWritingFunctionMapper.update(writingFunction);
        return referenceWritingFunctionMapper.findById(writingFunction.getId());
    }

    @Override
    public boolean delete(Integer id) {
        return referenceWritingFunctionMapper.deleteById(id) > 0;
    }
}
