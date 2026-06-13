package com.it.yts_project.service.Impl;

import com.it.yts_project.mapper.WearResistantGoldPaperMappingMapper;
import com.it.yts_project.mapper.ProductsMapper;
import com.it.yts_project.mapper.ProductMapper;
import com.it.yts_project.model.WearResistantGoldPaperMapping;
import com.it.yts_project.model.Products;
import com.it.yts_project.model.Product;
import com.it.yts_project.service.WearResistantGoldPaperMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 耐磨金纸映射服务实现类
 */
@Service
public class WearResistantGoldPaperMappingServiceImpl implements WearResistantGoldPaperMappingService {
    
    @Autowired
    private WearResistantGoldPaperMappingMapper mapper;
    
    @Autowired
    private ProductsMapper productsMapper;
    
    @Autowired
    private ProductMapper productMapper;
    
    @Override
    public List<WearResistantGoldPaperMapping> getAllMappings() {
        return mapper.findAll();
    }
    
    @Override
    public List<WearResistantGoldPaperMapping> getMappingsWithPagination(int page, int size) {
        int offset = (page - 1) * size;
        return mapper.findWithPagination(offset, size);
    }
    
    @Override
    public int getTotalCount() {
        return mapper.getTotalCount();
    }
    
    @Override
    public WearResistantGoldPaperMapping getMappingById(Integer id) {
        return mapper.findById(id);
    }
    
    @Override
    @Transactional
    public WearResistantGoldPaperMapping createMapping(WearResistantGoldPaperMapping mapping) {
        // 强制设置烫金类型为"燙金後擊凸"
        mapping.setHotStampingType("燙金後擊凸");
        
        // 处理空字符串为NULL
        if (mapping.getProductModelNumber() != null && mapping.getProductModelNumber().trim().isEmpty()) {
            mapping.setProductModelNumber(null);
        }
        
        LocalDateTime now = LocalDateTime.now();
        mapping.setCreatedAt(now);
        mapping.setUpdatedAt(now);
        
        mapper.insert(mapping);
        return mapping;
    }
    
    @Override
    @Transactional
    public WearResistantGoldPaperMapping updateMapping(WearResistantGoldPaperMapping mapping) {
        // 强制设置烫金类型为"燙金後擊凸"
        mapping.setHotStampingType("燙金後擊凸");
        
        // 处理空字符串为NULL
        if (mapping.getProductModelNumber() != null && mapping.getProductModelNumber().trim().isEmpty()) {
            mapping.setProductModelNumber(null);
        }
        
        mapping.setUpdatedAt(LocalDateTime.now());
        mapper.update(mapping);
        return mapping;
    }
    
    @Override
    @Transactional
    public void deleteMapping(Integer id) {
        mapper.deleteById(id);
    }
    
    @Override
    @Transactional
    public void batchDeleteMappings(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        mapper.batchDeleteByIds(ids);
    }
    
    @Override
    public List<WearResistantGoldPaperMapping> searchByProductName(String productName) {
        return mapper.findByProductName(productName);
    }
    
    @Override
    public List<WearResistantGoldPaperMapping> searchByProductModelNumber(String productModelNumber) {
        return mapper.findByProductModelNumber(productModelNumber);
    }
    
    @Override
    public List<WearResistantGoldPaperMapping> searchByGoldPaperType(String goldPaperType) {
        return mapper.findByGoldPaperType(goldPaperType);
    }
    
    @Override
    public List<WearResistantGoldPaperMapping> search(String productName, String productModelNumber, String goldPaperType) {
        return mapper.search(productName, productModelNumber, goldPaperType);
    }
    
    @Override
    public WearResistantGoldPaperMapping findByUniqueKey(String productName, String productModelNumber, String goldPaperType) {
        // 处理空字符串为NULL
        if (productModelNumber != null && productModelNumber.trim().isEmpty()) {
            productModelNumber = null;
        }
        return mapper.findByUniqueKey(productName, productModelNumber, goldPaperType);
    }
    
    @Override
    @Transactional
    public void batchUpdateStatus(List<Integer> ids, String compatibilityStatus) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        mapper.batchUpdateStatus(ids, compatibilityStatus);
    }
    
    @Override
    public List<String> getAllGoldPaperTypes() {
        return mapper.getAllGoldPaperTypes();
    }
    
    @Override
    public boolean validateProductName(String productName) {
        if (productName == null || productName.trim().isEmpty()) {
            return false;
        }
        try {
            List<Products> products = productsMapper.getProductsBySeriesName(productName.trim());
            return products != null && !products.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean validateProductModelNumber(String productModelNumber) {
        if (productModelNumber == null || productModelNumber.trim().isEmpty()) {
            return true; // 产品型号可以为空（系列级映射）
        }
        try {
            List<Product> products = productMapper.findByModelNumber(productModelNumber.trim());
            return products != null && !products.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean validateProductModelBelongsToSeries(String productName, String productModelNumber) {
        if (productName == null || productName.trim().isEmpty()) {
            return false;
        }
        if (productModelNumber == null || productModelNumber.trim().isEmpty()) {
            return true; // 产品型号为空时，不需要验证关联性
        }
        try {
            List<Product> products = productMapper.findByModelNumber(productModelNumber.trim());
            if (products == null || products.isEmpty()) {
                return false;
            }
            // 验证产品是否属于指定系列
            List<Products> seriesProducts = productsMapper.getProductsBySeriesName(productName.trim());
            if (seriesProducts == null || seriesProducts.isEmpty()) {
                return false;
            }
            // 检查是否有产品属于指定系列
            return products.stream()
                    .anyMatch(p -> p.getName() != null && p.getName().equals(productName.trim()));
        } catch (Exception e) {
            return false;
        }
    }
}

