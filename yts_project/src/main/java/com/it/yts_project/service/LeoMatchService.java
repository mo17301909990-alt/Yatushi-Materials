package com.it.yts_project.service;

import com.it.yts_project.dto.LeoMatchParams;
import com.it.yts_project.dto.LeoProductVO;
import com.it.yts_project.dto.PagedResult;

import java.util.List;
import java.util.Map;

/**
 * LEO纸品 组合匹配查询 Service
 */
public interface LeoMatchService {

    /**
     * 跨表组合匹配查询
     */
    PagedResult<LeoProductVO> match(LeoMatchParams params);

    /**
     * 获取所有步骤（按大类分组），供前端级联选择器使用
     */
    List<Map<String, Object>> getSteps();
}
