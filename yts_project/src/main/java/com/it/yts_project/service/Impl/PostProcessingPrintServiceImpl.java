package com.it.yts_project.service.Impl;

import com.it.yts_project.mapper.PostProcessingPrintMapper;
import com.it.yts_project.model.PostProcessingPrint;
import com.it.yts_project.service.PostProcessingPrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 印刷后加工服务实现类
 */
@Service
public class PostProcessingPrintServiceImpl implements PostProcessingPrintService {
    
    @Autowired
    private PostProcessingPrintMapper mapper;
    
    @Override
    public List<PostProcessingPrint> getAllPrintConfigs() {
        return mapper.findAll();
    }
    
    @Override
    public List<PostProcessingPrint> getPrintConfigsWithPagination(int page, int size) {
        int offset = (page - 1) * size;
        return mapper.findWithPagination(offset, size);
    }
    
    @Override
    public int getTotalCount() {
        return mapper.getTotalCount();
    }
    
    @Override
    public PostProcessingPrint getPrintConfigById(Integer id) {
        return mapper.findById(id);
    }
    
    @Override
    public PostProcessingPrint createPrintConfig(PostProcessingPrint postProcessingPrint) {
        LocalDateTime now = LocalDateTime.now();
        postProcessingPrint.setCreatedAt(now);
        postProcessingPrint.setUpdatedAt(now);
        
        mapper.insert(postProcessingPrint);
        return postProcessingPrint;
    }
    
    @Override
    public PostProcessingPrint updatePrintConfig(PostProcessingPrint postProcessingPrint) {
        postProcessingPrint.setUpdatedAt(LocalDateTime.now());
        mapper.update(postProcessingPrint);
        return postProcessingPrint;
    }
    
    @Override
    public void deletePrintConfig(Integer id) {
        mapper.deleteById(id);
    }
    
    @Override
    public void batchDeletePrintConfigs(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        mapper.batchDeleteByIds(ids);
    }
    
    @Override
    public List<PostProcessingPrint> searchByProductName(String productName) {
        return mapper.findByProductName(productName);
    }
    
    @Override
    public List<PostProcessingPrint> searchByProductModelNumber(String productModelNumber) {
        return mapper.findByProductModelNumber(productModelNumber);
    }
    
    @Override
    public List<PostProcessingPrint> searchByColor(String color) {
        return mapper.findByColor(color);
    }
    
    @Override
    public List<PostProcessingPrint> searchByClothPaperTypeId(Integer clothPaperTypeId) {
        return mapper.findByClothPaperTypeId(clothPaperTypeId);
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
    public List<String> getAllColors() {
        return mapper.getAllColors();
    }
    
    @Override
    public List<String> getColorsByProductAndModel(String productName, String productModelNumber) {
        return mapper.getColorsByProductAndModel(productName, productModelNumber);
    }
    
    @Override
    public List<String> getColorsByProductName(String productName) {
        return mapper.getColorsByProductName(productName);
    }
    
    @Override
    public List<String> getColorsByProductModelNumber(String productModelNumber) {
        return mapper.getColorsByProductModelNumber(productModelNumber);
    }
    
    @Override
    public PostProcessingPrint findByUniqueKey(
            String productName,
            String productModelNumber,
            String color,
            Integer clothPaperTypeId,
            String paperType) {
        return mapper.findByUniqueKey(productName, productModelNumber, color, clothPaperTypeId, paperType);
    }
    
    @Override
    public void batchUpdateStatus(List<Integer> ids, String compatibilityStatus) {
        mapper.batchUpdateStatus(ids, compatibilityStatus);
    }
    
    @Override
    public boolean validateColor(String color, String productName, String productModelNumber) {
        if (color == null || color.trim().isEmpty()) {
            return true; // 颜色可以为空
        }
        return mapper.validateColor(color.trim(), productName, productModelNumber);
    }
}