package com.it.yts_project.service;

import java.util.Map;

/**
 * 燙金物料匹配規則 Hub 頁統計（僅計數，不拉全表）。
 */
public interface MaterialRuleStatisticsService {

    Map<String, Long> getOverviewCounts();
}
