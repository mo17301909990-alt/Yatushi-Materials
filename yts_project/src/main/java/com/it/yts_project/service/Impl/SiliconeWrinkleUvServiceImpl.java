package com.it.yts_project.service.Impl;

import com.it.yts_project.mapper.SiliconeWrinkleUvMapper;
import com.it.yts_project.model.SiliconeWrinkleUvCompatibility;
import com.it.yts_project.model.SiliconeWrinkleUvProduct;
import com.it.yts_project.service.SiliconeWrinkleUvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 硅胶_皱纹UV Service实现类
 */
@Service
@Transactional
public class SiliconeWrinkleUvServiceImpl implements SiliconeWrinkleUvService {

    @Autowired
    private SiliconeWrinkleUvMapper mapper;

    // ========== 产品管理 ==========

    @Override
    public List<SiliconeWrinkleUvProduct> getAllProducts() {
        return mapper.findAllProducts();
    }

    @Override
    public List<SiliconeWrinkleUvProduct> getActiveProducts() {
        return mapper.findActiveProducts();
    }

    @Override
    public SiliconeWrinkleUvProduct getProductById(Integer id) {
        return mapper.findProductById(id);
    }

    @Override
    public List<SiliconeWrinkleUvProduct> searchProducts(String keyword) {
        return mapper.searchProducts(keyword);
    }

    @Override
    public SiliconeWrinkleUvProduct saveProduct(SiliconeWrinkleUvProduct product) {
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
    public List<SiliconeWrinkleUvCompatibility> getCompatibilitiesByProductId(Integer productId) {
        return mapper.findCompatibilitiesByProductId(productId);
    }

    @Override
    public List<SiliconeWrinkleUvCompatibility> getAllCompatibilities() {
        return mapper.findAllCompatibilities();
    }

    @Override
    public SiliconeWrinkleUvCompatibility getCompatibilityById(Integer id) {
        return mapper.findCompatibilityById(id);
    }

    @Override
    public SiliconeWrinkleUvCompatibility saveCompatibility(SiliconeWrinkleUvCompatibility compatibility) {
        if (compatibility.getId() == null) {
            // 检查唯一键
            SiliconeWrinkleUvCompatibility existing = mapper.findCompatibilityByUniqueKey(
                    compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null) {
                throw new IllegalArgumentException("该产品与工序的兼容性配置已存在");
            }
            mapper.insertCompatibility(compatibility);
        } else {
            // 更新时检查唯一键冲突（排除自身）
            SiliconeWrinkleUvCompatibility existing = mapper.findCompatibilityByUniqueKey(
                    compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null && !existing.getId().equals(compatibility.getId())) {
                throw new IllegalArgumentException("该产品与工序的兼容性配置已存在");
            }
            mapper.updateCompatibility(compatibility);
        }
        return compatibility;
    }

    @Override
    public void batchSaveCompatibilities(List<SiliconeWrinkleUvCompatibility> compatibilities) {
        if (compatibilities == null || compatibilities.isEmpty()) {
            return;
        }
        for (SiliconeWrinkleUvCompatibility compatibility : compatibilities) {
            // 检查是否已存在
            SiliconeWrinkleUvCompatibility existing = mapper.findCompatibilityByUniqueKey(
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
    public void batchDeleteCompatibilities(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        mapper.batchDeleteCompatibilities(ids);
    }
}
