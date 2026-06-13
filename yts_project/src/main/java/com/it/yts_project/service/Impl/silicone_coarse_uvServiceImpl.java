package com.it.yts_project.service.Impl;

import com.it.yts_project.mapper.silicone_coarse_uvMapper;
import com.it.yts_project.model.silicone_coarse_uvCompatibility;
import com.it.yts_project.model.silicone_coarse_uvProduct;
import com.it.yts_project.service.silicone_coarse_uvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 硅胶粗纹UV Service实现类
 */
@Service
@Transactional
public class silicone_coarse_uvServiceImpl implements silicone_coarse_uvService {

    @Autowired
    private silicone_coarse_uvMapper mapper;

    // ========== 产品管理 ==========

    @Override
    public List<silicone_coarse_uvProduct> getAllProducts() {
        return mapper.findAllProducts();
    }

    @Override
    public List<silicone_coarse_uvProduct> getActiveProducts() {
        return mapper.findActiveProducts();
    }

    @Override
    public silicone_coarse_uvProduct getProductById(Integer id) {
        return mapper.findProductById(id);
    }

    @Override
    public List<silicone_coarse_uvProduct> searchProducts(String keyword) {
        return mapper.searchProducts(keyword);
    }

    @Override
    public silicone_coarse_uvProduct saveProduct(silicone_coarse_uvProduct product) {
        if (product.getId() == null) {
            mapper.insertProduct(product);
        } else {
            mapper.updateProduct(product);
        }
        return product;
    }

    @Override
    public void deleteProduct(Integer id) {
        // 先删除关联的兼容性
        mapper.deleteCompatibilitiesByProductId(id);
        // 再删除产品
        mapper.deleteProductById(id);
    }

    // ========== 兼容性管理 ==========

    @Override
    public List<silicone_coarse_uvCompatibility> getCompatibilitiesByProductId(Integer productId) {
        return mapper.findCompatibilitiesByProductId(productId);
    }

    @Override
    public List<silicone_coarse_uvCompatibility> getAllCompatibilities() {
        return mapper.findAllCompatibilities();
    }

    @Override
    public silicone_coarse_uvCompatibility getCompatibilityById(Integer id) {
        return mapper.findCompatibilityById(id);
    }

    @Override
    public silicone_coarse_uvCompatibility saveCompatibility(silicone_coarse_uvCompatibility compatibility) {
        // 验证产品是否存在
        if (compatibility.getProductId() == null) {
            throw new IllegalArgumentException("产品ID不能为空");
        }
        silicone_coarse_uvProduct product = mapper.findProductById(compatibility.getProductId());
        if (product == null) {
            throw new IllegalArgumentException("产品ID " + compatibility.getProductId() + " 不存在");
        }

        if (compatibility.getId() == null) {
            // 新增：检查唯一键
            silicone_coarse_uvCompatibility existing = mapper.findCompatibilityByUniqueKey(
                    compatibility.getProductId(),
                    compatibility.getPostProcessingStep()
            );
            if (existing != null) {
                throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            }
            mapper.insertCompatibility(compatibility);
        } else {
            // 更新：检查唯一键冲突（排除自身）
            silicone_coarse_uvCompatibility existing = mapper.findCompatibilityByUniqueKey(
                    compatibility.getProductId(),
                    compatibility.getPostProcessingStep()
            );
            if (existing != null && !existing.getId().equals(compatibility.getId())) {
                throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            }
            mapper.updateCompatibility(compatibility);
        }

        // 回填产品名称
        compatibility.setProductName(product.getMaterialName());
        return compatibility;
    }

    @Override
    public void deleteCompatibility(Integer id) {
        mapper.deleteCompatibilityById(id);
    }

    @Override
    public void batchSaveCompatibilities(List<silicone_coarse_uvCompatibility> compatibilities) {
        if (compatibilities == null || compatibilities.isEmpty()) {
            return;
        }
        for (silicone_coarse_uvCompatibility compatibility : compatibilities) {
            // 验证产品是否存在
            if (compatibility.getProductId() == null) {
                throw new IllegalArgumentException("产品ID不能为空");
            }
            silicone_coarse_uvProduct product = mapper.findProductById(compatibility.getProductId());
            if (product == null) {
                throw new IllegalArgumentException("产品ID " + compatibility.getProductId() + " 不存在");
            }

            // 检查唯一键
            silicone_coarse_uvCompatibility existing = mapper.findCompatibilityByUniqueKey(
                    compatibility.getProductId(),
                    compatibility.getPostProcessingStep()
            );
            if (existing != null) {
                // 已存在则更新
                compatibility.setId(existing.getId());
                mapper.updateCompatibility(compatibility);
            } else {
                mapper.insertCompatibility(compatibility);
            }
        }
    }

    @Override
    public List<String> getAllPostProcessingSteps() {
        return mapper.getAllPostProcessingSteps();
    }

    @Override
    public List<String> getAllProductNames() {
        return mapper.getAllProductNames();
    }
}
