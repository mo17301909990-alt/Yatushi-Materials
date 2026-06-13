package com.it.yts_project.service.Impl;

import com.it.yts_project.mapper.ReferencePaperSurfaceMapper;
import com.it.yts_project.model.ReferencePaperSurface;
import com.it.yts_project.service.ReferencePaperSurfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 纸張面字典表 Service 实现类
 */
@Service
public class ReferencePaperSurfaceServiceImpl implements ReferencePaperSurfaceService {

    @Autowired
    private ReferencePaperSurfaceMapper referencePaperSurfaceMapper;

    @Override
    public List<ReferencePaperSurface> findAll() {
        return referencePaperSurfaceMapper.findAll();
    }

    @Override
    public ReferencePaperSurface findById(Integer id) {
        return referencePaperSurfaceMapper.findById(id);
    }

    @Override
    public List<ReferencePaperSurface> findByCategory(String category) {
        return referencePaperSurfaceMapper.findByCategory(category);
    }

    @Override
    public ReferencePaperSurface create(ReferencePaperSurface paperSurface) {
        if (paperSurface.getDisplayOrder() == null) {
            paperSurface.setDisplayOrder(0);
        }
        referencePaperSurfaceMapper.insert(paperSurface);
        return paperSurface;
    }

    @Override
    public ReferencePaperSurface update(ReferencePaperSurface paperSurface) {
        referencePaperSurfaceMapper.update(paperSurface);
        return referencePaperSurfaceMapper.findById(paperSurface.getId());
    }

    @Override
    public boolean delete(Integer id) {
        return referencePaperSurfaceMapper.deleteById(id) > 0;
    }
}
