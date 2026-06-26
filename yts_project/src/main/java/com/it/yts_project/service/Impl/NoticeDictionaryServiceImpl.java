package com.it.yts_project.service.Impl;

import com.it.yts_project.mapper.NoticeDictionaryMapper;
import com.it.yts_project.model.NoticeDictionary;
import com.it.yts_project.service.NoticeDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 注意事项字典表 Service 实现类
 */
@Service
public class NoticeDictionaryServiceImpl implements NoticeDictionaryService {

    @Autowired
    private NoticeDictionaryMapper noticeDictionaryMapper;

    @Override
    public List<NoticeDictionary> findByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return noticeDictionaryMapper.findByIds(ids);
    }

    @Override
    public List<NoticeDictionary> findAll() {
        return noticeDictionaryMapper.findAll();
    }

    @Override
    public List<NoticeDictionary> findAllActive() {
        return noticeDictionaryMapper.findAllActive();
    }

    @Override
    public NoticeDictionary findById(Integer id) {
        return noticeDictionaryMapper.findById(id);
    }

    @Override
    public NoticeDictionary findByCode(String noticeCode) {
        return noticeDictionaryMapper.findByCode(noticeCode);
    }

    @Override
    public List<NoticeDictionary> findByCategory(String category) {
        return noticeDictionaryMapper.findByCategory(category);
    }

    @Override
    public NoticeDictionary create(NoticeDictionary notice) {
        if (notice.getPriority() == null) {
            notice.setPriority(0);
        }
        if (notice.getIsActive() == null) {
            notice.setIsActive(true);
        }
        noticeDictionaryMapper.insert(notice);
        return notice;
    }

    @Override
    public NoticeDictionary update(NoticeDictionary notice) {
        noticeDictionaryMapper.update(notice);
        return noticeDictionaryMapper.findById(notice.getId());
    }

    @Override
    public boolean delete(Integer id) {
        return noticeDictionaryMapper.deleteById(id) > 0;
    }
}
