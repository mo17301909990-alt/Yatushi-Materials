package com.it.yts_project.service.Impl;

import com.it.yts_project.dto.ClothPaperCompatibilityDTO;
import com.it.yts_project.mapper.ClothPaperCompatibilityMapper;
import com.it.yts_project.service.ClothPaperCompatibilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 布面纸兼容性服务实现类
 */
@Service
public class ClothPaperCompatibilityServiceImpl implements ClothPaperCompatibilityService {
    
    @Autowired
    private ClothPaperCompatibilityMapper clothPaperCompatibilityMapper;
    
    @Override
    public List<ClothPaperCompatibilityDTO> getAllCompatibility() {
        return clothPaperCompatibilityMapper.getAllCompatibility();
    }
    
    @Override
    public List<ClothPaperCompatibilityDTO> getByProjectId(String projectId) {
        return clothPaperCompatibilityMapper.getByProjectId(projectId);
    }
    
    @Override
    public List<ClothPaperCompatibilityDTO> getByClothPaperTypeId(Integer clothPaperTypeId) {
        return clothPaperCompatibilityMapper.getByClothPaperTypeId(clothPaperTypeId);
    }
    
    @Override
    public ClothPaperCompatibilityDTO getByProjectAndType(String projectId, Integer clothPaperTypeId) {
        return clothPaperCompatibilityMapper.getByProjectAndType(projectId, clothPaperTypeId);
    }
    
    @Override
    public ClothPaperCompatibilityDTO getByProjectAndTypeAndPaperType(String projectId, Integer clothPaperTypeId, String paperType) {
        return clothPaperCompatibilityMapper.getByProjectAndTypeAndPaperType(projectId, clothPaperTypeId, paperType);
    }
    
    @Override
    public ClothPaperCompatibilityDTO create(ClothPaperCompatibilityDTO compatibility) {
        // 检查是否已存在相同的组合（包含 paper_type）
        ClothPaperCompatibilityDTO existing = clothPaperCompatibilityMapper.getByProjectAndTypeAndPaperType(
            compatibility.getProductName(), 
            compatibility.getClothPaperTypeId(), 
            compatibility.getPaperType()
        );
        
        if (existing != null) {
            throw new org.springframework.dao.DuplicateKeyException(
                "该燙金紙系列、布面纸类型和烫金纸性能类型的组合已存在"
            );
        }
        
        // 设置创建时间
        compatibility.setCreatedAt(LocalDateTime.now());
        compatibility.setUpdatedAt(LocalDateTime.now());
        
        // 默认兼容性状态
        if (compatibility.getCompatibilityStatus() == null) {
            compatibility.setCompatibilityStatus("NA");
        }
        
        clothPaperCompatibilityMapper.insert(compatibility);
        return compatibility;
    }
    
    @Override
    public ClothPaperCompatibilityDTO update(ClothPaperCompatibilityDTO compatibility) {
        // 检查是否已存在相同的组合（排除当前记录）
        ClothPaperCompatibilityDTO existing = clothPaperCompatibilityMapper.getByProjectAndTypeAndPaperType(
            compatibility.getProductName(), 
            compatibility.getClothPaperTypeId(), 
            compatibility.getPaperType()
        );
        
        // 如果存在且不是当前记录，则抛出异常
        if (existing != null && !existing.getId().equals(compatibility.getId())) {
            throw new org.springframework.dao.DuplicateKeyException(
                "该燙金紙系列、布面纸类型和烫金纸性能类型的组合已存在"
            );
        }
        
        // 设置更新时间
        compatibility.setUpdatedAt(LocalDateTime.now());
        
        clothPaperCompatibilityMapper.update(compatibility);
        return clothPaperCompatibilityMapper.getByProjectAndTypeAndPaperType(
            compatibility.getProductName(), 
            compatibility.getClothPaperTypeId(), 
            compatibility.getPaperType()
        );
    }
    
    @Override
    public boolean deleteById(Integer id) {
        int result = clothPaperCompatibilityMapper.deleteById(id);
        return result > 0;
    }
    
    @Override
    public boolean deleteByProjectAndType(String projectId, Integer clothPaperTypeId) {
        int result = clothPaperCompatibilityMapper.deleteByProjectAndType(projectId, clothPaperTypeId);
        return result > 0;
    }
    
    @Override
    public boolean batchCreate(List<ClothPaperCompatibilityDTO> compatibilities) {
        if (compatibilities == null || compatibilities.isEmpty()) {
            return false;
        }
        
        // 设置时间戳
        LocalDateTime now = LocalDateTime.now();
        compatibilities.forEach(compatibility -> {
            compatibility.setCreatedAt(now);
            compatibility.setUpdatedAt(now);
            if (compatibility.getCompatibilityStatus() == null) {
                compatibility.setCompatibilityStatus("NA");
            }
        });
        
        int result = clothPaperCompatibilityMapper.batchInsert(compatibilities);
        return result > 0;
    }
    
    @Override
    public boolean batchUpdateByProject(String projectId, List<ClothPaperCompatibilityDTO> compatibilities) {
        if (compatibilities == null || compatibilities.isEmpty()) {
            return false;
        }
        
        // 设置更新时间
        LocalDateTime now = LocalDateTime.now();
        compatibilities.forEach(compatibility -> {
            compatibility.setUpdatedAt(now);
        });
        
        int result = clothPaperCompatibilityMapper.batchUpdateByProject(projectId, compatibilities);
        return result > 0;
    }
    
    @Override
    public List<String> getAllProductNames() {
        return clothPaperCompatibilityMapper.getAllProductNames();
    }
    
    @Override
    public String getProjectIdByName(String productName) {
        return clothPaperCompatibilityMapper.getProjectIdByName(productName);
    }
}
