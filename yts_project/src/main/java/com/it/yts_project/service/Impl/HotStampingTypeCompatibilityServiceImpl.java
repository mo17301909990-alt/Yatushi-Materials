package com.it.yts_project.service.Impl;

import com.it.yts_project.dto.HotStampingTypeCompatibilityCopyResult;
import com.it.yts_project.mapper.HotStampingTypeCompatibilityMapper;
import com.it.yts_project.model.HotStampingTypeCompatibility;
import com.it.yts_project.service.HotStampingTypeCompatibilityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 烫金类型兼容性服务实现类
 */
@Service
public class HotStampingTypeCompatibilityServiceImpl implements HotStampingTypeCompatibilityService {
    
    private static final Logger log = LoggerFactory.getLogger(HotStampingTypeCompatibilityServiceImpl.class);
    
    @Autowired
    private HotStampingTypeCompatibilityMapper mapper;
    
    @Override
    public List<HotStampingTypeCompatibility> getCompatibilityByHotStampingTypeId(Integer hotStampingTypeId) {
        return mapper.findByHotStampingTypeId(hotStampingTypeId);
    }
    
    @Override
    public HotStampingTypeCompatibility getCompatibilityById(Integer id) {
        return mapper.findById(id);
    }
    
    @Override
    public HotStampingTypeCompatibility createCompatibility(HotStampingTypeCompatibility compatibility) {
        LocalDateTime now = LocalDateTime.now();
        compatibility.setCreatedAt(now);
        compatibility.setUpdatedAt(now);
        
        mapper.insert(compatibility);
        return compatibility;
    }
    
    @Override
    public HotStampingTypeCompatibility updateCompatibility(HotStampingTypeCompatibility compatibility) {
        compatibility.setUpdatedAt(LocalDateTime.now());
        mapper.update(compatibility);
        return compatibility;
    }
    
    @Override
    public void deleteCompatibility(Integer id) {
        mapper.deleteById(id);
    }
    
    @Override
    public void deleteCompatibilityByHotStampingTypeId(Integer hotStampingTypeId) {
        mapper.deleteByHotStampingTypeId(hotStampingTypeId);
    }
    
    @Override
    public List<String> getAllProductNames() {
        return mapper.getAllProductNames();
    }
    
    @Override
    public List<String> getAllPaperTypes() {
        return mapper.getAllPaperTypes();
    }

    @Override
    public List<String> getPaperTypesByProductName(String productName) {
        return mapper.getPaperTypesByProductName(productName);
    }
    
    @Override
    public boolean existsByProductNameAndHotStampingTypeId(String productName, Integer hotStampingTypeId) {
        return mapper.countByProductNameAndHotStampingTypeId(productName, hotStampingTypeId) > 0;
    }
    
    @Override
    public HotStampingTypeCompatibilityCopyResult batchCopyCompatibility(List<Integer> sourceIds, List<Integer> targetHotStampingTypeIds) {
        int successCount = 0;
        int failCount = 0;
        
        if (sourceIds == null || sourceIds.isEmpty() || targetHotStampingTypeIds == null || targetHotStampingTypeIds.isEmpty()) {
            return new HotStampingTypeCompatibilityCopyResult(0, 0, "源映射或目标烫金类型ID不能为空");
        }

        for (Integer sourceId : sourceIds) {
            HotStampingTypeCompatibility sourceMapping = mapper.findById(sourceId);
            if (sourceMapping == null) {
                failCount++;
                continue;
            }

            for (Integer targetHotStampingTypeId : targetHotStampingTypeIds) {
                // 检查目标烫金类型是否是源烫金类型本身，如果是则跳过
                if (sourceMapping.getHotStampingTypeId().equals(targetHotStampingTypeId)) {
                    continue;
                }

                // 检查目标烫金类型下是否已存在相同的产品名称和纸张类型的映射
                if (mapper.countByProductNameAndHotStampingTypeId(sourceMapping.getProductName(), targetHotStampingTypeId) > 0) {
                    // 已存在，跳过复制，计入失败
                    failCount++;
                    continue;
                }

                HotStampingTypeCompatibility newMapping = new HotStampingTypeCompatibility();
                newMapping.setProductName(sourceMapping.getProductName());
                newMapping.setHotStampingTypeId(targetHotStampingTypeId); // 复制到新的烫金类型ID
                newMapping.setCompatibilityStatus(sourceMapping.getCompatibilityStatus());
                newMapping.setPaperType(sourceMapping.getPaperType());
                // 注意：HotStampingTypeCompatibility模型类没有isActive字段，如果需要可以后续添加
                newMapping.setCreatedAt(LocalDateTime.now());
                newMapping.setUpdatedAt(LocalDateTime.now());

                try {
                    mapper.insert(newMapping);
                    successCount++;
                } catch (Exception e) {
                    log.error("复制映射失败", e);
                    failCount++;
                }
            }
        }
        
        String message = String.format("批量复制完成：成功 %d 条，失败 %d 条", successCount, failCount);
        return new HotStampingTypeCompatibilityCopyResult(successCount, failCount, message);
    }
    
    @Override
    public int batchDeleteCompatibility(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        
        int totalDeleted = 0;
        for (Integer id : ids) {
            try {
                mapper.deleteById(id);
                totalDeleted++;
            } catch (Exception e) {
                log.error("批量删除映射失败，ID: {}", id, e);
            }
        }
        return totalDeleted;
    }
    
    @Override
    public List<HotStampingTypeCompatibility> getAllCompatibility() {
        return mapper.findAll();
    }
}
