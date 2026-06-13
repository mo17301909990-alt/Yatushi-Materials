package com.it.yts_project.service;

import com.it.yts_project.dto.GoldFoilQueryParams;
import com.it.yts_project.model.NoticeDictionary;

import java.util.List;

/**
 * 匹配注意事项服务接口
 * 用于在匹配过程中收集所有命中规则的注意事项
 */
public interface MatchingNoticeService {
    
    /**
     * 根据匹配参数收集所有命中规则的注意事项
     * @param params 匹配查询参数
     * @return 注意事项列表（去重后的并集）
     */
    List<NoticeDictionary> collectMatchingNotices(GoldFoilQueryParams params);
}
