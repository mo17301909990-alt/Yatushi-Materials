package com.it.yts_project.mapper;

import com.it.yts_project.model.PostProcessingOption;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 后处理工序选项Mapper接口
 */
@Mapper
public interface PostProcessingOptionMapper {
    
    /**
     * 查询所有后处理工序选项
     */
    List<PostProcessingOption> findAll();
    
    /**
     * 查询激活的后处理工序选项
     */
    List<PostProcessingOption> findActive();
    
    /**
     * 根据ID查询
     */
    PostProcessingOption findById(@Param("id") Integer id);
    
    /**
     * 根据工序名称查询
     */
    PostProcessingOption findByStepName(@Param("stepName") String stepName);
    
    /**
     * 根据工序编码查询
     */
    PostProcessingOption findByStepCode(@Param("stepCode") String stepCode);
    
    /**
     * 搜索后处理工序选项
     */
    List<PostProcessingOption> search(@Param("keyword") String keyword);
    
    /**
     * 插入后处理工序选项
     */
    int insert(PostProcessingOption option);
    
    /**
     * 更新后处理工序选项
     */
    int update(PostProcessingOption option);
    
    /**
     * 删除后处理工序选项
     */
    int deleteById(@Param("id") Integer id);
}
