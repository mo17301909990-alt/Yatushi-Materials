package com.it.yts_project.mapper;

import com.it.yts_project.model.LaminationCompatibility;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.it.yts_project.dto.CompatibilityQueryParams;
import com.it.yts_project.model.LaminationMaterialOptions;
import com.it.yts_project.model.PostProcessingOptions;

import java.util.List;

/**
 * 过胶兼容性Mapper接口
 */
@Mapper
public interface LaminationCompatibilityMapper {
    
    /**
     * 查询所有过胶兼容性
     */
    List<LaminationCompatibility> findAll();
    
    /**
     * 根据条件查询过胶兼容性（带分页）
     */
    List<LaminationCompatibility> findByCondition(@Param("foilSeries") String foilSeries,
                                                  @Param("productType") String productType,
                                                  @Param("modelNumber") String modelNumber,
                                                  @Param("postProcessingStepId") Integer postProcessingStepId,
                                                  @Param("laminationMaterialId") Integer laminationMaterialId,
                                                  @Param("compatibility") String compatibility,
                                                  @Param("offset") Integer offset,
                                                  @Param("limit") Integer limit);

    /**
     * 基于DTO参数查询过胶兼容性（供智能兼容等服务使用）
     */
    List<LaminationCompatibility> findLaminationCompatibility(CompatibilityQueryParams params);
    
    /**
     * 统计符合条件的记录数
     */
    int countByCondition(@Param("foilSeries") String foilSeries,
                        @Param("productType") String productType,
                        @Param("modelNumber") String modelNumber,
                        @Param("postProcessingStepId") Integer postProcessingStepId,
                        @Param("laminationMaterialId") Integer laminationMaterialId,
                        @Param("compatibility") String compatibility);
    
    /**
     * 根据ID查询
     */
    LaminationCompatibility findById(@Param("id") Integer id);
    
    /**
     * 根据唯一键查询（旧版本，保留兼容性）
     */
    LaminationCompatibility findByUniqueKey(@Param("foilSeries") String foilSeries,
                                           @Param("interfaceTypeId") Integer interfaceTypeId,
                                           @Param("postProcessingStepId") Integer postProcessingStepId,
                                           @Param("laminationMaterialId") Integer laminationMaterialId);
    
    /**
     * 根据新的唯一键查询
     * 唯一键：foilSeries + modelNumber + productType + laminationMaterialId + postProcessingStepId
     */
    LaminationCompatibility findByUniqueKeyNew(
        @Param("foilSeries") String foilSeries,
        @Param("modelNumber") String modelNumber,
        @Param("productType") String productType,
        @Param("laminationMaterialId") Integer laminationMaterialId,
        @Param("postProcessingStepId") Integer postProcessingStepId
    );
    
    /**
     * 批量更新兼容性状态
     */
    int batchUpdateStatus(@Param("ids") List<Integer> ids, @Param("compatibility") String compatibility);
    
    /**
     * 插入过胶兼容性
     */
    int insert(LaminationCompatibility compatibility);
    
    /**
     * 更新过胶兼容性
     */
    int update(LaminationCompatibility compatibility);
    
    /**
     * 根据唯一键更新或插入
     */
    int upsert(LaminationCompatibility compatibility);
    
    /**
     * 批量保存过胶兼容性
     */
    int batchSave(@Param("compatibilities") List<LaminationCompatibility> compatibilities);
    
    /**
     * 删除过胶兼容性
     */
    int deleteById(@Param("id") Integer id);
    
    /**
     * 批量删除过胶兼容性
     */
    int batchDeleteByIds(@Param("ids") List<Integer> ids);
    
    /**
     * 根据唯一键删除
     */
    int deleteByUniqueKey(@Param("foilSeries") String foilSeries,
                         @Param("interfaceTypeId") Integer interfaceTypeId,
                         @Param("postProcessingStepId") Integer postProcessingStepId,
                         @Param("laminationMaterialId") Integer laminationMaterialId);
    
    /**
     * 获取所有产品系列名称
     */
    List<String> getAllFoilSeries();
    
    /**
     * 获取所有产品类型
     */
    List<String> getAllProductTypes();
    
    /**
     * 获取所有型号
     */
    List<String> getAllModelNumbers();

    // ========== 兼容材料/工序选项及校验 ==========

    /**
     * 查询与指定参数兼容的过胶材料列表
     */
    List<LaminationMaterialOptions> findCompatibleMaterials(@Param("foilSeries") String foilSeries,
                                                            @Param("postProcessingStepId") Integer postProcessingStepId,
                                                            @Param("interfaceTypeId") Integer interfaceTypeId);

    /**
     * 查询与指定参数兼容的后加工步骤列表
     */
    List<PostProcessingOptions> findCompatiblePostProcessingSteps(@Param("foilSeries") String foilSeries,
                                                                  @Param("laminationMaterialId") Integer laminationMaterialId,
                                                                  @Param("interfaceTypeId") Integer interfaceTypeId);

    /** 获取所有过胶材料选项 */
    List<LaminationMaterialOptions> getAllLaminationMaterials();

    /** 获取所有后加工步骤选项 */
    List<PostProcessingOptions> getAllPostProcessingSteps();

    /** 校验是否兼容，返回 'V' 或 'X' */
    Character checkLaminationCompatibility(@Param("foilSeries") String foilSeries,
                                           @Param("postProcessingStepId") Integer postProcessingStepId,
                                           @Param("laminationMaterialId") Integer laminationMaterialId,
                                           @Param("interfaceTypeId") Integer interfaceTypeId);
}