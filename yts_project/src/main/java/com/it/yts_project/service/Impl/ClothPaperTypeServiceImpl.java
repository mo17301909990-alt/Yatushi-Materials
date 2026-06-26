package com.it.yts_project.service.Impl;

import com.it.yts_project.dto.ClothPaperTypeDTO;
import com.it.yts_project.mapper.ClothPaperTypeMapper;
import com.it.yts_project.service.ClothPaperTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 布料纸类型服务实现类
 */
@Service
public class ClothPaperTypeServiceImpl implements ClothPaperTypeService {
    
    @Autowired
    private ClothPaperTypeMapper clothPaperTypeMapper;
    
    @Override
    public List<ClothPaperTypeDTO> getAllActiveClothPaperTypes() {
        return clothPaperTypeMapper.getAllActiveClothPaperTypes();
    }
    
    @Override
    public List<ClothPaperTypeDTO> getAllClothPaperTypes() {
        return clothPaperTypeMapper.getAllClothPaperTypes();
    }
    
    @Override
    public List<ClothPaperTypeDTO> getActiveClothPaperTypesForCommonInterfaceMatrix() {
        return clothPaperTypeMapper.getAllActiveClothPaperTypes().stream()
                .filter(t -> !Boolean.TRUE.equals(t.getExcludeFromCommonInterfaceMatrix()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ClothPaperTypeDTO> getActiveSpecialInterfaceClothPaperTypes() {
        return clothPaperTypeMapper.getAllActiveClothPaperTypes().stream()
                .filter(t -> Boolean.TRUE.equals(t.getExcludeFromCommonInterfaceMatrix()))
                .collect(Collectors.toList());
    }

    @Override
    public ClothPaperTypeDTO getById(Integer id) {
        return clothPaperTypeMapper.getById(id);
    }
    
    @Override
    public List<ClothPaperTypeDTO> getByCategory(String category) {
        return clothPaperTypeMapper.getByCategory(category);
    }
    
    @Override
    public ClothPaperTypeDTO create(ClothPaperTypeDTO clothPaperType) {
        // 如果没有设置排序顺序，设置为最大值+1
        if (clothPaperType.getSortOrder() == null) {
            List<ClothPaperTypeDTO> allTypes = clothPaperTypeMapper.getAllClothPaperTypes();
            int maxOrder = allTypes.stream()
                    .mapToInt(type -> type.getSortOrder() != null ? type.getSortOrder() : 0)
                    .max()
                    .orElse(0);
            clothPaperType.setSortOrder(maxOrder + 1);
        }
        
        // 默认激活状态
        if (clothPaperType.getIsActive() == null) {
            clothPaperType.setIsActive(true);
        }
        
        // 注意：created_at 和 updated_at 由数据库自动设置（使用 CURRENT_TIMESTAMP）
        clothPaperTypeMapper.insert(clothPaperType);
        // 插入后重新查询以获取数据库生成的时间戳
        return clothPaperTypeMapper.getById(clothPaperType.getId());
    }
    
    @Override
    public ClothPaperTypeDTO update(ClothPaperTypeDTO clothPaperType) {
        // 注意：updated_at 由数据库自动设置（使用 CURRENT_TIMESTAMP）
        clothPaperTypeMapper.update(clothPaperType);
        return clothPaperTypeMapper.getById(clothPaperType.getId());
    }
    
    @Override
    public boolean deleteById(Integer id) {
        int result = clothPaperTypeMapper.deleteById(id);
        return result > 0;
    }
    
    @Override
    public boolean batchUpdateStatus(List<Integer> ids, Boolean isActive) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        int result = clothPaperTypeMapper.batchUpdateStatus(ids, isActive);
        return result > 0;
    }
    
    @Override
    public boolean updateSortOrder(Integer id, Integer sortOrder) {
        int result = clothPaperTypeMapper.updateSortOrder(id, sortOrder);
        return result > 0;
    }
    
    @Override
    public ClothPaperTypeDTO getByTypeNameAndCategory(String typeName, String category) {
        return clothPaperTypeMapper.getByTypeNameAndCategory(typeName, category);
    }
    
    @Override
    public ClothPaperTypeDTO getByTypeName(String typeName) {
        return clothPaperTypeMapper.getByTypeName(typeName);
    }
}


