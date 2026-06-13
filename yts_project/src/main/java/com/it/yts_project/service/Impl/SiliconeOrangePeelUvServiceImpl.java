package com.it.yts_project.service.Impl;

import com.it.yts_project.mapper.SiliconeOrangePeelUvMapper;
import com.it.yts_project.model.SiliconeOrangePeelUvProduct;
import com.it.yts_project.model.SiliconeOrangePeelUvCompatibility;
import com.it.yts_project.service.SiliconeOrangePeelUvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 硅胶桔纹UV(Orange Peel UV) Service实现类
 */
@Service
@Transactional
public class SiliconeOrangePeelUvServiceImpl implements SiliconeOrangePeelUvService {

    @Autowired
    private SiliconeOrangePeelUvMapper mapper;

    // ========== 产品管理 ==========

    @Override
    public List<SiliconeOrangePeelUvProduct> getAllProducts() {
        return mapper.findAllProducts();
    }

    @Override
    public List<SiliconeOrangePeelUvProduct> getActiveProducts() {
        return mapper.findActiveProducts();
    }

    @Override
    public SiliconeOrangePeelUvProduct getProductById(Integer id) {
        return mapper.findProductById(id);
    }

    @Override
    public List<SiliconeOrangePeelUvProduct> searchProducts(String keyword) {
        return mapper.searchProducts(keyword);
    }

    @Override
    public SiliconeOrangePeelUvProduct saveProduct(SiliconeOrangePeelUvProduct product) {
        if (product.getId() == null) {
            mapper.insertProduct(product);
        } else {
            mapper.updateProduct(product);
        }
        return product;
    }

    @Override
    public void deleteProduct(Integer id) {
        mapper.deleteProductById(id);
    }

    // ========== 兼容性管理 ==========

    @Override
    public List<SiliconeOrangePeelUvCompatibility> getCompatibilitiesByProductId(Integer productId) {
        return mapper.findCompatibilitiesByProductId(productId);
    }

    @Override
    public List<SiliconeOrangePeelUvCompatibility> getAllCompatibilities() {
        return mapper.findAllCompatibilities();
    }

    @Override
    public SiliconeOrangePeelUvCompatibility getCompatibilityById(Integer id) {
        return mapper.findCompatibilityById(id);
    }

    @Override
    public SiliconeOrangePeelUvCompatibility saveCompatibility(SiliconeOrangePeelUvCompatibility compatibility) {
        // 检查唯一性
        SiliconeOrangePeelUvCompatibility existing = mapper.findCompatibilityByProductAndStep(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
        if (existing != null && !existing.getId().equals(compatibility.getId())) {
            throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
        }
        if (compatibility.getId() == null) {
            mapper.insertCompatibility(compatibility);
        } else {
            mapper.updateCompatibility(compatibility);
        }
        return compatibility;
    }

    @Override
    @Transactional
    public void batchSaveCompatibilities(List<SiliconeOrangePeelUvCompatibility> compatibilities) {
        for (SiliconeOrangePeelUvCompatibility compatibility : compatibilities) {
            // 先检查是否已存在
            SiliconeOrangePeelUvCompatibility existing = mapper.findCompatibilityByProductAndStep(
                    compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null) {
                compatibility.setId(existing.getId());
                mapper.updateCompatibility(compatibility);
            } else {
                mapper.insertCompatibility(compatibility);
            }
        }
    }

    @Override
    public void deleteCompatibility(Integer id) {
        mapper.deleteCompatibilityById(id);
    }

    @Override
    public void deleteCompatibilitiesByProductId(Integer productId) {
        mapper.deleteCompatibilitiesByProductId(productId);
    }

    @Override
    public List<String> getAllPostProcessingSteps() {
        return mapper.getAllPostProcessingSteps();
    }
}
