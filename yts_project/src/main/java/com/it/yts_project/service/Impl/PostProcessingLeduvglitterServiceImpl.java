package com.it.yts_project.service.Impl;

import com.it.yts_project.dto.ClothPaperTypeDTO;
import com.it.yts_project.mapper.PostProcessingLeduvglitterMapper;
import com.it.yts_project.model.PostProcessingLeduvglitter;
import com.it.yts_project.service.ClothPaperTypeService;
import com.it.yts_project.service.PostProcessingLeduvglitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 絲印LED UV灑閃粉后加工服务实现类
 */
@Service
public class PostProcessingLeduvglitterServiceImpl implements PostProcessingLeduvglitterService {
    
    @Autowired
    private PostProcessingLeduvglitterMapper mapper;

    @Autowired(required = false)
    private ClothPaperTypeService clothPaperTypeService;
    
    @Override
    public List<PostProcessingLeduvglitter> getAllLeduvglitterConfigs() {
        return mapper.findAll();
    }
    
    @Override
    public List<PostProcessingLeduvglitter> getLeduvglitterConfigsWithPagination(int page, int size) {
        int offset = (page - 1) * size;
        return mapper.findWithPagination(offset, size);
    }
    
    @Override
    public int getTotalCount() {
        return mapper.getTotalCount();
    }
    
    @Override
    public PostProcessingLeduvglitter getLeduvglitterConfigById(Integer id) {
        return mapper.findById(id);
    }
    
    @Override
    public PostProcessingLeduvglitter createLeduvglitterConfig(PostProcessingLeduvglitter postProcessingLeduvglitter) {
        LocalDateTime now = LocalDateTime.now();
        postProcessingLeduvglitter.setCreatedAt(now);
        postProcessingLeduvglitter.setUpdatedAt(now);
        
        mapper.insert(postProcessingLeduvglitter);
        fillClothPaperTypeName(postProcessingLeduvglitter);
        return postProcessingLeduvglitter;
    }
    
    @Override
    public PostProcessingLeduvglitter updateLeduvglitterConfig(PostProcessingLeduvglitter postProcessingLeduvglitter) {
        postProcessingLeduvglitter.setUpdatedAt(LocalDateTime.now());
        mapper.update(postProcessingLeduvglitter);
        fillClothPaperTypeName(postProcessingLeduvglitter);
        return postProcessingLeduvglitter;
    }

    /** 回填布面纸类型名称，便于接口返回与操作日志显示 */
    private void fillClothPaperTypeName(PostProcessingLeduvglitter config) {
        if (config == null || config.getClothPaperTypeId() == null || clothPaperTypeService == null) return;
        try {
            ClothPaperTypeDTO dto = clothPaperTypeService.getById(config.getClothPaperTypeId());
            if (dto != null) {
                String name = dto.getCategory() != null && !dto.getCategory().isBlank()
                    ? (dto.getTypeName() != null && !dto.getTypeName().isBlank() ? dto.getCategory() + "." + dto.getTypeName() : dto.getCategory())
                    : (dto.getTypeName() != null ? dto.getTypeName() : "");
                config.setClothPaperTypeName(name);
            }
        } catch (Exception ignored) {
            // 仅用于展示，失败不抛
        }
    }
    
    @Override
    public void deleteLeduvglitterConfig(Integer id) {
        mapper.deleteById(id);
    }
    
    @Override
    public List<PostProcessingLeduvglitter> searchByProductName(String productName) {
        return mapper.findByProductName(productName);
    }
    
    @Override
    public List<PostProcessingLeduvglitter> searchByProductModelNumber(String productModelNumber) {
        return mapper.findByProductModelNumber(productModelNumber);
    }
    
    @Override
    public List<PostProcessingLeduvglitter> searchByClothPaperTypeId(Integer clothPaperTypeId) {
        return mapper.findByClothPaperTypeId(clothPaperTypeId);
    }
    
    @Override
    public List<PostProcessingLeduvglitter> searchByPaperType(String paperType) {
        return mapper.findByPaperType(paperType);
    }
    
    @Override
    public List<String> getAllProductNames() {
        return mapper.getAllProductNames();
    }
    
    @Override
    public List<String> getAllProductModelNumbers() {
        return mapper.getAllProductModelNumbers();
    }
    
    @Override
    public List<Integer> getAllClothPaperTypeIds() {
        return mapper.getAllClothPaperTypeIds();
    }
    
    @Override
    public List<String> getAllPaperTypes() {
        return mapper.getAllPaperTypes();
    }
    
    @Override
    public String checkLeduvglitterCompatibility(
        String productName,
        String productModelNumber,
        Integer clothPaperTypeId,
        String paperType
    ) {
        return mapper.checkLeduvglitterCompatibility(
            productName,
            productModelNumber,
            clothPaperTypeId,
            paperType
        );
    }
    
    @Override
    public PostProcessingLeduvglitter findByUniqueKey(
        String productName,
        String productModelNumber,
        Integer clothPaperTypeId,
        String paperType
    ) {
        return mapper.findByUniqueKey(productName, productModelNumber, clothPaperTypeId, paperType);
    }
    
    @Override
    public void batchUpdateStatus(List<Integer> ids, String compatibilityStatus) {
        mapper.batchUpdateStatus(ids, compatibilityStatus);
    }
}