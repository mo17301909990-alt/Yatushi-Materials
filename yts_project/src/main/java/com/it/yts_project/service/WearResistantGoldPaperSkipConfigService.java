package com.it.yts_project.service;

import java.util.List;

/**
 * 耐磨金纸映射「跳过 Match 耐磨规则」配置服务
 */
public interface WearResistantGoldPaperSkipConfigService {

    /**
     * 获取所有需要跳过耐磨映射的烫金纸类型（product.hot_stamping_paper_type）
     */
    List<String> getAllSkipPaperTypes();

    /**
     * 保存跳过配置：整体替换为新的列表
     */
    void saveSkipPaperTypes(List<String> paperTypes);
}



