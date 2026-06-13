package com.it.yts_project.service.Impl;

import com.it.yts_project.mapper.ReferenceProductFamilyMapper;
import com.it.yts_project.model.ReferenceProductFamily;
import com.it.yts_project.service.ReferenceProductFamilyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 产品家族字典表 Service 实现类
 */
@Service
public class ReferenceProductFamilyServiceImpl implements ReferenceProductFamilyService {

    @Autowired
    private ReferenceProductFamilyMapper referenceProductFamilyMapper;

    @Override
    public List<ReferenceProductFamily> findAll() {
        return referenceProductFamilyMapper.findAll();
    }

    @Override
    public ReferenceProductFamily findById(Integer id) {
        return referenceProductFamilyMapper.findById(id);
    }

    @Override
    public List<ReferenceProductFamily> findByCategory(String category) {
        return referenceProductFamilyMapper.findByCategory(category);
    }

    @Override
    public List<String> findDistinctCategories() {
        return referenceProductFamilyMapper.findDistinctCategories();
    }

    @Override
    public List<String> findDistinctSubCategories(String category) {
        return referenceProductFamilyMapper.findDistinctSubCategories(category);
    }

    @Override
    public ReferenceProductFamily create(ReferenceProductFamily entity) {
        if (entity.getDisplayOrder() == null) {
            entity.setDisplayOrder(0);
        }
        referenceProductFamilyMapper.insert(entity);
        return entity;
    }

    @Override
    public ReferenceProductFamily update(ReferenceProductFamily entity) {
        referenceProductFamilyMapper.update(entity);
        return referenceProductFamilyMapper.findById(entity.getId());
    }

    @Override
    public boolean delete(Integer id) {
        return referenceProductFamilyMapper.deleteById(id) > 0;
    }
}
