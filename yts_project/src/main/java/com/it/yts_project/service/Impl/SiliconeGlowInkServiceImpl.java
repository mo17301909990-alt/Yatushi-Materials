package com.it.yts_project.service.Impl;

import com.it.yts_project.mapper.SiliconeGlowInkMapper;
import com.it.yts_project.model.SiliconeGlowInkCompatibility;
import com.it.yts_project.model.SiliconeGlowInkProduct;
import com.it.yts_project.service.SiliconeGlowInkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 硅胶发光油墨Service实现类
 */
@Service
@Transactional
public class SiliconeGlowInkServiceImpl implements SiliconeGlowInkService {

    @Autowired
    private SiliconeGlowInkMapper siliconeGlowInkMapper;

    // ========== 产品管理 ==========

    @Override
    public List<SiliconeGlowInkProduct> getAllProducts() {
        return siliconeGlowInkMapper.findAllProducts();
    }

    @Override
    public SiliconeGlowInkProduct getProductById(Integer id) {
        return siliconeGlowInkMapper.findProductById(id);
    }

    @Override
    public List<SiliconeGlowInkProduct> searchProducts(String keyword) {
        return siliconeGlowInkMapper.searchProducts(keyword);
    }

    @Override
    public SiliconeGlowInkProduct saveProduct(SiliconeGlowInkProduct product) {
        if (product.getId() == null) {
            if (product.getIsActive() == null) {
                product.setIsActive(true);
            }
            siliconeGlowInkMapper.insertProduct(product);
        } else {
            siliconeGlowInkMapper.updateProduct(product);
        }
        return product;
    }

    @Override
    public void deleteProduct(Integer id) {
        siliconeGlowInkMapper.deleteProductById(id);
    }

    // ========== 兼容性管理 ==========

    @Override
    public List<SiliconeGlowInkCompatibility> getCompatibilitiesByProductId(Integer productId) {
        return siliconeGlowInkMapper.findCompatibilitiesByProductId(productId);
    }

    @Override
    public List<SiliconeGlowInkCompatibility> getAllCompatibilities() {
        return siliconeGlowInkMapper.findAllCompatibilities();
    }

    @Override
    public SiliconeGlowInkCompatibility getCompatibilityById(Integer id) {
        return siliconeGlowInkMapper.findCompatibilityById(id);
    }

    @Override
    public SiliconeGlowInkCompatibility saveCompatibility(SiliconeGlowInkCompatibility compatibility) {
        // 验证产品是否存在
        if (compatibility.getProductId() == null) {
            throw new IllegalArgumentException("产品ID不能为空");
        }

        SiliconeGlowInkProduct product = siliconeGlowInkMapper.findProductById(compatibility.getProductId());
        if (product == null) {
            throw new IllegalArgumentException("产品ID " + compatibility.getProductId() + " 不存在");
        }

        if (compatibility.getId() == null) {
            // 检查是否已存在相同唯一键组合
            SiliconeGlowInkCompatibility existing = siliconeGlowInkMapper.findCompatibilityByUniqueKey(
                    compatibility.getProductId(), compatibility.getPostProcessingStep());

            if (existing != null) {
                throw new IllegalArgumentException("该产品与工序的兼容性配置已存在，请勿重复添加");
            }

            if (compatibility.getDisplayOrder() == null) {
                compatibility.setDisplayOrder(0);
            }

            siliconeGlowInkMapper.insertCompatibility(compatibility);
        } else {
            // 更新时检查唯一键冲突（排除自身）
            SiliconeGlowInkCompatibility existing = siliconeGlowInkMapper.findCompatibilityByUniqueKey(
                    compatibility.getProductId(), compatibility.getPostProcessingStep());

            if (existing != null && !existing.getId().equals(compatibility.getId())) {
                throw new IllegalArgumentException("该产品与工序的兼容性配置已存在");
            }

            siliconeGlowInkMapper.updateCompatibility(compatibility);
        }

        return compatibility;
    }

    @Override
    public void deleteCompatibility(Integer id) {
        siliconeGlowInkMapper.deleteCompatibilityById(id);
    }

    @Override
    public void batchSaveCompatibilities(List<SiliconeGlowInkCompatibility> compatibilities) {
        if (compatibilities == null || compatibilities.isEmpty()) {
            return;
        }

        for (SiliconeGlowInkCompatibility compatibility : compatibilities) {
            // 验证产品是否存在
            if (compatibility.getProductId() == null) {
                throw new IllegalArgumentException("产品ID不能为空");
            }

            SiliconeGlowInkProduct product = siliconeGlowInkMapper.findProductById(compatibility.getProductId());
            if (product == null) {
                throw new IllegalArgumentException("产品ID " + compatibility.getProductId() + " 不存在");
            }

            if (compatibility.getDisplayOrder() == null) {
                compatibility.setDisplayOrder(0);
            }

            // 检查是否已存在相同唯一键组合
            SiliconeGlowInkCompatibility existing = siliconeGlowInkMapper.findCompatibilityByUniqueKey(
                    compatibility.getProductId(), compatibility.getPostProcessingStep());

            if (existing != null) {
                // 已存在则更新
                compatibility.setId(existing.getId());
                siliconeGlowInkMapper.updateCompatibility(compatibility);
            } else {
                // 不存在则插入
                siliconeGlowInkMapper.insertCompatibility(compatibility);
            }
        }
    }

    @Override
    public List<String> getAllPostProcessingSteps() {
        return siliconeGlowInkMapper.getAllPostProcessingSteps();
    }
}
