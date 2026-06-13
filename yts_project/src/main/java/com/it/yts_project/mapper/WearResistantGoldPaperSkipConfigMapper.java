package com.it.yts_project.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 耐磨金纸映射「跳过 Match 耐磨规则」配置 Mapper
 */
@Mapper
public interface WearResistantGoldPaperSkipConfigMapper {

    /**
     * 获取所有需要跳过耐磨映射的烫金纸类型
     */
    List<String> getAllSkipPaperTypes();

    /**
     * 删除所有配置
     */
    int deleteAll();

    /**
     * 批量插入配置
     */
    int batchInsert(@Param("types") List<String> types);
}



