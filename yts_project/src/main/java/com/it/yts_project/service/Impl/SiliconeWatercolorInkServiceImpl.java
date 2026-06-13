package com.it.yts_project.service.Impl;

import com.it.yts_project.mapper.SiliconeWatercolorInkMapper;
import com.it.yts_project.model.SiliconeWatercolorInkProduct;
import com.it.yts_project.model.SiliconeWatercolorInkCompatibility;
import com.it.yts_project.service.SiliconeWatercolorInkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 硅胶水彩油墨Service实现类
 */
@Service
@Transactional
public class SiliconeWatercolorInkServiceImpl implements SiliconeWatercolorInkService {

    @Autowired
    private SiliconeWatercolorInkMapper mapper;

    // ========== 产品管理 ==========

    @Override
    public List<SiliconeWatercolorInkProduct> getAllProducts() {
        return mapper.findAllProducts();
    }

    @Override
    public SiliconeWatercolorInkProduct getProductById(Integer id) {
        return mapper.findProductById(id);
    }

    @Override
    public List<SiliconeWatercolorInkProduct> searchProducts(String keyword) {
        return mapper.searchProducts(keyword);
    }

    @Override
    public SiliconeWatercolorInkProduct saveProduct(SiliconeWatercolorInkProduct product) {
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
    public List<SiliconeWatercolorInkCompatibility> getAllCompatibilities() {
        return mapper.findAllCompatibilities();
    }

    @Override
    public List<SiliconeWatercolorInkCompatibility> getCompatibilitiesByProductId(Integer productId) {
        return mapper.findCompatibilitiesByProductId(productId);
    }

    @Override
    public SiliconeWatercolorInkCompatibility getCompatibilityById(Integer id) {
        return mapper.findCompatibilityById(id);
    }

    @Override
    public SiliconeWatercolorInkCompatibility saveCompatibility(SiliconeWatercolorInkCompatibility compatibility) {
        // 验证必填字段
        if (compatibility.getProductId() == null) {
            throw new IllegalArgumentException("产品ID不能为空");
        }
        if (compatibility.getPostProcessingStep() == null || compatibility.getPostProcessingStep().trim().isEmpty()) {
            throw new IllegalArgumentException("后加工工序不能为空");
        }

        // 检查唯一性
        SiliconeWatercolorInkCompatibility existing = mapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(),
                compatibility.getPostProcessingStep()
        );

        if (compatibility.getId() == null) {
            // 新增
            if (existing != null) {
                throw new IllegalArgumentException("该产品与工序的兼容性配置已存在，请勿重复添加");
            }
            mapper.insertCompatibility(compatibility);
        } else {
            // 更新
            if (existing != null && !existing.getId().equals(compatibility.getId())) {
                throw new IllegalArgumentException("该产品与工序的兼容性配置已存在");
            }
            mapper.updateCompatibility(compatibility);
        }

        return compatibility;
    }

    @Override
    public void batchSaveCompatibility(List<SiliconeWatercolorInkCompatibility> compatibilities) {
        if (compatibilities == null || compatibilities.isEmpty()) {
            return;
        }
        for (SiliconeWatercolorInkCompatibility compatibility : compatibilities) {
            // 验证必填字段
            if (compatibility.getProductId() == null) {
                throw new IllegalArgumentException("产品ID不能为空");
            }
            if (compatibility.getPostProcessingStep() == null || compatibility.getPostProcessingStep().trim().isEmpty()) {
                throw new IllegalArgumentException("后加工工序不能为空");
            }

            // 检查是否已存在
            SiliconeWatercolorInkCompatibility existing = mapper.findCompatibilityByUniqueKey(
                    compatibility.getProductId(),
                    compatibility.getPostProcessingStep()
            );

            if (existing != null) {
                // 已存在，更新
                compatibility.setId(existing.getId());
                mapper.updateCompatibility(compatibility);
            } else {
                // 不存在，插入
                mapper.insertCompatibility(compatibility);
            }
        }
    }

    @Override
    public void deleteCompatibility(Integer id) {
        mapper.deleteCompatibilityById(id);
    }
}
