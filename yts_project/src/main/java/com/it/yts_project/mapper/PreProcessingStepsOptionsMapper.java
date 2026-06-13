package com.it.yts_project.mapper;

import com.it.yts_project.model.PreProcessingStepsOptions;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 前工序选项数据访问层
 */
@Mapper
public interface PreProcessingStepsOptionsMapper {
    
    /**
     * 获取所有激活的前工序选项
     * @return 前工序选项列表
     */
    List<PreProcessingStepsOptions> getAllActiveOptions();
    
    /**
     * 根据ID获取前工序选项
     * @param id 选项ID
     * @return 前工序选项
     */
    PreProcessingStepsOptions getById(Integer id);
    
    /**
     * 获取所有前工序选项（包括未激活的）
     * @return 前工序选项列表
     */
    List<PreProcessingStepsOptions> getAllOptions();
    
    /**
     * 创建前工序选项
     * @param option 前工序选项
     * @return 影响行数
     */
    int insert(PreProcessingStepsOptions option);
    
    /**
     * 更新前工序选项
     * @param option 前工序选项
     * @return 影响行数
     */
    int update(PreProcessingStepsOptions option);
    
    /**
     * 删除前工序选项
     * @param id 选项ID
     * @return 影响行数
     */
    int deleteById(Integer id);
    
    /**
     * 批量更新状态
     * @param ids 选项ID列表
     * @param isActive 激活状态
     * @return 影响行数
     */
    int batchUpdateStatus(@Param("ids") List<Integer> ids, @Param("isActive") Boolean isActive);
    
    /**
     * 更新显示顺序
     * @param id 选项ID
     * @param displayOrder 显示顺序
     * @return 影响行数
     */
    int updateDisplayOrder(@Param("id") Integer id, @Param("displayOrder") Integer displayOrder);
    
    /**
     * 根据工艺类型查找前工序选项
     * @param process 工艺类型
     * @return 前工序选项
     */
    PreProcessingStepsOptions getByProcess(@Param("process") String process);
    
    /**
     * 根据前工序名称查找前工序选项
     * @param preProcessingStepsName 前工序名称
     * @return 前工序选项
     */
    PreProcessingStepsOptions getByPreProcessingStepsName(@Param("preProcessingStepsName") String preProcessingStepsName);
}
