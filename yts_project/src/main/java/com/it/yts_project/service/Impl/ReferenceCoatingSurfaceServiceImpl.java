package com.it.yts_project.service.Impl;

import com.it.yts_project.mapper.ReferenceCoatingSurfaceMapper;
import com.it.yts_project.model.ReferenceCoatingSurface;
import com.it.yts_project.service.ReferenceCoatingSurfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 後加工塗層面字典表 Service 实现类
 */
@Service
public class ReferenceCoatingSurfaceServiceImpl implements ReferenceCoatingSurfaceService {

    @Autowired
    private ReferenceCoatingSurfaceMapper referenceCoatingSurfaceMapper;

    @Override
    public List<ReferenceCoatingSurface> findAll() {
        return referenceCoatingSurfaceMapper.findAll();
    }

    @Override
    public ReferenceCoatingSurface findById(Integer id) {
        return referenceCoatingSurfaceMapper.findById(id);
    }

    @Override
    public List<ReferenceCoatingSurface> findByCategory(String category) {
        return referenceCoatingSurfaceMapper.findByCategory(category);
    }

    @Override
    public List<ReferenceCoatingSurface> search(String keyword) {
        return referenceCoatingSurfaceMapper.search(keyword);
    }

    @Override
    @Transactional
    public ReferenceCoatingSurface create(ReferenceCoatingSurface coatingSurface) {
        if (coatingSurface.getDisplayOrder() == null) {
            coatingSurface.setDisplayOrder(0);
        }
        if (coatingSurface.getIsActive() == null) {
            coatingSurface.setIsActive(true);
        }
        referenceCoatingSurfaceMapper.insert(coatingSurface);
        return coatingSurface;
    }

    @Override
    @Transactional
    public ReferenceCoatingSurface update(ReferenceCoatingSurface coatingSurface) {
        referenceCoatingSurfaceMapper.update(coatingSurface);
        return referenceCoatingSurfaceMapper.findById(coatingSurface.getId());
    }

    @Override
    @Transactional
    public boolean delete(Integer id) {
        return referenceCoatingSurfaceMapper.deleteById(id) > 0;
    }
}
