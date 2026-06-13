package com.it.yts_project.service.Impl;

import com.it.yts_project.mapper.ReferenceInkSurfaceMapper;
import com.it.yts_project.model.ReferenceInkSurface;
import com.it.yts_project.service.ReferenceInkSurfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 印刷油墨面字典表 Service 实现类
 */
@Service
public class ReferenceInkSurfaceServiceImpl implements ReferenceInkSurfaceService {

    @Autowired
    private ReferenceInkSurfaceMapper referenceInkSurfaceMapper;

    @Override
    public List<ReferenceInkSurface> findAll() {
        return referenceInkSurfaceMapper.findAll();
    }

    @Override
    public ReferenceInkSurface findById(Integer id) {
        return referenceInkSurfaceMapper.findById(id);
    }

    @Override
    public List<ReferenceInkSurface> findByCategory(String category) {
        return referenceInkSurfaceMapper.findByCategory(category);
    }

    @Override
    public ReferenceInkSurface create(ReferenceInkSurface inkSurface) {
        if (inkSurface.getDisplayOrder() == null) {
            inkSurface.setDisplayOrder(0);
        }
        referenceInkSurfaceMapper.insert(inkSurface);
        return inkSurface;
    }

    @Override
    public ReferenceInkSurface update(ReferenceInkSurface inkSurface) {
        referenceInkSurfaceMapper.update(inkSurface);
        return referenceInkSurfaceMapper.findById(inkSurface.getId());
    }

    @Override
    public boolean delete(Integer id) {
        return referenceInkSurfaceMapper.deleteById(id) > 0;
    }
}
