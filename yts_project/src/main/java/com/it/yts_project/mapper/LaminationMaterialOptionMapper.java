package com.it.yts_project.mapper;

import com.it.yts_project.model.LaminationMaterialOption;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 过胶材料选项Mapper接口
 */
@Mapper
public interface LaminationMaterialOptionMapper {
    
    /**
     * 查询所有过胶材料选项
     */
    List<LaminationMaterialOption> findAll();
    
    /**
     * 查询激活的过胶材料选项
     */
    List<LaminationMaterialOption> findActive();
    
    /**
     * 根据ID查询
     */
    LaminationMaterialOption findById(@Param("id") Integer id);
    
    /**
     * 根据材料编码查询
     */
    LaminationMaterialOption findByMaterialCode(@Param("materialCode") String materialCode);
    
    /**
     * 搜索过胶材料选项
     */
    List<LaminationMaterialOption> search(@Param("keyword") String keyword);
    
    /**
     * 插入过胶材料选项
     */
    int insert(LaminationMaterialOption option);
    
    /**
     * 更新过胶材料选项
     */
    int update(LaminationMaterialOption option);
    
    /**
     * 删除过胶材料选项
     */
    int deleteById(@Param("id") Integer id);
}
