package com.it.yts_project.service.Impl;

import com.it.yts_project.mapper.LaminationCompatibilityMapper;
import com.it.yts_project.mapper.LaminationMaterialOptionMapper;
import com.it.yts_project.mapper.PostProcessingOptionMapper;
import com.it.yts_project.model.LaminationCompatibility;
import com.it.yts_project.model.LaminationMaterialOption;
import com.it.yts_project.model.PostProcessingOption;
import com.it.yts_project.service.LaminatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 过胶管理Service实现类
 */
@Service
@Transactional
public class LaminatingServiceImpl implements LaminatingService {
    
    @Autowired
    private LaminationMaterialOptionMapper materialOptionMapper;
    
    @Autowired
    private PostProcessingOptionMapper processingOptionMapper;
    
    @Autowired
    private LaminationCompatibilityMapper compatibilityMapper;
    
    // ========== 过胶材料选项管理 ==========
    
    @Override
    public List<LaminationMaterialOption> getAllMaterialOptions() {
        return materialOptionMapper.findAll();
    }
    
    @Override
    public List<LaminationMaterialOption> getActiveMaterialOptions() {
        return materialOptionMapper.findActive();
    }
    
    @Override
    public LaminationMaterialOption getMaterialOptionById(Integer id) {
        return materialOptionMapper.findById(id);
    }
    
    @Override
    public LaminationMaterialOption saveMaterialOption(LaminationMaterialOption option) {
        if (option.getId() == null) {
            materialOptionMapper.insert(option);
        } else {
            materialOptionMapper.update(option);
        }
        return option;
    }
    
    @Override
    public void deleteMaterialOption(Integer id) {
        materialOptionMapper.deleteById(id);
    }
    
    // ========== 后处理工序选项管理 ==========
    
    @Override
    public List<PostProcessingOption> getAllProcessingOptions() {
        return processingOptionMapper.findAll();
    }
    
    @Override
    public List<PostProcessingOption> getActiveProcessingOptions() {
        return processingOptionMapper.findActive();
    }
    
    @Override
    public PostProcessingOption getProcessingOptionByStepName(String stepName) {
        return processingOptionMapper.findByStepName(stepName);
    }
    
    @Override
    public PostProcessingOption saveProcessingOption(PostProcessingOption option) {
        if (option.getId() == null) {
            processingOptionMapper.insert(option);
        } else {
            processingOptionMapper.update(option);
        }
        return option;
    }
    
    @Override
    public void deleteProcessingOption(Integer id) {
        processingOptionMapper.deleteById(id);
    }
    
    // ========== 过胶兼容性管理 ==========
    
    @Override
    public List<LaminationCompatibility> getCompatibilityList(String foilSeries, String productType, 
                                                             String modelNumber, Integer postProcessingStepId,
                                                             Integer laminationMaterialId, String compatibility,
                                                             Integer page, Integer size) {
        int offset = (page - 1) * size;
        return compatibilityMapper.findByCondition(foilSeries, productType, modelNumber, 
                                                 postProcessingStepId, laminationMaterialId, compatibility,
                                                 offset, size);
    }
    
    @Override
    public int countCompatibility(String foilSeries, String productType, String modelNumber,
                                 Integer postProcessingStepId, Integer laminationMaterialId, String compatibility) {
        return compatibilityMapper.countByCondition(foilSeries, productType, modelNumber,
                                                  postProcessingStepId, laminationMaterialId, compatibility);
    }
    
    @Override
    public LaminationCompatibility getCompatibilityById(Integer id) {
        return compatibilityMapper.findById(id);
    }
    
    @Override
    public LaminationCompatibility saveCompatibility(LaminationCompatibility compatibility) {
        // 确保interfaceTypeId为0（废弃字段）
        if (compatibility.getInterfaceTypeId() == null) {
            compatibility.setInterfaceTypeId(0);
        }
        
        // 验证过胶材料是否存在
        if (compatibility.getLaminationMaterialId() == null || compatibility.getLaminationMaterialId() == 0) {
            throw new IllegalArgumentException("过胶材料不能为空");
        }
        
        LaminationMaterialOption materialOption = materialOptionMapper.findById(compatibility.getLaminationMaterialId());
        if (materialOption == null) {
            throw new IllegalArgumentException("过胶材料ID " + compatibility.getLaminationMaterialId() + " 不存在");
        }
        
        // 验证过胶后工序是否存在（如果提供）
        if (compatibility.getPostProcessingStepId() != null && compatibility.getPostProcessingStepId() != 0) {
            PostProcessingOption stepOption = processingOptionMapper.findById(compatibility.getPostProcessingStepId());
            if (stepOption == null) {
                throw new IllegalArgumentException("过胶后工序ID " + compatibility.getPostProcessingStepId() + " 不存在");
            }
        }
        
        if (compatibility.getId() == null) {
            // 添加新记录：检查是否已存在相同的唯一键组合
            LaminationCompatibility existing = compatibilityMapper.findByUniqueKeyNew(
                compatibility.getFoilSeries(),
                compatibility.getModelNumber(),
                compatibility.getProductType(),
                compatibility.getLaminationMaterialId(),
                compatibility.getPostProcessingStepId()
            );
            
            if (existing != null) {
                // 已存在相同配置，抛出异常
                throw new IllegalArgumentException("该配置已存在，请勿重复添加");
            }
            
                // 插入新记录
                compatibilityMapper.insert(compatibility);
        } else {
            // 更新现有记录：检查修改后的唯一键是否与其他记录冲突（排除当前记录）
            LaminationCompatibility existing = compatibilityMapper.findByUniqueKeyNew(
                compatibility.getFoilSeries(),
                compatibility.getModelNumber(),
                compatibility.getProductType(),
                compatibility.getLaminationMaterialId(),
                compatibility.getPostProcessingStepId()
            );
            
            if (existing != null && !existing.getId().equals(compatibility.getId())) {
                // 存在其他记录使用相同的唯一键，抛出异常
                throw new IllegalArgumentException("该配置已存在，请修改唯一键字段（产品系列、产品型号、产品类型、过胶材料、后处理步骤）");
            }
            
            // 更新现有记录
            compatibilityMapper.update(compatibility);
        }
        
        return compatibility;
    }
    
