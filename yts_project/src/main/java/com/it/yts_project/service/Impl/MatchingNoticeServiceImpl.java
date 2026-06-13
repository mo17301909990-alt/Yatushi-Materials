package com.it.yts_project.service.Impl;

import com.it.yts_project.dto.GoldFoilQueryParams;
import com.it.yts_project.model.NoticeDictionary;
import com.it.yts_project.service.MatchingNoticeService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 匹配注意事项服务实现
 * 根据匹配参数收集所有命中规则的注意事项
 */
@Service
public class MatchingNoticeServiceImpl implements MatchingNoticeService {

    @Override
    public List<NoticeDictionary> collectMatchingNotices(GoldFoilQueryParams params) {
        // TODO: 根据匹配参数查询注意事项
        return Collections.emptyList();
    }
}