    @Override
    public void batchSaveCompatibility(List<LaminationCompatibility> compatibilities) {
        // 确保所有记录的interfaceTypeId为0，并逐个处理以避免唯一性冲突
        for (LaminationCompatibility compatibility : compatibilities) {
            if (compatibility.getInterfaceTypeId() == null) {
                compatibility.setInterfaceTypeId(0);
            }
            
            // 验证过胶材料是否存在
            if (compatibility.getLaminationMaterialId() == null || compatibility.getLaminationMaterialId() == 0) {
                throw new IllegalArgumentException("过胶材料不能为空");
            }
            
            LaminationMaterialOption materialOption = materialOptionMapper.findById(compatibility.getLaminationMaterialId());
            if (materialOption == null) {
                throw new IllegalArgumentException("过胶材料ID " + compatibility.getLaminationMaterialId() + " 不存在");
            }
            
            // 验证过胶后工序是否存在（如果提供）
            if (compatibility.getPostProcessingStepId() != null && compatibility.getPostProcessingStepId() != 0) {
                PostProcessingOption stepOption = processingOptionMapper.findById(compatibility.getPostProcessingStepId());
                if (stepOption == null) {
                    throw new IllegalArgumentException("过胶后工序ID " + compatibility.getPostProcessingStepId() + " 不存在");
                }
            }
            
            // 检查是否已存在相同的唯一键组合
            LaminationCompatibility existing = compatibilityMapper.findByUniqueKeyNew(
                compatibility.getFoilSeries(),
                compatibility.getModelNumber(),
                compatibility.getProductType(),
                compatibility.getLaminationMaterialId(),
                compatibility.getPostProcessingStepId()
            );
            
            if (existing != null) {
                // 已存在，更新现有记录
                compatibility.setId(existing.getId());
                compatibilityMapper.update(compatibility);
            } else {
                // 不存在，插入新记录
                compatibilityMapper.insert(compatibility);
            }
        }
    }
    
    @Override
    public void deleteCompatibility(Integer id) {
        compatibilityMapper.deleteById(id);
    }
    
    @Override
    public void batchDeleteCompatibility(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        compatibilityMapper.batchDeleteByIds(ids);
    }
    
    // ========== 选项数据获取 ==========
    
    @Override
    public List<String> getAllFoilSeries() {
        return compatibilityMapper.getAllFoilSeries();
    }
    
    @Override
    public List<String> getAllProductTypes() {
        return compatibilityMapper.getAllProductTypes();
    }
    
    @Override
    public List<String> getAllModelNumbers() {
        return compatibilityMapper.getAllModelNumbers();
    }
    
    @Override
    public Integer getLaminatingStepId() {
        // 查找"过胶"工序
        PostProcessingOption laminatingStep = processingOptionMapper.findByStepName("过胶");
        if (laminatingStep != null) {
            return laminatingStep.getId();
        }
        
        // 如果没找到"过胶"，尝试查找"過膠"
        laminatingStep = processingOptionMapper.findByStepName("過膠");
        if (laminatingStep != null) {
            return laminatingStep.getId();
        }
        
        // 如果没找到"過膠"，尝试查找"LAMINATING"
        laminatingStep = processingOptionMapper.findByStepCode("LAMINATING");
        if (laminatingStep != null) {
            return laminatingStep.getId();
        }
        
        return null;
    }
    
    @Override
    public LaminationCompatibility findByUniqueKeyNew(
            String foilSeries,
            String modelNumber,
            String productType,
            Integer laminationMaterialId,
            Integer postProcessingStepId) {
        return compatibilityMapper.findByUniqueKeyNew(
            foilSeries,
            modelNumber,
            productType,
            laminationMaterialId,
            postProcessingStepId
        );
    }
    
    @Override
    public void batchUpdateStatus(List<Integer> ids, String compatibility) {
        compatibilityMapper.batchUpdateStatus(ids, compatibility);
    }
}
